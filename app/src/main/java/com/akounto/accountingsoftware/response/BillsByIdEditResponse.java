package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.request.AddBillTransaction;
import com.google.gson.annotations.SerializedName;

public class BillsByIdEditResponse {
    @SerializedName("Data")
    private AddBillTransaction data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public AddBillTransaction getData() {
        return this.data;
    }

    public void setData(AddBillTransaction data2) {
        this.data = data2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
