package com.example.tracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;

public class activity_dialog_add_expense extends AppCompatActivity {

    private EditText editTextAmount, editTextDate;
    private Spinner spinnerExpenseSource;
    private Button buttonAddExpense;

    // Sample list to store expenses (Replace this with your actual storage mechanism)
    private ArrayList<Expense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_expense);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerExpenseSource = findViewById(R.id.spinnerExpenseSource);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);

        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        buttonAddExpense.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String source = spinnerExpenseSource.getSelectedItem().toString();

            if (amount.isEmpty() || date.isEmpty() || source.isEmpty()) {
                Toast.makeText(activity_dialog_add_expense.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Expense expense = new Expense(amount, date, source);
                expenseList.add(expense);
                Toast.makeText(activity_dialog_add_expense.this, "Expense Added", Toast.LENGTH_SHORT).show();
                // Clear fields after adding
                editTextAmount.setText("");
                editTextDate.setText("");
                spinnerExpenseSource.setSelection(0);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editTextDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Sample Expense class (Replace this with your actual implementation)
    private class Expense {
        String amount;
        String date;
        String source;

        public Expense(String amount, String date, String source) {
            this.amount = amount;
            this.date = date;
            this.source = source;
        }
    }
}