package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        databaseHelper = new DatabaseHelper(this);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fabAdd clicked");
                Intent intent = new Intent(Home.this, add.class);
                startActivity(intent);
            }
        });

        TextView viewCategories = findViewById(R.id.textViewCategories);
        viewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CategoryDialogFragment categoryDialogFragment = new CategoryDialogFragment();
                categoryDialogFragment.show(fragmentManager, "category_dialog");
            }
        });

        Button viewAllTransactions = findViewById(R.id.buttonViewAllTransactions);
        viewAllTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View All Transactions clicked");
                Intent intent = new Intent(Home.this, TransactionsActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewRecentTransactions = findViewById(R.id.recyclerViewTransactions);
        recyclerViewRecentTransactions.setLayoutManager(new LinearLayoutManager(this));
        loadRecentTransactions();

        // Retrieve username from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = preferences.getString("username", "User"); // Default to "User" if no username is found

        // Set the welcome message
        textWelcome = findViewById(R.id.textWelcome);
        textWelcome.setText("Welcome " + username);

        // Initialize TextViews
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
