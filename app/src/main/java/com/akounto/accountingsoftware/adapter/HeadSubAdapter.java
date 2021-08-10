package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.ChartsOfAccountItemClick;
import com.akounto.accountingsoftware.response.chartaccount.HeadTransactions;
import java.util.List;

public class HeadSubAdapter extends RecyclerView.Adapter<HeadSubAdapter.HeadSubHolder> {
    ChartsOfAccountItemClick chartsOfAccountItemClick;
    Context context;
    List<HeadTransactions> headList;

    public HeadSubAdapter(Context context2, List<HeadTransactions> headList2, ChartsOfAccountItemClick chartsOfAccountItemClick2) {
        this.context = context2;
        this.headList = headList2;
        this.chartsOfAccountItemClick = chartsOfAccountItemClick2;
    }

    public HeadSubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeadSubHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subhead, parent, false));
    }

    public void onBindViewHolder(HeadSubHolder holder, int position) {
        String str;
        HeadSubHolder vh = holder;
        TextView textView = vh.accountDesc;
        String str2 = "--";
        if (TextUtils.isEmpty(this.headList.get(position).getDescription())) {
            str = str2;
        } else {
            str = this.headList.get(position).getDescription();
        }
        textView.setText(str);
        TextView textView2 = vh.accountName;
        if (!TextUtils.isEmpty(this.headList.get(position).getName())) {
            str2 = this.headList.get(position).getName();
        }
        textView2.setText(str2);
        vh.accountId.setText(this.headList.get(position).getAccountId());
        vh.accountAction.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HeadSubAdapter.this.lambda$onBindViewHolder$0$HeadSubAdapter(position, view);
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$HeadSubAdapter(int position, View v) {
        this.chartsOfAccountItemClick.editAccountAction(this.headList.get(position).getId());
    }

    public int getItemCount() {
        return this.headList.size();
    }

    public class HeadSubHolder extends RecyclerView.ViewHolder {
        ImageView accountAction;
        TextView accountDesc;
        TextView accountId;
        TextView accountName;

        public HeadSubHolder(View itemView) {
            super(itemView);
            this.accountName = itemView.findViewById(R.id.accountName);
            this.accountDesc = itemView.findViewById(R.id.accountDesc);
            this.accountId = itemView.findViewById(R.id.accountId);
            this.accountAction = itemView.findViewById(R.id.accountAction);
        }
    }
}
