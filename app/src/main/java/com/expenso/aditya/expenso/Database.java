package com.expenso.aditya.expenso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Expenses.db";
    private static final String TABLE_EXPENSES = "Expenses";
    private static final String TABLE_INCOMES = "Incomes";
    private static final int DATABASE_VERSION = 1;

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type VARCHAR(10) NOT NULL, " +
                "amount INT(6) NOT NULL, " +
                "description VARCHAR(250) NOT NULL, " +
                "date DATE NOT NULL);";

        String CREATE_INCOMES = "CREATE TABLE " + TABLE_INCOMES +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount INT(6) NOT NULL, " +
                "date DATE NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_EXPENSES);
        sqLiteDatabase.execSQL(CREATE_INCOMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLES = "DROP TABLE " + TABLE_EXPENSES + ", " + TABLE_INCOMES + ";";
        sqLiteDatabase.execSQL(DROP_TABLES);
        onCreate(sqLiteDatabase);
    }

    void addExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", expense.getType());
        contentValues.put("amount", expense.getAmount());
        contentValues.put("description", expense.getDescription());
        contentValues.put("date", expense.getDate());

        db.insert(TABLE_EXPENSES, null, contentValues);
    }

    void addIncome(int amount, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("amount", amount);
        contentValues.put("date", date);

        db.insert(TABLE_INCOMES, null, contentValues);
    }

    List<Expense> getExpensesForMonth(String month, String year) {
        SQLiteDatabase db = getReadableDatabase();
        List<Expense> expenses = new ArrayList<>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_EXPENSES + " WHERE date LIKE '%/" + month + "/" + year + "' ORDER BY date DESC;";
        Cursor selectCursor = db.rawQuery(SELECT_ALL, null);

        if (selectCursor.moveToFirst()) {
            do {
                expenses.add(new Expense(selectCursor.getString(selectCursor.getColumnIndex("description")), selectCursor.getString(selectCursor.getColumnIndex("type")), selectCursor.getString(selectCursor.getColumnIndex("date")), selectCursor.getInt(selectCursor.getColumnIndex("amount"))));
            } while (selectCursor.moveToNext());
        }

        selectCursor.close();
        return expenses;
    }
}
