package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.UpdateProduct;
import java.util.List;

public class InvalidAdapter extends RecyclerView.Adapter<InvalidAdapter.MyViewHolder> {
    Context context;
    UpdateProduct onClickListener = this.onClickListener;
    private final List<String> productList;

    public InvalidAdapter(Context context2, List<String> productList2) {
        this.context = context2;
        this.productList = productList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invalid, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        CharSequence charSequence;
        TextView textView = holder.textViewPrice;
        if (TextUtils.isEmpty(this.productList.get(position))) {
            charSequence = "--";
        } else {
            charSequence = (productList.size()-(position))+" . "+this.productList.get(position);
        }
        textView.setText(charSequence);
    }

    public int getItemCount() {
        return this.productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView optionMenu;
        LinearLayout pConstarnt;
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
