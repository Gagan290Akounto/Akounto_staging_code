package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class SendBill {
    @SerializedName("FilterKeyword")
    private String FilterKeyword;
    @SerializedName("From")
    private String From;
    @SerializedName("InvoiceNo")
    private String InvoiceNo;
    @SerializedName("PageNumber")
    private int PageNumber;
    @SerializedName("PageRequestType")
    private int PageRequestType;
    @SerializedName("RecordsPerPage")
    private int RecordsPerPage;
    @SerializedName("Status")
    private String Status;
    @SerializedName("To")

    /* renamed from: To */
    private String f111To;
    @SerializedName("VendorId")
    private String VendorId;

    public String getFilterKeyword() {
        return this.FilterKeyword;
    }

    public void setFilterKeyword(String filterKeyword) {
        this.FilterKeyword = filterKeyword;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getTo() {
        return this.f111To;
    }

    public void setTo(String to) {
        this.f111To = to;
    }

    public int getPageNumber() {
        return this.PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.PageNumber = pageNumber;
    }

    public int getRecordsPerPage() {
        return this.RecordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.RecordsPerPage = recordsPerPage;
    }

    public int getPageRequestType() {
        return this.PageRequestType;
    }

    public void setPageRequestType(int pageRequestType) {
        this.PageRequestType = pageRequestType;
    }

    public String getVendorId() {
        return this.VendorId;
    }

    public void setVendorId(String vendorId) {
        this.VendorId = vendorId;
    }

    public String getInvoiceNo() {
        return this.InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.InvoiceNo = invoiceNo;
    }
}
