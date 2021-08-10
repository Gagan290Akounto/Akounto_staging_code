package com.akounto.accountingsoftware.request.BillUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillTaxs {
    @SerializedName("TransactionHeadTaxId")
    @Expose
    private Integer transactionHeadTaxId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Rate")
    @Expose
    private Integer rate;

    public Integer getTransactionHeadTaxId() {
        return transactionHeadTaxId;
    }

    public void setTransactionHeadTaxId(Integer transactionHeadTaxId) {
        this.transactionHeadTaxId = transactionHeadTaxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
