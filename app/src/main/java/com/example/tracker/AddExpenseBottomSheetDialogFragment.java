package com.example.tracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Calendar;

public class AddExpenseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextDate;
    private EditText editTextAmount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_add_expense, container, false);

        // Updated IDs
        EditText editTextAmount = view.findViewById(R.id.editTextAmount);
        editTextDate = view.findViewById(R.id.editTextDate);
        Spinner spinnerExpenseCategory = view.findViewById(R.id.spinnerExpenseSource); // Updated ID
        Button buttonAddExpense = view.findViewById(R.id.buttonAddExpense);

        // Set the date picker dialog on click for the date field
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonAddExpense.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String category = spinnerExpenseCategory.getSelectedItem().toString(); // Use category for expense

            // Add the expense to DataHolder
            DataHolder.getInstance().addExpense(new Expense(amount, date, category));

            // Notify the adapter of the new data
            if (getActivity() instanceof add) { // Ensure this is the correct activity
                add activity = (add) getActivity();
                activity.expenseAdapter.notifyDataSetChanged();
                activity.recyclerViewExpense.scrollToPosition(activity.expenseAdapter.getItemCount() - 1); // Scroll to the last added item
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
