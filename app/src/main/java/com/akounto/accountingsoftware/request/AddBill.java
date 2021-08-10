package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddBill {
    @SerializedName("BillAt")
    private String BillAt;
    @SerializedName("BillTransaction")
    private List<AddBillTransaction> BillTransaction;
    @SerializedName("Currency")
    private String Currency;
    @SerializedName("DueAt")
    private String DueAt;
    @SerializedName("ExchangeRate")
    private Double ExchangeRate = Double.valueOf(1.0d);
    @SerializedName("HeadTransactionVendorId")
    private int HeadTransactionVendorId;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f97Id;
    @SerializedName("InvoiceNo")
    private String InvoiceNo;
    @SerializedName("PoNumber")
    private String PoNumber;

    public String getBillAt() {
        return this.BillAt;
    }

    public void setBillAt(String billAt) {
        this.BillAt = billAt;
    }

    public String getCurrency() {
        return this.Currency;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public String getDueAt() {
        return this.DueAt;
    }

    public void setDueAt(String dueAt) {
        this.DueAt = dueAt;
    }

    public Double getExchangeRate() {
        return this.ExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.ExchangeRate = exchangeRate;
    }

    public int getHeadTransactionVendorId() {
        return this.HeadTransactionVendorId;
    }

    public void setHeadTransactionVendorId(int headTransactionVendorId) {
        this.HeadTransactionVendorId = headTransactionVendorId;
    }

    public String getInvoiceNo() {
        return this.InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.InvoiceNo = invoiceNo;
    }

    public String getPoNumber() {
        return this.PoNumber;
    }

    public void setPoNumber(String poNumber) {
        this.PoNumber = poNumber;
    }

    public int getId() {
        return this.f97Id;
    }

    public void setId(int id) {
        this.f97Id = id;
    }

    public List<AddBillTransaction> getBillTransaction() {
        return this.BillTransaction;
    }

    public void setBillTransaction(List<AddBillTransaction> billTransaction) {
        this.BillTransaction = billTransaction;
    }
}
