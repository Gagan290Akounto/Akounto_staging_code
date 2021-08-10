package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("City")
    private String city;
    @SerializedName("Country")
    private int country;
    @SerializedName("DOB")
    private String dOB;
    @SerializedName("Email")
    private String email;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("Id")

    /* renamed from: id */
    private String f130id;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("PostCode")
    private String postCode;
    @SerializedName("Role")
    private String role;

    public String getRole() {
        return this.role;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getDOB() {
        return this.dOB;
    }

    public int getCountry() {
        return this.country;
    }

    public String getId() {
        return this.f130id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostCode() {
        return this.postCode;
    }
}
