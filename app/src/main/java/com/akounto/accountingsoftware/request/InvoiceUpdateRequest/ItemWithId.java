package com.akounto.accountingsoftware.request.InvoiceUpdateRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemWithId {
    @SerializedName("Id")
    @Expose
    private Integer id;
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
    private Double price;
    @SerializedName("ProductTransactionHeadId")
    @Expose
    private Integer productTransactionHeadId;
    @SerializedName("DiscountType")
    private int discountType;
    @SerializedName("Discount")
    private int discount;
    @SerializedName("Taxes")
    @Expose
    private List<Object> taxes = null;

    public ItemWithId(Integer id, Integer productId, String productName, String description, Integer quantity, Double price, Integer productTransactionHeadId, List<Object> taxes) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.productTransactionHeadId = productTransactionHeadId;
        this.taxes = taxes;
    }
    public ItemWithId(Integer id, Integer productId, String productName, String description, Integer quantity, Double price, Integer productTransactionHeadId, List<Object> taxes,int discountType,int discount) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.productTransactionHeadId = productTransactionHeadId;
        this.taxes = taxes;
        this.discountType=discountType;
        this.discount=discount;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
