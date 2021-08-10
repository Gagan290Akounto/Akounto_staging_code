package com.akounto.accountingsoftware.response;

public class SignUpResponse {
    private Boolean Data;
    private TransactionStatus TransactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.TransactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.TransactionStatus = transactionStatus;
    }

    public Boolean getData() {
        return this.Data;
    }

    public void setData(Boolean data) {
        this.Data = data;
    }
}
