package com.example.tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class add extends AppCompatActivity implements IncomeAdapter.OnItemClickListener, ExpenseAdapter.OnItemClickListener {

    private static final String TAG = "AddActivity";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private RecyclerView recyclerViewIncome;
    private RecyclerView recyclerViewExpense;
    private IncomeAdapter incomeAdapter;
    private ExpenseAdapter expenseAdapter;
    private TransactionViewModel transactionViewModel;
    private ImageView iconCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Log.d(TAG, "Add activity started");

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Initialize toolbar buttons
        MaterialToolbar buttonAddIncome = findViewById(R.id.Income);
        MaterialToolbar buttonAddExpense = findViewById(R.id.Expense);

        // Initialize RecyclerViews for income and expense transactions
        recyclerViewIncome = findViewById(R.id.recyclerViewTransactionsIncome);
        recyclerViewExpense = findViewById(R.id.recyclerViewTransactionsExpense);

        recyclerViewIncome.setLayoutManager(new LinearLayoutManager(this));
        incomeAdapter = new IncomeAdapter(this, this);
        recyclerViewIncome.setAdapter(incomeAdapter);

        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this, this);
        recyclerViewExpense.setAdapter(expenseAdapter);

        // Observe data changes and update UI accordingly
        transactionViewModel.getAllIncomes().observe(this, incomes -> {
            incomeAdapter.setIncomes(incomes);
            recyclerViewIncome.scrollToPosition(incomeAdapter.getItemCount() - 1);
        });

        transactionViewModel.getAllExpenses().observe(this, expenses -> {
            expenseAdapter.setExpenses(expenses);
            recyclerViewExpense.scrollToPosition(expenseAdapter.getItemCount() - 1);
        });

        // Set button click listeners for adding income and expense
        buttonAddIncome.setOnClickListener(v -> {
            Log.d(TAG, "Add Income button clicked");
            showAddIncomeDialog();
        });

        buttonAddExpense.setOnClickListener(v -> {
            Log.d(TAG, "Add Expense button clicked");
            showAddExpenseDialog();
        });

        // Initialize camera icon and set up click listener
        iconCamera = findViewById(R.id.iconCamera);
        iconCamera.setOnClickListener(v -> {
            Log.d(TAG, "Camera icon clicked");
            requestCameraPermission(); // Open the camera when icon is clicked
        });

        // Initialize and set up the Budget button
        Button buttonViewBudgets = findViewById(R.id.buttonViewBudgets);
        buttonViewBudgets.setOnClickListener(v -> {
            Intent intent = new Intent(add.this, BudgetDashboardActivity.class);
            startActivity(intent);
        });

        // Initialize BottomNavigationView and handle navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the correct menu item
        bottomNavigationView.setSelectedItemId(R.id.nav_add);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(add.this, Home.class));
                return true;
            } else if (id == R.id.nav_add) {
                // Current activity, no action needed
                return true;
            } else if (id == R.id.nav_transactions) {
                startActivity(new Intent(add.this, TransactionsActivity.class));
                return true;
            } else if (id == R.id.nav_statistics) {
                startActivity(new Intent(add.this, StatisticsActivity.class));
                return true;
            }
            return false;
        });
    }





    private void showAddIncomeDialog() {
        Log.d(TAG, "Showing Add Income Dialog");
        AddIncomeBottomSheetDialogFragment dialogFragment = new AddIncomeBottomSheetDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddIncomeDialog");
    }

    private void showAddExpenseDialog() {
        Log.d(TAG, "Showing Add Expense Dialog");
        AddExpenseBottomSheetDialogFragment dialogFragment = new AddExpenseBottomSheetDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddExpenseDialog");
    }

    @Override
    public void onDeleteClick(Income income) {
        // Handle delete income action
        transactionViewModel.deleteIncome(income);
    }

    @Override
    public void onDeleteClick(Expense expense) {
        // Handle delete expense action
        transactionViewModel.deleteExpense(expense);
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    // Open the camera
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Camera result OK");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                processImage(imageBitmap);
            } else {
                Log.e(TAG, "Image capture failed");
            }
        }
    }

    private void processImage(Bitmap imageBitmap) {
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        String recognizedText = visionText.getText();
                        Log.d(TAG, "Extracted text: " + recognizedText);
                        Toast.makeText(add.this, "OCR success: " + recognizedText, Toast.LENGTH_LONG).show();

                        // Extract details from the recognized text
                        double amount = parseAmountFromText(recognizedText);
                        String date = parseDateFromText(recognizedText);
                        String category = parseCategoryFromText(recognizedText);

                        Log.d(TAG, "Parsed amount: " + amount);
                        Log.d(TAG, "Parsed date: " + date);
                        Log.d(TAG, "Parsed category: " + category);

                        if (amount > 0) {
                            // Create and insert the Expense object
                            Expense newExpense = new Expense(amount, date, category);
                            transactionViewModel.insert(newExpense);
                            Toast.makeText(add.this, "Expense added: KES " + amount, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(add.this, "Failed to parse amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "OCR failed", e);
                        Toast.makeText(add.this, "OCR failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double parseAmountFromText(String text) {
        // Improved regex to capture amounts, including decimal points and currency symbols
        Pattern pattern = Pattern.compile("\\b\\d+(?:\\.\\d{1,2})?\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String amountStr = matcher.group().replaceAll("[^\\d.]", ""); // Remove non-numeric characters
            try {
                return Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Failed to parse amount from text: " + amountStr, e);
            }
        }
        return 0.0;
    }

    private String parseDateFromText(String text) {
        // Simple regex to capture dates in common formats (e.g., 21/12/2023, 12-21-23)
        Pattern pattern = Pattern.compile("\\b\\d{1,2}[-/]\\d{1,2}[-/]\\d{2,4}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()); // Return current date if no date found
    }

    private String parseCategoryFromText(String text) {
        // Example regex to extract the first word as a category
        Pattern pattern = Pattern.compile("\\b\\w+\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Uncategorized";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Log.e(TAG, "Camera permission denied");
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
