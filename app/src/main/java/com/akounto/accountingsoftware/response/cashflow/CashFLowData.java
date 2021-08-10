package com.akounto.accountingsoftware.response.cashflow;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class CashFLowData {
    @SerializedName("Data")
    private CashFlowTransactionDetails Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public CashFlowTransactionDetails getData() {
        return this.Data;
    }

    public void setData(CashFlowTransactionDetails data) {
        this.Data = data;
    }
}
