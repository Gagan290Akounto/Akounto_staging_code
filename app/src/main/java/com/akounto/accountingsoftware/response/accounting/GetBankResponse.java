package com.akounto.accountingsoftware.response.accounting;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class GetBankResponse {
    @SerializedName("Data")
    private BankResponse bankResponse;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public BankResponse getBankResponse() {
        return this.bankResponse;
    }
}
