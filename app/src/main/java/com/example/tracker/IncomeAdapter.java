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

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private final LayoutInflater inflater;
    private List<Income> incomes;
    private final OnItemClickListener onItemClickListener;

    public IncomeAdapter(Context context, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = listener;
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
        private final ImageButton deleteButton;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewSource = itemView.findViewById(R.id.textViewCategory);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onDeleteClick(incomes.get(position));
                }
            });
        }

        public void bind(Income income) {
            textViewAmount.setText(String.valueOf(income.getAmount()));
            textViewDate.setText(income.getDate());
            textViewSource.setText(income.getSource());
        }
    }

    public interface OnItemClickListener {
        void onIncomeClick(Income income);

        void onExpenseClick(Expense expense);

        void onDeleteClick(Income income);
    }
}
