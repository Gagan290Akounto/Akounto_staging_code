package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.Data.Business;

public class AddBusinessResponse {

    @SerializedName("TransactionStatus")
    @Expose
    private com.akounto.accountingsoftware.model.TransactionStatus transactionStatus;

    @SerializedName("Data")
    @Expose
    private Business data;

    public com.akounto.accountingsoftware.model.TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(com.akounto.accountingsoftware.model.TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Business getData() {
        return data;
    }

    public void setData(Business data) {
        this.data = data;
    }

}
