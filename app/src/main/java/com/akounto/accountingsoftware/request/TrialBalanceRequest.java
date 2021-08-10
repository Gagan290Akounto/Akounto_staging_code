package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class TrialBalanceRequest {
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("IsPDF")
    private Boolean IsPDF;
    @SerializedName("TillDate")
    private String TillDate;

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getTillDate() {
        return this.TillDate;
    }

    public void setTillDate(String tillDate) {
        this.TillDate = tillDate;
    }

    public Boolean getPDF() {
        return this.IsPDF;
    }

    public void setPDF(Boolean PDF) {
        this.IsPDF = PDF;
    }
}
