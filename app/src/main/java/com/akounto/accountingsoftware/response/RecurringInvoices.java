package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecurringInvoices {
    @SerializedName("From")
    private int from;
    @SerializedName("Invoices")
    private List<RecurringInvoicesItem> invoices;
    @SerializedName("To")

    /* renamed from: to */
    private int f123to;
    @SerializedName("TotalRecords")
    private int totalRecords;

    public int getTotalRecords() {
        return this.totalRecords;
    }

    public List<RecurringInvoicesItem> getInvoices() {
        return this.invoices;
    }

    public int getFrom() {
        return this.from;
    }

    public int getTo() {
        return this.f123to;
    }
}
