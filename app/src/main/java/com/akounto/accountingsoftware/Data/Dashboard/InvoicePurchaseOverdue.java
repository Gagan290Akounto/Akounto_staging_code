
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoicePurchaseOverdue {

    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("DueAmount")
    @Expose
    private Double dueAmount;
    @SerializedName("OverdueAmount")
    @Expose
    private Double overdueAmount;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Double getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Double overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

}
