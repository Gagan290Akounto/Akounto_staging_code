package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PurchaseItem {
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("Description")
    private String Description;
    @SerializedName("ExpenseAccountId")
    private int ExpenseAccountId;
    @SerializedName("ExpenseAccountName")
    private String ExpenseAccountName;
    @SerializedName("Id")
    private int id;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("IncomeAccountId")
    private int IncomeAccountId;
    @SerializedName("IncomeAccountName")
    private String IncomeAccountName;
    @SerializedName("IsBuy")
    private Boolean IsBuy;
    @SerializedName("IsSell")
    private Boolean IsSell;
    @SerializedName(value="Name", alternate={"ProductName", "user"})
    private String Name;
    @SerializedName("ProductTransactionHeadId")
    private String productTransactionHeadId;
    @SerializedName("ProductTransactionHeadName")
    private String productTransactionHeadName;
    @SerializedName("Price")
    private double Price;
    @SerializedName("ProductServiceTaxes")
    private List<ProductServiceTaxesItem> ProductServiceTaxes;
    @SerializedName("Quantity")
    private int Quantity = 1;

    public int getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getExpenseAccountId() {
        return this.ExpenseAccountId;
    }

    public void setExpenseAccountId(int expenseAccountId) {
        this.ExpenseAccountId = expenseAccountId;
    }

    public String getExpenseAccountName() {
        return this.ExpenseAccountName;
    }

    public void setExpenseAccountName(String expenseAccountName) {
        this.ExpenseAccountName = expenseAccountName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncomeAccountId() {
        return this.IncomeAccountId;
    }

    public void setIncomeAccountId(int incomeAccountId) {
        this.IncomeAccountId = incomeAccountId;
    }

    public String getIncomeAccountName() {
        return this.IncomeAccountName;
    }

    public void setIncomeAccountName(String incomeAccountName) {
        this.IncomeAccountName = incomeAccountName;
    }

    public Boolean getBuy() {
        return this.IsBuy;
    }

    public void setBuy(Boolean buy) {
        this.IsBuy = buy;
    }

    public Boolean getSell() {
        return this.IsSell;
    }

    public void setSell(Boolean sell) {
        this.IsSell = sell;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public double getPrice() {
        return this.Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public List<ProductServiceTaxesItem> getProductServiceTaxes() {
        return this.ProductServiceTaxes;
    }

    public void setProductServiceTaxes(List<ProductServiceTaxesItem> productServiceTaxes) {
        this.ProductServiceTaxes = productServiceTaxes;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductTransactionHeadId() {
        return productTransactionHeadId;
    }

    public void setProductTransactionHeadId(String productTransactionHeadId) {
        this.productTransactionHeadId = productTransactionHeadId;
    }

    public String getProductTransactionHeadName() {
        return productTransactionHeadName;
    }

    public void setProductTransactionHeadName(String productTransactionHeadName) {
        this.productTransactionHeadName = productTransactionHeadName;
    }
}
