package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper databaseHelper;

    public TransactionsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex("timestamp")); // Get timestamp
            @SuppressLint("Range") final int id = cursor.getInt(cursor.getColumnIndex("_id")); // Use the correct column name

            holder.dateTextView.setText(date);
            holder.categoryTextView.setText("Category: " + category);
            holder.amountTextView.setText("Amount: KES " + amount);
            holder.timestampTextView.setText("Time: " + timestamp); // Display timestamp

            holder.deleteImageView.setOnClickListener(v -> {
                databaseHelper.deleteTransaction(id);
                Cursor newCursor = databaseHelper.getAllTransactions();
                swapCursor(newCursor);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, categoryTextView, amountTextView, timestampTextView;
        ImageView deleteImageView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textViewDateHeader);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            amountTextView = itemView.findViewById(R.id.textViewAmount);
            timestampTextView = itemView.findViewById(R.id.textViewTimestamp); // New TextView for timestamp
            deleteImageView = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
