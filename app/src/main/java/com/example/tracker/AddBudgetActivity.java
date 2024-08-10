package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class AddBudgetActivity extends AppCompatActivity {

    private EditText budgetNameEditText;
    private EditText budgetAmountEditText;
    private Button buttonSaveBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget); // Make sure this layout file exists

        // Initialize the views
        budgetNameEditText = findViewById(R.id.editTextBudgetName);
        budgetAmountEditText = findViewById(R.id.editTextBudgetAmount);
        buttonSaveBudget = findViewById(R.id.buttonSaveBudget);

        // Set the OnClickListener for the save button
        buttonSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });
    }

    private void saveBudget() {
        String budgetName = budgetNameEditText.getText().toString().trim();
        String budgetAmountStr = budgetAmountEditText.getText().toString().trim();

        if (budgetName.isEmpty() || budgetAmountStr.isEmpty()) {
            // Show a message if the input is incomplete
            Toast.makeText(AddBudgetActivity.this, "Please enter both name and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int budgetAmount = Integer.parseInt(budgetAmountStr);

        // Create a new Budget object
        Budget newBudget = new Budget(budgetName, budgetAmount);

        // Send the new budget back to the BudgetDashboardActivity
        Intent intent = new Intent();
        intent.putExtra("newBudget", (Serializable) newBudget);
        setResult(RESULT_OK, intent);

        // Show a success message
        Toast.makeText(AddBudgetActivity.this, "Budget added successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity and go back to the dashboard
        finish();
    }
}
