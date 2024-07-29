package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fabAdd clicked");
                Intent intent = new Intent(Home.this, add.class); // Ensure this matches your Add Transaction activity class
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
                Intent intent = new Intent(Home.this, TransactionsActivity.class); // Updated to start TransactionsActivity
                startActivity(intent);
            }
        });
    }
}
