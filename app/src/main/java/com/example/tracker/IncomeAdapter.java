package com.example.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private final LayoutInflater inflater;
    private List<Income> incomes;

    public IncomeAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        if (incomes != null) {
            Income current = incomes.get(position);
            holder.bind(current);
        }
    }

    @Override
    public int getItemCount() {
        return incomes != null ? incomes.size() : 0;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
        notifyDataSetChanged();
    }

    class IncomeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAmount;
        private final TextView textViewDate;
        private final TextView textViewSource;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewSource = itemView.findViewById(R.id.textViewCategory);
        }

        public void bind(Income income) {
            textViewAmount.setText(String.valueOf(income.getAmount()));
            textViewDate.setText(income.getDate());
            textViewSource.setText(income.getSource());
        }
    }
}
