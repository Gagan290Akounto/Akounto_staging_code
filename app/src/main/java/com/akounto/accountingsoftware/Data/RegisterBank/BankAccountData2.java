package com.akounto.accountingsoftware.Data.RegisterBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankAccountData2 {

    @SerializedName("TransactionStatus")
    @Expose
    private TransactionStatus transactionStatus;

    @SerializedName("Data")
    @Expose
    private BankOptionsData data;

    private int status;
    private String statusMessage;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BankOptionsData getData() {
        return data;
    }

    public void setData(BankOptionsData data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
