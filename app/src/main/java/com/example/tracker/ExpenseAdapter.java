package com.example.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final LayoutInflater inflater;
    private List<Expense> expenses;

    public ExpenseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
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

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
        }

        public void bind(Expense expense) {
            textViewAmount.setText(String.valueOf(expense.getAmount()));
            textViewDate.setText(expense.getDate());
            textViewCategory.setText(expense.getCategory());
        }
    }
}
