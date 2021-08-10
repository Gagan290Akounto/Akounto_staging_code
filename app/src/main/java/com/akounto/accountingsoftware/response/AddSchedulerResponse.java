package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class AddSchedulerResponse {
    @SerializedName("Data")
    private int Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public int getData() {
        return this.Data;
    }

    public void setData(int data) {
        this.Data = data;
    }
}
