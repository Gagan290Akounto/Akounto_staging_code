package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.CustomerClick;
import com.akounto.accountingsoftware.response.Vendor;
import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    public List<Vendor> customerList;
    CustomerClick onClickListener;

    public VendorAdapter(Context context2, List<Vendor> customerList2, CustomerClick onClickListener2) {
        this.context = context2;
        this.customerList = customerList2;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productandservices, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String str;
        MyViewHolder vh = holder;
        TextView textView = vh.productName;
        String str2 = "--";
        if (TextUtils.isEmpty(this.customerList.get(position).getVendorName())) {
            str = str2;
        } else {
            str = this.customerList.get(position).getVendorName();
        }
        textView.setText(str);
        TextView textView2 = vh.productDesc;
        if (!TextUtils.isEmpty(this.customerList.get(position).getEmail())) {
            str2 = this.customerList.get(position).getEmail();
        }
        textView2.setText(str2);
        vh.productPrice.setVisibility(View.GONE);
        vh.optionMenu.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_delete_icon));
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                VendorAdapter.this.lambda$onBindViewHolder$0$VendorAdapter(position, view);
            }
        });
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(VendorAdapter.this.context, holder.textViewOptions);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.delete) {
                            VendorAdapter.this.onClickListener.deleteCustomer(VendorAdapter.this.customerList.get(position), 3);
                            return false;
                        } else if (itemId != R.id.edit) {
                            return false;
                        } else {
                            VendorAdapter.this.onClickListener.deleteCustomer(VendorAdapter.this.customerList.get(position), 2);
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$VendorAdapter(int position, View v) {
        this.onClickListener.deleteCustomer(this.customerList.get(position), 1);
    }

    public int getItemCount() {
        List<Vendor> list = this.customerList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView optionMenu;
        TextView productDesc;
        TextView productName;
        TextView productPrice;
        TextView textViewOptions;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.productName = itemView.findViewById(R.id.tv_productName);
            this.productDesc = itemView.findViewById(R.id.tv_productDesc);
            this.productPrice = itemView.findViewById(R.id.tv_productPrice);
            this.optionMenu = itemView.findViewById(R.id.optionButton);
            this.textViewOptions = itemView.findViewById(R.id.textViewPrice);
            this.cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
