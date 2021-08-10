package com.akounto.accountingsoftware.request.BillUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillWithoutId {
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Price")
    @Expose
    private double price;
    @SerializedName("ProductTransactionHeadId")
    @Expose
    private Integer productTransactionHeadId;
    @SerializedName("Taxes")
    @Expose
    private List<Object> taxes = null;

    public BillWithoutId(Integer productId, String productName, String description, Integer quantity, double price, Integer productTransactionHeadId, List<Object> taxes) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.productTransactionHeadId = productTransactionHeadId;
        this.taxes = taxes;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getProductTransactionHeadId() {
        return productTransactionHeadId;
    }

    public void setProductTransactionHeadId(Integer productTransactionHeadId) {
        this.productTransactionHeadId = productTransactionHeadId;
    }

    public List<Object> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Object> taxes) {
        this.taxes = taxes;
    }
}
