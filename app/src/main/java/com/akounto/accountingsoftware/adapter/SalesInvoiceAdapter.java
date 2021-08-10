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

public class SalesInvoiceAdapter extends RecyclerView.Adapter<SalesInvoiceAdapter.MyViewHolder> {

    Context context;
    InvoiceItemClick onClickListener;
    List<RecurringInvoicesItem> salesInvoiceItems;

    public SalesInvoiceAdapter(Context context2, List<RecurringInvoicesItem> salesInvoiceItems2, InvoiceItemClick invoiceItemClick) {
        this.context = context2;
        this.salesInvoiceItems = salesInvoiceItems2;
        this.onClickListener = invoiceItemClick;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_invoice, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String str;
        String str2;
        String str3;
        MyViewHolder vh = holder;
        TextView textView = vh.customerName;
        String curancy = "$";
        try {
            curancy = UiUtil.getBussinessCurrenSymbul(context);
        } catch (Exception e) {
        }
        String str4 = "--";
        if (TextUtils.isEmpty(this.salesInvoiceItems.get(position).getCustomerName())) {
            str = str4;
        } else {
            str = this.salesInvoiceItems.get(position).getCustomerName();
        }
        textView.setText(str);
        TextView textView2 = vh.invoiceAmount;
        if (TextUtils.isEmpty(String.valueOf(this.salesInvoiceItems.get(position).getAmount()))) {
            str2 = str4;
        } else {
            str2 = "Amount: " + curancy + " " + String.format("%.2f", this.salesInvoiceItems.get(position).getAmount());
        }
        textView2.setText(str2);
        TextView textView3 = vh.invoiceNo;
        if (TextUtils.isEmpty(String.valueOf(this.salesInvoiceItems.get(position).getInvoiceNo()))) {
            str3 = str4.trim();
        } else {
            str3 = String.valueOf(this.salesInvoiceItems.get(position).getInvoiceNoPS()).trim();
        }
        textView3.setText(str3);
        TextView textView4 = vh.amountDue;
        if (!TextUtils.isEmpty(String.valueOf(this.salesInvoiceItems.get(position).getDueAmount()))) {
            str4 = String.valueOf(curancy + " " + String.format("%.2f", this.salesInvoiceItems.get(position).getDueAmount()));
        }
        textView4.setText(str4);
        String invoiceDate = CommonInvoiceActivity.getFormattedDate(this.salesInvoiceItems.get(position).getInvoiceDate());
        String paymentDueDate = CommonInvoiceActivity.getFormattedDate(this.salesInvoiceItems.get(position).getPaymentDue());
        vh.date.setText(invoiceDate);
        vh.dueDate.setText(paymentDueDate);
        if (this.salesInvoiceItems.get(position).getStatus() == 0) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_draft));
            vh.statusTV.setText("Draft");
        } else if (this.salesInvoiceItems.get(position).getStatus() == 100) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_approved));
            vh.statusTV.setText("Approved");
        } else if (this.salesInvoiceItems.get(position).getStatus() == 200) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_converted));
            vh.statusTV.setText("Converted to invoice");
        } else if (this.salesInvoiceItems.get(position).getStatus() == 150) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_partialsettled));
            vh.statusTV.setText("Partial settled");
        } else if (this.salesInvoiceItems.get(position).getStatus() == 400) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_cancelled));
            vh.statusTV.setText("Cancelled");
        }
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(SalesInvoiceAdapter.this.context, holder.textViewOptions);
                popup.inflate(R.menu.menu_salesestimation);
                if (salesInvoiceItems.get(position).getStatus() == 0) {
                    //Noting
                } else if (salesInvoiceItems.get(position).getStatus() == 100) {
                    popup.getMenu().removeItem(R.id.edit);
                } else if (salesInvoiceItems.get(position).getStatus() == 200) {
                    popup.getMenu().removeItem(R.id.edit);
                } else if (salesInvoiceItems.get(position).getStatus() == 150) {
                    popup.getMenu().removeItem(R.id.edit);
                } else if (salesInvoiceItems.get(position).getStatus() == 400) {
                    popup.getMenu().removeItem(R.id.edit);
                    popup.getMenu().removeItem(R.id.download);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.download) {
                            SalesInvoiceAdapter.this.onClickListener.viewItem(SalesInvoiceAdapter.this.salesInvoiceItems.get(position), 3);
                            return false;
                        } else if (itemId == R.id.edit) {
                            SalesInvoiceAdapter.this.onClickListener.viewItem(SalesInvoiceAdapter.this.salesInvoiceItems.get(position), 1);
                            return false;
                        } else if (itemId != R.id.view) {
                            return false;
                        } else {
                            SalesInvoiceAdapter.this.onClickListener.viewItem(SalesInvoiceAdapter.this.salesInvoiceItems.get(position), 2);
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SalesInvoiceAdapter.this.lambda$onBindViewHolder$0$SalesInvoiceAdapter(position, view);
            }
        });
    }

    public void lambda$onBindViewHolder$0$SalesInvoiceAdapter(int position, View v) {
        this.onClickListener.viewItem(this.salesInvoiceItems.get(position), 2);
    }

    public int getItemCount() {
        return this.salesInvoiceItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView amountDue;
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
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.invoiceNo = (TextView) itemView.findViewById(R.id.tv_invoiceNo);
            this.customerName = (TextView) itemView.findViewById(R.id.tv_customerName);
            this.invoiceAmount = (TextView) itemView.findViewById(R.id.tv_invoiceAmount);
            this.date = (TextView) itemView.findViewById(R.id.tv_date);
            this.dueDate = (TextView) itemView.findViewById(R.id.tv_dueDate);
            this.amountDue = (TextView) itemView.findViewById(R.id.tv_amountDue);
            this.statusTV = (TextView) itemView.findViewById(R.id.statusTV);
            this.textViewOptions = (TextView) itemView.findViewById(R.id.textViewPrice);
        }
    }
}
