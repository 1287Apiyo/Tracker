package com.example.Tracker;

public class Transaction {
    private String description;
    private String amount;

    public Transaction(String description, String amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
}
