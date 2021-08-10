package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.request.BillsById;
import com.google.gson.annotations.SerializedName;

public class BillsByIdResponse {
    @SerializedName("Data")
    private BillsById data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public BillsById getData() {
        return this.data;
    }

    public void setData(BillsById data2) {
        this.data = data2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
