package com.example.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private List<Expense> expenseList;

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.textViewAmount.setText(expense.getAmount());
        holder.textViewDate.setText(expense.getDate());
        holder.textViewCategory.setText(expense.getCategory());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void addExpense(Expense newExpense) {
        expenseList.add(newExpense);
        notifyItemInserted(expenseList.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewAmount;
        public TextView textViewDate;
        public TextView textViewCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
        }
    }
}
