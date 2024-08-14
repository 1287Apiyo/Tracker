package com.example.tracker;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final LiveData<List<Income>> allIncomes;
    private final LiveData<List<Expense>> allExpenses;
    private final ApiService apiService; // Retrofit API service

    public TransactionRepository(Application application) {
        TransactionDatabase db = TransactionDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        allIncomes = transactionDao.getAllIncomes();
        allExpenses = transactionDao.getAllExpenses();
        apiService = RetrofitClient.getClient("http://your-server-address/").create(ApiService.class);
    }

    public LiveData<List<Income>> getIncomes() {
        return allIncomes;
    }

    public LiveData<List<Expense>> getExpenses() {
        return allExpenses;
    }

    public void insert(Income income) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
            transactionDao.insertIncome(income);
            uploadIncomeToServer(income);
        });
    }

    public void insert(Expense expense) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
            transactionDao.insertExpense(expense);
            uploadExpenseToServer(expense);
        });
    }

    public void deleteIncome(Income income) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
            transactionDao.deleteIncome(income);
            deleteIncomeFromServer(income);
        });
    }

    public void deleteExpense(Expense expense) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
            transactionDao.deleteExpense(expense);
            deleteExpenseFromServer(expense);
        });
    }

    public void addIncome(Income income, ApiCallback<Void> callback) {
        apiService.addIncome(income).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(null);
                } else {
                    callback.onFailure("Failed to add income");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void addExpense(Expense expense, ApiCallback<Void> callback) {
        apiService.addExpense(expense).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(null);
                } else {
                    callback.onFailure("Failed to add expense");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void deleteIncome(Income income, ApiCallback<Void> callback) {
        apiService.deleteIncome(income.getId()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(null);
                } else {
                    callback.onFailure("Failed to delete income");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void deleteExpense(Expense expense, ApiCallback<Void> callback) {
        apiService.deleteExpense(expense.getId()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(null);
                } else {
                    callback.onFailure("Failed to delete expense");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void fetchIncomes(ApiCallback<List<Income>> callback) {
        apiService.getIncomes().enqueue(new retrofit2.Callback<List<Income>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Income>> call, retrofit2.Response<List<Income>> response) {
                if (response.isSuccessful()) {
                    List<Income> incomes = response.body();
                    if (incomes != null) {
                        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
                            transactionDao.deleteAllIncomes();
                            transactionDao.insertIncomes(incomes);
                        });
                    }
                    callback.onResponse(incomes);
                } else {
                    callback.onFailure("Failed to fetch incomes");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Income>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void fetchExpenses(ApiCallback<List<Expense>> callback) {
        apiService.getExpenses().enqueue(new retrofit2.Callback<List<Expense>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Expense>> call, retrofit2.Response<List<Expense>> response) {
                if (response.isSuccessful()) {
                    List<Expense> expenses = response.body();
                    if (expenses != null) {
                        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> {
                            transactionDao.deleteAllExpenses();
                            transactionDao.insertExpenses(expenses);
                        });
                    }
                    callback.onResponse(expenses);
                } else {
                    callback.onFailure("Failed to fetch expenses");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Expense>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private void uploadIncomeToServer(Income income) {
        addIncome(income, new ApiCallback<Void>() {
            @Override
            public void onResponse(Void result) {
                Log.d("UploadIncome", "Income uploaded successfully.");
            }

            @Override
            public void onFailure(String error) {
                Log.e("UploadIncome", "Failed to upload income: " + error);
            }
        });
    }

    private void uploadExpenseToServer(Expense expense) {
        addExpense(expense, new ApiCallback<Void>() {
            @Override
            public void onResponse(Void result) {
                // Handle successful upload if needed
            }

            @Override
            public void onFailure(String error) {
                // Handle failure to upload if needed
            }
        });
    }

    private void deleteIncomeFromServer(Income income) {
        deleteIncome(income, new ApiCallback<Void>() {
            @Override
            public void onResponse(Void result) {
                // Handle successful deletion if needed
            }

            @Override
            public void onFailure(String error) {
                // Handle failure to delete if needed
            }
        });
    }

    private void deleteExpenseFromServer(Expense expense) {
        deleteExpense(expense, new ApiCallback<Void>() {
            @Override
            public void onResponse(Void result) {
                // Handle successful deletion if needed
            }

            @Override
            public void onFailure(String error) {
                // Handle failure to delete if needed
            }
        });
    }
}
