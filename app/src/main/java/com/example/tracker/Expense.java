package com.example.tracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    private String date;
    private String category;

    // Public no-argument constructor
    public Expense() {
        // Required by Room for entity instantiation
    }

    // Constructor with parameters
    public Expense(double amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
}