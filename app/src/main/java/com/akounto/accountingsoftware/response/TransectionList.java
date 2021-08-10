package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransectionList {
    @SerializedName("From")
    private int From;
    @SerializedName("To")

    /* renamed from: To */
    private int f129To;
    @SerializedName("TotalRecords")
    private int TotalRecords;
    @SerializedName("Transactions")
    private List<Transaction> Transactions;

    public int getFrom() {
        return this.From;
    }

    public void setFrom(int from) {
        this.From = from;
    }

    public int getTo() {
        return this.f129To;
    }

    public void setTo(int to) {
        this.f129To = to;
    }

    public int getTotalRecords() {
        return this.TotalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.TotalRecords = totalRecords;
    }

    public List<Transaction> getTransactions() {
        return this.Transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.Transactions = transactions;
    }
}
