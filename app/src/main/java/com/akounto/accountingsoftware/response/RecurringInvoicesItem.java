package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RecurringInvoicesItem implements Serializable {
    @SerializedName("Amount")
    private double amount;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("CustCurrency")
    private Object custCurrency;
    @SerializedName("CustExchangeRate")
    private double custExchangeRate;
    @SerializedName("Customer")
    private Object customer;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustCurrencySymbol")
    private String custCurrencySymbol;
    @SerializedName("DueAmount")
    private double dueAmount;
    @SerializedName("ExchangeRate")
    private double exchangeRate;
    @SerializedName("Guid")
    private String guid;
    @SerializedName("HeadTransactionCustomerId")
    private int headTransactionCustomerId;
    @SerializedName("Id")
    private int id;
    @SerializedName("InvoiceDate")
    private String invoiceDate;
    @SerializedName("InvoiceNo")
    private int invoiceNo;
    @SerializedName("InvoiceNoPS")
    private Object invoiceNoPS;
    @SerializedName("InvoiceTransaction")
    private Object invoiceTransaction;
    @SerializedName("LastInvoice")
    private Object lastInvoice;
    @SerializedName("MailStatus")
    private int mailStatus;
    @SerializedName("Name")
    private String name;
    @SerializedName("NextInvoice")
    private Object nextInvoice;
    @SerializedName("Notes")
    private Object notes;
    @SerializedName("PaymentDue")
    private String paymentDue;
    @SerializedName("PaymentDueDays")
    private int paymentDueDays;
    @SerializedName("PoNumber")
    private Object poNumber;
    @SerializedName("Schedule")
    private Schedule schedule;
    @SerializedName("ScheduleDescription")
    private String scheduleDescription;
    @SerializedName("Status")
    private int status;
    @SerializedName("TotalNoOfInvoice")
    private int totalNoOfInvoice;

    public double getCustExchangeRate() {
        return this.custExchangeRate;
    }

    public String getScheduleDescription() {
        return this.scheduleDescription;
    }

    public double getExchangeRate() {
        return this.exchangeRate;
    }

    public Object getCustomer() {
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

    public Object getCustCurrency() {
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

    public String getCustomerName() {
        return this.customerName;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public int getMailStatus() {
        return this.mailStatus;
    }

    public Object getNotes() {
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

    public Object getPoNumber() {
        return this.poNumber;
    }

    public int getInvoiceNo() {
        return this.invoiceNo;
    }

    public Object getInvoiceTransaction() {
        return this.invoiceTransaction;
    }

    public int getHeadTransactionCustomerId() {
        return this.headTransactionCustomerId;
    }

    public Object getInvoiceNoPS() {
        return this.invoiceNoPS;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public int getId() {
        return this.id;
    }

    public String getCustCurrencySymbol() {
        return custCurrencySymbol;
    }

    public void setCustCurrencySymbol(String custCurrencySymbol) {
        this.custCurrencySymbol = custCurrencySymbol;
    }
}
