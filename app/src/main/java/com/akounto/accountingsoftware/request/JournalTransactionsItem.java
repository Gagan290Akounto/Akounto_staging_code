package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class JournalTransactionsItem implements Serializable {
    @SerializedName("Amount")
    private String amount;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f110id;
    @SerializedName("JournalId")
    private int journalId;
    @SerializedName("Notes")
    private Object notes;
    @SerializedName("TransactionHeadId")
    private int transactionHeadId;
    @SerializedName("TransactionType")
    private int transactionType;

    public void setTransactionType(int transactionType2) {
        this.transactionType = transactionType2;
    }

    public int getTransactionType() {
        return this.transactionType;
    }

    public void setJournalId(int journalId2) {
        this.journalId = journalId2;
    }

    public int getJournalId() {
        return this.journalId;
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
        this.f110id = id;
    }

    public int getId() {
        return this.f110id;
    }

    public void setNotes(Object notes2) {
        this.notes = notes2;
    }

    public Object getNotes() {
        return this.notes;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(int companyId2) {
        this.companyId = companyId2;
    }

    public JournalTransactionsItem(int transactionType2, int journalId2, String amount2, int transactionHeadId2, int id, Object notes2) {
        this.transactionType = transactionType2;
        this.journalId = journalId2;
        this.amount = amount2;
        this.transactionHeadId = transactionHeadId2;
        this.f110id = id;
        this.notes = notes2;
    }
}
