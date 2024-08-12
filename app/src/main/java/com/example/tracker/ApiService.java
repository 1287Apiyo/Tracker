package com.example.tracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Get all incomes
    @GET("incomes")
    Call<List<Income>> getIncomes();

    // Create a new income
    @POST("incomes")
    Call<Income> createIncome(@Body Income income);

    // Delete an income by ID
    @DELETE("incomes/{id}")
    Call<Void> deleteIncome(@Path("id") int id);

    // Get all expenses
    @GET("expenses")
    Call<List<Expense>> getExpenses();

    // Create a new expense
    @POST("expenses")
    Call<Expense> createExpense(@Body Expense expense);

    // Delete an expense by ID
    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(@Path("id") int id);
}
