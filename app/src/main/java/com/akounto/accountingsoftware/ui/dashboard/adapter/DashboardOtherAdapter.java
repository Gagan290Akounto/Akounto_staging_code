package com.akounto.accountingsoftware.ui.dashboard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Dashboard.BankBalance;
import com.akounto.accountingsoftware.Data.Dashboard.LastActivity;
import com.akounto.accountingsoftware.Data.Dashboard.LastBankTransaction;
import com.akounto.accountingsoftware.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardOtherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ConnectedAccounts = 1;
    private final int BankTransactions = 2;
    private final int RecentActivities = 3;

    private final String transactionDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private final String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";

    Context context;
    int dialogType;
    private ArrayList<BankBalance> connectedAccountList;
    private ArrayList<LastBankTransaction> bankTransactionList;
    private ArrayList<LastActivity> recentActivityList;

    public DashboardOtherAdapter(Context context, int dialogType) {
        this.context = context;
        this.dialogType = dialogType;
    }

    public void setConnectedAccountList(List<BankBalance> list) {
        connectedAccountList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            connectedAccountList.clear();
            connectedAccountList.addAll(list);
        }
    }

    public void setBankTransactionList(List<LastBankTransaction> list) {
        bankTransactionList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            bankTransactionList.clear();
            bankTransactionList.addAll(list);
        }
    }

    public void setRecentActivityList(List<LastActivity> list) {
        recentActivityList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            recentActivityList.clear();
            recentActivityList.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ConnectedAccounts) {
            view = LayoutInflater.from(context).inflate(R.layout.item_connected_account_layout, parent, false);
            return new ConnectedAccountsHolder(view);
        } else if (viewType == BankTransactions) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bank_transactions_layout, parent, false);
            return new BankTransactionHolder(view);
        } else if (viewType == RecentActivities) {
            view = LayoutInflater.from(context).inflate(R.layout.item_recent_activities_layout, parent, false);
            return new RecentTransactionHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (dialogType == ConnectedAccounts) {
            ConnectedAccountsHolder accountsHolder = (ConnectedAccountsHolder) holder;
            accountsHolder.bindData(connectedAccountList.get(position));

        } else if (dialogType == BankTransactions) {
            BankTransactionHolder transactionHolder = (BankTransactionHolder) holder;
            transactionHolder.bindData(bankTransactionList.get(position));

        } else if (dialogType == RecentActivities) {
            RecentTransactionHolder recentTransactionHolder = (RecentTransactionHolder) holder;
            recentTransactionHolder.bindData(recentActivityList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (dialogType == ConnectedAccounts) {
            return ConnectedAccounts;
        } else if (dialogType == BankTransactions) {
            return BankTransactions;
        } else if (dialogType == RecentActivities) {
            return RecentActivities;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        if (dialogType == ConnectedAccounts) {
            return connectedAccountList.size();

        } else if (dialogType == BankTransactions) {
            return bankTransactionList.size();

        } else if (dialogType == RecentActivities) {
            return recentActivityList.size();

        } else {
            return 0;
        }
    }

    public class ConnectedAccountsHolder extends RecyclerView.ViewHolder {
        TextView tvConnectedTitle, tvConnectedDate,
                tvConnectedCreditCard, tvConnectedAmount;

        public ConnectedAccountsHolder(View view) {
            super(view);
            tvConnectedTitle = view.findViewById(R.id.tvConnectedTitle);
            tvConnectedDate = view.findViewById(R.id.tvConnectedDate);
            tvConnectedCreditCard = view.findViewById(R.id.tvConnectedCreditCard);
            tvConnectedAmount = view.findViewById(R.id.tvConnectedAmount);
        }

        public void bindData(BankBalance bankBalance) {
            if (bankBalance != null) {
                if (!TextUtils.isEmpty(bankBalance.getName())) {
                    tvConnectedTitle.setText(bankBalance.getName());
                }
                //tvConnectedDate.setText(bankBalance.getBAnkName());
                if (!TextUtils.isEmpty(bankBalance.getAccountNumber())) {
                    tvConnectedCreditCard.setText("************" + bankBalance.getAccountNumber());
                }
                if (!TextUtils.isEmpty(bankBalance.getAmount().toString())) {
                    tvConnectedAmount.setText(bankBalance.getAmount().toString());
                }
            }
        }
    }

    public class BankTransactionHolder extends RecyclerView.ViewHolder {
        TextView tvBankTransactionTitle, tvBankTransactionDate,
                tvBankTransactionPaidBy, tvBankTransactionAmount;

        public BankTransactionHolder(View view) {
            super(view);
            tvBankTransactionTitle = view.findViewById(R.id.tvBankTransactionTitle);
            tvBankTransactionDate = view.findViewById(R.id.tvBankTransactionDate);
            tvBankTransactionPaidBy = view.findViewById(R.id.tvBankTransactionPaidBy);
            tvBankTransactionAmount = view.findViewById(R.id.tvBankTransactionAmount);
        }

        public void bindData(LastBankTransaction lastBankTransaction) {
            if (lastBankTransaction != null) {
                if (!TextUtils.isEmpty(lastBankTransaction.getTransactionHeadName())) {
                    tvBankTransactionTitle.setText(lastBankTransaction.getTransactionHeadName());
                } else {
                    tvBankTransactionTitle.setText("Transaction");
                }

                if (!TextUtils.isEmpty(lastBankTransaction.getBankName())) {
                    tvBankTransactionPaidBy.setText(lastBankTransaction.getBankName());
                } else {
                    tvBankTransactionPaidBy.setText("Bank");
                }

                String date;
                if (TextUtils.isEmpty(lastBankTransaction.getTransactionDate())) {
                    date = "";
                } else {
                    date = lastBankTransaction.getTransactionDate();
                }
                try {
                    Date inputDate2 = new SimpleDateFormat(transactionDateFormat, Locale.US).parse(date);
                    tvBankTransactionDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.US).format(inputDate2));
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }

                String prefix = "";
                if (lastBankTransaction.getTransactionType() == 1) {
                    prefix = "+$";
                } else {
                    prefix = "-$";
                }
                if (!TextUtils.isEmpty(String.valueOf(lastBankTransaction.getAmount()))) {
                    tvBankTransactionAmount.setText(prefix + lastBankTransaction.getAmount());
                }
            }
        }
    }

    public class RecentTransactionHolder extends RecyclerView.ViewHolder {
        TextView tvRecentActivityTitle, tvRecentActivityDateTime;

        public RecentTransactionHolder(View view) {
            super(view);
            tvRecentActivityTitle = view.findViewById(R.id.tvRecentActivityTitle);
            tvRecentActivityDateTime = view.findViewById(R.id.tvRecentActivityDateTime);
        }

        public void bindData(LastActivity lastActivity) {
            if (lastActivity != null) {
                if (!TextUtils.isEmpty(lastActivity.getRemarks())) {
                    tvRecentActivityTitle.setText(lastActivity.getRemarks());
                } else {
                    tvRecentActivityTitle.setText("--");
                }

                try {
                    Log.e("DashboardOtherAdapter", "Recent : " + lastActivity.getCreated());
                    String date = "";
                    if (!TextUtils.isEmpty(lastActivity.getCreated())) {
                        date = lastActivity.getCreated();
                    }
                    Date inputDate = new SimpleDateFormat(isoDatePattern, Locale.US).parse(date);
                    SimpleDateFormat outPutFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.US);

                    tvRecentActivityDateTime.setText(outPutFormat.format(inputDate));
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
