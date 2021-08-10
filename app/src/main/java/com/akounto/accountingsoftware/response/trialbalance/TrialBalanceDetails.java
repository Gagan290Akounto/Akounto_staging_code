package com.akounto.accountingsoftware.response.trialbalance;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class TrialBalanceDetails {
    @SerializedName("Data")
    private TrialBalance Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public TrialBalance getData() {
        return this.Data;
    }

    public void setData(TrialBalance data) {
        this.Data = data;
    }
}
