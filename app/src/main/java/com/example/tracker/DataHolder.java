package com.example.tracker;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

    private static DataHolder instance;
    private List<Income> incomes;
    private List<Expense> expenses;

    private DataHolder() {
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void addIncome(Income income) {
        incomes.add(income);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}
