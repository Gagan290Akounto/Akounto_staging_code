
package com.akounto.accountingsoftware.response.ReportSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionStatus {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Error")
    @Expose
    private Object error;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
