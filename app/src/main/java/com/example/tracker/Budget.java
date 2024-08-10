package com.example.tracker;

import java.io.Serializable;

public class Budget implements Serializable {
    private int id;  // Unique identifier for each budget
    private String category;
    private double amount;

    // Public no-argument constructor
    public Budget() {
        // Required for deserialization
    }

    // Constructor with parameters
    public Budget(String category, double amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
