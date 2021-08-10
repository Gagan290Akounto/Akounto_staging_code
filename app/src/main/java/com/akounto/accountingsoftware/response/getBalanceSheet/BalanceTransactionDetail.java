package com.akounto.accountingsoftware.response.getBalanceSheet;

import com.google.gson.annotations.SerializedName;

public class BalanceTransactionDetail {
    @SerializedName("BalanceTransactionType")
    private int BalanceTransactionType;
    @SerializedName("HeadId")
    private int HeadId;
    @SerializedName("SubHeadId")
    private int SubHeadId;
    @SerializedName("SubHeadName")
    private String SubHeadName;
    @SerializedName("TransactionAmount")
    private Double TransactionAmount;
    @SerializedName("TransactionId")
    private int TransactionId;
    @SerializedName("TransactionName")
    private String TransactionName;
    @SerializedName("TransactionType")
    private int TransactionType;

    public int getTransactionType() {
        return this.TransactionType;
    }

    public void setTransactionType(int transactionType) {
        this.TransactionType = transactionType;
    }

    public int getBalanceTransactionType() {
        return this.BalanceTransactionType;
    }

    public void setBalanceTransactionType(int balanceTransactionType) {
        this.BalanceTransactionType = balanceTransactionType;
    }

    public int getHeadId() {
        return this.HeadId;
    }

    public void setHeadId(int headId) {
        this.HeadId = headId;
    }

    public int getSubHeadId() {
        return this.SubHeadId;
    }

    public void setSubHeadId(int subHeadId) {
        this.SubHeadId = subHeadId;
    }

    public String getSubHeadName() {
        return this.SubHeadName;
    }

    public void setSubHeadName(String subHeadName) {
        this.SubHeadName = subHeadName;
    }

    public Double getTransactionAmount() {
        return this.TransactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.TransactionAmount = transactionAmount;
    }

    public int getTransactionId() {
        return this.TransactionId;
    }

    public void setTransactionId(int transactionId) {
        this.TransactionId = transactionId;
    }

    public String getTransactionName() {
        return this.TransactionName;
    }

    public void setTransactionName(String transactionName) {
        this.TransactionName = transactionName;
    }
}
