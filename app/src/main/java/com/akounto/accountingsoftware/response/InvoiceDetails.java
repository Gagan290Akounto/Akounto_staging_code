package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InvoiceDetails implements Serializable {
    @SerializedName("Amount")
    private double amount;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("CustCurrency")
    private String custCurrency;
    @SerializedName("CustExchangeRate")
    private double custExchangeRate;
    @SerializedName("Customer")
    private Customer customer;
    @SerializedName("CustomerName")
    private Object customerName;
    @SerializedName("DueAmount")
    private double dueAmount;
    @SerializedName("ExchangeRate")
    private double exchangeRate;
    @SerializedName("Guid")
    private String guid;
    @SerializedName("CustCurrencySymbol")
    private String CustCurrencySymbol;
    @SerializedName("HeadTransactionCustomerId")
    private int headTransactionCustomerId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f118id;
    @SerializedName("InvoiceDate")
    private String invoiceDate;
    @SerializedName("InvoiceNo")
    private int invoiceNo;
    @SerializedName("InvoiceNoPS")
    private String invoiceNoPS;
    @SerializedName("InvoiceTransaction")
    private List<GetInvoiceTransactionItem> invoiceTransaction;
    @SerializedName("LastInvoice")
    private Object lastInvoice;
    @SerializedName("MailStatus")
    private int mailStatus;
    @SerializedName("Name")
    private String name;
    @SerializedName("NextInvoice")
    private Object nextInvoice;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("PaymentDue")
    private String paymentDue;
    @SerializedName("PaymentDueDays")
    private int paymentDueDays;
    @SerializedName("PoNumber")
    private String poNumber;
    @SerializedName("Schedule")
    private Schedule schedule;
    @SerializedName("ScheduleDescription")
    private Object scheduleDescription;
    @SerializedName("Status")
    private int status;
    @SerializedName("TotalNoOfInvoice")
    private int totalNoOfInvoice;

    public double getCustExchangeRate() {
        return this.custExchangeRate;
    }

    public Object getScheduleDescription() {
        return this.scheduleDescription;
    }

    public double getExchangeRate() {
        return this.exchangeRate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public double getDueAmount() {
        return this.dueAmount;
    }

    public Object getNextInvoice() {
        return this.nextInvoice;
    }

    public String getGuid() {
        return this.guid;
    }

    public String getCustCurrency() {
        return this.custCurrency;
    }

    public String getName() {
        return this.name;
    }

    public int getTotalNoOfInvoice() {
        return this.totalNoOfInvoice;
    }

    public int getPaymentDueDays() {
        return this.paymentDueDays;
    }

    public String getPaymentDue() {
        return this.paymentDue;
    }

    public Object getLastInvoice() {
        return this.lastInvoice;
    }

    public Object getCustomerName() {
        return this.customerName;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public int getMailStatus() {
        return this.mailStatus;
    }

    public String getNotes() {
        return this.notes;
    }

    public int getStatus() {
        return this.status;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getPoNumber() {
        return this.poNumber;
    }

    public int getInvoiceNo() {
        return this.invoiceNo;
    }

    public List<GetInvoiceTransactionItem> getInvoiceTransaction() {
        return this.invoiceTransaction;
    }

    public int getHeadTransactionCustomerId() {
        return this.headTransactionCustomerId;
    }

    public String getInvoiceNoPS() {
        return this.invoiceNoPS;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public int getId() {
        return this.f118id;
    }

    public String getCustCurrencySymbol() {
        return CustCurrencySymbol;
    }

    public void setCustCurrencySymbol(String custCurrencySymbol) {
        CustCurrencySymbol = custCurrencySymbol;
    }
}
