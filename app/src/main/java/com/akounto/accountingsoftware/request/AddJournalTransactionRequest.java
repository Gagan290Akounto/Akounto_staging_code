package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class AddJournalTransactionRequest implements Serializable {
    @SerializedName("Amount")
    private int amount;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")

    /* renamed from: id */
    private int f100id;
    @SerializedName("JournalTransactions")
    private List<JournalTransactionsItem> journalTransactions;
    @SerializedName("Notes")
    private Object notes;
    @SerializedName("Review")
    private int review;
    @SerializedName("TransactionDate")
    private String transactionDate;

    public int getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(int companyId2) {
        this.companyId = companyId2;
    }

    public int getReview() {
        return this.review;
    }

    public void setReview(int review2) {
        this.review = review2;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setAmount(int amount2) {
        this.amount = amount2;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setJournalTransactions(List<JournalTransactionsItem> journalTransactions2) {
        this.journalTransactions = journalTransactions2;
    }

    public List<JournalTransactionsItem> getJournalTransactions() {
        return this.journalTransactions;
    }

    public void setId(int id) {
        this.f100id = id;
    }

    public int getId() {
        return this.f100id;
    }

    public void setNotes(Object notes2) {
        this.notes = notes2;
    }

    public Object getNotes() {
        return this.notes;
    }

    public void setTransactionDate(String transactionDate2) {
        this.transactionDate = transactionDate2;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public AddJournalTransactionRequest(String description2, int amount2, List<JournalTransactionsItem> journalTransactions2, int id, Object notes2, String transactionDate2) {
        this.description = description2;
        this.amount = amount2;
        this.journalTransactions = journalTransactions2;
        this.f100id = id;
        this.notes = notes2;
        this.transactionDate = transactionDate2;
    }
}
