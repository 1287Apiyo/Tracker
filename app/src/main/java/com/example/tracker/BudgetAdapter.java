package com.example.tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgetList;
    private OnDeleteClickListener onDeleteClickListener;

    public BudgetAdapter(List<Budget> budgetList, OnDeleteClickListener onDeleteClickListener) {
        this.budgetList = budgetList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "KE"));
        String formattedAmount = currencyFormat.format(budget.getAmount());

        holder.textViewCategory.setText(budget.getCategory());
        holder.textViewAmount.setText(formattedAmount);
        holder.imageViewDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(budget);
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgetList = budgets;
        notifyDataSetChanged();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Budget budget);
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategory;
        TextView textViewAmount;
        ImageView imageViewDelete;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete); // Make sure to add this in item_budget.xml
        }
    }
}
