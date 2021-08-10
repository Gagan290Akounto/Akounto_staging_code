package com.akounto.accountingsoftware.response.cashflow;

import com.akounto.accountingsoftware.response.getBalanceSheet.BalanceTransactionDetail;
import com.akounto.accountingsoftware.response.getBalanceSheet.ReportRQ;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CashFlowTransactionDetails {
    @SerializedName("CashFlowTransactionDetails1")
    private List<BalanceTransactionDetail> CashFlowTransactionDetails1;
    @SerializedName("CashFlowTransactionDetails2")
    private List<BalanceTransactionDetail> CashFlowTransactionDetails2;
    @SerializedName("ReportRQ")
    private ReportRQ reportRQ;

    public List<BalanceTransactionDetail> getCashFlowTransactionDetails1() {
        return this.CashFlowTransactionDetails1;
    }

    public void setCashFlowTransactionDetails1(List<BalanceTransactionDetail> cashFlowTransactionDetails1) {
        this.CashFlowTransactionDetails1 = cashFlowTransactionDetails1;
    }

    public List<BalanceTransactionDetail> getCashFlowTransactionDetails2() {
        return this.CashFlowTransactionDetails2;
    }

    public void setCashFlowTransactionDetails2(List<BalanceTransactionDetail> cashFlowTransactionDetails2) {
        this.CashFlowTransactionDetails2 = cashFlowTransactionDetails2;
    }

    public ReportRQ getReportRQ() {
        return this.reportRQ;
    }

    public void setReportRQ(ReportRQ reportRQ2) {
        this.reportRQ = reportRQ2;
    }
}
