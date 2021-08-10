package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IncomeAccountsResponse {
    @SerializedName("Data")
    private List<IncomeAccount> data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public List<IncomeAccount> getData() {
        return this.data;
    }
}
