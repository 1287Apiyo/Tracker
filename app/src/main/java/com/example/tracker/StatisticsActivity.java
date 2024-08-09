package com.example.tracker;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class StatisticsActivity extends AppCompatActivity {

    private WebView webView;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        webView = findViewById(R.id.webView);
        Button buttonLoadChart = findViewById(R.id.buttonLoadChart);

        // Initialize WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/chart.html");

        // Initialize ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        buttonLoadChart.setOnClickListener(v -> {
            // Fetch data from ViewModel
            transactionViewModel.getAllExpenses().observe(this, expenses -> {
                // Convert the data to JSON format
                String jsonData = convertExpensesToJson(expenses);
                // Pass data to WebView
                webView.evaluateJavascript("javascript:receiveData('" + jsonData + "')", null);
            });
        });
    }

    // Convert expenses to JSON
    private String convertExpensesToJson(List<Expense> expenses) {
        List<String> categories = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (Expense expense : expenses) {
            categories.add(expense.getCategory()); // Ensure these methods exist
            values.add(expense.getAmount()); // Ensure these methods exist
        }

        // Creating a JSON object
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("values", values);

        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
