
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastBankTransaction {

    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("TransactionType")
    @Expose
    private Integer transactionType;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("TransactionHeadName")
    @Expose
    private String transactionHeadName;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTransactionHeadName() {
        return transactionHeadName;
    }

    public void setTransactionHeadName(String transactionHeadName) {
        this.transactionHeadName = transactionHeadName;
    }

}
