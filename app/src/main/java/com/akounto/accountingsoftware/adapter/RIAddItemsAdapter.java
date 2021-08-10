package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.Activity.CommonInvoiceItemUpdate;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.util.TaxSummaryListView;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.cache.DiskLruCache;

public class RIAddItemsAdapter extends RecyclerView.Adapter<RIAddItemsAdapter.MyViewHolder> {
    Context context;
    List<Product> productList;
    double productPrice = Utils.DOUBLE_EPSILON;
    CommonInvoiceItemUpdate recurringInvoiceItemUpdate;
    List<Integer> taxesToRemove = new ArrayList();

    public RIAddItemsAdapter(Context context, List<Product> productList, CommonInvoiceItemUpdate recurringInvoiceItemUpdate) {
        this.context = context;
        this.productList = productList;
        this.recurringInvoiceItemUpdate = recurringInvoiceItemUpdate;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recurring_invoice_products_list, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str;
        String str2;
        MyViewHolder vh = holder;
        TextView textView = vh.name;
        String str3 = "--";
        if (TextUtils.isEmpty(this.productList.get(position).getName())) {
            str = str3;
        } else {
            str = this.productList.get(position).getName();
        }
        textView.setText(str);
        TextView textView2 = vh.price;
        if (TextUtils.isEmpty(String.valueOf(this.productList.get(position).getPrice()))) {
            str2 = str3;
        } else {
            str2 = String.valueOf(this.productList.get(position).getPrice());
        }
        textView2.setText(str2);
        vh.quantity.setText(DiskLruCache.VERSION_1);
        TextView textView3 = vh.amount;
        if (!TextUtils.isEmpty(String.valueOf(this.productList.get(position).getPrice()))) {
            str3 = String.valueOf(this.productList.get(position).getPrice());
        }
        textView3.setText(str3);
        this.productPrice = this.productList.get(position).getPrice();
        vh.taxItems.setAdapter(new ArrayAdapter<>(this.context, R.layout.item_taxlist_for_recurring_invoices, R.id.text1, prepareTaxList(this.productList.get(position).getProductServiceTaxes())));
        if (position == this.productList.size() - 1) {
            vh.divider.setVisibility(View.GONE);
        } else {
            vh.divider.setVisibility(View.VISIBLE);
        }
        vh.deleteItem.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                RIAddItemsAdapter.this.lambda$onBindViewHolder$0$RIAddItemsAdapter(position, view);
            }
        });
        vh.taxItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ((CommonInvoiceActivity) RIAddItemsAdapter.this.context).addTax();
            }
        });
    }

    public void lambda$onBindViewHolder$0$RIAddItemsAdapter(int position, View v) {
        this.recurringInvoiceItemUpdate.deleteItem(this.productList.get(position));
    }

    private List<String> prepareTaxList(List<ProductServiceTaxesItem> productServiceTaxes) {
        List<String> taxItems = new ArrayList<>();
        for (ProductServiceTaxesItem psti : productServiceTaxes) {
            taxItems.add(psti.getTaxName() + "  " + psti.getRate() + "%");
        }
        return taxItems;
    }

    public int getItemCount() {
        return this.productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        ImageView deleteItem;
        View divider;
        TextView name;
        TextView price;
        TextView quantity;
        TextView tax;
        TaxSummaryListView taxItems;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_name);
            this.price = itemView.findViewById(R.id.tv_price);
            this.quantity = itemView.findViewById(R.id.tv_quantity);
            this.divider = itemView.findViewById(R.id.divider);
            this.amount = itemView.findViewById(R.id.tv_amount);
            this.taxItems = itemView.findViewById(R.id.tv_taxList);
            this.deleteItem = itemView.findViewById(R.id.deleteItem);
        }
    }
}
