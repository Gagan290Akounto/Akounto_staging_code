package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("City")
    private Object city;
    @SerializedName("Country")
    private int country;
    @SerializedName("DOB")
    private Object dOB;
    @SerializedName("Email")
    private String email;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("Id")

    /* renamed from: id */
    private String f131id;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("PostCode")
    private Object postCode;
    @SerializedName("Role")
    private String role;
    @SerializedName("IsOwner")
    private boolean isOwner;

    public String getRole() {
        return this.role;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Object getDOB() {
        return this.dOB;
    }

    public int getCountry() {
        return this.country;
    }

    public String getId() {
        return this.f131id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Object getCity() {
        return this.city;
    }

    public Object getPostCode() {
        return this.postCode;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
