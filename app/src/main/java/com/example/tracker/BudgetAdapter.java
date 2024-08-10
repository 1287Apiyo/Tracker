package com.example.tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private final ArrayList<Budget> budgetList;

    public BudgetAdapter(ArrayList<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget currentBudget = budgetList.get(position);
        holder.categoryTextView.setText(currentBudget.getCategory());
        holder.amountTextView.setText(String.format("$%.2f", currentBudget.getAmount()));
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTextView;
        public TextView amountTextView;

        public BudgetViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            amountTextView = itemView.findViewById(R.id.textViewAmount);
        }
    }
}
