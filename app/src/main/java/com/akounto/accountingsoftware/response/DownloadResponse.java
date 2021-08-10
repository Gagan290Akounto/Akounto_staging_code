package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class DownloadResponse {
    @SerializedName("Data")
    private pdfData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public pdfData getData() {
        return this.Data;
    }

    public void setData(pdfData data) {
        this.Data = data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
