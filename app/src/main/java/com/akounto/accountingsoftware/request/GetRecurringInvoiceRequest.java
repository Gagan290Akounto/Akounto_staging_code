package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class GetRecurringInvoiceRequest {
    @SerializedName("CustomerId")
    private Object customerId;
    @SerializedName("FilterKeyword")
    private Object filterKeyword;
    @SerializedName("From")
    private Object from;
    @SerializedName("InvoiceNo")
    private Object invoiceNo;
    @SerializedName("PageNumber")
    private final int pageNumber;
    @SerializedName("PageRequestType")
    private final int pageRequestType;
    @SerializedName("RecordsPerPage")
    private final int recordsPerPage;
    @SerializedName("Status")
    private Object status;
    @SerializedName("To")
    private Object to;

    public Object getStatus() {
        return this.status;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageRequestType() {
        return this.pageRequestType;
    }

    public Object getFilterKeyword() {
        return this.filterKeyword;
    }

    public Object getInvoiceNo() {
        return this.invoiceNo;
    }

    public Object getCustomerId() {
        return this.customerId;
    }

    public Object getFrom() {
        return this.from;
    }

    public Object getTo() {
        return this.to;
    }

    public int getRecordsPerPage() {
        return this.recordsPerPage;
    }

    public GetRecurringInvoiceRequest(String filterKeyword) {
        this.filterKeyword=filterKeyword;
        this.pageNumber = 1;
        this.pageRequestType = 1;
        this.recordsPerPage = 50;
    }

    public GetRecurringInvoiceRequest(int Status,int customerId,String from,String to,String invoiceNo) {

        if (Status == -1) {
            this.status = null;
        } else {
            this.status = String.valueOf(Status);
        }

        if (customerId == 0) {
            this.customerId =null;
        } else {
            this.customerId = customerId;
        }
        this.from=from;
        this.to=to;
        this.invoiceNo=invoiceNo;
        this.pageNumber = 1;
        this.pageRequestType = 1;
        this.recordsPerPage = 50;
    }

    public GetRecurringInvoiceRequest(String Status, int pageNumber2, int pageRequestType2, int recordsPerPage2) {
        this.pageNumber = pageNumber2;
        this.pageRequestType = pageRequestType2;
        this.status = Status;
        this.recordsPerPage = recordsPerPage2;
    }
    public GetRecurringInvoiceRequest(int customerId, int pageNumber2, int pageRequestType2, int recordsPerPage2) {
        if (customerId == 0) {
            this.customerId =null;
        } else {
            this.customerId = customerId;
        }
        this.pageNumber = pageNumber2;
        this.pageRequestType = pageRequestType2;
        this.recordsPerPage = recordsPerPage2;
    }
}
