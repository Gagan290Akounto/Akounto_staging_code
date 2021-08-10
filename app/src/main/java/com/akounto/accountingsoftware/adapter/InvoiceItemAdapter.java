package com.akounto.accountingsoftware.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.Product;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemAdapter  extends RecyclerView.Adapter<InvoiceItemAdapter.ViewHolder> {
    private List<Product> item_list;
    private String cur;
    private OnItemClickListener onItemClickListener;

    public InvoiceItemAdapter(List<Product> item_list, String cur, OnItemClickListener onItemClickListener) {
        if (item_list != null) {
            this.item_list = item_list;
        } else {
            this.item_list = new ArrayList<>();
        }
        this.cur = cur;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            if (item_list.get(position).getQty() == null)
                holder.item_name.setText(item_list.get(position).getName().trim());
            else
                holder.item_name.setText(item_list.get(position).getName().trim() + " +" + item_list.get(position).getQty());

            holder.item_price.setText(cur + " " + String.valueOf(item_list.get(position).getPrice()));
            holder.product_decription.setText(item_list.get(position).getDescription().trim());
            if (item_list.get(position).getProductServiceTaxes().get(0).getTaxName() == null) {
                if (item_list.get(position).getProductServiceTaxes().size() != 1)
                    holder.taxtes.setText(item_list.get(position).getProductServiceTaxes().get(0).getName().trim() + "+" + item_list.get(position).getProductServiceTaxes().size());
                else
                    holder.taxtes.setText(item_list.get(position).getProductServiceTaxes().get(0).getName().trim());
            } else {
                if (item_list.get(position).getProductServiceTaxes().size() != 1)
                    holder.taxtes.setText(item_list.get(position).getProductServiceTaxes().get(0).getTaxName().trim() + "+" + item_list.get(position).getProductServiceTaxes().size());
                else
                    holder.taxtes.setText(item_list.get(position).getProductServiceTaxes().get(0).getTaxName().trim());
            }

        } catch (Exception e) {
        }
        holder.item_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_list.size() != 0)
                    onItemClickListener.onItemClick(item_list.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Product item, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_name, item_price, product_decription, taxtes;
        public LinearLayout item_main;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.product_decription = itemView.findViewById(R.id.product_decription);
            this.taxtes = itemView.findViewById(R.id.taxtes);
            this.item_main = itemView.findViewById(R.id.item_main);
        }
    }
}
