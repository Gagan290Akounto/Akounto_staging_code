
package com.akounto.accountingsoftware.response.ReportSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportSettings {

    @SerializedName("TransactionStatus")
    @Expose
    private TransactionStatus transactionStatus;
    @SerializedName("Data")
    @Expose
    private Data data;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
