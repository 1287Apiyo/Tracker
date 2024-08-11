package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements TransactionsAdapter.OnTransactionDeleteListener {

    private static final String TAG = "HomeActivity";
    private static final long BACK_PRESS_INTERVAL = 2000; // 2 seconds interval for back press
    private boolean backPressedOnce = false;
    private Handler handler = new Handler();

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewRecentTransactions;
    private TransactionsAdapter transactionsAdapter;
    private TextView textWelcome;
    private TextView balanceTextView;
    private TextView totalIncomeTextView;
    private TextView totalExpenseTextView;
    private TransactionViewModel transactionViewModel;

    private FirebaseFirestore db;
    private RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private List<Category> categoryList;

    // New: Button for Viewing Statistics
    private Button btnStatistics;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        databaseHelper = new DatabaseHelper(this);


        // TextView to show categories in a dialog
        TextView viewCategories = findViewById(R.id.textViewCategories);
        viewCategories.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            CategoryDialogFragment categoryDialogFragment = new CategoryDialogFragment();
            categoryDialogFragment.show(fragmentManager, "category_dialog");
        });

        // Button to view all transactions
        Button viewAllTransactions = findViewById(R.id.buttonViewAllTransactions);
        viewAllTransactions.setOnClickListener(v -> {
            Log.d(TAG, "View All Transactions clicked");
            Intent intent = new Intent(Home.this, TransactionsActivity.class);
            startActivity(intent);
        });

        // Initialize and Set OnClickListener for the Statistics Button
        btnStatistics = findViewById(R.id.btnStatistics);
        btnStatistics.setOnClickListener(v -> {
            Log.d(TAG, "View Statistics button clicked");
            Intent intent = new Intent(Home.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView for recent transactions
        recyclerViewRecentTransactions = findViewById(R.id.recyclerViewTransactions);
        recyclerViewRecentTransactions.setLayoutManager(new LinearLayoutManager(this));
        loadRecentTransactions();

        // Initialize TextViews for balance, income, and expenses
        balanceTextView = findViewById(R.id.balanceTextView);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);

        // Observe the total balance LiveData
        transactionViewModel.getTotalBalance().observe(this, totalBalance -> {
            updateAvailableBalance(); // Update the available balance
        });

        // Observe the total income LiveData
        transactionViewModel.getTotalIncome().observe(this, totalIncome -> {
            totalIncomeTextView.setText(String.format("KES %.2f", totalIncome));
            updateAvailableBalance(); // Update available balance when income changes
        });

        // Observe the total expense LiveData
        transactionViewModel.getTotalExpense().observe(this, totalExpense -> {
            totalExpenseTextView.setText(String.format("KES %.2f", totalExpense));
            updateAvailableBalance(); // Update available balance when expense changes
        });

        // Initialize RecyclerView for categories
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4); // 4 columns
        recyclerViewCategories.setLayoutManager(gridLayoutManager);
        categoryList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categoryList);
        recyclerViewCategories.setAdapter(categoriesAdapter);

        // Fetch categories from Firestore
        fetchCategoriesFromFirestore();

        // BottomNavigationView initialization and setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle Home navigation
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(Home.this, add.class));
                return true;
            } else if (id == R.id.nav_transactions) {
                startActivity(new Intent(Home.this, TransactionsActivity.class));
                return true;
            } else if (id == R.id.nav_statistics) {
                startActivity(new Intent(Home.this, StatisticsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentTransactions(); // Refresh the data when the activity resumes
    }

    private void loadRecentTransactions() {
        Cursor cursor = databaseHelper.getRecentTransactions(5); // Fetch 5 most recent transactions
        transactionsAdapter = new TransactionsAdapter(this, cursor, this);
        recyclerViewRecentTransactions.setAdapter(transactionsAdapter);
    }

    @Override
    public void onTransactionDelete(int id) {
        databaseHelper.deleteTransaction(id);
        loadRecentTransactions(); // Refresh the data after deletion
    }

    private void updateAvailableBalance() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String mpesaBalanceStr = preferences.getString("mpesa_balance", "0");
        double mpesaBalance = Double.parseDouble(mpesaBalanceStr);

        double totalIncome = transactionViewModel.getTotalIncome().getValue() != null
                ? transactionViewModel.getTotalIncome().getValue()
                : 0;
        double totalExpense = transactionViewModel.getTotalExpense().getValue() != null
                ? transactionViewModel.getTotalExpense().getValue()
                : 0;

        double availableBalance = mpesaBalance + totalIncome - totalExpense;
        balanceTextView.setText(String.format("KES %.2f", availableBalance));
    }

    private void fetchCategoriesFromFirestore() {
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categoryList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                categoryList.add(category);
                            }
                            categoriesAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            // Navigate to AddBalanceActivity
            Intent intent = new Intent(Home.this, AddBalanceActivity.class);
            startActivity(intent);
            finish(); // Close the Home activity
        } else {
            this.backPressedOnce = true;
            Toast.makeText(this, "Press again to go to balance update screen", Toast.LENGTH_SHORT).show();

            // Reset the backPressedOnce flag after BACK_PRESS_INTERVAL
            handler.postDelayed(() -> backPressedOnce = false, BACK_PRESS_INTERVAL);
        }
    }
}
