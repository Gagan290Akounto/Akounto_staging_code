package com.akounto.accountingsoftware.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {

    @SerializedName("ActiveBusiness")//An annotation that indicates this member should be serialized to JSON with the provided name value as its field name.
    @Expose//An annotation that indicates this member should be exposed for JSON serialization or deserialization.
    private Business activeBusiness;

    @SerializedName("Business")
    @Expose
    private List<Business> business = null;

    public Business getActiveBusiness() {
        return activeBusiness;
    }

    public void setActiveBusiness(Business activeBusiness) {
        this.activeBusiness = activeBusiness;
    }

    public List<Business> getBusiness() {
        return business;
    }

    public void setBusiness(List<Business> business) {
        this.business = business;
    }

}
