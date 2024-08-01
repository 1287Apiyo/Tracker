package com.example.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema update

    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TIMESTAMP = "timestamp"; // New column for timestamp

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_TIMESTAMP + " TEXT)"; // Add timestamp column
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + " ADD COLUMN " + COLUMN_TIMESTAMP + " TEXT");
        }
    }

    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TIMESTAMP, transaction.getTimestamp());
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC", null);
    }

    public Cursor getRecentTransactions(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_TRANSACTIONS,          // Table name
                null,                        // Columns (null for all)
                null,                        // Selection (null for all)
                null,                        // Selection args (null for all)
                null,                        // Group by (null for none)
                null,                        // Having (null for none)
                COLUMN_TIMESTAMP + " DESC",  // Order by timestamp in descending order
                String.valueOf(limit)        // Limit the number of results
        );
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}