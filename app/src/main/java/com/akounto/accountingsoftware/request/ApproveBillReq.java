package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class ApproveBillReq {
    @SerializedName("BillId")
    private int BillId;
    @SerializedName("Status")
    private int Status;

    public int getBillId() {
        return this.BillId;
    }

    public void setBillId(int billId) {
        this.BillId = billId;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }
}
