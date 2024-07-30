package com.example.tracker;

public class Transaction {
    private String date;
    private String category;
    private double amount;
    private String timestamp; // New attribute

    public Transaction(String date, String category, double amount, String timestamp) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
