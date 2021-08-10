package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddBillTax {

    @SerializedName("Id")
    private int id;
    @SerializedName("TransactionHeadTaxId")
    private int headTransactionTexId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Rate")
    private double rate;
    @SerializedName("HeadTransactionId")
    private int headTransactionId;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("TaxId")
    private int taxId;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("TaxName")
    private String taxName;
    @SerializedName("Selected")
    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeadTransactionTexId() {
        return headTransactionTexId;
    }

    public void setHeadTransactionTexId(int headTransactionTexId) {
        this.headTransactionTexId = headTransactionTexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getHeadTransactionId() {
        return headTransactionId;
    }

    public void setHeadTransactionId(int headTransactionId) {
        this.headTransactionId = headTransactionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
