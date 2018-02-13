package com.expenso.aditya.expenso;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeDefault extends Fragment {
    RecyclerView expensesRecycler;
    LineChart dailyExpenses;
    ExpenseAdapter expenseAdapter;
    ScrollView scrollView;
    FloatingActionMenu menu;
    FloatingActionButton buttonExpense, buttonIncome;
    TextView emptyView;
    Database database;
    String month, year;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home_def, container, false);
        scrollView = view.findViewById(R.id.parentScrollView);
        expensesRecycler = view.findViewById(R.id.expense_list);
        dailyExpenses = view.findViewById(R.id.graph_expense);
        emptyView = view.findViewById(R.id.emptyExpenses);
        buttonExpense = view.findViewById(R.id.add_expense);
        buttonIncome = view.findViewById(R.id.add_income);

        database = new Database(getContext());

        menu = view.findViewById(R.id.fab_menu);
        menu.setClosedOnTouchOutside(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        expensesRecycler.setLayoutManager(mLayoutManager);
        expensesRecycler.setFocusable(false);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        month = dateFormat.format(calendar.getTime()).split("/")[0];
        year = dateFormat.format(calendar.getTime()).split("/")[1];

        final List<Expense> expenses = database.getExpensesForMonth(month, year);

        if (expenses.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            expensesRecycler.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            expensesRecycler.setVisibility(View.VISIBLE);
        }

        expenseAdapter = new ExpenseAdapter(getContext(), expenses);
        expensesRecycler.setAdapter(expenseAdapter);

        buttonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIncomeAlert();
                menu.close(true);
            }
        });

        buttonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseAlert();
                menu.close(true);
            }
        });

        int lastDate = Integer.parseInt(expenses.get(0).getDate().split("/")[0]);
        int firstDate = Integer.parseInt(expenses.get(expenses.size() - 1).getDate().split("/")[0]);
        ArrayList<Entry> yValues = new ArrayList<>();

        for (int i = firstDate; i<=lastDate; i++) {
            yValues.add(new Entry(i*1.5f, i));
        }

        LineDataSet set1 = new LineDataSet(yValues, "DataSet 1");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        dailyExpenses.setData(data);

        LimitLine limitLine = new LimitLine(10f, "Income");
        YAxis leftAxis = dailyExpenses.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        return view;
    }

    private void showExpenseAlert() {
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.alert_expense, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(alertView);

        final EditText amountEntered = alertView.findViewById(R.id.alert_expense_amount);
        final EditText descEntered = alertView.findViewById(R.id.alert_expense_desc);
        final Spinner typeEntered = alertView.findViewById(R.id.alert_expense_type);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Food");
        spinnerArray.add("Shopping");
        spinnerArray.add("Transport");
        spinnerArray.add("Debt");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeEntered.setAdapter(adapter);

        final Button addBtn = alertView.findViewById(R.id.alert_expense_add);
        final Button cancelBtn = alertView.findViewById(R.id.alert_expense_cancel);

        final AlertDialog expenseDialog = builder.create();

        expenseDialog.setCanceledOnTouchOutside(true);
        expenseDialog.show();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());
                String desc = descEntered.getText().toString().trim();
                String type = typeEntered.getSelectedItem().toString();
                int amount = 0;

                try {
                    amount = Integer.parseInt(amountEntered.getText().toString().trim());
                } catch (NumberFormatException e) {
                    amountEntered.setError("Amount value cannot be accepted");
                }

                if (amount <= 0) {
                    amountEntered.setError("Amount value cannot be accepted");
                    check = false;
                }
                if (desc.length() == 0) {
                    descEntered.setError("Description cannot be blank");
                    check = false;
                }
                if (type.length() == 0) {
                    check = false;
                }

                if (check){
                    database.addExpense(new Expense(desc, type, date, amount));
                    expenseDialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDialog.dismiss();
            }
        });

        expenseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                expenseAdapter = new ExpenseAdapter(getContext(), database.getExpensesForMonth(month, year));
                expensesRecycler.setAdapter(expenseAdapter);
            }
        });
    }

    private void showIncomeAlert() {
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.alert_income, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(alertView);

        final EditText amountEntered = alertView.findViewById(R.id.amount_alert);
        final Button addBtn = alertView.findViewById(R.id.alert_income_add);
        final Button cancelBtn = alertView.findViewById(R.id.alert_income_cancel);

        final AlertDialog incomeDialog = builder.create();

        incomeDialog.setCanceledOnTouchOutside(true);
        incomeDialog.show();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());
                int amount = 0;
                try {
                    amount = Integer.parseInt(amountEntered.getText().toString().trim());
                } catch (NumberFormatException e) {
                    amountEntered.setError("Amount value cannot be accepted");
                }

                if (amount <= 0) {
                    amountEntered.setError("Amount value cannot be accepted");
                } else {
                    database.addIncome(amount, date);
                    incomeDialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDialog.dismiss();
            }
        });
    }
}
