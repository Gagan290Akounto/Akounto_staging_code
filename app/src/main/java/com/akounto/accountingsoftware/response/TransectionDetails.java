package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class TransectionDetails {
    @SerializedName("Data")
    private TransectionList Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public TransectionList getData() {
        return this.Data;
    }

    public void setData(TransectionList data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
