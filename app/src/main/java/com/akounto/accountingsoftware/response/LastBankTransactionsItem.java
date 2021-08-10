package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class LastBankTransactionsItem {
    @SerializedName("Amount")
    private double amount;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("TransactionDate")
    private String transactionDate;
    @SerializedName("TransactionHeadName")
    private String transactionHeadName;
    @SerializedName("TransactionType")
    private int transactionType;

    public String getBankName() {
        return this.bankName;
    }

    public int getTransactionType() {
        return this.transactionType;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getTransactionHeadName() {
        return this.transactionHeadName;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }
}
