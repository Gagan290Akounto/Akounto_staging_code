package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class ViewReportRequest {
    @SerializedName("EndDate1")
    private String endDate1;
    @SerializedName("EndDate2")
    private Object endDate2;
    @SerializedName("StartDate1")
    private String startDate1;
    @SerializedName("StartDate2")
    private Object startDate2;

    public void setEndDate2(Object endDate22) {
        this.endDate2 = endDate22;
    }

    public void setEndDate1(String endDate12) {
        this.endDate1 = endDate12;
    }

    public void setStartDate1(String startDate12) {
        this.startDate1 = startDate12;
    }

    public void setStartDate2(Object startDate22) {
        this.startDate2 = startDate22;
    }

    public Object getEndDate2() {
        return this.endDate2;
    }

    public String getEndDate1() {
        return this.endDate1;
    }

    public String getStartDate1() {
        return this.startDate1;
    }

    public Object getStartDate2() {
        return this.startDate2;
    }
}
