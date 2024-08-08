package com.example.tracker;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository repository;
    private final LiveData<List<Income>> allIncomes;
    private final LiveData<List<Expense>> allExpenses;
    private final MediatorLiveData<Double> totalBalance;
    private final MediatorLiveData<Double> totalIncome;
    private final MediatorLiveData<Double> totalExpense;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
        allIncomes = repository.getIncomes();
        allExpenses = repository.getExpenses();

        totalBalance = new MediatorLiveData<>();
        totalIncome = new MediatorLiveData<>();
        totalExpense = new MediatorLiveData<>();

        // Initialize total values
        totalBalance.setValue(0.0);
        totalIncome.setValue(0.0);
        totalExpense.setValue(0.0);

        // Add sources to MediatorLiveData
        totalBalance.addSource(allIncomes, incomes -> updateTotals(incomes, allExpenses.getValue()));
        totalBalance.addSource(allExpenses, expenses -> updateTotals(allIncomes.getValue(), expenses));
        totalIncome.addSource(allIncomes, incomes -> updateTotals(incomes, allExpenses.getValue()));
        totalExpense.addSource(allExpenses, expenses -> updateTotals(allIncomes.getValue(), expenses));
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

    public void deleteIncome(Income income) {
        repository.deleteIncome(income);
    }

    public void deleteExpense(Expense expense) {
        repository.deleteExpense(expense);
    }

    public LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    public LiveData<Double> getTotalIncome() {
        return totalIncome;
    }

    public LiveData<Double> getTotalExpense() {
        return totalExpense;
    }

    private void updateTotals(List<Income> incomes, List<Expense> expenses) {
        double totalIncomeValue = 0;
        double totalExpenseValue = 0;

        // Compute total income
        if (incomes != null) {
            for (Income income : incomes) {
                totalIncomeValue += income.getAmount();
            }
        }
        totalIncome.setValue(totalIncomeValue);

        // Compute total expense
        if (expenses != null) {
            for (Expense expense : expenses) {
                totalExpenseValue += expense.getAmount();
            }
        }
        totalExpense.setValue(totalExpenseValue);

        // Update total balance
        totalBalance.setValue(totalIncomeValue - totalExpenseValue);
    }
}
