package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BusinessDetails implements Serializable {
    @SerializedName("Address1")
    private String address1;
    @SerializedName("Address2")
    private String address2;
    @SerializedName("BusinessCurrency")
    private String businessCurrency;
    @SerializedName("BusinessName")
    private String businessName;
    @SerializedName("City")
    private String city;
    @SerializedName("Country")
    private int country;
    @SerializedName("DomainUrl")
    private String domainUrl;
    @SerializedName("Fax")
    private String fax;
    @SerializedName("Id")

    /* renamed from: id */
    private int f113id;
    @SerializedName("Logo")
    private String logo;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("PostCode")
    private String postCode;
    @SerializedName("State")
    private String state;
    @SerializedName("TimeZone")
    private String timeZone;
    @SerializedName("TollFree")
    private String tollFree;

    public void setBusinessCurrency(String businessCurrency2) {
        this.businessCurrency = businessCurrency2;
    }

    public String getBusinessCurrency() {
        return this.businessCurrency;
    }

    public void setBusinessName(String businessName2) {
        this.businessName = businessName2;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public void setAddress2(String address22) {
        this.address2 = address22;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress1(String address12) {
        this.address1 = address12;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getCity() {
        return this.city;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setLogo(String logo2) {
        this.logo = logo2;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setTimeZone(String timeZone2) {
        this.timeZone = timeZone2;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTollFree(String tollFree2) {
        this.tollFree = tollFree2;
    }

    public String getTollFree() {
        return this.tollFree;
    }

    public void setDomainUrl(String domainUrl2) {
        this.domainUrl = domainUrl2;
    }

    public String getDomainUrl() {
        return this.domainUrl;
    }

    public void setState(String state2) {
        this.state = state2;
    }

    public String getState() {
        return this.state;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setCountry(int country2) {
        this.country = country2;
    }

    public int getCountry() {
        return this.country;
    }

    public void setId(int id) {
        this.f113id = id;
    }

    public int getId() {
        return this.f113id;
    }

    public void setPostCode(String postCode2) {
        this.postCode = postCode2;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setFax(String fax2) {
        this.fax = fax2;
    }

    public String getFax() {
        return this.fax;
    }
}
