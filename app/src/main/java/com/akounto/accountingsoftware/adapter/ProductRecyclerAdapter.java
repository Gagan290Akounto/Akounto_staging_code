package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.currency.Currency;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductRecyclerHolder> {

    Context context;
    Currency currency;
    List<GetInvoiceTransactionItem> invoiceTransaction;

    public ProductRecyclerAdapter(Context context2, List<GetInvoiceTransactionItem> invoiceTransaction2, Currency currency2) {
        this.context = context2;
        this.invoiceTransaction = invoiceTransaction2;
        this.currency = currency2;
    }

    public ProductRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productrecycler, parent, false));
    }

    public void onBindViewHolder(ProductRecyclerHolder holder, int position) {
        ProductRecyclerHolder vh = holder;
        GetInvoiceTransactionItem item = null;
        try {
            item = this.invoiceTransaction.get(position);
            vh.itemdesc.setText(item.getDescription());
            vh.itemName.setText(item.getProductName());
            vh.itemPrice.setText(UiUtil.getActiveBussinessCurren(context) + " " + item.getPrice() + "");
            vh.itemquanc.setText(" : " + item.getQuantity() + "");
        } catch (Exception e) {
        }
        String taxList = "";
        if (item.getTaxes() != null) {
            for (ProductServiceTaxesItem taxItem : item.getTaxes()) {
                if (taxList.length() > 0) {
                    taxList = taxList + "\n" + taxItem.getName()+" "+taxItem.getRate()+"%";
                } else {
                    taxList = taxList + taxItem.getName()+" "+taxItem.getRate()+"%";
                }
            }
            vh.taxList.setText(taxList);
        }
    }

    public int getItemCount() {
        List<GetInvoiceTransactionItem> list = this.invoiceTransaction;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ProductRecyclerHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView itemdesc;
        TextView itemquanc;
        ImageView optionMenu;
        TextView taxList;

        public ProductRecyclerHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemdesc = itemView.findViewById(R.id.itemdesc);
            this.itemPrice = itemView.findViewById(R.id.itemPrice);
            this.itemquanc = itemView.findViewById(R.id.itemquanc);
            this.taxList = itemView.findViewById(R.id.taxList);
        }
    }
}
