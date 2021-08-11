package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Dashboard.LastActivity;
import com.akounto.accountingsoftware.Data.Dashboard.LastBankTransaction;
import com.akounto.accountingsoftware.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitiesDashboardAdapter extends RecyclerView.Adapter<ActivitiesDashboardAdapter.MyViewHolder> {
    private final int TYPE_ACTIVITY = 1;
    private final int TYPE_TRANSACTION = 0;
    List<LastActivity> activitiesItems;
    Context context;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";
    String transactionDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    List<LastBankTransaction> transactionsItems;
    private final int type;

    public ActivitiesDashboardAdapter(Context context2, List<LastActivity> list, List<LastBankTransaction> lastBankTransactions, int type2) {
        this.context = context2;
        this.activitiesItems = list;
        this.transactionsItems = lastBankTransactions;
        this.type = type2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_activities_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        String str2;
        String date;
        String prefix;
        String str3;
        String str4;
        int i = this.type;
        String amount = "";
        if (i == this.TYPE_ACTIVITY) {
            TextView textView = holder.title;
            if (TextUtils.isEmpty(this.activitiesItems.get(position).getTransactionHeadName())) {
                str3 = "Activity";
            } else {
                str3 = this.activitiesItems.get(position).getTransactionHeadName();
            }
            textView.setText(str3);
            TextView textView2 = holder.desc;
            if (TextUtils.isEmpty(this.activitiesItems.get(position).getRemarks())) {
                str4 = "--";
            } else {
                str4 = this.activitiesItems.get(position).getRemarks();
            }
            textView2.setText(str4);
            if (!TextUtils.isEmpty(this.activitiesItems.get(position).getCreated())) {
                amount = this.activitiesItems.get(position).getCreated();
            }
            try {
                Date inputDate = new SimpleDateFormat(this.isoDatePattern, Locale.US).parse(amount);
                SimpleDateFormat outPutFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.US);
                TextView textView3 = holder.date;
                textView3.setText("created on " + outPutFormat.format(inputDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.amount.setVisibility(View.GONE);
        } else if (i == this.TYPE_TRANSACTION) {
            TextView textView4 = holder.title;
            if (TextUtils.isEmpty(this.transactionsItems.get(position).getTransactionHeadName())) {
                str = "Transaction";
            } else {
                str = this.transactionsItems.get(position).getTransactionHeadName();
            }
            textView4.setText(str);
            TextView textView5 = holder.desc;
            if (TextUtils.isEmpty(this.transactionsItems.get(position).getBankName())) {
                str2 = "Bank";
            } else {
                str2 = this.transactionsItems.get(position).getBankName();
            }
            textView5.setText(str2);
            if (TextUtils.isEmpty(this.transactionsItems.get(position).getTransactionDate())) {
                date = amount;
            } else {
                date = this.transactionsItems.get(position).getTransactionDate();
            }
            try {
                Date inputDate2 = new SimpleDateFormat(this.transactionDateFormat, Locale.US).parse(date);
                holder.date.setText(new SimpleDateFormat("dd MMM yyyy", Locale.US).format(inputDate2));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            holder.amount.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(String.valueOf(this.transactionsItems.get(position).getAmount()))) {
                amount = String.valueOf(this.transactionsItems.get(position).getAmount());
            }
            if (this.transactionsItems.get(position).getTransactionType() == 1) {
                prefix = "+$";
            } else {
                prefix = "-$";
            }
            TextView textView6 = holder.amount;
            textView6.setText(prefix + amount);
        }
    }

    public int getItemCount() {
        if (this.type == this.TYPE_TRANSACTION) {
            return this.transactionsItems.size();
        }
        return this.activitiesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView date;
        TextView desc;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.date = itemView.findViewById(R.id.date);
            this.desc = itemView.findViewById(R.id.desc);
            this.amount = itemView.findViewById(R.id.amount);
        }
    }
}
