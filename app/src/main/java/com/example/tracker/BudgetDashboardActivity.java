package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BudgetDashboardActivity extends AppCompatActivity {

    private static final int ADD_BUDGET_REQUEST_CODE = 1;
    private RecyclerView recyclerViewBudgets;
    private BudgetAdapter budgetAdapter;
    private BudgetViewModel budgetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_dashboard);

        recyclerViewBudgets = findViewById(R.id.recyclerViewBudgets);
        Button buttonAddBudget = findViewById(R.id.buttonAddBudget);

        recyclerViewBudgets.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBudgets.setHasFixedSize(true);

        budgetAdapter = new BudgetAdapter(new ArrayList<>(), budget -> {
            // Handle delete action
            budgetViewModel.delete(budget);
            Toast.makeText(BudgetDashboardActivity.this, "Budget deleted successfully", Toast.LENGTH_SHORT).show();
        });
        recyclerViewBudgets.setAdapter(budgetAdapter);

        // Initialize the ViewModel
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // Observe the LiveData from the ViewModel
        budgetViewModel.getAllBudgets().observe(this, new Observer<List<Budget>>() {
            @Override
            public void onChanged(List<Budget> budgets) {
                // Update the RecyclerView when the data changes
                budgetAdapter.setBudgets(budgets);
            }
        });

        buttonAddBudget.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetDashboardActivity.this, AddBudgetActivity.class);
            startActivityForResult(intent, ADD_BUDGET_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_BUDGET_REQUEST_CODE && resultCode == RESULT_OK) {
            // No need to manually reload budgets; LiveData handles it
            Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
