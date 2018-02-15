package com.expenso.aditya.expenso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private Context context;
    private List<History> histories = new ArrayList<>();

    HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.histories = historyList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView historyType, historyText;
        ImageView historyImage;

        MyViewHolder(View itemView) {
            super(itemView);
            historyType = itemView.findViewById(R.id.history_type);
            historyText = itemView.findViewById(R.id.history_result);
            historyImage = itemView.findViewById(R.id.history_image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_element, parent, false);
        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final History history = histories.get(position);
        holder.historyText.setText(history.getResultText());

        String type = history.getResultFormat();
        holder.historyType.setText(type);

        switch (type){
            case "QR CODE":
                Glide.with(context).load(R.drawable.ic_qr_code).into(holder.historyImage);
                break;
            default:
                Glide.with(context).load(R.drawable.ic_barcode).into(holder.historyImage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
