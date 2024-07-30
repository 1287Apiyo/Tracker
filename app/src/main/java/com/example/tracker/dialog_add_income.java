package com.example.tracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;

public class dialog_add_income extends AppCompatActivity {

    private EditText editTextAmount, editTextDate;
    private Spinner spinnerIncomeSource;
    private Button buttonAddIncome;

    // Sample list to store incomes (Replace this with your actual storage mechanism)
    private ArrayList<Income> incomeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_income);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerIncomeSource = findViewById(R.id.spinnerIncomeSource);
        buttonAddIncome = findViewById(R.id.buttonAddIncome);

        editTextDate.setOnClickListener(v -> {
            Log.d("IncomeDialog", "Date EditText clicked");
            showDatePickerDialog();
        });

        buttonAddIncome.setOnClickListener(v -> {
            Log.d("IncomeDialog", "Add Income button clicked");

            String amount = editTextAmount.getText().toString();
            String date = editTextDate.getText().toString();
            String source = spinnerIncomeSource.getSelectedItem().toString();

            if (amount.isEmpty() || date.isEmpty() || source.isEmpty()) {
                Toast.makeText(dialog_add_income.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Income income = new Income(amount, date, source);
                incomeList.add(income);
                Toast.makeText(dialog_add_income.this, "Income Added", Toast.LENGTH_SHORT).show();
                // Clear fields after adding
                editTextAmount.setText("");
                editTextDate.setText("");
                spinnerIncomeSource.setSelection(0);
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

    // Sample Income class (Replace this with your actual implementation)
    private class Income {
        String amount;
        String date;
        String source;

        public Income(String amount, String date, String source) {
            this.amount = amount;
            this.date = date;
            this.source = source;
        }
    }
}