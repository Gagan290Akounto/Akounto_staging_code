package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class GetDashboardRequest {
    @SerializedName("CurrentDate")
    private final String currentDate;
    @SerializedName("EndDate")
    private final String endDate;
    @SerializedName("GraphType")
    private final int graphType;
    @SerializedName("StartDate")
    private final String startDate;
    @SerializedName("AccountingType")
    private final int accountingType;

    public String getStartDate() {
        return this.startDate;
    }

    public int getGraphType() {
        return this.graphType;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getCurrentDate() {
        return this.currentDate;
    }

    public int getAccountingType() {
        return accountingType;
    }

    public GetDashboardRequest(String currentDate,String startDate, String endDate, int accountingType,int graphType) {
        this.startDate = startDate;
        this.graphType = graphType;
        this.endDate = endDate;
        this.currentDate = currentDate;
        this.accountingType = accountingType;
    }


}
