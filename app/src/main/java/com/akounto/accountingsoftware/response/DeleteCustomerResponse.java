package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class DeleteCustomerResponse {
    @SerializedName("Data")
    private String data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
