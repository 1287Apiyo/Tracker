package com.example.tracker;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private static DataHolder instance;
    private final List<Income> incomes = new ArrayList<>();
    private final List<Expense> expenses = new ArrayList<>();

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public void addIncome(Income income) {
        incomes.add(income);
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public static class Income {
        private final String amount;
        private final String date;
        private final String source;

        public Income(String amount, String date, String source) {
            this.amount = amount;
            this.date = date;
            this.source = source;
        }

        public String getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }

        public String getSource() {
            return source;
        }
    }

    public static class Expense {
        private final String amount;
        private final String date;
        private final String source;

        public Expense(String amount, String date, String source) {
            this.amount = amount;
            this.date = date;
            this.source = source;
        }

        public String getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }

        public String getSource() {
            return source;
        }
    }
}
