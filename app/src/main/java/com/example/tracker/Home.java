package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity implements TransactionsAdapter.OnTransactionDeleteListener {

    private static final String TAG = "HomeActivity";
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewRecentTransactions;
    private TransactionsAdapter transactionsAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
