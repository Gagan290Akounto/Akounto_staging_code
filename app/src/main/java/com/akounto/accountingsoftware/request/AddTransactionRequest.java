package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddTransactionRequest {
    @SerializedName("Amount")
    private String amount;
    @SerializedName("BankAccountId")
    private String bankAccountId;
    @SerializedName("BankSubTransactions")
    private List<BankSubTransactionsItem> bankSubTransactions;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")

    /* renamed from: id */
    private int f101id;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("Review")
    private int review;
    @SerializedName("TransactionDate")
    private String transactionDate;
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

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setBankAccountId(String bankAccountId2) {
        this.bankAccountId = bankAccountId2;
    }

    public String getBankAccountId() {
        return this.bankAccountId;
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
        this.f101id = id;
    }

    public int getId() {
        return this.f101id;
    }

    public void setReview(int review2) {
        this.review = review2;
    }

    public int getReview() {
        return this.review;
    }

    public void setNotes(String notes2) {
        this.notes = notes2;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setTransactionDate(String transactionDate2) {
        this.transactionDate = transactionDate2;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public void setBankSubTransactions(List<BankSubTransactionsItem> bankSubTransactions2) {
        this.bankSubTransactions = bankSubTransactions2;
    }

    public List<BankSubTransactionsItem> getBankSubTransactions() {
        return this.bankSubTransactions;
    }

    public AddTransactionRequest(int transactionType2, String description2, String bankAccountId2, String amount2, int transactionHeadId2, int id, int review2, String notes2, String transactionDate2, List<BankSubTransactionsItem> bankSubTransactions2) {
        this.transactionType = transactionType2;
        this.description = description2;
        this.bankAccountId = bankAccountId2;
        this.amount = amount2;
        this.transactionHeadId = transactionHeadId2;
        this.f101id = id;
        this.review = review2;
        this.notes = notes2;
        this.transactionDate = transactionDate2;
        this.bankSubTransactions = bankSubTransactions2;
    }
}
