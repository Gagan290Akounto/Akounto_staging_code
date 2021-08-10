package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TaxResponseList {
    @SerializedName("Data")
    private List<TaxResponse> Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public List<TaxResponse> getData() {
        return this.Data;
    }

    public void setData(List<TaxResponse> data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
