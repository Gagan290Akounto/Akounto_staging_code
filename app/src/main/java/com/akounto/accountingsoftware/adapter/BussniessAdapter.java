package com.akounto.accountingsoftware.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Business;
import com.akounto.accountingsoftware.R;

import java.util.ArrayList;
import java.util.List;

public class BussniessAdapter extends RecyclerView.Adapter<BussniessAdapter.ViewHolder> {


    private List<Business> item_list;
    private OnItemClickListener onItemClickListener;
    private int id;

    public BussniessAdapter(List<Business> item_list, int id, OnItemClickListener onItemClickListener) {
        if (item_list != null) {
            this.item_list = item_list;
        } else {
            this.item_list = new ArrayList<>();
        }
        this.id = id;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bussniess, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tv_name_char.setText("" + item_list.get(position).getName().charAt(0));
            holder.comp_name.setText(item_list.get(position).getName());
            holder.tv_name.setText(item_list.get(position).getUName());
            if (item_list.get(position).getId() == id) {
                holder.is_selected.setImageResource(R.drawable.selected_busns);
            } else {
                holder.is_selected.setImageResource(R.drawable.unselected_busns);
            }
            holder.id_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item_list.size() != 0)
                        onItemClickListener.onItemClick(item_list.get(position), position);
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Business cust, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name_char, comp_name, tv_name;
        public LinearLayout id_customer;
        public ImageView is_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_name_char = itemView.findViewById(R.id.tv_name_char);
            this.comp_name = itemView.findViewById(R.id.comp_name);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.id_customer = itemView.findViewById(R.id.id_customer);
            this.is_selected = itemView.findViewById(R.id.is_selected);
        }
    }
}
