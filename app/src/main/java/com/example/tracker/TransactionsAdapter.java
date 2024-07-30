package com.example.tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private Context context;
    private Cursor cursor;

    public TransactionsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));

            holder.textViewDateHeader.setText(date);
            holder.textViewCategory.setText(category);
            holder.textViewAmount.setText(String.valueOf(amount));
            holder.textViewTimestamp.setText(timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDateHeader;
        public TextView textViewCategory;
        public TextView textViewAmount;
        public TextView textViewTimestamp;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            textViewDateHeader = itemView.findViewById(R.id.textViewDateHeader);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
