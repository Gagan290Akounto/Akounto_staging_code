package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetStatesResponse {
    @SerializedName("Data")
    private List<String> states;

    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    private int status;
    private String statusMessage;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public List<String> getStates() {
        return this.states;
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