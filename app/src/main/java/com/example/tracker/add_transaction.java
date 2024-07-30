package com.example.tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_transaction extends AppCompatActivity {

    private EditText editTextDate, editTextAmount;
    private AutoCompleteTextView autoCompleteCategory;
    private Button buttonSaveTransaction;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        editTextDate = findViewById(R.id.editTextDate);
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonSaveTransaction = findViewById(R.id.buttonSaveTransaction);

        // Setup AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.category_array));
        autoCompleteCategory.setAdapter(adapter);

        // Set date picker on date field
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    updateDateLabel();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateDateLabel() {
        String format = "yyyy-MM-dd"; // Date format
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveTransaction() {
        String date = editTextDate.getText().toString().trim();
        String category = autoCompleteCategory.getText().toString().trim();
        String amount = editTextAmount.getText().toString().trim();

        if (date.isEmpty() || category.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amountValue;
        try {
            amountValue = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save transaction to database
        Transaction transaction = new Transaction(date, category, amountValue);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.addTransaction(transaction);

        // Notify TransactionsActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("date", date);
        resultIntent.putExtra("category", category);
        resultIntent.putExtra("amount", amount);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after saving
    }
}
