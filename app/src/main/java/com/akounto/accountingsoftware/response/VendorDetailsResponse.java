package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.google.gson.annotations.SerializedName;

public class VendorDetailsResponse {
    @SerializedName("Data")
    private AddVendorRequest data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public AddVendorRequest getData() {
        return this.data;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }
}
