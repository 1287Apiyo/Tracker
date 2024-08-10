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

    @Delete
    void deleteIncome(Income income);

    @Delete
    void deleteExpense(Expense expense);
}
