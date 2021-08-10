
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseBreakdown {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Amount")
    @Expose
    private Double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
