package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class CustomerOnlyResponse {
    @SerializedName("Data")
    private Customer data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public Customer getData() {
        return this.data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
