package com.akounto.accountingsoftware.response.saletax;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class SaleTaxDetails {
    @SerializedName("Data")
    private SaleTaxResponseData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public SaleTaxResponseData getData() {
        return this.Data;
    }

    public void setData(SaleTaxResponseData data) {
        this.Data = data;
    }
}
