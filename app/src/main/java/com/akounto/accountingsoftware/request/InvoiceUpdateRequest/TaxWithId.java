package com.akounto.accountingsoftware.request.InvoiceUpdateRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxWithId {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Rate")
    @Expose
    private Integer rate;
    @SerializedName("TransactionHeadTaxId")
    @Expose
    private Integer transactionHeadTaxId;
    @SerializedName("Name")
    @Expose
    private String name;

    public TaxWithId(Integer id, Integer rate, Integer transactionHeadTaxId, String name) {
        this.id = id;
        this.rate = rate;
        this.transactionHeadTaxId = transactionHeadTaxId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

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
}
