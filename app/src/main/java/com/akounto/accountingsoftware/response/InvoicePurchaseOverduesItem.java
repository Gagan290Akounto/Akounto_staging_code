package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class InvoicePurchaseOverduesItem {
    @SerializedName("DueAmount")
    private double dueAmount;
    @SerializedName("OverdueAmount")
    private double overdueAmount;
    @SerializedName("Type")
    private int type;

    public double getOverdueAmount() {
        return this.overdueAmount;
    }

    public int getType() {
        return this.type;
    }

    public double getDueAmount() {
        return this.dueAmount;
    }
}
