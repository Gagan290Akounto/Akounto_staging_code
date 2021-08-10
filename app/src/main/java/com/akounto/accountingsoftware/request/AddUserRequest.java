package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddUserRequest {
    @SerializedName("City")
    private String city;
    @SerializedName("Country")
    private int country;
    @SerializedName("DOB")
    private String dob;
    @SerializedName("Email")
    private final String email;
    @SerializedName("FirstName")
    private final String firstName;
    @SerializedName("Id")

    /* renamed from: id */
    private final String f102id;
    @SerializedName("LastName")
    private final String lastName;
    @SerializedName("PostCode")
    private String postcode;
    @SerializedName("Role")
    private final String role;

    public String getRole() {
        return this.role;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getId() {
        return this.f102id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AddUserRequest(String role2, String email2, String firstName2, String id, String lastName2) {
        this.role = role2;
        this.email = email2;
        this.firstName = firstName2;
        this.f102id = id;
        this.lastName = lastName2;
    }

    public AddUserRequest(String role2, String email2, String firstName2, String id, String lastName2, String city2, int country2, String dob2, String postcode2) {
        this.role = role2;
        this.email = email2;
        this.firstName = firstName2;
        this.f102id = id;
        this.lastName = lastName2;
        this.city = city2;
        this.country = country2;
        this.dob = dob2;
        this.postcode = postcode2;
    }
}
