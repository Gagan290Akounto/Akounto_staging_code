package com.akounto.accountingsoftware.response.accounting;

import com.akounto.accountingsoftware.request.AddJournalTransactionRequest;
import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class JournalTransactionDetailByIdResponse {
    @SerializedName("Data")
    private AddJournalTransactionRequest data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public AddJournalTransactionRequest getData() {
        return this.data;
    }
}
