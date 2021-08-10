package com.akounto.accountingsoftware.response.viewreport;

import com.google.gson.annotations.SerializedName;

public class InvoiceProfitLoss {
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("EndDate1")
    private String EndDate1;
    @SerializedName("EndDate2")
    private String EndDate2;
    @SerializedName("ReportType")
    private int ReportType;
    @SerializedName("StartDate1")
    private String StartDate1;
    @SerializedName("StartDate2")
    private String StartDate2;

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
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

    public int getReportType() {
        return this.ReportType;
    }

    public void setReportType(int reportType) {
        this.ReportType = reportType;
    }
}
