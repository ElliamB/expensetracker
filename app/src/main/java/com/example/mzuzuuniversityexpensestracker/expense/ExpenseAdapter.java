package com.example.mzuzuuniversityexpensestracker.expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.mzuzuuniversityexpensestracker.R;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Expense expense);
    }

    public ExpenseAdapter(List<Expense> expenseList, OnDeleteClickListener deleteListener) {
        this.expenseList = expenseList;
        this.deleteListener = deleteListener;
    }

    public void updateList(List<Expense> newList) {
        this.expenseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense e = expenseList.get(position);
        holder.tvDate.setText(e.getDate());
        holder.tvAmount.setText("MWK " + e.getAmount());
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(e));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvAmount;
        ImageButton btnDelete;

        ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}
