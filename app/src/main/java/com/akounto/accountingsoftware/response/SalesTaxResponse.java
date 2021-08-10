package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SalesTaxResponse {
    @SerializedName("Data")
    private List<SaleTax> data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public List<SaleTax> getData() {
        return this.data;
    }
}
