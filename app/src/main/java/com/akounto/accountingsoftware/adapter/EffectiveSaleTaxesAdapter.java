package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EffectiveSaleTaxesAdapter extends RecyclerView.Adapter<EffectiveSaleTaxesAdapter.MyViewHolder> {
    Context context;
    private final List<EffectiveTaxesItem> effectiveTaxesItems;
    String receivedDateFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public EffectiveSaleTaxesAdapter(Context context2, List<EffectiveTaxesItem> users) {
        this.context = context2;
        this.effectiveTaxesItems = users;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_taxes_effective_rate, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder vh = holder;
        EffectiveTaxesItem effectiveTaxesItem = this.effectiveTaxesItems.get(position);
        TextView textView = vh.rate;
        textView.setText(effectiveTaxesItem.getRate() + "");
        try {
            Date date = new SimpleDateFormat(this.receivedDateFormat, Locale.US).parse(effectiveTaxesItem.getEffectiveDate());
            vh.date.setText(new SimpleDateFormat("MMM dd,yyyy", Locale.US).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (position % 2 == 0) {
            vh.layout.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        } else {
            vh.layout.setBackgroundColor(this.context.getResources().getColor(R.color.fadedWhite));
        }
    }

    public int getItemCount() {
        return this.effectiveTaxesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        ConstraintLayout layout;
        TextView rate;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.rate = itemView.findViewById(R.id.rate);
            this.date = itemView.findViewById(R.id.effectiveDate);
            this.layout = itemView.findViewById(R.id.layout);
        }
    }
}
