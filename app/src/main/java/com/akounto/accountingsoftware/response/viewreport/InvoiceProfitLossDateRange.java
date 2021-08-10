package com.akounto.accountingsoftware.response.viewreport;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InvoiceProfitLossDateRange {
    @SerializedName("ExpenceDetails")
    private List<IncomeDetail> ExpenceDetails;
    @SerializedName("IncomeDetails")
    private List<IncomeDetail> IncomeDetails;

    public List<IncomeDetail> getIncomeDetails() {
        return this.IncomeDetails;
    }

    public void setIncomeDetails(List<IncomeDetail> incomeDetails) {
        this.IncomeDetails = incomeDetails;
    }

    public List<IncomeDetail> getExpenceDetails() {
        return this.ExpenceDetails;
    }

    public void setExpenceDetails(List<IncomeDetail> expenceDetails) {
        this.ExpenceDetails = expenceDetails;
    }
}
