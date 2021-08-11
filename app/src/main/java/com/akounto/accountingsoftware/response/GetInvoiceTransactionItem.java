package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class GetInvoiceTransactionItem implements Serializable {
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")
    private int id;
    @SerializedName("Price")
    private double price;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductTransactionHeadId")
    private int productTransactionHeadId;
    @SerializedName("ProductTransactionHeadName")
    private String productTransactionHeadName;
    @SerializedName("Quantity")
    private int quantity;
    @SerializedName("SubHeadId")
    private int subHeadId;
    @SerializedName("SubHeadName")
    private String subHeadName;
    @SerializedName("DiscountType")
    private int discountType;
    @SerializedName("Discount")
    private int Discount;
    @SerializedName("Taxes")
    private List<ProductServiceTaxesItem> taxes;

    public String getDescription() {
        return this.description;
    }

    public String getProductName() {
        return this.productName;
    }

    public List<ProductServiceTaxesItem> getTaxes() {
        return this.taxes;
    }

    public String getSubHeadName() {
        return this.subHeadName;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getId() {
        return this.id;
    }

    public int getProductTransactionHeadId() {
        return this.productTransactionHeadId;
    }

    public String getProductTransactionHeadName() {
        return this.productTransactionHeadName;
    }

    public int getSubHeadId() {
        return this.subHeadId;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }
}
