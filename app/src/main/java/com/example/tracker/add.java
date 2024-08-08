package com.example.tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            processImage(imageBitmap);
        }
    }

    private void processImage(Bitmap imageBitmap) {
        // Create an InputImage object from the bitmap
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);

        // Initialize the TextRecognizer
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Process the image and recognize text
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Extract and parse recognized text
                        String extractedText = extractTextFromVisionText(visionText);
                        Log.d(TAG, "Extracted text: " + extractedText);

                        // Parse and update the UI
                        parseAndUpdateUI(extractedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "OCR failed", e);
                        Toast.makeText(add.this, "Failed to extract text from image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String extractTextFromVisionText(Text visionText) {
        StringBuilder extractedText = new StringBuilder();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                extractedText.append(line.getText()).append("\n");
            }
        }
        return extractedText.toString();
    }

    private void parseAndUpdateUI(String text) {
        Pattern patternDate = Pattern.compile("Date: (\\d{2}/\\d{2}/\\d{4})");
        Pattern patternAmount = Pattern.compile("Amount: (\\d+\\.\\d{2})");
        Pattern patternExpenseType = Pattern.compile("Expense Type: (.*)");

        Matcher matcherDate = patternDate.matcher(text);
        Matcher matcherAmount = patternAmount.matcher(text);
        Matcher matcherExpenseType = patternExpenseType.matcher(text);

        String date = matcherDate.find() ? matcherDate.group(1) : "N/A";
        String amount = matcherAmount.find() ? matcherAmount.group(1) : "0.00";
        String expenseType = matcherExpenseType.find() ? matcherExpenseType.group(1) : "Unknown";

        Log.d(TAG, "Parsed date: " + date);
        Log.d(TAG, "Parsed amount: " + amount);
        Log.d(TAG, "Parsed expense type: " + expenseType);

        try {
            double parsedAmount = Double.parseDouble(amount);
            Expense newExpense = new Expense(parsedAmount, date, expenseType);

            // Insert into ViewModel
            transactionViewModel.insert(newExpense);
            Log.d(TAG, "New expense added: " + newExpense);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to parse amount", e);
        }
    }


}