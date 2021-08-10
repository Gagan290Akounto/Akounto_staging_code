package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VendorResponse {
    @SerializedName("Data")
    private List<Vendor> data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public List<Vendor> getData() {
        return this.data;
    }
}
