package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class ConvertToInvoice {
    @SerializedName("EstimateId")
    private int EstimateId;

    public int getEstimateId() {
        return this.EstimateId;
    }

    public void setEstimateId(int estimateId) {
        this.EstimateId = estimateId;
    }
}
