package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class GetMailRequest {
    @SerializedName("HeadTrnsactionId")
    private int HeadTrnsactionId;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f108Id;
    @SerializedName("InvoiceNo")
    private int InvoiceNo;
    @SerializedName("MailType")
    private int MailType;

    public int getId() {
        return this.f108Id;
    }

    public void setId(int id) {
        this.f108Id = id;
    }

    public int getHeadTrnsactionId() {
        return this.HeadTrnsactionId;
    }

    public void setHeadTrnsactionId(int headTrnsactionId) {
        this.HeadTrnsactionId = headTrnsactionId;
    }

    public int getMailType() {
        return this.MailType;
    }

    public void setMailType(int mailType) {
        this.MailType = mailType;
    }

    public int getInvoiceNo() {
        return this.InvoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.InvoiceNo = invoiceNo;
    }
}
