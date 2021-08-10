package com.akounto.accountingsoftware.response;

import com.akounto.accountingsoftware.request.Bills;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BillListResponse {
    @SerializedName("Bills")
    private List<Bills> Bills;

    public List<Bills> getBills() {
        return this.Bills;
    }

    public void setBills(List<Bills> bills) {
        this.Bills = bills;
    }
}
