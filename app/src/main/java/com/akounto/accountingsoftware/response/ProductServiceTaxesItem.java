package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProductServiceTaxesItem implements Serializable {

    @SerializedName("HeadTransactionId")
    private int headTransactionId;
    @SerializedName("TransactionHeadTaxId")
    private int headTransactionTexId;
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("Rate")
    private double rate;
    @SerializedName("TaxId")
    private int taxId;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("TaxName")
    private String taxName;
    @SerializedName("Selected")
    private boolean selected;

    public void setRate(double rate2) {
        this.rate = rate2;
    }

    public void setTaxName(String taxName2) {
        this.taxName = taxName2;
    }

    public void setProductId(int productId2) {
        this.productId = productId2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getHeadTransactionId() {
        return this.headTransactionId;
    }

    public void setHeadTransactionId(int headTransactionId2) {
        this.headTransactionId = headTransactionId2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTaxId(int taxId2) {
        this.taxId = taxId2;
    }

    public double getRate() {
        return this.rate;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getId() {
        return this.id;
    }

    public int getTaxId() {
        return this.taxId;
    }

    public ProductServiceTaxesItem(String name2, int headTransactionId2) {
        this.name = name2;
        this.headTransactionId = headTransactionId2;
    }
    public ProductServiceTaxesItem() {

    }
    public int getHeadTransactionTexId() {
        return headTransactionTexId;
    }

    public void setHeadTransactionTexId(int headTransactionTexId) {
        this.headTransactionTexId = headTransactionTexId;
    }

    public double getAmount() {
        return amount;
    }
    public int getAmountInt() {
        return (int)amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
