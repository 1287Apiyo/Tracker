package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BudgetDashboardActivity extends AppCompatActivity {

    private static final int ADD_BUDGET_REQUEST_CODE = 1;
    private RecyclerView recyclerViewBudgets;
    private BudgetAdapter budgetAdapter;
    private ArrayList<Budget> budgetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_dashboard);

        recyclerViewBudgets = findViewById(R.id.recyclerViewBudgets);
        Button buttonAddBudget = findViewById(R.id.buttonAddBudget);

        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(budgetList);
        recyclerViewBudgets.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBudgets.setAdapter(budgetAdapter);

        buttonAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetDashboardActivity.this, AddBudgetActivity.class);
                startActivityForResult(intent, ADD_BUDGET_REQUEST_CODE);
            }
        });

        loadBudgets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_BUDGET_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Budget newBudget = (Budget) data.getSerializableExtra("newBudget");
            if (newBudget != null) {
                budgetList.add(newBudget);
                budgetAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Budget updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadBudgets() {
        // Load budgets from the database or data source
        // Example:
        budgetList.add(new Budget("Groceries", 5000));
        budgetList.add(new Budget("Rent", 15000));
        budgetList.add(new Budget("Entertainment", 3000));

        budgetAdapter.notifyDataSetChanged();
    }
}
