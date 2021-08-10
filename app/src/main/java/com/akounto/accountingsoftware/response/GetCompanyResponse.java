package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class GetCompanyResponse {
    @SerializedName("Data")
    private BusinessDetails businessDetails;

    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    private int status;
    private String statusMessage;


    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public BusinessDetails getBusinessDetails() {
        return this.businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails2) {
        this.businessDetails = businessDetails2;
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
