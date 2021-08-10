package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.request.BankSubTransactionsItem;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {
    @SerializedName("AccountName")
    private String AccountName;
    @SerializedName("Amount")
    private double Amount;
    @SerializedName("BankAccountId")
    private int BankAccountId;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("ContinueWithReconcile")
    private Boolean ContinueWithReconcile;
    @SerializedName("Description")
    private String Description;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f128Id;
    @SerializedName("IsReconcileTransaction")
    private Boolean IsReconcileTransaction;
    @SerializedName("Notes")
    private String Notes;
    @SerializedName("Review")
    private int Review;
    @SerializedName("SubTransactionCount")
    private int SubTransactionCount;
    @SerializedName("TransactionDate")
    private String TransactionDate;
    @SerializedName("TransactionHeadId")
    private int TransactionHeadId;
    @SerializedName("TransactionHeadName")
    private String TransactionHeadName;
    @SerializedName("TransactionType")
    private int TransactionType;
    @SerializedName("BankSubTransactions")
    private List<BankSubTransactionsItem> bankSubTransactions;
    @SerializedName("Payee")
    private String payee;

    public Boolean getContinueWithReconcile() {
        return this.ContinueWithReconcile;
    }

    public void setContinueWithReconcile(Boolean continueWithReconcile) {
        this.ContinueWithReconcile = continueWithReconcile;
    }

    public Boolean getReconcileTransaction() {
        return this.IsReconcileTransaction;
    }

    public void setReconcileTransaction(Boolean reconcileTransaction) {
        this.IsReconcileTransaction = reconcileTransaction;
    }

    public List<BankSubTransactionsItem> getBankSubTransactions() {
        return this.bankSubTransactions;
    }

    public void setBankSubTransactions(List<BankSubTransactionsItem> bankSubTransactions2) {
        this.bankSubTransactions = bankSubTransactions2;
    }

    public String getPayee() {
        return this.payee;
    }

    public void setPayee(String payee2) {
        this.payee = payee2;
    }

    public String getAccountName() {
        return this.AccountName;
    }

    public void setAccountName(String accountName) {
        this.AccountName = accountName;
    }

    public double getAmount() {
        return this.Amount;
    }

    public void setAmount(double amount) {
        this.Amount = amount;
    }

    public int getBankAccountId() {
        return this.BankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.BankAccountId = bankAccountId;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getId() {
        return this.f128Id;
    }

    public void setId(int id) {
        this.f128Id = id;
    }

    public String getNotes() {
        return this.Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }

    public int getReview() {
        return this.Review;
    }

    public void setReview(int review) {
        this.Review = review;
    }

    public int getSubTransactionCount() {
        return this.SubTransactionCount;
    }

    public void setSubTransactionCount(int subTransactionCount) {
        this.SubTransactionCount = subTransactionCount;
    }

    public String getTransactionDate() {
        return this.TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.TransactionDate = transactionDate;
    }

    public int getTransactionHeadId() {
        return this.TransactionHeadId;
    }

    public void setTransactionHeadId(int transactionHeadId) {
        this.TransactionHeadId = transactionHeadId;
    }

    public String getTransactionHeadName() {
        return this.TransactionHeadName;
    }

    public void setTransactionHeadName(String transactionHeadName) {
        this.TransactionHeadName = transactionHeadName;
    }

    public int getTransactionType() {
        return this.TransactionType;
    }

    public void setTransactionType(int transactionType) {
        this.TransactionType = transactionType;
    }
}
