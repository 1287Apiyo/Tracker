package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransactionsActivity extends AppCompatActivity {

    private static final String TAG = "TransactionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions); // Ensure this matches your layout XML

        FloatingActionButton fabAddTransaction = findViewById(R.id.fabAddTransaction);
        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fabAddTransaction clicked");
                Intent intent = new Intent(TransactionsActivity.this, add_transaction.class);
                startActivity(intent);
            }
        });
    }
}
