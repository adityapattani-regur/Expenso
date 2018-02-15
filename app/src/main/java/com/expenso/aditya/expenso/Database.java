package com.expenso.aditya.expenso;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Expenses.db";
    private static final String TABLE_EXPENSES = "Expenses";
    private static final String TABLE_INCOMES = "Incomes";
    private static final String TABLE_SCANS = "Scans";
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

        String CREATE_SCANS = "CREATE TABLE " + TABLE_SCANS +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "resultText VARCHAR(20) NOT NULL, " +
                "resultFormat VARCHAR(20) NOT NULL, " +
                "resultTimestamp VARCHAR(20) NOT NULL, " +
                "date DATE NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_EXPENSES);
        sqLiteDatabase.execSQL(CREATE_INCOMES);
        sqLiteDatabase.execSQL(CREATE_SCANS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLES = "DROP TABLE " + TABLE_EXPENSES + ", " + TABLE_INCOMES + ", " + TABLE_SCANS + ";";
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

        String SELECT_ALL = "SELECT * FROM " + TABLE_EXPENSES + " WHERE date LIKE '%-" + month + "-" + year + "' ORDER BY id DESC;";
        Cursor selectCursor = db.rawQuery(SELECT_ALL, null);

        if (selectCursor.moveToFirst()) {
            do {
                expenses.add(new Expense(selectCursor.getString(selectCursor.getColumnIndex("description")), selectCursor.getString(selectCursor.getColumnIndex("type")), selectCursor.getString(selectCursor.getColumnIndex("date")), selectCursor.getInt(selectCursor.getColumnIndex("amount"))));
            } while (selectCursor.moveToNext());
        }

        selectCursor.close();
        return expenses;
    }

    JSONObject getGraphData(String month, String year) {
        SQLiteDatabase db = getReadableDatabase();
        JSONObject result = new JSONObject();
        String SELECT_MAX_DATE = "SELECT MAX(date) FROM " + TABLE_EXPENSES + " WHERE date LIKE '%-" + month + "-" + year + "';";
        String SELECT_MIN_DATE = "SELECT MIN(date) FROM " + TABLE_EXPENSES + " WHERE date LIKE '%-" + month + "-" + year + "';";
        String SELECT_MAX_EXPENSE = "SELECT MAX(amount) FROM " + TABLE_EXPENSES + " WHERE date LIKE '%-" + month + "-" + year + "';";
        String SELECT_TOTAL_INCOME = "SELECT SUM(amount) FROM " + TABLE_INCOMES + " WHERE date LIKE '%-" + month + "-" + year + "';";

        Cursor values = null;
        try {
            values = db.rawQuery(SELECT_MAX_DATE, null);
            values.moveToFirst();
            result.put("lastDate", values.getString(0));

            values = db.rawQuery(SELECT_MIN_DATE, null);
            values.moveToFirst();
            result.put("firstDate", values.getString(0));

            values = db.rawQuery(SELECT_MAX_EXPENSE, null);
            values.moveToFirst();
            result.put("maxExpense", values.getString(0));

            values = db.rawQuery(SELECT_TOTAL_INCOME, null);
            values.moveToFirst();
            result.put("totalIncome", values.getString(0));
        } catch (JSONException e) {
            Log.e("JSON ERROR", e.toString());
        }

        Log.e("Result", result.toString());
        values.close();
        return result;
    }

    float getExpensesForDate(int date, String month, String year) {
        String day;
        if(date < 10) {
            day = "0" + date + "-" + month + "-" + year;
        }
        else {
            day = date + "-" + month + "-" + year;
        }
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_EXPENSE = "SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE date = '" + day + "';";
        Cursor expense = db.rawQuery(SELECT_EXPENSE, null);
        expense.moveToFirst();
        float result = expense.getFloat(0);
        expense.close();
        return result;
    }

    float getExpenseForCategory(String value) {
        float result;
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        String monthYear = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = getReadableDatabase();
        String SELECT_QUERY = "SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE type = '" + value + "' AND date LIKE '%-" + monthYear + "';";
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        cursor.moveToFirst();
        result = cursor.getFloat(0);
        cursor.close();
        return result;
    }

    void addScanResults(String text, String type, String timestamp) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("resultText", text);
        contentValues.put("resultFormat", type);
        contentValues.put("resultTimestamp", timestamp);
        contentValues.put("date", date);

        db.insert(TABLE_SCANS, null, contentValues);
    }

    List<History> getScanResults() {
        SQLiteDatabase db = getReadableDatabase();
        List<History> historyList = new ArrayList<>();
        String SELECT_SCAN_RES = "SELECT * FROM " + TABLE_SCANS + " ORDER BY id DESC;";

        Cursor selectCursor = db.rawQuery(SELECT_SCAN_RES, null);
        if(selectCursor.moveToFirst()) {
            do {
                historyList.add(new History(selectCursor.getString(1), selectCursor.getString(2), selectCursor.getString(3)));
            } while (selectCursor.moveToNext());
        }

        selectCursor.close();
        return historyList;
    }
}
