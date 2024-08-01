package com.example.tracker;

public class Expense {
    private String amount;
    private String date;
    private String category;

    // Constructor
    public Expense(String amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters
    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
