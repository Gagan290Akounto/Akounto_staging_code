package com.akounto.accountingsoftware.response.viewreport;

import com.google.gson.annotations.SerializedName;

public class IncomeDetail {
    @SerializedName("HeadId")
    private int HeadId;
    @SerializedName("ProductPrice")
    private Double ProductPrice;
    @SerializedName("SubHeadId")
    private int SubHeadId;
    @SerializedName("SubHeadName")
    private String SubHeadName;
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

    public Double getProductPrice() {
        return this.ProductPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.ProductPrice = productPrice;
    }
}
