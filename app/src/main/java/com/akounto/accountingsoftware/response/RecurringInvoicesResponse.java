package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class RecurringInvoicesResponse {
    @SerializedName("Data")
    private RecurringInvoices recurringInvoices;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public RecurringInvoices getRecurringInvoices() {
        return this.recurringInvoices;
    }
}
