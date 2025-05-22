package com.example.mzuzuuniversityexpensestracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mzuzuuniversityexpensestracker.database.DatabaseHelper;
import com.example.mzuzuuniversityexpensestracker.expense.Expense;
import com.example.mzuzuuniversityexpensestracker.expense.ExpenseAdapter;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnAddExpense;
    private RecyclerView rvExpenses;
    private ExpenseAdapter adapter;
    private ArrayList<Expense> expenseList;
    private DatabaseHelper db;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddExpense = findViewById(R.id.btnAddExpense);
        rvExpenses = findViewById(R.id.rvExpenses);

        btnAddExpense.setOnClickListener(v -> showAddExpenseDialog());

        db = new DatabaseHelper(this);

        // Read data from SQLite
        expenseList = db.getAllExpenses();

        // Set up RecyclerView
        adapter = new ExpenseAdapter(expenseList, expense -> {
            db.deleteExpense(expense);
            expenseList.clear();
            expenseList.addAll(db.getAllExpenses());
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Expense deleted", Toast.LENGTH_SHORT).show();
        });
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        rvExpenses.setAdapter(adapter);
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add New Expense");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputDate = new EditText(MainActivity.this);
        inputDate.setHint("Enter date (e.g. 2025-05-22)");
        layout.addView(inputDate);

        final EditText inputAmount = new EditText(MainActivity.this);
        inputAmount.setHint("Enter amount");
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputAmount);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String date = inputDate.getText().toString().trim();
            String amountText = inputAmount.getText().toString().trim();

            if (date.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to database and refresh UI
            Expense newExpense = new Expense(date, amount);
            db.insertExpense(newExpense);
            expenseList.clear();
            expenseList.addAll(db.getAllExpenses());
            adapter.notifyDataSetChanged();

            Toast.makeText(MainActivity.this, "Expense added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

}