package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class RecurringInvoiceSalesTaxItem {
    @SerializedName("Id")
    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Rate")
    private int rate;
    @SerializedName("TransactionHeadTaxId")
    private int transactionHeadTaxId;

    public void setRate(int rate2) {
        this.rate = rate2;
    }

    public void setTransactionHeadTaxId(int transactionHeadTaxId2) {
        this.transactionHeadTaxId = transactionHeadTaxId2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public RecurringInvoiceSalesTaxItem(int rate2, int transactionHeadTaxId2, String name2) {
        this.rate = rate2;
        this.transactionHeadTaxId = transactionHeadTaxId2;
        this.name = name2;
    }
    public RecurringInvoiceSalesTaxItem(String id,int rate2, int transactionHeadTaxId2, String name2) {
        this.id=id;
        this.rate = rate2;
        this.transactionHeadTaxId = transactionHeadTaxId2;
        this.name = name2;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
