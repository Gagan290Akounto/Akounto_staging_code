package com.akounto.accountingsoftware.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.Vendor;

import java.util.ArrayList;
import java.util.List;

public class BillVendorAdapter extends RecyclerView.Adapter<BillVendorAdapter.ViewHolder>  {

    private List<Vendor> item_list;
    private OnItemClickListener onItemClickListener;

    public BillVendorAdapter(List<Vendor> item_list, OnItemClickListener onItemClickListener) {
        if (item_list != null) {
            this.item_list = item_list;
        } else {
            this.item_list = new ArrayList<>();
        }
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillVendorAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name_char.setText(""+item_list.get(position).getVendorName().charAt(0));
        holder.comp_name.setText(item_list.get(position).getVendorName());
        holder.tv_name.setText(item_list.get(position).getEmail());
        holder.id_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(item_list.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Vendor vendor, int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name_char, comp_name, tv_name;
        public LinearLayout id_customer;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_name_char = itemView.findViewById(R.id.tv_name_char);
            this.comp_name = itemView.findViewById(R.id.comp_name);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.id_customer = itemView.findViewById(R.id.id_customer);
        }
    }
}
