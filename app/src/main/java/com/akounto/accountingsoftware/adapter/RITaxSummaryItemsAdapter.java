package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.model.TaxSummaryList;
import com.akounto.accountingsoftware.response.currency.Currency;
import java.util.List;

public class RITaxSummaryItemsAdapter extends RecyclerView.Adapter<RITaxSummaryItemsAdapter.MyViewHolder> {
    Context context;
    Currency selectedExchangeCurrency;
    List<TaxSummaryList> taxSummaryList;

    public RITaxSummaryItemsAdapter(Context context2, List<TaxSummaryList> taxSummaryList2) {
        this.context = context2;
        this.taxSummaryList = taxSummaryList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recurring_invoice_tax_summary, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        MyViewHolder vh = holder;
        String symbol = "$";
        Currency currency = this.selectedExchangeCurrency;
        if (currency != null) {
            symbol = currency.getSymbol();
        }
        TextView textView = vh.name;
        String str2 = "--";
        if (TextUtils.isEmpty(this.taxSummaryList.get(position).getTaxName())) {
            str = str2;
        } else {
            str = this.taxSummaryList.get(position).getTaxName();
        }
        textView.setText(str);
        TextView textView2 = vh.value;
        if (!TextUtils.isEmpty(String.valueOf(this.taxSummaryList.get(position).getTaxValue()))) {
            str2 = symbol + this.taxSummaryList.get(position).getTaxValue();
        }
        textView2.setText(str2);
    }

    public int getItemCount() {
        return this.taxSummaryList.size();
    }

    public void notifyDataChange(List<TaxSummaryList> taxSummaryList2, Currency selectedExchangeCurrency2) {
        this.taxSummaryList = taxSummaryList2;
        this.selectedExchangeCurrency = selectedExchangeCurrency2;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView value;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.taxName);
            this.value = itemView.findViewById(R.id.taxValue);
        }
    }
}
