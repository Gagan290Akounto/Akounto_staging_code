package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.Html;
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
import com.akounto.accountingsoftware.response.RecurringInvoicesItem;
import java.util.List;

public class RecurringInvoicesAdapter extends RecyclerView.Adapter<RecurringInvoicesAdapter.MyViewHolder> {
    Context context;
    InvoiceItemClick onClickListener;
    List<RecurringInvoicesItem> recurringInvoicesItems;

    public RecurringInvoicesAdapter(Context context2, List<RecurringInvoicesItem> recurringInvoicesItems2, InvoiceItemClick onClickListener2) {
        this.context = context2;
        this.recurringInvoicesItems = recurringInvoicesItems2;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recurring_invoice, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String str;
        String str2;
        String str3;
        MyViewHolder vh = holder;
        TextView textView = vh.customerName;
        String str4 = "--";
        if (TextUtils.isEmpty(this.recurringInvoicesItems.get(position).getCustomerName())) {
            str = str4;
        } else {
            str = this.recurringInvoicesItems.get(position).getCustomerName();
        }
        textView.setText(str);
        TextView textView2 = vh.invoiceAmount;
        if (TextUtils.isEmpty(String.valueOf(this.recurringInvoicesItems.get(position).getAmount()))) {
            str2 = str4;
        } else {
            str2 = "Invoice Amount: $" + this.recurringInvoicesItems.get(position).getAmount();
        }
        textView2.setText(str2);
        TextView textView3 = vh.prevInvoice;
        if (this.recurringInvoicesItems.get(position).getLastInvoice() == null) {
            str3 = str4;
        } else {
            str3 = this.recurringInvoicesItems.get(position).getLastInvoice().toString();
        }
        textView3.setText(str3);
        TextView textView4 = vh.nextInvoice;
        if (this.recurringInvoicesItems.get(position).getNextInvoice() != null) {
            str4 = this.recurringInvoicesItems.get(position).getNextInvoice().toString();
        }
        textView4.setText(str4);
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                RecurringInvoicesAdapter.this.lambda$onBindViewHolder$0$RecurringInvoicesAdapter(position, view);
            }
        });
        if (this.recurringInvoicesItems.get(position).getScheduleDescription() != null) {
            vh.scheduleTv.setText(Html.fromHtml(this.recurringInvoicesItems.get(position).getScheduleDescription()));
        }
        if (this.recurringInvoicesItems.get(position).getStatus() == 0) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.left_rounded_draft));
            vh.statusTV.setText("Draft");
        } else if (this.recurringInvoicesItems.get(position).getStatus() == 100) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.left_rounded_approved));
            vh.statusTV.setText("Approved");
        }
        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(RecurringInvoicesAdapter.this.context, holder.optionMenu);
                popup.inflate(R.menu.bills_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.edit) {
                            RecurringInvoicesAdapter.this.onClickListener.viewItem(RecurringInvoicesAdapter.this.recurringInvoicesItems.get(position), 1);
                            return false;
                        } else if (itemId != R.id.view) {
                            return false;
                        } else {
                            RecurringInvoicesAdapter.this.onClickListener.viewItem(RecurringInvoicesAdapter.this.recurringInvoicesItems.get(position), 1);
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$RecurringInvoicesAdapter(int position, View v) {
        this.onClickListener.viewItem(this.recurringInvoicesItems.get(position), 2);
    }

    public int getItemCount() {
        return this.recurringInvoicesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView customerName;
        TextView invoiceAmount;
        TextView nextInvoice;
        TextView optionMenu;
        TextView prevInvoice;
        TextView scheduleTv;
        TextView statusTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardView);
            this.customerName = itemView.findViewById(R.id.tv_customerName);
            this.invoiceAmount = itemView.findViewById(R.id.tv_invoiceAmount);
            this.prevInvoice = itemView.findViewById(R.id.tv_prevInvoice);
            this.nextInvoice = itemView.findViewById(R.id.tv_nextInvoice);
            this.optionMenu = itemView.findViewById(R.id.textViewPrice);
            this.scheduleTv = itemView.findViewById(R.id.scheduleTv);
            this.statusTV = itemView.findViewById(R.id.statusTV);
        }
    }
}
