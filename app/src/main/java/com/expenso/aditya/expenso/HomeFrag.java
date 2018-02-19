package com.expenso.aditya.expenso;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class HomeFrag extends Fragment {
    BarChart categoryExpenses;
    Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home_frag, container, false);
        categoryExpenses = view.findViewById(R.id.bar_chart);
        database = new Database(getContext());

        categoryExpenses.getXAxis().setEnabled(false);
        categoryExpenses.getAxisRight().setEnabled(false);
        categoryExpenses.setTouchEnabled(false);
        categoryExpenses.getLegend().setTextSize(14);
        categoryExpenses.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        categoryExpenses.getAxisLeft().setDrawGridLines(false);
        categoryExpenses.getAxisLeft().setEnabled(false);
        categoryExpenses.getDescription().setEnabled(false);
        categoryExpenses.setDrawValueAboveBar(false);

        constructBar();
        return view;
    }

    private void constructBar() {
        ArrayList<BarEntry> dataFood = new ArrayList<>();
        dataFood.add(new BarEntry(1, database.getExpenseForCategory("Food")));
        ArrayList<BarEntry> dataTransport = new ArrayList<>();
        dataTransport.add(new BarEntry(2, database.getExpenseForCategory("Transport")));
        ArrayList<BarEntry> dataShopping = new ArrayList<>();
        dataShopping.add(new BarEntry(3, database.getExpenseForCategory("Shopping")));
        ArrayList<BarEntry> dataDebt = new ArrayList<>();
        dataDebt.add(new BarEntry(4, database.getExpenseForCategory("Debt")));

        BarDataSet barDataSetFood = new BarDataSet(dataFood, "Food");
        barDataSetFood.setColor(Color.parseColor("#FFDA44"));
        barDataSetFood.setDrawValues(true);
        BarDataSet barDataSetTransport = new BarDataSet(dataTransport, "Transport");
        barDataSetTransport.setColor(Color.parseColor("#87D7FF"));
        barDataSetTransport.setDrawValues(true);
        BarDataSet barDataSetShopping = new BarDataSet(dataShopping, "Shopping");
        barDataSetShopping.setColor(Color.parseColor("#E64C3C"));
        barDataSetShopping.setDrawValues(true);
        BarDataSet barDataSetDebt = new BarDataSet(dataDebt, "Debt");
        barDataSetDebt.setColor(Color.parseColor("#4CAF50"));
        barDataSetDebt.setDrawValues(true);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSetFood);
        dataSets.add(barDataSetTransport);
        dataSets.add(barDataSetShopping);
        dataSets.add(barDataSetDebt);

        BarData barData = new BarData(dataSets);
        barData.setValueTextSize(16);
        categoryExpenses.setData(barData);
        categoryExpenses.invalidate();
    }
}
