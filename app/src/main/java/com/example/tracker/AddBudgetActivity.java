package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class AddBudgetActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText budgetAmountEditText;
    private Button buttonSaveBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget); // Ensure this layout file exists

        // Initialize the views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        budgetAmountEditText = findViewById(R.id.editTextBudgetAmount);
        buttonSaveBudget = findViewById(R.id.buttonSaveBudget);

        // Setup the Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set the OnClickListener for the save button
        buttonSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });
    }

    private void saveBudget() {
        String category = spinnerCategory.getSelectedItem().toString();
        String budgetAmountStr = budgetAmountEditText.getText().toString().trim();

        if (category.equals("Select Category") || budgetAmountStr.isEmpty()) {
            // Show a message if the input is incomplete
            Toast.makeText(AddBudgetActivity.this, "Please select a category and enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int budgetAmount = Integer.parseInt(budgetAmountStr);

        // Create a new Budget object
        Budget newBudget = new Budget(category, budgetAmount);

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
