package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.Transaction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    TransactionItemClick onClickListener;
    List<Transaction> recurringInvoicesItems;
    TransactionIconClick transactionIconClick;

    public TransactionAdapter(Context context2, List<Transaction> recurringInvoicesItems2, TransactionItemClick onClickListener2, TransactionIconClick transactionIconClick2) {
        this.context = context2;
        this.recurringInvoicesItems = recurringInvoicesItems2;
        this.onClickListener = onClickListener2;
        this.transactionIconClick = transactionIconClick2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transectione, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        String str2;
        String str3 = "--";
        MyViewHolder vh = holder;
        try {
            vh.prevInvoice.setText(this.recurringInvoicesItems.get(position).getTransactionDate() == null ? str3 : new SimpleDateFormat("MMM dd, yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(this.recurringInvoicesItems.get(position).getTransactionDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView textView = vh.customerName;
        if (TextUtils.isEmpty(this.recurringInvoicesItems.get(position).getAccountName())) {
            str = this.recurringInvoicesItems.get(position).getDescription();
        } else {
            str = this.recurringInvoicesItems.get(position).getAccountName();
        }
        textView.setText(str);
        if (TextUtils.isEmpty(this.recurringInvoicesItems.get(position).getDescription())) {
            vh.tvDesc.setVisibility(View.GONE);
        } else {
            vh.tvDesc.setVisibility(View.VISIBLE);
            vh.tvDesc.setText(this.recurringInvoicesItems.get(position).getDescription());
        }
        if (TextUtils.isEmpty(this.recurringInvoicesItems.get(position).getAccountName())) {
            vh.customerName.setText(this.recurringInvoicesItems.get(position).getDescription());
            vh.tvDesc.setVisibility(View.GONE);
        } else {
            vh.customerName.setText(this.recurringInvoicesItems.get(position).getAccountName());
        }
        TextView textView2 = vh.invoiceAmount;
        if (TextUtils.isEmpty(String.valueOf(this.recurringInvoicesItems.get(position).getAmount()))) {
            str2 = String.format("%.2f", str3);
        } else {
            str2 = "$" + String.format("%.2f", this.recurringInvoicesItems.get(position).getAmount());
        }
        textView2.setText(str2);
        if (this.recurringInvoicesItems.get(position).getTransactionType() == 3) {
            vh.category.setText("Journal Transaction");
            vh.invoiceAmount.setTextColor(this.context.getResources().getColor(R.color.text_color_journal));
        } else {
            TextView textView3 = vh.category;
            if (this.recurringInvoicesItems.get(position).getTransactionHeadName() != null) {
                str3 = this.recurringInvoicesItems.get(position).getTransactionHeadName();
            }
            textView3.setText(str3);
            if (this.recurringInvoicesItems.get(position).getTransactionType() == 1) {
                vh.invoiceAmount.setTextColor(this.context.getResources().getColor(R.color.text_color_deposite));
            } else {
                vh.invoiceAmount.setTextColor(this.context.getResources().getColor(R.color.text_color_withdrawl));
            }
        }
        if (!this.recurringInvoicesItems.get(position).getTransactionHeadName().equals("Uncategorized") || this.recurringInvoicesItems.get(position).getTransactionType() == 3) {
            vh.categoryTV.setVisibility(View.VISIBLE);
            vh.category.setTextColor(this.context.getResources().getColor(R.color.black));
            vh.cat_IV.setVisibility(View.GONE);
        } else {
            vh.categoryTV.setVisibility(View.GONE);
            vh.category.setTextColor(this.context.getResources().getColor(R.color.text_color_withdrawl));
            vh.cat_IV.setVisibility(View.GONE);
            vh.category.setText("Choose a category");
        }
        int review = this.recurringInvoicesItems.get(position).getReview();
        if (review == 0) {
            vh.reviewIcon.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_tick_categorised));
        } else if (review != 1) {
            vh.reviewIcon.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_tick_round));
        } else {
            vh.reviewIcon.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_fill_tick));
        }
        vh.reviewIcon.setOnClickListener(view -> TransactionAdapter.this.lambda$onBindViewHolder$0$TransactionAdapter(position, vh, view));
        vh.reviewIcon.setOnClickListener(view -> TransactionAdapter.this.lambda$onBindViewHolder$1$TransactionAdapter(position, view));
        vh.cardView.setOnClickListener(view -> TransactionAdapter.this.lambda$onBindViewHolder$2$TransactionAdapter(position, view));
    }

    public void lambda$onBindViewHolder$0$TransactionAdapter(int position, MyViewHolder vh, View v) {
        String review;
        int review2 = this.recurringInvoicesItems.get(position).getReview();
        if (review2 == 0) {
            review = "Req. categorized action";
        } else if (review2 != 1) {
            review = "Choose a Category";
        } else {
            review = "Under Review";
        }
        TooltipCompat.setTooltipText(vh.reviewIcon, review);
    }

    public void lambda$onBindViewHolder$1$TransactionAdapter(int position, View v) {
        if (this.recurringInvoicesItems.get(position).getReview() == 2) {
            this.transactionIconClick.updateAction(this.recurringInvoicesItems.get(position));
        }
    }

    public void lambda$onBindViewHolder$2$TransactionAdapter(int position, View v) {
        this.onClickListener.viewItem(this.recurringInvoicesItems.get(position), this.recurringInvoicesItems.get(position).getTransactionType());
    }

    public int getItemCount() {
        return this.recurringInvoicesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cat_IV;
        TextView category;
        TextView categoryTV;
        TextView customerName;
        TextView invoiceAmount;
        TextView prevInvoice;
        ImageView reviewIcon;
        TextView tvDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardView);
            this.customerName = itemView.findViewById(R.id.tv_customerName);
            this.tvDesc = itemView.findViewById(R.id.tv_desc);
            this.invoiceAmount = itemView.findViewById(R.id.tv_invoiceAmount);
            this.prevInvoice = itemView.findViewById(R.id.tv_prevInvoice);
            this.category = itemView.findViewById(R.id.tv_category);
            this.reviewIcon = itemView.findViewById(R.id.status_icon);
            this.categoryTV = itemView.findViewById(R.id.nextInvoice);
            this.cat_IV = itemView.findViewById(R.id.cat_IV);
        }
    }
}
