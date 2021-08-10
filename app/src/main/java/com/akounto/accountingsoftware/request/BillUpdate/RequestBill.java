package com.akounto.accountingsoftware.request.BillUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RequestBill {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("HeadTransactionVendorId")
    @Expose
    private Integer headTransactionVendorId;
    @SerializedName("InvoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("BillAt")
    @Expose
    private String billAt;
    @SerializedName("DueAt")
    @Expose
    private String dueAt;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("ExchangeRate")
    @Expose
    private double exchangeRate;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("PoNumber")
    @Expose
    private String poNumber;
    @SerializedName("BillTransaction")
    @Expose
    private List<Object> billTransaction = null;

    public RequestBill(Integer id, Integer headTransactionVendorId, String invoiceNo, String billAt, String dueAt, String currency, double exchangeRate, String notes, String poNumber, List<Object> billTransaction) {
        this.id = id;
        this.headTransactionVendorId = headTransactionVendorId;
        this.invoiceNo = invoiceNo;
        this.billAt = billAt;
        this.dueAt = dueAt;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.notes = notes;
        this.poNumber = poNumber;
        this.billTransaction = billTransaction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeadTransactionVendorId() {
        return headTransactionVendorId;
    }

    public void setHeadTransactionVendorId(Integer headTransactionVendorId) {
        this.headTransactionVendorId = headTransactionVendorId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillAt() {
        return billAt;
    }

    public void setBillAt(String billAt) {
        this.billAt = billAt;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public List<Object> getBillTransaction() {
        return billTransaction;
    }

    public void setBillTransaction(List<Object> billTransaction) {
        this.billTransaction = billTransaction;
    }
}
