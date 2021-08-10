package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;
    @SerializedName("Data")
    private UserInfo userInfo;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }
}
