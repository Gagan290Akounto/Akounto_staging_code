package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class EditCompanyRequest {
    @SerializedName("Address1")
    private final String address1;
    @SerializedName("Address2")
    private final String address2;
    @SerializedName("BusinessCurrency")
    private final String businessCurrency;
    @SerializedName("BusinessName")
    private final String businessName;
    @SerializedName("City")
    private final String city;
    @SerializedName("Country")
    private final int country;
    @SerializedName("Fax")
    private final String fax;
    @SerializedName("Id")

    /* renamed from: id */
    private final int f107id;
    @SerializedName("Mobile")
    private final String mobile;
    @SerializedName("Phone")
    private final String phone;
    @SerializedName("PostCode")
    private final String postCode;
    @SerializedName("State")
    private final String state;
    @SerializedName("Timezone")
    private final String timezone;
    @SerializedName("TollFree")
    private final String tollFree;

    public String getTimezone() {
        return this.timezone;
    }

    public String getBusinessCurrency() {
        return this.businessCurrency;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public String getAddress2() {
        return this.address2;
    }

    public String getAddress1() {
        return this.address1;
    }

    public String getCity() {
        return this.city;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getTollFree() {
        return this.tollFree;
    }

    public String getState() {
        return this.state;
    }

    public String getPhone() {
        return this.phone;
    }

    public int getCountry() {
        return this.country;
    }

    public int getId() {
        return this.f107id;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public String getFax() {
        return this.fax;
    }

    public EditCompanyRequest(String timezone2, String businessCurrency2, String businessName2, String address22, String address12, String city2, String mobile2, String tollFree2, String state2, String phone2, int country2, int id, String postCode2, String fax2) {
        this.timezone = timezone2;
        this.businessCurrency = businessCurrency2;
        this.businessName = businessName2;
        this.address2 = address22;
        this.address1 = address12;
        this.city = city2;
        this.mobile = mobile2;
        this.tollFree = tollFree2;
        this.state = state2;
        this.phone = phone2;
        this.country = country2;
        this.f107id = id;
        this.postCode = postCode2;
        this.fax = fax2;
    }
}
