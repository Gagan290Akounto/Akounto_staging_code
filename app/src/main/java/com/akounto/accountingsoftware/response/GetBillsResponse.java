package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class GetBillsResponse {
    @SerializedName("Data")
    private BillListResponse Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public BillListResponse getData() {
        return this.Data;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public void setData(BillListResponse data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
