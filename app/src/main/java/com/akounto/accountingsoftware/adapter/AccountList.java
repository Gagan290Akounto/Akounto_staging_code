package com.akounto.accountingsoftware.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.RegisterBank.Account;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData2;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.databinding.LayoutBankImportLisItemBinding;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountList extends RecyclerView.Adapter<AccountList.RecyclerViewHolders> {

    private List<Account> itemList;
    private AccountList.IonItemSelect ionItemSelect;
    private ArrayList<Integer> selectCheck;
    private Context mContext;

    public AccountList(List<Account> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
        selectCheck = new ArrayList<>();
        setChecked();
    }

    public void registerOnItemClickListener(AccountList.IonItemSelect ionItemSelect) {
        this.ionItemSelect = ionItemSelect;
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LayoutBankImportLisItemBinding binding;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (ionItemSelect != null)
                ionItemSelect.onItemSelect(getAdapterPosition());
        }
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bank_import_lis_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.binding.tvBankName.setText(itemList.get(position).getName());
        holder.binding.tvBankAccount.setText("*" + itemList.get(position).getMaskAccountNumber());
        holder.binding.balance.setText("Balance " + UiUtil.getBussinessCurrenSymbul(mContext) + String.format("%.2f", itemList.get(position).getAvailableBalance()));
        if (!itemList.get(position).getIsDisable()) {
            if (!itemList.get(position).getIsConnected()) {
                holder.binding.checkBox.setVisibility(View.VISIBLE);
                holder.binding.isConnected.setVisibility(View.GONE);
            } else {
                holder.binding.checkBox.setVisibility(View.GONE);
                holder.binding.isConnected.setVisibility(View.VISIBLE);
            }
        } else {
            holder.binding.checkBox.setVisibility(View.GONE);
            if (!itemList.get(position).getIsConnected()) {
                holder.binding.isConnected.setVisibility(View.GONE);
            } else {
                holder.binding.isConnected.setVisibility(View.VISIBLE);
            }
           /* if (itemList.get(position).getIsDisable()) {
                holder.binding.isConnected.setAlpha(0.5f);
                holder.binding.tvBankName.setAlpha(0.5f);
                holder.binding.tvBankAccount.setAlpha(0.5f);
                holder.binding.balance.setAlpha(0.5f);
            }*/
        }

        if (selectCheck.get(position) == 1) {
            holder.binding.checkBox.setChecked(true);
        } else {
            holder.binding.checkBox.setChecked(false);
        }

        holder.binding.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int k = 0; k < selectCheck.size(); k++) {
                    if (k == position) {
                        selectCheck.set(k, 1);
                        itemList.get(k).setChecked(true);
                    } else {
                        selectCheck.set(k, 0);
                        itemList.get(k).setChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public interface IonItemSelect {
        void onItemSelect(int position);
    }

    public Account getSelectedAccount() {
        Account account = null;
        for (int i = 0; i < itemList.size(); i++) {
            try {
                if (itemList.get(i).getChecked()) {
                    account = itemList.get(i);
                }
            } catch (Exception e) {
            }
        }
        return account;
    }

    private void setChecked() {
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).setChecked(false);
            selectCheck.add(0);
        }
    }
}
