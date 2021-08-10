package com.akounto.accountingsoftware.request;

import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.Vendor;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BillsById {
    @SerializedName("BillAt")
    private String BillAt;
    @SerializedName("BillTransaction")
    private List<GetInvoiceTransactionItem> BillTransaction;
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
    private int f106Id;
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
    @SerializedName("Vendor")
    private Vendor Vendor;
    @SerializedName("VendorName")
    private String VendorName;

    public Vendor getVendor() {
        return this.Vendor;
    }

    public void setVendor(Vendor vendor) {
        this.Vendor = vendor;
    }

    public List<GetInvoiceTransactionItem> getBillTransaction() {
        return this.BillTransaction;
    }

    public void setBillTransaction(List<GetInvoiceTransactionItem> billTransaction) {
        this.BillTransaction = billTransaction;
    }

    public String getBillAt() {
        return this.BillAt;
    }

    public void setBillAt(String billAt) {
        this.BillAt = billAt;
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

    public void setDueAmount(Double dueAmount) {
        this.DueAmount = dueAmount.doubleValue();
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
        return this.f106Id;
    }

    public void setId(int id) {
        this.f106Id = id;
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
