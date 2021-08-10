package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BussinessData {

    @SerializedName("TransactionStatus")
    @Expose
    private TransactionStatus transactionStatus;
    @SerializedName("Data")
    @Expose
    private BData data;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BData getData() {
        return data;
    }

    public void setData(BData data) {
        this.data = data;
    }

}
