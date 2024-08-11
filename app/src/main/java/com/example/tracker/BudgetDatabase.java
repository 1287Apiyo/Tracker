package com.example.tracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Budget.class}, version = 1)
public abstract class BudgetDatabase extends RoomDatabase {
    private static BudgetDatabase instance;

    public abstract BudgetDao budgetDao();

    public static synchronized BudgetDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BudgetDatabase.class, "budget_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
