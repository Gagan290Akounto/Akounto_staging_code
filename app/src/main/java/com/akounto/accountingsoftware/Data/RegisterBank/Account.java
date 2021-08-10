
package com.akounto.accountingsoftware.Data.RegisterBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {


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
    private Integer accountType;
    @SerializedName("AccountSubType")
    @Expose
    private String accountSubType;
    @SerializedName("AvailableBalance")
    @Expose
    private Double availableBalance;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("IsDisable")
    @Expose
    private Boolean isDisable;
    @SerializedName("IsConnected")
    @Expose
    private Boolean isConnected;
    @SerializedName("IsChecked")
    @Expose
    private Boolean isChecked;
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

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(String accountSubType) {
        this.accountSubType = accountSubType;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Boolean isDisable) {
        this.isDisable = isDisable;
    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
