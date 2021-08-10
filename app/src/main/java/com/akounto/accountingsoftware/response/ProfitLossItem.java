package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class ProfitLossItem {
    @SerializedName("Label")
    private String label;
    @SerializedName("Order")
    private int order;
    @SerializedName("Purchases")
    private double purchases;
    @SerializedName("Sales")
    private double sales;

    public int getOrder() {
        return this.order;
    }

    public double getSales() {
        return this.sales;
    }

    public String getLabel() {
        return this.label;
    }

    public double getPurchases() {
        return this.purchases;
    }
}
