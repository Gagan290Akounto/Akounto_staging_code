package com.akounto.accountingsoftware.response.trialbalance;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrialBalanceTransactionDetails {
    @SerializedName("Id")

    /* renamed from: Id */
    private int f140Id;
    @SerializedName("Name")
    private String Name;
    @SerializedName("TrialBalanceTransactions")
    private List<TrialBalanceTransaction> TrialBalanceTransactions;

    public int getId() {
        return this.f140Id;
    }

    public void setId(int id) {
        this.f140Id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public List<TrialBalanceTransaction> getTrialBalanceTransactions() {
        return this.TrialBalanceTransactions;
    }

    public void setTrialBalanceTransactions(List<TrialBalanceTransaction> trialBalanceTransactions) {
        this.TrialBalanceTransactions = trialBalanceTransactions;
    }
}
