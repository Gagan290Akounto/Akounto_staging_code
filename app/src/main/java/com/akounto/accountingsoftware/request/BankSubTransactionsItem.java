package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BankSubTransactionsItem implements Serializable {
    @SerializedName("Amount")
    private String amount;
    @SerializedName("BankTransactionId")
    private int bankTransactionId;
    @SerializedName("CompanyId")
    private String companyId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f104id;
    @SerializedName("Status")
    private int status;
    @SerializedName("TransactionHeadId")
    private int transactionHeadId;
    @SerializedName("TransactionHeadName")
    private Object transactionHeadName;

    public void setStatus(int status2) {
        this.status = status2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setCompanyId(String companyId2) {
        this.companyId = companyId2;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setTransactionHeadId(int transactionHeadId2) {
        this.transactionHeadId = transactionHeadId2;
    }

    public int getTransactionHeadId() {
        return this.transactionHeadId;
    }

    public void setId(int id) {
        this.f104id = id;
    }

    public int getId() {
        return this.f104id;
    }

    public void setBankTransactionId(int bankTransactionId2) {
        this.bankTransactionId = bankTransactionId2;
    }

    public int getBankTransactionId() {
        return this.bankTransactionId;
    }

    public Object getTransactionHeadName() {
        return this.transactionHeadName;
    }

    public void setTransactionHeadName(Object transactionHeadName2) {
        this.transactionHeadName = transactionHeadName2;
    }

    public BankSubTransactionsItem(int status2, String companyId2, String amount2, int transactionHeadId2, int id, int bankTransactionId2) {
        this.status = status2;
        this.companyId = companyId2;
        this.amount = amount2;
        this.transactionHeadId = transactionHeadId2;
        this.f104id = id;
        this.bankTransactionId = bankTransactionId2;
    }
}
