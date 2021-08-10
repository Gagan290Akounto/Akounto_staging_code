package com.akounto.accountingsoftware.request.InvoiceUpdateRequest;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InvoiceRequest {
    @SerializedName("Id")
    private int id;
    @SerializedName("InvoiceNo")
    private int invoiceNo;
    @SerializedName("CustCurrency")
    private String custCurrency;
    @SerializedName("CustExchangeRate")
    private double custExchangeRate;
    @SerializedName("ExchangeRate")
    private double exchangeRate;
    @SerializedName("HeadTransactionCustomerId")
    private int headTransactionCustomerId;
    @SerializedName("InvoiceDate")
    private String invoiceDate;
    @SerializedName("InvoiceTransaction")
    private List<Object> invoiceTransaction;
    @SerializedName("Name")
    private final String name;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("PaymentDue")
    private String paymentDue;
    @SerializedName("PaymentDueDays")
    private int paymentDueDays;
    @SerializedName("PoNumber")
    private final String poNumber;
    @SerializedName("InvoiceNoPS")
    private String invoiceNoPS;

    public InvoiceRequest(int id,int invoiceNo,double custExchangeRate2, double exchangeRate2, int headTransactionCustomerId2, int paymentDueDays2, String paymentDue2, String invoiceDate2, List<Object> invoiceTransaction2, String custCurrency2, String notes2, String poNumber2, String name2,String invoiceNoPS) {
        this.id=id;
        this.invoiceNo=invoiceNo;
        this.invoiceNoPS=invoiceNoPS;
        this.custExchangeRate = custExchangeRate2;
        this.exchangeRate = exchangeRate2;
        this.headTransactionCustomerId = headTransactionCustomerId2;
        this.paymentDueDays = paymentDueDays2;
        this.paymentDue = paymentDue2;
        this.invoiceDate = invoiceDate2;
        this.invoiceTransaction = invoiceTransaction2;
        this.custCurrency = custCurrency2;
        this.notes = notes2;
        this.poNumber = poNumber2;
        this.name = name2;
    }
    public InvoiceRequest(int id,int invoiceNo,double custExchangeRate2, double exchangeRate2, int headTransactionCustomerId2, int paymentDueDays2, String paymentDue2, String invoiceDate2, List<Object> invoiceTransaction2, String custCurrency2, String notes2, String poNumber2, String name2) {
        this.id=id;
        this.invoiceNo=invoiceNo;
        this.custExchangeRate = custExchangeRate2;
        this.exchangeRate = exchangeRate2;
        this.headTransactionCustomerId = headTransactionCustomerId2;
        this.paymentDueDays = paymentDueDays2;
        this.paymentDue = paymentDue2;
        this.invoiceDate = invoiceDate2;
        this.invoiceTransaction = invoiceTransaction2;
        this.custCurrency = custCurrency2;
        this.notes = notes2;
        this.poNumber = poNumber2;
        this.name = name2;
    }
    public InvoiceRequest(double custExchangeRate2, double exchangeRate2, int headTransactionCustomerId2, int paymentDueDays2, String paymentDue2, String invoiceDate2, List<Object> invoiceTransaction2, String custCurrency2, String notes2, String poNumber2, String name2,String invoiceNoPS) {
        this.custExchangeRate = custExchangeRate2;
        this.exchangeRate = exchangeRate2;
        this.invoiceNoPS=invoiceNoPS;
        this.headTransactionCustomerId = headTransactionCustomerId2;
        this.paymentDueDays = paymentDueDays2;
        this.paymentDue = paymentDue2;
        this.invoiceDate = invoiceDate2;
        this.invoiceTransaction = invoiceTransaction2;
        this.custCurrency = custCurrency2;
        this.notes = notes2;
        this.poNumber = poNumber2;
        this.name = name2;
    }
    public InvoiceRequest(double custExchangeRate2, double exchangeRate2, int headTransactionCustomerId2, int paymentDueDays2, String paymentDue2, String invoiceDate2, List<Object> invoiceTransaction2, String custCurrency2, String notes2, String poNumber2, String name2) {
        this.custExchangeRate = custExchangeRate2;
        this.exchangeRate = exchangeRate2;
        this.headTransactionCustomerId = headTransactionCustomerId2;
        this.paymentDueDays = paymentDueDays2;
        this.paymentDue = paymentDue2;
        this.invoiceDate = invoiceDate2;
        this.invoiceTransaction = invoiceTransaction2;
        this.custCurrency = custCurrency2;
        this.notes = notes2;
        this.poNumber = poNumber2;
        this.name = name2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustCurrency() {
        return custCurrency;
    }

    public void setCustCurrency(String custCurrency) {
        this.custCurrency = custCurrency;
    }

    public double getCustExchangeRate() {
        return custExchangeRate;
    }

    public void setCustExchangeRate(double custExchangeRate) {
        this.custExchangeRate = custExchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getHeadTransactionCustomerId() {
        return headTransactionCustomerId;
    }

    public void setHeadTransactionCustomerId(int headTransactionCustomerId) {
        this.headTransactionCustomerId = headTransactionCustomerId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<Object> getInvoiceTransaction() {
        return invoiceTransaction;
    }

    public void setInvoiceTransaction(List<Object> invoiceTransaction) {
        this.invoiceTransaction = invoiceTransaction;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }

    public int getPaymentDueDays() {
        return paymentDueDays;
    }

    public void setPaymentDueDays(int paymentDueDays) {
        this.paymentDueDays = paymentDueDays;
    }

    public String getPoNumber() {
        return poNumber;
    }
}
