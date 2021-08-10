package com.akounto.accountingsoftware.response.currency;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class CurrencyResponse {
    @SerializedName("Data")
    private CurrenucyData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public CurrenucyData getData() {
        return Data;
    }

    public void setData(CurrenucyData data) {
        Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
