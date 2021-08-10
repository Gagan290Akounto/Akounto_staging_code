package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.util.UiConstants;
import java.util.ArrayList;

public class DashboardItemAdapter extends RecyclerView.Adapter {
    Context context;
    View.OnClickListener onClickListener;
    ArrayList personImages;
    ArrayList personNames;

    public DashboardItemAdapter(Context context2, ArrayList personNames2, ArrayList personImages2, View.OnClickListener onClickListener2) {
        this.context = context2;
        this.personNames = personNames2;
        this.personImages = personImages2;
        this.onClickListener = onClickListener2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_new, parent, false), this.onClickListener);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.textViewTitle.setText(UiConstants.getDashboardItemName().get(position));
        if (position == 0) {
            vh.menuImageView.setImageResource(R.drawable.ic_shopping_cart);
            vh.textViewTitle.setText("Purchase");
        } else if (position == 1) {
            vh.menuImageView.setImageResource(R.drawable.ic_discount_sales);
            vh.textViewTitle.setText("Sales");
        } else if (position == 2) {
            vh.menuImageView.setImageResource(R.drawable.ic_keys_accounting);
            vh.textViewTitle.setText("Accounting");
        } else if (position != 3) {
            vh.menuImageView.setImageResource(R.drawable.ic_shopping_cart);
            vh.textViewTitle.setText("Sales");
        } else {
            vh.menuImageView.setImageResource(R.drawable.ic_report_sales);
            vh.textViewTitle.setText("Report");
        }
        vh.parentRL.setTag(Integer.valueOf(position));
    }

    public int getItemCount() {
        return this.personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView menuImageView;
        CardView parentRL;
        TextView textViewMsg;
        TextView textViewTitle;

        public MyViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.menuImageView = itemView.findViewById(R.id.menuImageView);
            CardView cardView = itemView.findViewById(R.id.parentRL);
            this.parentRL = cardView;
            cardView.setOnClickListener(onClickListener);
        }
    }
}
