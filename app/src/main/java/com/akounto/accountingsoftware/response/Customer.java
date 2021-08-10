package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Customer implements Serializable {
    @SerializedName("AccountNumber")
    private String accountNumber;
    @SerializedName("BillAddress1")
    private String billAddress1;
    @SerializedName("BillAddress2")
    private String billAddress2;
    @SerializedName("BillCity")
    private String billCity;
    @SerializedName("BillCountry")
    private int billCountry;
    @SerializedName("BillPostCode")
    private String billPostCode;
    @SerializedName("BillState")
    private String billState;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("Email")
    private String email;
    @SerializedName("Fax")
    private String fax;
    @SerializedName("HeadTransactionId")
    private int headTransactionId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f114id;
    @SerializedName("IsShippingSame")
    private boolean isShippingSame;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("Name")
    private String name;
    @SerializedName("Notes")
    private Object notes;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Portal")
    private String portal;
    @SerializedName("ShipAddress1")
    private String shipAddress1;
    @SerializedName("ShipAddress2")
    private String shipAddress2;
    @SerializedName("ShipCity")
    private String shipCity;
    @SerializedName("ShipCountry")
    private int shipCountry;
    @SerializedName("ShipPostCode")
    private String shipPostCode;
    @SerializedName("ShipState")
    private Object shipState;
    @SerializedName("TollFree")
    private String tollFree;

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getBillPostCode() {
        return this.billPostCode;
    }

    public int getHeadTransactionId() {
        return this.headTransactionId;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getPortal() {
        return this.portal;
    }

    public String getBillState() {
        return this.billState;
    }

    public Object getNotes() {
        return this.notes;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public String getShipCity() {
        return this.shipCity;
    }

    public Object getShipState() {
        return this.shipState;
    }

    public String getShipPostCode() {
        return this.shipPostCode;
    }

    public String getBillCity() {
        return this.billCity;
    }

    public int getBillCountry() {
        return this.billCountry;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getBillAddress1() {
        return this.billAddress1;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getBillAddress2() {
        return this.billAddress2;
    }

    public String getTollFree() {
        return this.tollFree;
    }

    public int getId() {
        return this.f114id;
    }

    public boolean isIsShippingSame() {
        return this.isShippingSame;
    }

    public int getShipCountry() {
        return this.shipCountry;
    }

    public String getFax() {
        return this.fax;
    }

    public String getShipAddress2() {
        return this.shipAddress2;
    }

    public String getShipAddress1() {
        return this.shipAddress1;
    }

    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHeadTransactionId(int headTransactionId) {
        this.headTransactionId = headTransactionId;
    }
}
