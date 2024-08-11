package com.example.tracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBudgetActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText budgetAmountEditText;
    private Button buttonSaveBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        budgetAmountEditText = findViewById(R.id.editTextBudgetAmount);
        buttonSaveBudget = findViewById(R.id.buttonSaveBudget);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

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
            Toast.makeText(AddBudgetActivity.this, "Please select a category and enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double budgetAmount = Double.parseDouble(budgetAmountStr);
        String date = "";  // You can add a date picker to set the date.

        Budget newBudget = new Budget(budgetAmount, category, date);

        new InsertBudgetAsyncTask(BudgetDatabase.getInstance(getApplicationContext()).budgetDao()).execute(newBudget);

        Toast.makeText(AddBudgetActivity.this, "Budget added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private static class InsertBudgetAsyncTask extends AsyncTask<Budget, Void, Void> {
        private BudgetDao budgetDao;

        private InsertBudgetAsyncTask(BudgetDao budgetDao) {
            this.budgetDao = budgetDao;
        }

        @Override
        protected Void doInBackground(Budget... budgets) {
            budgetDao.insert(budgets[0]);
            return null;
        }
    }
}
