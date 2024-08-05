package com.example.tracker;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;

public class add extends AppCompatActivity {

    private static final String TAG = "AddActivity";

    private RecyclerView recyclerViewIncome;
    private RecyclerView recyclerViewExpense;
    private IncomeAdapter incomeAdapter;
    private ExpenseAdapter expenseAdapter;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Log.d(TAG, "Add activity started");

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Initialize toolbar buttons
        MaterialToolbar buttonAddIncome = findViewById(R.id.Income);
        MaterialToolbar buttonAddExpense = findViewById(R.id.Expense);

        // Initialize RecyclerViews
        recyclerViewIncome = findViewById(R.id.recyclerViewTransactionsIncome);
        recyclerViewExpense = findViewById(R.id.recyclerViewTransactionsExpense);

        recyclerViewIncome.setLayoutManager(new LinearLayoutManager(this));
        incomeAdapter = new IncomeAdapter(this);
        recyclerViewIncome.setAdapter(incomeAdapter);

        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this);
        recyclerViewExpense.setAdapter(expenseAdapter);

        // Observe data changes
        transactionViewModel.getAllIncomes().observe(this, incomes -> {
            incomeAdapter.setIncomes(incomes);
            recyclerViewIncome.scrollToPosition(incomeAdapter.getItemCount() - 1);
        });

        transactionViewModel.getAllExpenses().observe(this, expenses -> {
            expenseAdapter.setExpenses(expenses);
            recyclerViewExpense.scrollToPosition(expenseAdapter.getItemCount() - 1);
        });

        // Set button click listeners
        buttonAddIncome.setOnClickListener(v -> {
            Log.d(TAG, "Add Income button clicked");
            showAddIncomeDialog();
        });

        buttonAddExpense.setOnClickListener(v -> {
            Log.d(TAG, "Add Expense button clicked");
            showAddExpenseDialog();
        });
    }

    private void showAddIncomeDialog() {
        Log.d(TAG, "Showing Add Income Dialog");
        AddIncomeBottomSheetDialogFragment dialogFragment = new AddIncomeBottomSheetDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddIncomeDialog");
    }

    private void showAddExpenseDialog() {
        Log.d(TAG, "Showing Add Expense Dialog");
        AddExpenseBottomSheetDialogFragment dialogFragment = new AddExpenseBottomSheetDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddExpenseDialog");
    }
}
