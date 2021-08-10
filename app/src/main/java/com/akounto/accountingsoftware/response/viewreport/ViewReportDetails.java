package com.akounto.accountingsoftware.response.viewreport;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class ViewReportDetails {
    @SerializedName("Data")
    private ViewReportData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public ViewReportData getData() {
        return this.Data;
    }

    public void setData(ViewReportData data) {
        this.Data = data;
    }
}
