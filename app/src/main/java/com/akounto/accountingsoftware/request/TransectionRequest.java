package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class TransectionRequest {
    @SerializedName("BankId")
    private int BankId;
    @SerializedName("FilterKeyword")
    private String FilterKeyword;
    @SerializedName("PageNumber")
    private int PageNumber;
    @SerializedName("PageRequestType")
    private int PageRequestType;
    @SerializedName("RecordsPerPage")
    private int RecordsPerPage;
    @SerializedName("BankAccountId")
    private int bankAccountId;
    @SerializedName("From")
    private String from = null;
    @SerializedName("HeadTransactionId")
    private Object headTransactionId;
    @SerializedName("To")
    private String to = null;
    @SerializedName("TransactionType")
    private int transactionType;
    @SerializedName("UnderReviewHeadId")
    private Object underReviewHeadId;

    public int getBankId() {
        return this.BankId;
    }

    public void setBankId(int bankId) {
        this.BankId = bankId;
    }

    public String getFilterKeyword() {
        return this.FilterKeyword;
    }

    public void setFilterKeyword(String filterKeyword) {
        this.FilterKeyword = filterKeyword;
    }

    public int getPageNumber() {
        return this.PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.PageNumber = pageNumber;
    }

    public int getPageRequestType() {
        return this.PageRequestType;
    }

    public void setPageRequestType(int pageRequestType) {
        this.PageRequestType = pageRequestType;
    }

    public int getRecordsPerPage() {
        return this.RecordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.RecordsPerPage = recordsPerPage;
    }

    public int getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(int transactionType2) {
    }

    public TransectionRequest() {
    }

    public TransectionRequest(int bankAccountId2, int bankId, int transactionType2) {
        this.bankAccountId = bankAccountId2;
        this.BankId = bankId;
        this.transactionType = transactionType2;
    }

    public TransectionRequest(Integer transactionType2, Object headTransactionId2, String from2, String to, Object underReviewHeadId2) {
        try {
            this.transactionType = transactionType2;
        } catch (Exception e) {
        }
        this.headTransactionId = headTransactionId2;
        this.from = from2;
        this.to = to;
        this.underReviewHeadId = underReviewHeadId2;
    }
}
