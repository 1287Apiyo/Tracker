package com.example.tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AddBalanceActivity extends AppCompatActivity {

    private EditText balanceEditText;
    private Button saveButton, resetButton;
    private TextView currentBalanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        // Initialize views
        balanceEditText = findViewById(R.id.balance_edit_text);
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);
        currentBalanceTextView = findViewById(R.id.current_balance_text_view);

        // Load and display current balance
        loadCurrentBalance();

        saveButton.setOnClickListener(v -> saveOrUpdateBalance());

        resetButton.setOnClickListener(v -> saveOrUpdateBalance());
    }

    private void loadCurrentBalance() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String balanceStr = preferences.getString("mpesa_balance", "0");
        currentBalanceTextView.setText("Current Balance: KES " + balanceStr);
    }

    private void saveOrUpdateBalance() {
        String balanceStr = balanceEditText.getText().toString().trim();
        if (balanceStr.isEmpty()) {
            balanceEditText.setError("Please enter a balance");
            return;
        }

        // Save or update balance in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mpesa_balance", balanceStr);
        editor.putBoolean("is_balance_set", true);
        editor.apply();

        // Navigate to Home activity
        redirectToHomeActivity();
    }

    private void redirectToHomeActivity() {
        Intent intent = new Intent(AddBalanceActivity.this, Home.class);
        startActivity(intent);
        finish(); // Close the AddBalanceActivity
    }
}