package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class GetBalanceSheetRequest {
    @SerializedName("BalanceStartDate")
    private String BalanceStartDate;
    @SerializedName("BalanceTillDate")
    private String BalanceTillDate;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("IsPDF")
    private Boolean IsPDF;

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public Boolean getPDF() {
        return this.IsPDF;
    }

    public void setPDF(Boolean PDF) {
        this.IsPDF = PDF;
    }

    public String getBalanceStartDate() {
        return this.BalanceStartDate;
    }

    public void setBalanceStartDate(String balanceStartDate) {
        this.BalanceStartDate = balanceStartDate;
    }

    public String getBalanceTillDate() {
        return this.BalanceTillDate;
    }

    public void setBalanceTillDate(String balanceTillDate) {
        this.BalanceTillDate = balanceTillDate;
    }
}
