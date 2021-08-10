package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Currency;
import com.akounto.accountingsoftware.Data.CurrencyData;
import com.akounto.accountingsoftware.Data.RegisterBank.Bank;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {

    private List<Bank> item_list;
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private CurrencyData currencyData;

    public BankListAdapter(List<Bank> item_list, OnItemClickListener onItemClickListener, Context mContext) {
        this.onItemClickListener = onItemClickListener;
        this.item_list = item_list;
        this.mContext = mContext;
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("test.json", mContext);
        currencyData = new Gson().fromJson(loadJSONFromAsset, CurrencyData.class);
    }

    public String getcurrencyData(String curreCode) {
        Currency result = null;
        try {
            if (curreCode != null) {
                for (int i = 0; i < currencyData.getCurrency().size(); i++) {
                    if (currencyData.getCurrency().get(i).getId().endsWith(curreCode)) {
                        result = currencyData.getCurrency().get(i);
                    }
                }
            } else {
                result = currencyData.getCurrency().get(0);
            }
        } catch (Exception e) {
            result = currencyData.getCurrency().get(0);
        }
        return result.getSymbol();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banklist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Bank myListData = item_list.get(position);
        holder.tv_bank_name.setText(myListData.getInstitutionName());
        holder.tv_bank_Account_name.setText(myListData.getBankAccounts().getName());
        holder.tv_last_update.setText("Last updated " + converttodays(myListData.getBankAccounts().getLastTransactionFetchedOn().split("T")[0]) + " ago");
        if (myListData.getBankAccounts().getCurrency() == null) {
            holder.tv_currency.setText(getcurrencyData(myListData.getBankAccounts().getCurrency()) + " " + String.format("%.2f", myListData.getBankAccounts().getAvailableBalance()) + " USD");
        } else {
            holder.tv_currency.setText(getcurrencyData(myListData.getBankAccounts().getCurrency()) + " " + String.format("%.2f", myListData.getBankAccounts().getAvailableBalance()) + " " + myListData.getBankAccounts().getCurrency());
        }
        if (myListData.getBankAccounts().getIsAutoImport()) {
            holder.auto_sync.setImageResource(R.drawable.on_toggle);
        } else {
            holder.auto_sync.setImageResource(R.drawable.off_toggle);
        }
        holder.auto_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(myListData, position, !myListData.getBankAccounts().getIsAutoImport());
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemDownload(myListData, position, myListData.getBankAccounts().getIsAutoImport());
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Bank bank, int position, boolean state);

        void onItemDownload(Bank bank, int position, boolean state);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_bank_name, tv_bank_Account_name, tv_last_update, tv_currency;
        public ImageView auto_sync, download;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_bank_name = itemView.findViewById(R.id.bank_name);
            this.tv_bank_Account_name = itemView.findViewById(R.id.bank_account_name);
            this.tv_last_update = itemView.findViewById(R.id.last_update);
            this.tv_currency = itemView.findViewById(R.id.currency_sign);
            this.auto_sync = itemView.findViewById(R.id.auto_sync);
            this.download = itemView.findViewById(R.id.download);
        }
    }

    public String converttodays(String inputString2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = "";
        long m, d;
        try {
            Date date1 = myFormat.parse(inputString2);
            Date date2 = Calendar.getInstance().getTime();
            long diff = date2.getTime() - date1.getTime();
            long r = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            m = r / 30;
            d = r % 30;
            if (m == 0) {
                if (d == 1) {
                    result = d + " day";
                } else {
                    result = d + " days";
                }
            } else if (d == 0) {
                if (m == 1) {
                    result = m + " months";
                } else {
                    result = m + " months";
                }
            } else {
                result = m + " month " + d + " day";
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return result;
    }
}