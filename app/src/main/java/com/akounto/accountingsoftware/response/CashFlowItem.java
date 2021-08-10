package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class CashFlowItem {
    @SerializedName("Credit")
    private double credit;
    @SerializedName("Dredit")
    private double dredit;
    @SerializedName("Label")
    private String label;
    @SerializedName("Order")
    private int order;

    public int getOrder() {
        return this.order;
    }

    public double getCredit() {
        return this.credit;
    }

    public double getDredit() {
        return this.dredit;
    }

    public String getLabel() {
        return this.label;
    }
}
