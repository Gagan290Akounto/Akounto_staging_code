
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashFlow {

    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("Credit")
    @Expose
    private Double credit;
    @SerializedName("Dredit")
    @Expose
    private Double dredit;
    @SerializedName("IsDummyData")
    @Expose
    private Boolean isDummyData;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDredit() {
        return dredit;
    }

    public void setDredit(Double dredit) {
        this.dredit = dredit;
    }

    public Boolean getIsDummyData() {
        return isDummyData;
    }

    public void setIsDummyData(Boolean isDummyData) {
        this.isDummyData = isDummyData;
    }

}
