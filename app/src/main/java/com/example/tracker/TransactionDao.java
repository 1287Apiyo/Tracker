package com.example.tracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM income_table")
    LiveData<List<Income>> getAllIncomes();

    @Query("SELECT * FROM expense_table")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expense_table WHERE category = :category")
    List<Expense> getExpensesByCategory(String category);  // New method to get expenses by category

    @Insert
    void insertIncome(Income income);

    @Insert
    void insertExpense(Expense expense);

    @Insert
    void insertIncomes(List<Income> incomes); // Method to insert multiple incomes

    @Insert
    void insertExpenses(List<Expense> expenses); // Method to insert multiple expenses

    @Delete
    void deleteIncome(Income income);

    @Delete
    void deleteExpense(Expense expense);

    @Query("DELETE FROM income_table")
    void deleteAllIncomes(); // Method to delete all incomes

    @Query("DELETE FROM expense_table")
    void deleteAllExpenses(); // Method to delete all expenses
}
