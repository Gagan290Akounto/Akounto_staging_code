
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectedProfit {

    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("Amount")
    @Expose
    private Double amount;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
