package com.akounto.accountingsoftware.response.getBalanceSheet;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetBalanceSheetData {
    @SerializedName("BalanceTransactionDetails")
    private List<BalanceTransactionDetail> BalanceTransactionDetails;
    @SerializedName("ReportRQ")
    private ReportRQ reportRQ;

    public ReportRQ getReportRQ() {
        return this.reportRQ;
    }

    public void setReportRQ(ReportRQ reportRQ2) {
        this.reportRQ = reportRQ2;
    }

    public List<BalanceTransactionDetail> getBalanceTransactionDetails() {
        return this.BalanceTransactionDetails;
    }

    public void setBalanceTransactionDetails(List<BalanceTransactionDetail> balanceTransactionDetails) {
        this.BalanceTransactionDetails = balanceTransactionDetails;
    }
}
