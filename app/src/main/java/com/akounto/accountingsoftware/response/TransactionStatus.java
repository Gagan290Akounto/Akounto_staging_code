package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.model.TransactionError;
import com.google.gson.annotations.SerializedName;

public class TransactionStatus {
    @SerializedName("Error")
    private TransactionError error;
    @SerializedName("IsSuccess")
    private boolean isSuccess;

    public void setIsSuccess(boolean isSuccess2) {
        this.isSuccess = isSuccess2;
    }

    public boolean isIsSuccess() {
        return this.isSuccess;
    }

    public void setError(TransactionError error2) {
        this.error = error2;
    }

    public TransactionError getError() {
        return this.error;
    }
}
