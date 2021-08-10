package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class CashFlowRequest {
    @SerializedName("EndDate1")
    private String EndDate1;
    @SerializedName("EndDate2")
    private String EndDate2;
    @SerializedName("StartDate1")
    private String StartDate1;
    @SerializedName("StartDate2")
    private String StartDate2;

    public String getEndDate1() {
        return this.EndDate1;
    }

    public void setEndDate1(String endDate1) {
        this.EndDate1 = endDate1;
    }

    public String getEndDate2() {
        return this.EndDate2;
    }

    public void setEndDate2(String endDate2) {
        this.EndDate2 = endDate2;
    }

    public String getStartDate1() {
        return this.StartDate1;
    }

    public void setStartDate1(String startDate1) {
        this.StartDate1 = startDate1;
    }

    public String getStartDate2() {
        return this.StartDate2;
    }

    public void setStartDate2(String startDate2) {
        this.StartDate2 = startDate2;
    }
}
