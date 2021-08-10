package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class SaleTaxRequest {
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("EndDate")
    private String EndDate;
    @SerializedName("IsPDF")
    private Boolean IsPDF;
    @SerializedName("StartDate")
    private String StartDate;

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getEndDate() {
        return this.EndDate;
    }

    public void setEndDate(String endDate) {
        this.EndDate = endDate;
    }

    public String getStartDate() {
        return this.StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }

    public Boolean getPDF() {
        return this.IsPDF;
    }

    public void setPDF(Boolean PDF) {
        this.IsPDF = PDF;
    }
}
