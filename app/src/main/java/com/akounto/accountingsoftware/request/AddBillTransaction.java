package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddBillTransaction {
    @SerializedName("Description")
    private String Description;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f98Id;
    @SerializedName("Price")
    private Double Price;
    @SerializedName("ProductId")
    private int ProductId;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("ProductTransactionHeadId")
    private int ProductTransactionHeadId;
    @SerializedName("Quantity")
    private int Quantity;
    @SerializedName("Taxes")
    private List<AddBillTax> Taxes;

    public int getProductId() {
        return this.ProductId;
    }

    public void setProductId(int productId) {
        this.ProductId = productId;
    }

    public String getProductName() {
        return this.ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public Double getPrice() {
        return this.Price;
    }

    public void setPrice(Double price) {
        this.Price = price;
    }

    public int getProductTransactionHeadId() {
        return this.ProductTransactionHeadId;
    }

    public void setProductTransactionHeadId(int productTransactionHeadId) {
        this.ProductTransactionHeadId = productTransactionHeadId;
    }

    public List<AddBillTax> getTaxes() {
        return this.Taxes;
    }

    public void setTaxes(List<AddBillTax> taxes) {
        this.Taxes = taxes;
    }

    public int getId() {
        return this.f98Id;
    }

    public void setId(int id) {
        this.f98Id = id;
    }

    public AddBillTransaction() {
    }

    public AddBillTransaction(int Id, int ProductId2, List<AddBillTax> Taxes2, int ProductTransactionHeadId2, int Price2, int Quantity2, String Description2, String ProductName2) {
        this.f98Id = Id;
        this.Taxes = Taxes2;
        this.ProductId = ProductId2;
        this.ProductTransactionHeadId = Id;
        this.f98Id = ProductTransactionHeadId2;
        this.Quantity = Quantity2;
        this.Description = Description2;
        this.ProductName = ProductName2;
    }
}
