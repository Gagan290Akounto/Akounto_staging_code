package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.response.RecurringInvoicesItem;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

public class SalesEstimateAdapter extends RecyclerView.Adapter<SalesEstimateAdapter.MyViewHolder> {
    Context context;
    InvoiceItemClick onClickListener;
    List<RecurringInvoicesItem> salesEstimatesItem;

    public SalesEstimateAdapter(Context context2, List<RecurringInvoicesItem> salesEstimatesItem2, InvoiceItemClick onClickListener2) {
        this.context = context2;
        this.salesEstimatesItem = salesEstimatesItem2;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_estimate, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String str;
        String str2;
        MyViewHolder vh = holder;
        TextView textView = vh.customerName;
        String str3 = "--";
        String simble= UiUtil.getBussinessCurrenSymbul(context);
        if (TextUtils.isEmpty(this.salesEstimatesItem.get(position).getCustomerName())) {
            str = str3;
        } else {
            str = this.salesEstimatesItem.get(position).getCustomerName();
        }
        textView.setText(str);
        TextView textView2 = vh.invoiceAmount;
        if (TextUtils.isEmpty(String.valueOf(this.salesEstimatesItem.get(position).getAmount()))) {
            str2 = str3;
        } else {
            str2 = "Amount: "+simble+" " + String.format("%.2f", salesEstimatesItem.get(position).getAmount());
        }
        textView2.setText(str2);
        TextView textView3 = vh.invoiceNo;
        if (!TextUtils.isEmpty(String.valueOf(this.salesEstimatesItem.get(position).getInvoiceNo()))) {
            str3 = "Estimate-" + this.salesEstimatesItem.get(position).getInvoiceNo();
        }
        textView3.setText(str3);
        String invoiceDate = CommonInvoiceActivity.getFormattedDate(this.salesEstimatesItem.get(position).getInvoiceDate());
        String paymentDueDate = CommonInvoiceActivity.getFormattedDate(this.salesEstimatesItem.get(position).getPaymentDue());

        if (this.salesEstimatesItem.get(position).getStatus() == 0) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_draft));
            vh.statusTV.setText("Draft");
        } else if (this.salesEstimatesItem.get(position).getStatus() == 100) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_approved));
            vh.statusTV.setText("Approved");
        } else if (this.salesEstimatesItem.get(position).getStatus() == 200) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_converted));
            vh.statusTV.setText("Converted to invoice");
        }else if (this.salesEstimatesItem.get(position).getStatus() == 150) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_partialsettled));
            vh.statusTV.setText("Partial settled");
        } else if (this.salesEstimatesItem.get(position).getStatus() == 400) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_cancelled));
            vh.statusTV.setText("Cancelled");
        }
        vh.date.setText(invoiceDate);
        vh.dueDate.setText(paymentDueDate);
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(SalesEstimateAdapter.this.context, holder.textViewOptions);
                popup.inflate(R.menu.menu_salesestimation);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.download) {
                            SalesEstimateAdapter.this.onClickListener.viewItem(SalesEstimateAdapter.this.salesEstimatesItem.get(position), 3);
                            return false;
                        } else if (itemId == R.id.edit) {
                            SalesEstimateAdapter.this.onClickListener.viewItem(SalesEstimateAdapter.this.salesEstimatesItem.get(position), 1);
                            return false;
                        } else if (itemId != R.id.view) {
                            return false;
                        } else {
                            SalesEstimateAdapter.this.onClickListener.viewItem(SalesEstimateAdapter.this.salesEstimatesItem.get(position), 2);
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    public int getItemCount() {
        return this.salesEstimatesItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView customerName;
        TextView date;
        TextView dueDate;
        TextView invoiceAmount;
        TextView invoiceNo;
        TextView statusTV;
        TextView textViewOptions;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardView);
            this.invoiceNo = itemView.findViewById(R.id.tv_invoiceNo);
            this.customerName = itemView.findViewById(R.id.tv_customerName);
            this.invoiceAmount = itemView.findViewById(R.id.tv_invoiceAmount);
            this.date = itemView.findViewById(R.id.tv_date);
            this.dueDate = itemView.findViewById(R.id.tv_expiryDate);
            this.textViewOptions = itemView.findViewById(R.id.textViewPrice);
            this.statusTV = itemView.findViewById(R.id.statusTV);
        }
    }
}
