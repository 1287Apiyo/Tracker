package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private WebView webView;
    private Button buttonLoadChart;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        webView = findViewById(R.id.webView);
        buttonLoadChart = findViewById(R.id.buttonLoadChart);

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
                // Convert the data to JSON format with aggregation
                String jsonData = convertExpensesToJson(expenses);
                // Pass data to WebView
                webView.evaluateJavascript("javascript:receiveData('" + jsonData + "')", null);
            });
        });

        // Initialize BottomNavigationView and set up the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_statistics); // Set 'Statistics' as selected

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(StatisticsActivity.this, Home.class));
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(StatisticsActivity.this, add.class));
                return true;
            } else if (id == R.id.nav_transactions) {
                startActivity(new Intent(StatisticsActivity.this, TransactionsActivity.class));
                return true;
            } else if (id == R.id.nav_statistics) {
                // Current activity, no action needed
                return true;
            }
            return false;
        });
    }

    // Convert expenses to JSON with aggregation
    private String convertExpensesToJson(List<Expense> expenses) {
        Map<String, Double> aggregatedData = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory(); // Ensure this method exists
            double amount = expense.getAmount(); // Ensure this method exists

            // Aggregate amounts by category
            if (aggregatedData.containsKey(category)) {
                aggregatedData.put(category, aggregatedData.get(category) + amount);
            } else {
                aggregatedData.put(category, amount);
            }
        }

        // Prepare the data for the chart
        List<String> categories = new ArrayList<>(aggregatedData.keySet());
        List<Double> values = new ArrayList<>(aggregatedData.values());

        // Creating a JSON object
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("values", values);

        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
