package com.example.mzuzuuniversityexpensestracker.expense;
public class Expense {
    private String date;
    private double amount;

    public Expense(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}

