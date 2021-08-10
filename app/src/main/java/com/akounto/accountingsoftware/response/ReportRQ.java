package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class ReportRQ {
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("EndDate1")
    private String endDate1;
    @SerializedName("EndDate2")
    private Object endDate2;
    @SerializedName("StartDate1")
    private String startDate1;
    @SerializedName("StartDate2")
    private Object startDate2;

    public int getCompanyId() {
        return this.companyId;
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
