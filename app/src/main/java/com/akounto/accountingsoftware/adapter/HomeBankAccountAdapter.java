package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Dashboard.BankBalance;
import com.akounto.accountingsoftware.R;

import java.util.List;

public class HomeBankAccountAdapter extends RecyclerView.Adapter<HomeBankAccountAdapter.MyViewHolder> {
    private final List<BankBalance> bankAccounts;
    Context context;

    public HomeBankAccountAdapter(Context context2, List<BankBalance> bankAccounts2) {
        this.context = context2;
        this.bankAccounts = bankAccounts2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homebankaccount, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        String str2;
        MyViewHolder vh = holder;
        TextView textView = vh.tv_amount;
        String str3 = "--";
        if (TextUtils.isEmpty(String.valueOf(this.bankAccounts.get(position).getAmount()))) {
            str = str3;
        } else {
            str = "$" + this.bankAccounts.get(position).getAmount();
        }
        textView.setText(str);
        TextView textView2 = vh.tv_bankName;
        if (TextUtils.isEmpty(this.bankAccounts.get(position).getName())) {
            str2 = str3;
        } else {
            str2 = this.bankAccounts.get(position).getName();
        }
        textView2.setText(str2);
        TextView textView3 = vh.tv_accountNo;
        if (!TextUtils.isEmpty(String.valueOf(this.bankAccounts.get(position).getAccountNumber()))) {
            str3 = String.valueOf(this.bankAccounts.get(position).getAccountNumber());
        }
        textView3.setText(str3);
        if (position == this.bankAccounts.size() - 1) {
            vh.divider.setVisibility(View.GONE);
        } else {
            vh.divider.setVisibility(View.VISIBLE);
        }
    }

    public int getItemCount() {
        return this.bankAccounts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View divider;
        ImageView optionMenu;
        TextView tv_accountNo;
        TextView tv_amount;
        TextView tv_bankName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tv_amount = itemView.findViewById(R.id.balanceAmount);
            this.tv_bankName = itemView.findViewById(R.id.bankName);
            this.tv_accountNo = itemView.findViewById(R.id.accountNumber);
            this.divider = itemView.findViewById(R.id.divider);
        }
    }
}
