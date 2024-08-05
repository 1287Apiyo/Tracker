package com.example.tracker;

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
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Calendar;

public class AddExpenseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextDate;
    private EditText editTextAmount;
    private Spinner spinnerExpenseCategory;
    private TransactionViewModel transactionViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_add_expense, container, false);

        editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextDate = view.findViewById(R.id.editTextDate);
        spinnerExpenseCategory = view.findViewById(R.id.spinnerExpenseSource);
        Button buttonAddExpense = view.findViewById(R.id.buttonAddExpense);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Set the date picker dialog on click for the date field
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonAddExpense.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String category = spinnerExpenseCategory.getSelectedItem().toString();

            // Validate input
            if (amountStr.isEmpty() || date.isEmpty() || category.isEmpty()) {
                // Handle validation failure (e.g., show a Toast)
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Add the expense to the database
            Expense expense = new Expense(amount, date, category);
            transactionViewModel.insert(expense);

            dismiss();
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
}
