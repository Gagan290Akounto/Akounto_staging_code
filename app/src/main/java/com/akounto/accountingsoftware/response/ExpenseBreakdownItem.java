package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class ExpenseBreakdownItem {
    @SerializedName("Amount")
    private double amount;
    @SerializedName("Name")
    private String name;

    public double getAmount() {
        return this.amount;
    }

    public String getName() {
        return this.name;
    }
}
