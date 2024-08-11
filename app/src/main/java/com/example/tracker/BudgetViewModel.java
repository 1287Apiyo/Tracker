package com.example.tracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {

    private BudgetRepository budgetRepository;
    private LiveData<List<Budget>> allBudgets;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        budgetRepository = new BudgetRepository(application);
        allBudgets = budgetRepository.getAllBudgets();
    }

    public LiveData<List<Budget>> getAllBudgets() {
        return allBudgets;
    }

    public void insert(Budget budget) {
        budgetRepository.insert(budget);
    }
}