package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class CustomerUpdateResponse {
    @SerializedName("Data")
    private Customer data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public Customer getData() {
        return this.data;
    }

    public void setData(Customer data2) {
        this.data = data2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
