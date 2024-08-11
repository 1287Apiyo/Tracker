package com.example.tracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.List;
public class AddExpenseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextDate;
    private EditText editTextAmount;
    private Spinner spinnerExpenseCategory;
    private TransactionViewModel transactionViewModel;
    private BudgetViewModel budgetViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_add_expense, container, false);

        editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextDate = view.findViewById(R.id.editTextDate);
        spinnerExpenseCategory = view.findViewById(R.id.spinnerExpenseSource);
        Button buttonAddExpense = view.findViewById(R.id.buttonAddExpense);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);

        // Set the date picker dialog on click for the date field
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonAddExpense.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String category = spinnerExpenseCategory.getSelectedItem().toString();

            if (amountStr.isEmpty() || date.isEmpty() || category.isEmpty()) {
                // Handle validation failure
                return;
            }

            double amount = Double.parseDouble(amountStr);
            Expense expense = new Expense(amount, date, category);

            checkBudgetAndAddExpense(expense);
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            editTextDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void checkBudgetAndAddExpense(Expense expense) {
        budgetViewModel.getAllBudgets().observe(getViewLifecycleOwner(), new Observer<List<Budget>>() {
            @Override
            public void onChanged(List<Budget> budgets) {
                boolean budgetExceeded = false;
                for (Budget budget : budgets) {
                    if (budget.getCategory().equals(expense.getCategory())) {
                        if (expense.getAmount() > budget.getAmount()) {
                            budgetExceeded = true;
                            break;
                        }
                    }
                }

                if (budgetExceeded) {
                    showBudgetExceededDialog(expense);
                } else {
                    transactionViewModel.insert(expense);
                    dismiss();
                }
            }
        });
    }

    private void showBudgetExceededDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_budget_exceeded, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button buttonProceed = dialogView.findViewById(R.id.buttonProceed);
        Button buttonReview = dialogView.findViewById(R.id.buttonReview);

        buttonProceed.setOnClickListener(v -> {
            transactionViewModel.insert(expense);
            dialog.dismiss();
            dismiss();
        });

        buttonReview.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}
