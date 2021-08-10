package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import java.util.List;

public class PurchasetaxAddAdapter extends RecyclerView.Adapter<PurchasetaxAddAdapter.MyViewHolder> {
    Context context;
    DeleteTaxClick deleteTaxClick;
    List<String> taxList;

    public PurchasetaxAddAdapter(Context context2, List<String> taxList2, DeleteTaxClick deleteTaxClick2) {
        this.context = context2;
        this.taxList = taxList2;
        this.deleteTaxClick = deleteTaxClick2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchasetax, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder vh = holder;
        vh.taxName.setText(this.taxList.get(position));
        vh.taxNameDelete.setOnClickListener(new View.OnClickListener() {
            
            public final void onClick(View view) {
                PurchasetaxAddAdapter.this.lambda$onBindViewHolder$0$PurchasetaxAddAdapter(position, view);
            }
        });
    }

    public void lambda$onBindViewHolder$0$PurchasetaxAddAdapter(int position, View v) {
        this.deleteTaxClick.deleteTax(position);
    }

    public void notify(List<String> taxList2) {
        this.taxList = taxList2;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<String> list = this.taxList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taxName;
        ImageView taxNameDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.taxName = itemView.findViewById(R.id.taxName);
            this.taxNameDelete = itemView.findViewById(R.id.taxNameDelete);
        }
    }
}
