package com.example.tracker;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Income.class, Expense.class}, version = 1, exportSchema = false)
public abstract class TransactionDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static volatile TransactionDatabase INSTANCE;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static TransactionDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TransactionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TransactionDatabase.class, "transaction_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
