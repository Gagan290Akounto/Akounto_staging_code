package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class BankBalanceItem {
    @SerializedName("AccountNumber")
    private String accountNumber;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("Name")
    private String name;

    public String getBankName() {
        return this.bankName;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getName() {
        return this.name;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }
}
