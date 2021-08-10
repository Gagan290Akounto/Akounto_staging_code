package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.Data.Business;

import java.util.List;

public class BData {

    @SerializedName("Business")
    @Expose
    private List<Business> business = null;

    public List<Business> getBusiness() {
        return business;
    }

    public void setBusiness(List<Business> business) {
        this.business = business;
    }
}
