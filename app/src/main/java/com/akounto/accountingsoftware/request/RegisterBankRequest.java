package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterBankRequest {
    @SerializedName("BankId")
    @Expose
    private String bankId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("MaskAccountNumber")
    @Expose
    private String maskAccountNumber;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("AccountType")
    @Expose
    private String accountType;
    @SerializedName("AccountSubType")
    @Expose
    private String accountSubType;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("StartDate")
    @Expose
    private String startDate;

    public RegisterBankRequest(String bankId, String name, String maskAccountNumber, String accountNumber, String accountType, String accountSubType, String currency, String startDate) {
        this.bankId = bankId;
        this.name = name;
        this.maskAccountNumber = maskAccountNumber;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.currency = currency;
        this.startDate = startDate;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaskAccountNumber() {
        return maskAccountNumber;
    }

    public void setMaskAccountNumber(String maskAccountNumber) {
        this.maskAccountNumber = maskAccountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(String accountSubType) {
        this.accountSubType = accountSubType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
