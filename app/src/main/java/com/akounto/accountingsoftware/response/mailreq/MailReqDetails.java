package com.akounto.accountingsoftware.response.mailreq;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class MailReqDetails {
    @SerializedName("Data")
    private MailReceipentData Data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public MailReceipentData getData() {
        return this.Data;
    }

    public void setData(MailReceipentData data) {
        this.Data = data;
    }
}
