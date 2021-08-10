package com.akounto.accountingsoftware.response.viewreport;

import com.google.gson.annotations.SerializedName;

public class ViewReportData {
    @SerializedName("InvoiceProfitLossDateRange1")
    private InvoiceProfitLossDateRange InvoiceProfitLossDateRange1;
    @SerializedName("InvoiceProfitLossDateRange2")
    private InvoiceProfitLossDateRange InvoiceProfitLossDateRange2;
    @SerializedName("InvoiceProfitLossRQ")
    private InvoiceProfitLoss InvoiceProfitLossRQ;

    public InvoiceProfitLoss getInvoiceProfitLossRQ() {
        return this.InvoiceProfitLossRQ;
    }

    public void setInvoiceProfitLossRQ(InvoiceProfitLoss invoiceProfitLossRQ) {
        this.InvoiceProfitLossRQ = invoiceProfitLossRQ;
    }

    public InvoiceProfitLossDateRange getInvoiceProfitLossDateRange1() {
        return this.InvoiceProfitLossDateRange1;
    }

    public void setInvoiceProfitLossDateRange1(InvoiceProfitLossDateRange invoiceProfitLossDateRange1) {
        this.InvoiceProfitLossDateRange1 = invoiceProfitLossDateRange1;
    }

    public InvoiceProfitLossDateRange getInvoiceProfitLossDateRange2() {
        return this.InvoiceProfitLossDateRange2;
    }

    public void setInvoiceProfitLossDateRange2(InvoiceProfitLossDateRange invoiceProfitLossDateRange2) {
        this.InvoiceProfitLossDateRange2 = invoiceProfitLossDateRange2;
    }
}
