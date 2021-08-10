package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class Bills {
    @SerializedName("BillAt")
    private String BillAt;
    @SerializedName("BillTransaction")
    private String BillTransaction;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("Currency")
    private String Currency;
    @SerializedName("DocumentName")
    private String DocumentName;
    @SerializedName("DueAmount")
    private double DueAmount;
    @SerializedName("DueAt")
    private String DueAt;
    @SerializedName("ExchangeRate")
    private float ExchangeRate;
    @SerializedName("Guid")
    private String Guid;
    @SerializedName("HeadTransactionVendorId")
    private int HeadTransactionVendorId;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f105Id;
    @SerializedName("InvoiceNo")
    private String InvoiceNo;
    @SerializedName("Notes")
    private String Notes;
    @SerializedName("PoNumber")
    private String PoNumber;
    @SerializedName("Price")
    private float Price;
    @SerializedName("Status")
    private int Status;
    @SerializedName("VendorName")
    private String VendorName;

    public String getBillAt() {
        return this.BillAt;
    }

    public void setBillAt(String billAt) {
        this.BillAt = billAt;
    }

    public String getBillTransaction() {
        return this.BillTransaction;
    }

    public void setBillTransaction(String billTransaction) {
        this.BillTransaction = billTransaction;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getCurrency() {
        return this.Currency;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public String getDocumentName() {
        return this.DocumentName;
    }

    public void setDocumentName(String documentName) {
        this.DocumentName = documentName;
    }

    public double getDueAmount() {
        return this.DueAmount;
    }

    public void setDueAmount(int dueAmount) {
        this.DueAmount = dueAmount;
    }

    public String getDueAt() {
        return this.DueAt;
    }

    public void setDueAt(String dueAt) {
        this.DueAt = dueAt;
    }

    public String getGuid() {
        return this.Guid;
    }

    public void setGuid(String guid) {
        this.Guid = guid;
    }

    public int getHeadTransactionVendorId() {
        return this.HeadTransactionVendorId;
    }

    public void setHeadTransactionVendorId(int headTransactionVendorId) {
        this.HeadTransactionVendorId = headTransactionVendorId;
    }

    public int getId() {
        return this.f105Id;
    }

    public void setId(int id) {
        this.f105Id = id;
    }

    public String getInvoiceNo() {
        return this.InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.InvoiceNo = invoiceNo;
    }

    public String getNotes() {
        return this.Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }

    public String getPoNumber() {
        return this.PoNumber;
    }

    public void setPoNumber(String poNumber) {
        this.PoNumber = poNumber;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getVendorName() {
        return this.VendorName;
    }

    public void setVendorName(String vendorName) {
        this.VendorName = vendorName;
    }

    public float getExchangeRate() {
        return this.ExchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.ExchangeRate = exchangeRate;
    }

    public float getPrice() {
        return this.Price;
    }

    public void setPrice(float price) {
        this.Price = price;
    }
}
