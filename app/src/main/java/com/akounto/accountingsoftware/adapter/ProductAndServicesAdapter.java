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
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.UpdateProduct;
import com.akounto.accountingsoftware.response.Product;
import java.util.List;

public class ProductAndServicesAdapter extends RecyclerView.Adapter<ProductAndServicesAdapter.MyViewHolder> {
    Context context;
    UpdateProduct onClickListener;
    /* access modifiers changed from: private */
    public List<Product> productList;

    public ProductAndServicesAdapter(Context context2, List<Product> productList2, UpdateProduct onClickListener2) {
        this.context = context2;
        this.productList = productList2;
        this.onClickListener = onClickListener2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productandservices, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String str;
        String str2;
        MyViewHolder vh = holder;
        TextView textView = vh.productName;
        String str3 = "--";
        if (TextUtils.isEmpty(this.productList.get(position).getName())) {
            str = str3;
        } else {
            str = this.productList.get(position).getName();
        }
        textView.setText(str);
        TextView textView2 = vh.productDesc;
        if (TextUtils.isEmpty(this.productList.get(position).getDescription())) {
            str2 = str3;
        } else {
            str2 = this.productList.get(position).getDescription();
        }
        textView2.setText(str2);
        TextView textView3 = vh.productPrice;
        if (!TextUtils.isEmpty(String.valueOf(this.productList.get(position).getPrice()))) {
            str3 = "$" + this.productList.get(position).getPrice();
        }
        textView3.setText(str3);
        vh.optionMenu.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ProductAndServicesAdapter.this.lambda$onBindViewHolder$0$ProductAndServicesAdapter(position, view);
            }
        });
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ProductAndServicesAdapter.this.context, holder.textViewOptions);
                popup.inflate(R.menu.options_prduct_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() != R.id.edit) {
                            return false;
                        }
                        ProductAndServicesAdapter.this.onClickListener.editProduct(ProductAndServicesAdapter.this.productList.get(position));
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$ProductAndServicesAdapter(int position, View v) {
        this.onClickListener.editProduct(this.productList.get(position));
    }

    public int getItemCount() {
        List<Product> list = this.productList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
