package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class Vendor {
    @SerializedName("AccountNumber")
    private String AccountNumber;
    @SerializedName("BillAddress1")
    private String BillAddress1;
    @SerializedName("BillAddress2")
    private String BillAddress2;
    @SerializedName("BillCity")
    private String BillCity;
    @SerializedName("BillPostCode")
    private String BillPostCode;
    @SerializedName("BillState")
    private String BillState;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("Currency")
    private String Currency;
    @SerializedName("Email")
    private String Email;
    @SerializedName("HeadTransactionId")
    private int HeadTransactionId;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f132Id;
    @SerializedName("Phone")
    private String Phone;
    @SerializedName("VendorName")
    private String VendorName;

    public String getAccountNumber() {
        return this.AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.AccountNumber = accountNumber;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
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

    public int getHeadTransactionId() {
        return this.HeadTransactionId;
    }

    public void setHeadTransactionId(int headTransactionId) {
        this.HeadTransactionId = headTransactionId;
    }

    public int getId() {
        return this.f132Id;
    }

    public void setId(int id) {
        this.f132Id = id;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getVendorName() {
        return this.VendorName;
    }

    public void setVendorName(String vendorName) {
        this.VendorName = vendorName;
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

    public String getBillCity() {
        return this.BillCity;
    }

    public void setBillCity(String billCity) {
        this.BillCity = billCity;
    }

    public String getBillState() {
        return this.BillState;
    }

    public void setBillState(String billState) {
        this.BillState = billState;
    }

    public String getBillPostCode() {
        return this.BillPostCode;
    }

    public void setBillPostCode(String billPostCode) {
        this.BillPostCode = billPostCode;
    }
}
