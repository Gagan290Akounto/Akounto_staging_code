package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.Activity.fragment.UpdateProduct;
import com.akounto.accountingsoftware.response.PurchaseItem;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {
    Context context;
    UpdateProduct onClickListener = this.onClickListener;
    private final List<PurchaseItem> productList;

    public ListItemAdapter(Context context2, List<PurchaseItem> productList2) {
        this.context = context2;
        this.productList = productList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listitemservices, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        String str2;
        MyViewHolder vh = holder;
        TextView textView = vh.textViewPrice;
        String str3 = "--";
        if (TextUtils.isEmpty(this.productList.get(position).getPrice() + "")) {
            str = str3;
        } else {
            str = "$" + this.productList.get(position).getPrice();
        }
        textView.setText(str);
        TextView textView2 = vh.tv_productName;
        if (TextUtils.isEmpty(this.productList.get(position).getName())) {
            str2 = str3;
        } else {
            str2 = this.productList.get(position).getName();
        }
        textView2.setText(str2);
        TextView textView3 = vh.tv_productDesc;
        if (!TextUtils.isEmpty(String.valueOf(this.productList.get(position).getDescription()))) {
            str3 = String.valueOf(this.productList.get(position).getDescription());
        }
        textView3.setText(str3);
        if (this.productList.get(position).getDescription() == null) {
            vh.tv_productDesc.setVisibility(View.GONE);
        } else {
            vh.tv_productDesc.setVisibility(View.VISIBLE);
        }
        vh.pConstarnt.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ListItemAdapter.this.lambda$onBindViewHolder$0$ListItemAdapter(position, view);
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$ListItemAdapter(int position, View v) {
        ((CreateBillActivity) this.context).addBill(this.productList.get(position));
    }

    public int getItemCount() {
        return this.productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView optionMenu;
        CardView pConstarnt;
        TextView textViewOptions;
        TextView textViewPrice;
        TextView tv_productDesc;
        TextView tv_productName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tv_productName = itemView.findViewById(R.id.tv_productName);
            this.textViewPrice = itemView.findViewById(R.id.textViewPrice);
            this.tv_productDesc = itemView.findViewById(R.id.tv_productDesc);
            this.pConstarnt = itemView.findViewById(R.id.pConstarnt);
        }
    }
}
