package com.akounto.accountingsoftware.response.trialbalance;

import com.google.gson.annotations.SerializedName;

public class TrialBalanceTransaction {
    @SerializedName("Credit")
    private int Credit;
    @SerializedName("Debit")
    private int Debit;
    @SerializedName("SubHeadId")
    private int SubHeadId;
    @SerializedName("SubHeadName")
    private String SubHeadName;
    @SerializedName("TransactionDate")
    private String TransactionDate;
    @SerializedName("TransactionId")
    private int TransactionId;
    @SerializedName("TransactionName")
    private String TransactionName;

    public int getCredit() {
        return this.Credit;
    }

    public void setCredit(int credit) {
        this.Credit = credit;
    }

    public int getDebit() {
        return this.Debit;
    }

    public void setDebit(int debit) {
        this.Debit = debit;
    }

    public int getSubHeadId() {
        return this.SubHeadId;
    }

    public void setSubHeadId(int subHeadId) {
        this.SubHeadId = subHeadId;
    }

    public int getTransactionId() {
        return this.TransactionId;
    }

    public void setTransactionId(int transactionId) {
        this.TransactionId = transactionId;
    }

    public String getSubHeadName() {
        return this.SubHeadName;
    }

    public void setSubHeadName(String subHeadName) {
        this.SubHeadName = subHeadName;
    }

    public String getTransactionName() {
        return this.TransactionName;
    }

    public void setTransactionName(String transactionName) {
        this.TransactionName = transactionName;
    }

    public String getTransactionDate() {
        return this.TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.TransactionDate = transactionDate;
    }
}
