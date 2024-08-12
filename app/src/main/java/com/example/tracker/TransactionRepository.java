package com.example.tracker;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final LiveData<List<Income>> allIncomes;
    private final LiveData<List<Expense>> allExpenses;

    public TransactionRepository(Application application) {
        TransactionDatabase db = TransactionDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        allIncomes = transactionDao.getAllIncomes();
        allExpenses = transactionDao.getAllExpenses();
    }

    public LiveData<List<Income>> getIncomes() {
        return allIncomes;
    }

    public LiveData<List<Expense>> getExpenses() {
        return allExpenses;
    }

    public void insert(Income income) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> transactionDao.insertIncome(income));
    }

    public void insert(Expense expense) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> transactionDao.insertExpense(expense));
    }

    public void deleteIncome(Income income) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> transactionDao.deleteIncome(income));
    }

    public void deleteExpense(Expense expense) {
        TransactionDatabase.getDatabaseWriteExecutor().execute(() -> transactionDao.deleteExpense(expense));
    }
}