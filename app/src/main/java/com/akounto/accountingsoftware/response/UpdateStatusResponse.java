package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusResponse {
    @SerializedName("Data")
    private InvoiceDetails data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public InvoiceDetails getData() {
        return this.data;
    }

    public void setData(InvoiceDetails data2) {
        this.data = data2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
