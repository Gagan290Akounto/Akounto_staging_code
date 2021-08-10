package com.akounto.accountingsoftware.model;

import java.util.List;

public class UserBusiness {
    private Business ActiveBusiness;
    private List<Business> Business;

    public Business getActiveBusiness() {
        return this.ActiveBusiness;
    }

    public void setActiveBusiness(Business activeBusiness) {
        this.ActiveBusiness = activeBusiness;
    }

    public List<Business> getBusiness() {
        return this.Business;
    }

    public void setBusiness(List<Business> business) {
        this.Business = business;
    }
}
