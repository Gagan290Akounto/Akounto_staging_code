package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GetInvoiceByIdResponse implements Serializable {
    @SerializedName("Data")
    private InvoiceDetails invoiceDetails;
    @SerializedName("TransactionStatus")
    private TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public InvoiceDetails getInvoiceDetails() {
        return this.invoiceDetails;
    }
}
