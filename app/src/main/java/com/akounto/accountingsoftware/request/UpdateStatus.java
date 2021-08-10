package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class UpdateStatus {
    @SerializedName("InvoiceId")
    private int InvoiceId;
    @SerializedName("Status")
    private int Status;

    public int getInvoiceId() {
        return this.InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.InvoiceId = invoiceId;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }
}
