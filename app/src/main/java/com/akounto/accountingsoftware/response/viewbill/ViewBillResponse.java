package com.akounto.accountingsoftware.response.viewbill;

import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;

public class ViewBillResponse {
    @SerializedName("Data")
    private ViewBillByid data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public ViewBillByid getData() {
        return this.data;
    }

    public void setData(ViewBillByid data2) {
        this.data = data2;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
