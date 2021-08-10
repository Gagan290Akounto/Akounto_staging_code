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
import com.akounto.accountingsoftware.Activity.fragment.BillClick;
import com.akounto.accountingsoftware.request.Bills;
import com.akounto.accountingsoftware.util.UiUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.MyViewHolder> {
    /* access modifiers changed from: private */
    public List<Bills> billsList;
    BillClick click;
    Context context;
    BillClick onClickListener;

    public BillsAdapter(Context context2, List<Bills> billsList2, BillClick click2) {
        this.context = context2;
        this.billsList = billsList2;
        this.onClickListener = click2;
        this.click = click2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bills, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String str;
        MyViewHolder vh = holder;
        TextView textView = vh.productName;
        String curancy = "$";
        try {
            curancy = UiUtil.getBussinessCurrenSymbul(context);
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(this.billsList.get(position).getInvoiceNo())) {
            str = "--";
        } else {
            str = "Bill No : " + this.billsList.get(position).getInvoiceNo();
        }
        textView.setText(str);
        String dueAMount = new DecimalFormat("##.00").format(this.billsList.get(position).getDueAmount()).replace(',', '.');
        vh.productDesc.setText("Amount Due "+curancy+" " + dueAMount);
        vh.billcardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        if (this.billsList.get(position).getStatus() == 0) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_draft));
            vh.statusTV.setText("Draft");
        } else if (this.billsList.get(position).getStatus() == 100) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_approved));
            vh.statusTV.setText("Approved");
        } else if (this.billsList.get(position).getStatus() == 150) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_partialsettled));
            vh.statusTV.setText("Partialsettled");
        } else if (this.billsList.get(position).getStatus() == 200) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_settled));
            vh.statusTV.setText("Settled");
        } else if (this.billsList.get(position).getStatus() == 400) {
            vh.statusTV.setBackground(this.context.getResources().getDrawable(R.drawable.right_rounded_cancelled));
            vh.statusTV.setText("Cancelled");
        }
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(BillsAdapter.this.context, holder.textViewOptions);
                if (BillsAdapter.this.billsList.get(position).getStatus() == 100) {
                    popup.inflate(R.menu.bills_menu_approved);
                } else {
                    popup.inflate(R.menu.bills_menu);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.edit) {
                            BillsAdapter.this.onClickListener.deleteBill(BillsAdapter.this.billsList.get(position), 2);
                            return false;
                        } else if (itemId != R.id.view) {
                            return false;
                        } else {
                            BillsAdapter.this.onClickListener.deleteBill(BillsAdapter.this.billsList.get(position), 3);
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });
        vh.dateTV.setText(getFormattedDate(this.billsList.get(position).getBillAt()));
        vh.dueDateTv.setText(getFormattedDate(this.billsList.get(position).getDueAt()));
        vh.vendorTV.setText(this.billsList.get(position).getVendorName());
    }

    public int getItemCount() {
        return this.billsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView billcardView;
        TextView dateTV;
        TextView dueDateTv;
        TextView optionMenu;
        TextView productDesc;
        TextView productName;
        TextView productPrice;
        TextView statusTV;
        TextView textViewOptions;
        TextView vendorTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.productName = itemView.findViewById(R.id.tv_productName);
            this.productDesc = itemView.findViewById(R.id.tv_productDesc);
            this.productPrice = itemView.findViewById(R.id.tv_productPrice);
            this.optionMenu = itemView.findViewById(R.id.optionButton);
            this.billcardView = itemView.findViewById(R.id.billcardView);
            this.textViewOptions = itemView.findViewById(R.id.textViewPrice);
            this.dateTV = itemView.findViewById(R.id.dateTV);
            this.dueDateTv = itemView.findViewById(R.id.dueDateTv);
            this.vendorTV = itemView.findViewById(R.id.vendorTV);
            this.statusTV = itemView.findViewById(R.id.statusTV);
        }
    }

    public static String getFormattedDate(String invoiceDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.US);
        try {
            Date date = simpleDateFormat.parse(invoiceDate);
            if (date != null) {
                return newDateFormat.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
