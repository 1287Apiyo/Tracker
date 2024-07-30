package com.example.tracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransactionsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TRANSACTION = 1; // Request code for starting the add transaction activity
    private RecyclerView recyclerView;
    private TransactionsAdapter transactionsAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        recyclerView = findViewById(R.id.recyclerViewTransactions);
        FloatingActionButton fabAddTransaction = findViewById(R.id.fabAddTransaction);

        databaseHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateTransactionList();

        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsActivity.this, add_transaction.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TRANSACTION); // Start the activity for result
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            updateTransactionList(); // Refresh the transaction list when coming back from add transaction activity
        }
    }

    private void updateTransactionList() {
        Cursor cursor = databaseHelper.getAllTransactions();
        if (transactionsAdapter == null) {
            transactionsAdapter = new TransactionsAdapter(this, cursor);
            recyclerView.setAdapter(transactionsAdapter);
        } else {
            transactionsAdapter.swapCursor(cursor); // Update adapter with new data
        }
    }
}
