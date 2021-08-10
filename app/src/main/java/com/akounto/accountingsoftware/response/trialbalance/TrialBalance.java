package com.akounto.accountingsoftware.response.trialbalance;

import com.akounto.accountingsoftware.response.ReportRQ;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrialBalance {
    @SerializedName("TrialBalance")
    private List<TrialBalanceTransactionDetails> TrialBalance;
    @SerializedName("ReportRQ")
    private ReportRQ reportRQ;

    public ReportRQ getReportRQ() {
        return this.reportRQ;
    }

    public void setReportRQ(ReportRQ reportRQ2) {
        this.reportRQ = reportRQ2;
    }

    public List<TrialBalanceTransactionDetails> getTrialBalance() {
        return this.TrialBalance;
    }

    public void setTrialBalance(List<TrialBalanceTransactionDetails> trialBalance) {
        this.TrialBalance = trialBalance;
    }
}
