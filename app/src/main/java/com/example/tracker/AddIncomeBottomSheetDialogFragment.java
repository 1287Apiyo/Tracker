package com.example.tracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddIncomeBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextDate;
    private EditText editTextAmount;
    private Spinner spinnerIncomeSource;
    private TransactionViewModel transactionViewModel;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_add_income, container, false);

        spinnerIncomeSource = view.findViewById(R.id.spinnerIncomeSource);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextDate = view.findViewById(R.id.editTextDate);
        Button buttonAddIncome = view.findViewById(R.id.buttonAddIncome);

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Set the date picker dialog on click for the date field
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonAddIncome.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String source = spinnerIncomeSource.getSelectedItem().toString();

            // Validate input
            if (amountStr.isEmpty() || date.isEmpty() || source.isEmpty()) {
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid amount format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the income to the database
            Income income = new Income(amount, date, source);
            transactionViewModel.insert(income);

            // Notify user of successful addition
            Toast.makeText(getContext(), "Income added successfully", Toast.LENGTH_SHORT).show();

            // Optionally, notify the parent activity or fragment to refresh data
            if (getTargetFragment() != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
            }

            // Dismiss the dialog
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
