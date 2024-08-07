package com.example.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final LayoutInflater inflater;
    private List<Expense> expenses;
    private final OnItemClickListener onItemClickListener;

    public ExpenseAdapter(Context context, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        if (expenses != null) {
            Expense current = expenses.get(position);
            holder.bind(current);
        }
    }

    @Override
    public int getItemCount() {
        return expenses != null ? expenses.size() : 0;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAmount;
        private final TextView textViewDate;
        private final TextView textViewCategory;
        private final ImageButton deleteButton;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onDeleteClick(expenses.get(position));
                }
            });
        }

        public void bind(Expense expense) {
            textViewAmount.setText(String.valueOf(expense.getAmount()));
            textViewDate.setText(expense.getDate());
            textViewCategory.setText(expense.getCategory());
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(Expense expense);
    }
}
