package com.expenso.aditya.expenso;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {

    private final Context context;
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseAdapterListener listener;
    private Database database;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDesc, expenseAmount, expenseType, expensePopup;
        ImageView expenseImage;

        MyViewHolder(final View itemView) {
            super(itemView);
            expenseDesc = itemView.findViewById(R.id.expense_desc);
            expenseAmount = itemView.findViewById(R.id.expense_amount);
            expenseType = itemView.findViewById(R.id.expense_type);
            expenseImage = itemView.findViewById(R.id.expense_image);
            expensePopup = itemView.findViewById(R.id.expense_popup);

            expensePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, expensePopup);
                    popup.inflate(R.menu.popup_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.id_edit:
                                    listener.onEditSelected(expenseList.get(getAdapterPosition()));
                                    break;
                                case R.id.id_delete:
                                    listener.onDeleteSelected(expenseList.get(getAdapterPosition()));
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    void setExpenses(String month, String year) {
        database = new Database(context);
        expenseList = database.getExpensesForMonth(month, year);
        notifyDataSetChanged();
    }

    ExpenseAdapter(Context context, List<Expense> expenses, ExpenseAdapterListener listener) {
        this.context = context;
        this.expenseList = expenses;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        database = new Database(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Expense expense = expenseList.get(position);
        holder.expenseDesc.setText(expense.getDescription());
        String amount = "₹ " + String.valueOf(expense.getAmount());
        holder.expenseAmount.setText(amount);

        String type = expense.getType();
        holder.expenseType.setText(type);

        switch (type){
            case "Food":
                Glide.with(context).load(R.drawable.ic_food).into(holder.expenseImage);
                holder.expenseType.setTextColor(Color.parseColor("#FFDA44"));
                break;
            case "Shopping":
                Glide.with(context).load(R.drawable.ic_shopping).into(holder.expenseImage);
                holder.expenseType.setTextColor(Color.parseColor("#E64C3C"));
                break;
            case "Transport":
                Glide.with(context).load(R.drawable.ic_transport).into(holder.expenseImage);
                holder.expenseType.setTextColor(Color.parseColor("#87D7FF"));
                break;
            case "Debt":
                Glide.with(context).load(R.drawable.ic_debt).into(holder.expenseImage);
                holder.expenseType.setTextColor(Color.parseColor("#EAEAD7"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    interface ExpenseAdapterListener {
        void onEditSelected(Expense expense);
        void onDeleteSelected(Expense expense);
    }
}
