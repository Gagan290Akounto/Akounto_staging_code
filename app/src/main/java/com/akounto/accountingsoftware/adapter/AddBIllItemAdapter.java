package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.PurchaseItem;
import java.util.List;

public class AddBIllItemAdapter extends RecyclerView.Adapter<AddBIllItemAdapter.MyViewHolder> {
    Context context;
    List<PurchaseItem> purchaseItemList;

    public AddBIllItemAdapter(Context context2, List<PurchaseItem> purchaseItemList2) {
        this.context = context2;
        this.purchaseItemList = purchaseItemList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addbill, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        MyViewHolder vh = holder;
        TextView textView = vh.itemName;
        if (TextUtils.isEmpty(this.purchaseItemList.get(position).getName())) {
            str = "--";
        } else {
            str = this.purchaseItemList.get(position).getName();
        }
        textView.setText(str);
        TextView textView2 = vh.itemPrice;
        textView2.setText("Price $" + this.purchaseItemList.get(position).getPrice());
        TextView textView3 = vh.amount;
        textView3.setText("Amount $" + this.purchaseItemList.get(position).getPrice());
    }

    public void notifyData(List<PurchaseItem> purchaseItemListTmp) {
        this.purchaseItemList = purchaseItemListTmp;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.purchaseItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        CardView billcardView;
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        TextView optionMenu;
        TextView tax;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemPrice = itemView.findViewById(R.id.itemPrice);
            this.itemQuantity = itemView.findViewById(R.id.itemQuantity);
            this.amount = itemView.findViewById(R.id.amount);
            this.tax = itemView.findViewById(R.id.tax);
        }
    }
}
