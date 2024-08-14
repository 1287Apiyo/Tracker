package com.example.tracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("incomes")
    Call<Void> addIncome(@Body Income income);

    @POST("expenses")
    Call<Void> addExpense(@Body Expense expense);

    @DELETE("incomes/{id}")
    Call<Void> deleteIncome(@Path("id") int id);

    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(@Path("id") int id);

    @GET("incomes")
    Call<List<Income>> getIncomes();

    @GET("expenses")
    Call<List<Expense>> getExpenses();
}

