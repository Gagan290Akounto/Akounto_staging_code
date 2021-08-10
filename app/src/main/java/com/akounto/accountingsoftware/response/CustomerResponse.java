package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CustomerResponse {
    @SerializedName("Data")
    private List<Customer> data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public List<Customer> getData() {
        return this.data;
    }
}
