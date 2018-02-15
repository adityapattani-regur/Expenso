package com.expenso.aditya.expenso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ScanHistory extends Fragment{
    RecyclerView scanHistoryList;
    List<History> histories = new ArrayList<>();
    Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_history, container, false);
        scanHistoryList = view.findViewById(R.id.scan_list);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        scanHistoryList.setLayoutManager(linearLayoutManager);
        database = new Database(getContext());

        histories = database.getScanResults();
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), histories);

        scanHistoryList.setAdapter(historyAdapter);

        return view;
    }
}
