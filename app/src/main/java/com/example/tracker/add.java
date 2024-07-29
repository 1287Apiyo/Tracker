package com.example.tracker;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class add extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    private Dialog incomeDialog;
    private Dialog expenseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Log.d(TAG, "Add activity started");

        MaterialToolbar buttonAddIncome = findViewById(R.id.Income);
        MaterialToolbar buttonAddExpense = findViewById(R.id.Expense);

        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Income button clicked");
                showAddIncomeDialog();
            }
        });

        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Expense button clicked");
                showAddExpenseDialog();
            }
        });
    }

    private void showAddIncomeDialog() {
        Log.d(TAG, "Showing Add Income Dialog");
        incomeDialog = new Dialog(this);
        incomeDialog.setContentView(R.layout.activity_dialog_add_income);
        incomeDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        incomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        incomeDialog.getWindow().setGravity(Gravity.BOTTOM);

        incomeDialog.show();
    }

    private void showAddExpenseDialog() {
        Log.d(TAG, "Showing Add Expense Dialog");
        expenseDialog = new Dialog(this);
        expenseDialog.setContentView(R.layout.activity_dialog_add_expense);
        expenseDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        expenseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        expenseDialog.getWindow().setGravity(Gravity.BOTTOM);

        expenseDialog.show();
    }
}
