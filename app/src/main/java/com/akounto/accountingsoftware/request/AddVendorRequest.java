package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddVendorRequest {
    @SerializedName("AccountNumber")
    private String AccountNumber;
    @SerializedName("BillAddress1")
    private String BillAddress1;
    @SerializedName("BillAddress2")
    private String BillAddress2;
    @SerializedName("BillCity")
    private String BillCity;
    @SerializedName("BillCountry")
    private String BillCountry;
    @SerializedName("BillPostCode")
    private String BillPostCode;
    @SerializedName("BillState")
    private String BillState;
    @SerializedName("Currency")
    private String Currency;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Fax")
    private String Fax;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("HeadTransactionId")
    private int HeadTransactionId;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f103Id;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("Phone")
    private String Phone;
    @SerializedName("Portal")
    private String Portal;
    @SerializedName("TollFree")
    private String TollFree;
    @SerializedName("VendorName")
    private String VendorName;

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getVendorName() {
        return this.VendorName;
    }

    public void setVendorName(String vendorName) {
        this.VendorName = vendorName;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getCurrency() {
        return this.Currency;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public String getBillCountry() {
        return this.BillCountry;
    }

    public void setBillCountry(String billCountry) {
        this.BillCountry = billCountry;
    }

    public String getBillState() {
        return this.BillState;
    }

    public void setBillState(String billState) {
        this.BillState = billState;
    }

    public String getBillCity() {
        return this.BillCity;
    }

    public void setBillCity(String billCity) {
        this.BillCity = billCity;
    }

    public String getBillAddress1() {
        return this.BillAddress1;
    }

    public void setBillAddress1(String billAddress1) {
        this.BillAddress1 = billAddress1;
    }

    public String getBillAddress2() {
        return this.BillAddress2;
    }

    public void setBillAddress2(String billAddress2) {
        this.BillAddress2 = billAddress2;
    }

    public String getBillPostCode() {
        return this.BillPostCode;
    }

    public void setBillPostCode(String billPostCode) {
        this.BillPostCode = billPostCode;
    }

    public String getAccountNumber() {
        return this.AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.AccountNumber = accountNumber;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public String getFax() {
        return this.Fax;
    }

    public void setFax(String fax) {
        this.Fax = fax;
    }

    public String getTollFree() {
        return this.TollFree;
    }

    public void setTollFree(String tollFree) {
        this.TollFree = tollFree;
    }

    public String getPortal() {
        return this.Portal;
    }

    public void setPortal(String portal) {
        this.Portal = portal;
    }

    public int getHeadTransactionId() {
        return this.HeadTransactionId;
    }

    public void setHeadTransactionId(int headTransactionId) {
        this.HeadTransactionId = headTransactionId;
    }

    public int getId() {
        return this.f103Id;
    }

    public void setId(int id) {
        this.f103Id = id;
    }
}
