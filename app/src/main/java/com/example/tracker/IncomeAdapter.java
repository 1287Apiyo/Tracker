package com.example.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    private Context context;
    private List<Income> incomeList;

    public IncomeAdapter(Context context, List<Income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_income, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Income income = incomeList.get(position);
        holder.textViewAmount.setText(income.getAmount());
        holder.textViewDate.setText(income.getDate());
        holder.textViewCategory.setText(income.getCategory());
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public void addIncome(Income newIncome) {
        incomeList.add(newIncome);
        notifyItemInserted(incomeList.size() - 1);
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
