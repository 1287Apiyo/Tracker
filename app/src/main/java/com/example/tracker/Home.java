package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity implements TransactionsAdapter.OnTransactionDeleteListener {

    private static final String TAG = "HomeActivity";
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewRecentTransactions;
    private TransactionsAdapter transactionsAdapter;
    private TextView textWelcome;
    private TextView balanceTextView;
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

        // Initialize balance TextView
        balanceTextView = findViewById(R.id.balanceTextView);

        // Observe the total balance LiveData
        transactionViewModel.getTotalBalance().observe(this, totalBalance -> {
            // Update the UI with the total balance
            balanceTextView.setText(String.format("KES %.2f", totalBalance));
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
}
