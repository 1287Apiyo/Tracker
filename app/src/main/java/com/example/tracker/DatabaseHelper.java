package com.example.tracker;

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
        String insert = "INSERT INTO " + TABLE_TRANSACTIONS + " ("
                + COLUMN_DATE + ", "
                + COLUMN_CATEGORY + ", "
                + COLUMN_AMOUNT + ", "
                + COLUMN_TIMESTAMP + ") VALUES (?, ?, ?, ?)";
        db.execSQL(insert, new Object[]{transaction.getDate(), transaction.getCategory(), transaction.getAmount(), transaction.getTimestamp()});
        db.close();
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC", null);
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
