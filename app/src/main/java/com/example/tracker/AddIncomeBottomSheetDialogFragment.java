package com.example.tracker;

import android.annotation.SuppressLint;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Calendar;

public class AddIncomeBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextDate;
    private EditText editTextAmount;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_add_income, container, false);

        Spinner spinnerIncomeSource = view.findViewById(R.id.spinnerIncomeSource);
        editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextDate = view.findViewById(R.id.editTextDate);
        Button buttonAddIncome = view.findViewById(R.id.buttonAddIncome);

        // Set the date picker dialog on click for the date field
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonAddIncome.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String source = spinnerIncomeSource.getSelectedItem().toString();

            // Check if all required fields are filled
            if (amount.isEmpty() || date.isEmpty() || source.isEmpty()) {
                // Handle validation failure (e.g., show a Toast)
                return;
            }

            // Add the income to DataHolder
            DataHolder.getInstance().addIncome(new Income(amount, date, source));

            // Notify the adapter of the new data
            if (getActivity() instanceof add) { // Ensure the correct activity
                add activity = (add) getActivity();
                activity.incomeAdapter.notifyDataSetChanged();
                activity.recyclerViewIncome.scrollToPosition(activity.incomeAdapter.getItemCount() - 1); // Scroll to the last added item
            }

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
