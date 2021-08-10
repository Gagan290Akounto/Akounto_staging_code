package com.akounto.accountingsoftware.response.chartaccount;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class GetChartResponse {
    @SerializedName("Data")
    private GetChartData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public GetChartData getData() {
        return this.Data;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public void setData(GetChartData data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
