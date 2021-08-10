package com.akounto.accountingsoftware.Data.Profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.Data.RegisterBank.TransactionStatus;

public class UserRegister {

    @SerializedName("TransactionStatus")
    @Expose
    private TransactionStatus transactionStatus;

    @SerializedName("Data")
    @Expose
    private String data;

    private int status;
    private String statusMessage;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
