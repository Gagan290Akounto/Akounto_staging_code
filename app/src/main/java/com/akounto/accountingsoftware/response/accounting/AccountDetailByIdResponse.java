package com.akounto.accountingsoftware.response.accounting;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class AccountDetailByIdResponse {
    @SerializedName("Data")
    private AccountDetailById accountDetailById;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public AccountDetailById getAccountDetailById() {
        return this.accountDetailById;
    }
}
