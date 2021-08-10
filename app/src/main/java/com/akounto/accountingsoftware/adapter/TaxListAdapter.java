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
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.Activity.fragment.UpdateProduct;
import com.akounto.accountingsoftware.response.TaxResponse;

import java.util.List;

public class TaxListAdapter extends RecyclerView.Adapter<TaxListAdapter.MyViewHolder> {
    Context context;
    UpdateProduct onClickListener = this.onClickListener;
    private final List<TaxResponse> productList;//id
    List<TaxResponse> allreadyadded;//txtId
    List<TaxResponse> allreadyadded_taxtResponse;//txtId

    public TaxListAdapter(Context context2, List<TaxResponse> productList2, List<TaxResponse> allreadyadded) {
        this.context = context2;
        this.productList = productList2;
        this.allreadyadded = allreadyadded;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listitemservices, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!isPresent(productList.get(position).getId())) {
            String str;
            String str2;
            MyViewHolder vh = holder;
            TextView textView = vh.textViewPrice;
            String str3 = "--";
            try {
                if (TextUtils.isEmpty(this.productList.get(position).getEffectiveTaxes().get(0).getRate() + "")) {
                    str = str3;
                } else {
                    str = this.productList.get(position).getEffectiveTaxes().get(0).getRate() + "%";
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
                if (!TextUtils.isEmpty(String.valueOf(this.productList.get(position).getName()))) {
                    str3 = String.valueOf(this.productList.get(position).getName());
                }
                textView3.setText(str3);
                vh.pConstarnt.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        TaxListAdapter.this.lambda$onBindViewHolder$0$TaxListAdapter(position, view);
                    }
                });
            } catch (Exception e) {
            }
        }

        /*else{
            holder.itemView.setVisibility(View.GONE);
        }*/

    }

    public void lambda$onBindViewHolder$0$TaxListAdapter(int position, View v) {
        Context context2 = this.context;
        if (context2 instanceof CommonInvoiceActivity) {
            ((CommonInvoiceActivity) context2).addTaxTopup(this.productList.get(position));
        } else {
            ((CreateBillActivity) context2).addTaxTopup(this.productList.get(position));
        }
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

    public boolean isPresent(int list_id) {
        boolean result = false;
        for (int i = 0; i < allreadyadded.size(); i++) {
            if (list_id == allreadyadded.get(i).getId())
                return true;
        }
        return result;
    }
}
