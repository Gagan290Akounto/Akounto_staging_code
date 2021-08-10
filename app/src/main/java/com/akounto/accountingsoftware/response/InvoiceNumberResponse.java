package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class InvoiceNumberResponse {
    @SerializedName("Data")
    private String Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public String getData() {
        return this.Data;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
