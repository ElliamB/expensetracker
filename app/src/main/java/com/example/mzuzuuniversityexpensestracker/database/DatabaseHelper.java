package com.example.mzuzuuniversityexpensestracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mzuzuuniversityexpensestracker.expense.Expense;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AMOUNT = "amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "date = ? AND amount = ?", new String[]{expense.getDate(), String.valueOf(expense.getAmount())});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public void insertExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, expense.getDate());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                expenses.add(new Expense(date, amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }
}

