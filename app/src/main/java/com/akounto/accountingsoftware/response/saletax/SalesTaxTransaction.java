package com.akounto.accountingsoftware.response.saletax;

import com.google.gson.annotations.SerializedName;

public class SalesTaxTransaction {
    @SerializedName("ClosingBalance")
    private Double ClosingBalance;
    @SerializedName("Description")
    private String Description;
    @SerializedName("NetTaxOwing")
    private Double NetTaxOwing;
    @SerializedName("OpeningBalance")
    private Double OpeningBalance;
    @SerializedName("PaidTax")
    private Double PaidTax;
    @SerializedName("PaidTaxDescription")
    private String PaidTaxDescription;
    @SerializedName("PurchaseAmount")
    private Double PurchaseAmount;
    @SerializedName("PurchaseTax")
    private Double PurchaseTax;
    @SerializedName("SalesAmount")
    private Double SalesAmount;
    @SerializedName("SalesTax")
    private Double SalesTax;
    @SerializedName("SubHeadId")
    private int SubHeadId;
    @SerializedName("TransactionId")
    private int TransactionId;
    @SerializedName("TransactionName")
    private String TransactionName;

    public Double getClosingBalance() {
        return this.ClosingBalance;
    }

    public void setClosingBalance(Double closingBalance) {
        this.ClosingBalance = closingBalance;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Double getNetTaxOwing() {
        return this.NetTaxOwing;
    }

    public void setNetTaxOwing(Double netTaxOwing) {
        this.NetTaxOwing = netTaxOwing;
    }

    public Double getOpeningBalance() {
        return this.OpeningBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.OpeningBalance = openingBalance;
    }

    public Double getPaidTax() {
        return this.PaidTax;
    }

    public void setPaidTax(Double paidTax) {
        this.PaidTax = paidTax;
    }

    public String getPaidTaxDescription() {
        return this.PaidTaxDescription;
    }

    public void setPaidTaxDescription(String paidTaxDescription) {
        this.PaidTaxDescription = paidTaxDescription;
    }

    public Double getPurchaseAmount() {
        return this.PurchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.PurchaseAmount = purchaseAmount;
    }

    public Double getPurchaseTax() {
        return this.PurchaseTax;
    }

    public void setPurchaseTax(Double purchaseTax) {
        this.PurchaseTax = purchaseTax;
    }

    public Double getSalesAmount() {
        return this.SalesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.SalesAmount = salesAmount;
    }

    public Double getSalesTax() {
        return this.SalesTax;
    }

    public void setSalesTax(Double salesTax) {
        this.SalesTax = salesTax;
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

    public String getTransactionName() {
        return this.TransactionName;
    }

    public void setTransactionName(String transactionName) {
        this.TransactionName = transactionName;
    }
}
