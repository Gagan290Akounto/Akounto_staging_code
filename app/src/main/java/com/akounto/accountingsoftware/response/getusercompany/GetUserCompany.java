package com.akounto.accountingsoftware.response.getusercompany;

import com.akounto.accountingsoftware.model.UserBusiness;
import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class GetUserCompany {
    @SerializedName("Data")
    private UserBusiness Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public void setTransactionStatus(TransactionStatus transactionStatus2) {
        this.transactionStatus = transactionStatus2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public UserBusiness getData() {
        return this.Data;
    }

    public void setData(UserBusiness data) {
        this.Data = data;
    }
}
