package com.akounto.accountingsoftware.response.getBalanceSheet;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class GetBalanceSheet {
    @SerializedName("Data")
    private GetBalanceSheetData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public GetBalanceSheetData getData() {
        return this.Data;
    }

    public void setData(GetBalanceSheetData data) {
        this.Data = data;
    }
}
