package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class DashboardResponse {
    @SerializedName("Data")
    private DashboardData dashboardData;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public DashboardData getDashboardData() {
        return this.dashboardData;
    }
}
