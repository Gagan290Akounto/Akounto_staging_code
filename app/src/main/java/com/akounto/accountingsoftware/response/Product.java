package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("ProductId")
    private int ProductId;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Description")
    private String description;
    @SerializedName("ExpenseAccountId")
    private final int expenseAccountId;
    @SerializedName("ExpenseAccountName")
    private Object expenseAccountName;
    @SerializedName("Id")
    private int id;
    @SerializedName("IncomeAccountId")
    private final int incomeAccountId;
    @SerializedName("IncomeAccountName")
    private String incomeAccountName;
    @SerializedName("IsBuy")
    private final boolean isBuy;
    @SerializedName("IsSell")
    private final boolean isSell;
    @SerializedName(value="Name", alternate={"ProductName", "user"})
    private String name;
    @SerializedName("Price")
    private double price;
    @SerializedName("Quantity")
    private String qty;
    @SerializedName("ProductServiceTaxes")
    private List<ProductServiceTaxesItem> productServiceTaxes;
    @SerializedName("Taxes")
    private List<ProductServiceTaxesItem> taxes;
    @SerializedName("ProductTransactionHeadId")
    private String productTransactionHeadId;

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsSell() {
        return this.isSell;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<ProductServiceTaxesItem> getProductServiceTaxes() {
        return this.productServiceTaxes;
    }
    public void setProductServiceTaxes(List<ProductServiceTaxesItem> list_txt) {
        this.productServiceTaxes=list_txt;
    }
    public int getCompanyId() {
        return this.companyId;
    }

    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getIncomeAccountName() {
        return this.incomeAccountName;
    }

    public boolean isIsBuy() {
        return this.isBuy;
    }

    public int getIncomeAccountId() {
        return this.incomeAccountId;
    }

    public Object getExpenseAccountName() {
        return this.expenseAccountName;
    }

    public int getId() {
        return this.id;
    }

    public int getExpenseAccountId() {
        return this.expenseAccountId;
    }

    public String getName() {
        return this.name;
    }

    public Product(boolean isSell2, String description2, List<ProductServiceTaxesItem> productServiceTaxes2, double price2, boolean isBuy2, int incomeAccountId2, int expenseAccountId2, String name2) {
        this.isSell = isSell2;
        this.description = description2;
        this.productServiceTaxes = productServiceTaxes2;
        this.price = price2;
        this.isBuy = isBuy2;
        this.incomeAccountId = incomeAccountId2;
        this.expenseAccountId = expenseAccountId2;
        this.name = name2;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductServiceTaxesItem> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ProductServiceTaxesItem> taxes) {
        this.taxes = taxes;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductTransactionHeadId() {
        return productTransactionHeadId;
    }

    public void setProductTransactionHeadId(String productTransactionHeadId) {
        this.productTransactionHeadId = productTransactionHeadId;
    }
}
