package com.example.tracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "income_table")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    private String date;
    private String source;

    // Public no-argument constructor
    public Income() {
    }

    // Constructor with parameters
    public Income(double amount, String date, String source) {
        this.amount = amount;
        this.date = date;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
