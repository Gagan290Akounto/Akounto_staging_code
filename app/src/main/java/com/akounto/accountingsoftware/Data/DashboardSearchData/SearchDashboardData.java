package com.akounto.accountingsoftware.Data.DashboardSearchData;

import java.util.ArrayList;
import java.util.List;

import com.akounto.accountingsoftware.Data.RegisterBank.TransactionStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDashboardData {

    @SerializedName("TransactionStatus")
    @Expose
    private TransactionStatus transactionStatus;

    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;
    private int status;
    private String statusMessage;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public static List<String> setFilter(List<Datum> data) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getLabel());
        }
        return list;
    }
}
