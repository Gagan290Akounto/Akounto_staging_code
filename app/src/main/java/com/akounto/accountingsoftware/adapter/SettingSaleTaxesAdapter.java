package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.SaleTax;
import java.util.List;

public class SettingSaleTaxesAdapter extends RecyclerView.Adapter<SettingSaleTaxesAdapter.MyViewHolder> {
    Context context;
    SettingSaleTaxItemClick onClickListener;
    private final List<SaleTax> saleTaxList;

    public SettingSaleTaxesAdapter(Context context2, List<SaleTax> users, SettingSaleTaxItemClick onClickListener2) {
        this.context = context2;
        this.saleTaxList = users;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_sales_taxes, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder vh = holder;
        SaleTax saleTax = this.saleTaxList.get(position);
        vh.name.setText(saleTax.getName());
        if (saleTax.getEffectiveTaxes() != null) {
            TextView textView = vh.percentage;
            textView.setText(saleTax.getEffectiveTaxes().get(0).getRate() + "%");
        }
        if (position % 2 == 0) {
            vh.layout.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        } else {
            vh.layout.setBackgroundColor(this.context.getResources().getColor(R.color.fadedWhite));
        }
        vh.taxAction.setOnClickListener(new View.OnClickListener() {
         
            public final void onClick(View view) {
                SettingSaleTaxesAdapter.this.lambda$onBindViewHolder$0$SettingSaleTaxesAdapter(saleTax, view);
            }
        });
    }

    public void lambda$onBindViewHolder$0$SettingSaleTaxesAdapter(SaleTax saleTax, View v) {
        this.onClickListener.editTax(saleTax);
    }

    public int getItemCount() {
        return this.saleTaxList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView name;
        TextView percentage;
        ImageView taxAction;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.layout = itemView.findViewById(R.id.layout);
            this.percentage = itemView.findViewById(R.id.taxPercentage);
            this.taxAction = itemView.findViewById(R.id.taxAction);
        }
    }
}
