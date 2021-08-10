package com.akounto.accountingsoftware.model;

public class Business {

    private int Country;
    private Boolean DefaultBusiness;
    private int id;
    private String Name;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.Name;
    }

    public int getCountry() {
        return this.Country;
    }

    public void setCountry(int country) {
        this.Country = country;
    }
}
