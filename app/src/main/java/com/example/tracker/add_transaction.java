package com.example.tracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_transaction extends AppCompatActivity {

    private EditText editTextDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSaveTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        editTextDate = findViewById(R.id.editTextDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonSaveTransaction = findViewById(R.id.buttonSaveTransaction);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle category selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no item is selected if needed
            }
        });

        buttonSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });
    }

    private void saveTransaction() {
        String date = editTextDate.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String amount = editTextAmount.getText().toString().trim();

        if (date.isEmpty() || category.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert amount to double
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

        Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after saving
    }
}
