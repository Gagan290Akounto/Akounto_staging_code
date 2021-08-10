package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;

import java.util.ArrayList;
import java.util.List;

public class TaxtAdapter extends RecyclerView.Adapter<TaxtAdapter.TaxtHolder> {

    private Context context;
    private List<ProductServiceTaxesItem> list = new ArrayList<>();

    public TaxtAdapter(Context context, List<ProductServiceTaxesItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaxtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_taxts, parent, false);
        return new TaxtHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxtHolder holder, int position) {

        final ProductServiceTaxesItem item = list.get(position);
        holder.tv_name.setText(item.getTaxName());
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setTag(list.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                ProductServiceTaxesItem item1 = (ProductServiceTaxesItem) holder.checkBox.getTag();

                item1.setSelected(holder.checkBox.isChecked());

                list.get(position).setSelected(holder.checkBox.isChecked());

                for (int j = 0; j < list.size(); j++) {

                    if (list.get(j).isSelected() == true) {
                        data = data + "\n" + list.get(j).getTaxName().toString();
                    }
                }
                // Toast.makeText(context, "Selected Fruits : \n " + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaxtHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        CheckBox checkBox;

        public TaxtHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_taxt_name);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }

    public List<ProductServiceTaxesItem> getItemList() {
        List<ProductServiceTaxesItem> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                result.add(list.get(i));
            }
        }
        return result;
    }
}
