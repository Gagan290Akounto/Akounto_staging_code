package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.TransactionCategoryClick;
import com.akounto.accountingsoftware.model.AddWithDrawalCategory;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import java.util.List;

public class AddWithdrawalCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    public EditText editText;
    private final List<AddWithdrawalCategorySpinnerItem> list;
    /* access modifiers changed from: private */
    public TextView textView;
    TransactionCategoryClick transactionCategoryClick;

    public AddWithdrawalCategoryAdapter(Context context2, List<AddWithdrawalCategorySpinnerItem> itemList, TransactionCategoryClick transactionCategoryClick2) {
        this.context = context2;
        this.list = itemList;
        this.transactionCategoryClick = transactionCategoryClick2;
    }

    public AddWithdrawalCategoryAdapter(Context context2, List<AddWithdrawalCategorySpinnerItem> itemList, TextView textView2, EditText editText2, TransactionCategoryClick transactionCategoryClick2) {
        this.context = context2;
        this.list = itemList;
        this.textView = textView2;
        this.editText = editText2;
        this.transactionCategoryClick = transactionCategoryClick2;
    }

    public int getItemViewType(int position) {
        return this.list.get(position).getItemType().ordinal();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AddWithDrawalCategory.HEADER.ordinal()) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_withdrawal_category_header, parent, false));
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_withdrawal_category_item, parent, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.list.get(position).getItemType().ordinal() == AddWithDrawalCategory.HEADER.ordinal()) {
            ((HeaderViewHolder) holder).bind(this.list.get(position));
        } else {
            ((ItemViewHolder) holder).bind(this.list.get(position));
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.header = itemView.findViewById(R.id.category_header);
        }

        public void bind(AddWithdrawalCategorySpinnerItem addWithdrawalCategorySpinnerItem) {
            this.header.setText(addWithdrawalCategorySpinnerItem.getTitle());
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryItemLayout;
        TextView item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.category_item);
            this.categoryItemLayout = itemView.findViewById(R.id.category_item_layout);
        }

        public void bind(AddWithdrawalCategorySpinnerItem addWithdrawalCategorySpinnerItem) {
            this.item.setText(addWithdrawalCategorySpinnerItem.getTitle());
            this.categoryItemLayout.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AddWithdrawalCategoryAdapter.ItemViewHolder.this.lambda$bind$0$AddWithdrawalCategoryAdapter$ItemViewHolder(addWithdrawalCategorySpinnerItem, view);
                }
            });
        }

        public /* synthetic */ void lambda$bind$0$AddWithdrawalCategoryAdapter$ItemViewHolder(AddWithdrawalCategorySpinnerItem addWithdrawalCategorySpinnerItem, View v) {
            AddWithdrawalCategoryAdapter.this.transactionCategoryClick.onCategoryClick(addWithdrawalCategorySpinnerItem, AddWithdrawalCategoryAdapter.this.textView, AddWithdrawalCategoryAdapter.this.editText);
        }
    }
}
