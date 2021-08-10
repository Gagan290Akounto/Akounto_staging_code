package com.akounto.accountingsoftware.response.getBalanceSheet;

import com.google.gson.annotations.SerializedName;

public class ReportRQ {
    @SerializedName("BalanceStartDate")
    private String BalanceStartDate;
    @SerializedName("BalanceTillDate")
    private String BalanceTillDate;
    @SerializedName("CompanyId")
    private int CompanyId;

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

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }
}
