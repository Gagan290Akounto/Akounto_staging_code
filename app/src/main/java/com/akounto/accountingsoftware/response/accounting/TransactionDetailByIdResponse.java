package com.akounto.accountingsoftware.response.accounting;

import com.akounto.accountingsoftware.response.Transaction;
import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class TransactionDetailByIdResponse {
    @SerializedName("Data")
    private Transaction data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public Transaction getData() {
        return this.data;
    }
}
