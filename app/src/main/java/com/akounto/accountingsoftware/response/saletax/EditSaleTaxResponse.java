package com.akounto.accountingsoftware.response.saletax;

import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.response.TransactionStatus;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class EditSaleTaxResponse implements Serializable {
    @SerializedName("Data")
    private SaleTax data;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public SaleTax getData() {
        return this.data;
    }
}
