
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankBalance {

    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("B     ankName")
    @Expose
    private String bAnkName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBAnkName() {
        return bAnkName;
    }

    public void setBAnkName(String bAnkName) {
        this.bAnkName = bAnkName;
    }

}
