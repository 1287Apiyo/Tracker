package com.example.tracker;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository repository;
    private final LiveData<List<Income>> allIncomes;
    private final LiveData<List<Expense>> allExpenses;
    private final MediatorLiveData<Double> totalBalance;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
        allIncomes = repository.getIncomes();
        allExpenses = repository.getExpenses();

        totalBalance = new MediatorLiveData<>();

        // Add sources to MediatorLiveData
        totalBalance.addSource(allIncomes, incomes -> updateTotalBalance(incomes, allExpenses.getValue()));
        totalBalance.addSource(allExpenses, expenses -> updateTotalBalance(allIncomes.getValue(), expenses));
    }

    public LiveData<List<Income>> getAllIncomes() {
        return allIncomes;
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Income income) {
        repository.insert(income);
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    private void updateTotalBalance(List<Income> incomes, List<Expense> expenses) {
        double totalIncome = 0;
        double totalExpense = 0;

        if (incomes != null) {
            for (Income income : incomes) {
                totalIncome += income.getAmount();
            }
        }

        if (expenses != null) {
            for (Expense expense : expenses) {
                totalExpense += expense.getAmount();
            }
        }

        totalBalance.setValue(totalIncome - totalExpense);
    }
}
