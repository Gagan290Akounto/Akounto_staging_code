package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddCustomerRequest {
    @SerializedName("Id")

    /* renamed from: Id */
    private int f99Id;
    @SerializedName("AccountNumber")
    private Object accountNumber;
    @SerializedName("BillAddress1")
    private String billAddress1;
    @SerializedName("BillAddress2")
    private String billAddress2;
    @SerializedName("BillCity")
    private String billCity;
    @SerializedName("BillCountry")
    private String billCountry;
    @SerializedName("BillPostCode")
    private String billPostCode;
    @SerializedName("BillState")
    private String billState;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Currency")
    private Object currency;
    @SerializedName("Email")
    private String email;
    @SerializedName("Fax")
    private Object fax;
    @SerializedName("HeadTransactionId")
    private int headTransactionId;
    @SerializedName("IsShippingSame")
    private boolean isShippingSame;
    @SerializedName("Mobile")
    private Object mobile;
    @SerializedName("Name")
    private String name;
    @SerializedName("Notes")
    private Object notes;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Portal")
    private Object portal;
    @SerializedName("ShipAddress1")
    private Object shipAddress1;
    @SerializedName("ShipAddress2")
    private Object shipAddress2;
    @SerializedName("ShipCity")
    private Object shipCity;
    @SerializedName("ShipCont")
    private Object shipCont;
    @SerializedName("ShipCountry")
    private String shipCountry;
    @SerializedName("ShipInstruct")
    private Object shipInstruct;
    @SerializedName("ShipPhone")
    private Object shipPhone;
    @SerializedName("ShipPostCode")
    private Object shipPostCode;
    @SerializedName("ShipState")
    private Object shipState;
    @SerializedName("TollFree")
    private Object tollFree;

    public int getId() {
        return this.f99Id;
    }

    public void setId(int id) {
        this.f99Id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getBillPostCode() {
        return this.billPostCode;
    }

    public void setBillPostCode(String billPostCode2) {
        this.billPostCode = billPostCode2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public Object getCurrency() {
        return this.currency;
    }

    public void setCurrency(Object currency2) {
        this.currency = currency2;
    }

    public Object getPortal() {
        return this.portal;
    }

    public void setPortal(Object portal2) {
        this.portal = portal2;
    }

    public String getBillState() {
        return this.billState;
    }

    public void setBillState(String billState2) {
        this.billState = billState2;
    }

    public Object getNotes() {
        return this.notes;
    }

    public void setNotes(Object notes2) {
        this.notes = notes2;
    }

    public Object getShipCity() {
        return this.shipCity;
    }

    public void setShipCity(Object shipCity2) {
        this.shipCity = shipCity2;
    }

    public Object getShipState() {
        return this.shipState;
    }

    public void setShipState(Object shipState2) {
        this.shipState = shipState2;
    }

    public Object getShipPostCode() {
        return this.shipPostCode;
    }

    public void setShipPostCode(Object shipPostCode2) {
        this.shipPostCode = shipPostCode2;
    }

    public String getBillCity() {
        return this.billCity;
    }

    public void setBillCity(String billCity2) {
        this.billCity = billCity2;
    }

    public String getBillCountry() {
        return this.billCountry;
    }

    public void setBillCountry(String billCountry2) {
        this.billCountry = billCountry2;
    }

    public Object getMobile() {
        return this.mobile;
    }

    public void setMobile(Object mobile2) {
        this.mobile = mobile2;
    }

    public String getBillAddress1() {
        return this.billAddress1;
    }

    public void setBillAddress1(String billAddress12) {
        this.billAddress1 = billAddress12;
    }

    public Object getShipCont() {
        return this.shipCont;
    }

    public void setShipCont(Object shipCont2) {
        this.shipCont = shipCont2;
    }

    public String getBillAddress2() {
        return this.billAddress2;
    }

    public void setBillAddress2(String billAddress22) {
        this.billAddress2 = billAddress22;
    }

    public Object getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(Object accountNumber2) {
        this.accountNumber = accountNumber2;
    }

    public Object getTollFree() {
        return this.tollFree;
    }

    public void setTollFree(Object tollFree2) {
        this.tollFree = tollFree2;
    }

    public String getShipCountry() {
        return this.shipCountry;
    }

    public void setShipCountry(String shipCountry2) {
        this.shipCountry = shipCountry2;
    }

    public boolean isShippingSame() {
        return this.isShippingSame;
    }

    public void setShippingSame(boolean shippingSame) {
        this.isShippingSame = shippingSame;
    }

    public Object getFax() {
        return this.fax;
    }

    public void setFax(Object fax2) {
        this.fax = fax2;
    }

    public Object getShipAddress2() {
        return this.shipAddress2;
    }

    public void setShipAddress2(Object shipAddress22) {
        this.shipAddress2 = shipAddress22;
    }

    public Object getShipPhone() {
        return this.shipPhone;
    }

    public void setShipPhone(Object shipPhone2) {
        this.shipPhone = shipPhone2;
    }

    public Object getShipAddress1() {
        return this.shipAddress1;
    }

    public void setShipAddress1(Object shipAddress12) {
        this.shipAddress1 = shipAddress12;
    }

    public Object getShipInstruct() {
        return this.shipInstruct;
    }

    public void setShipInstruct(Object shipInstruct2) {
        this.shipInstruct = shipInstruct2;
    }

    public int getHeadTransactionId() {
        return this.headTransactionId;
    }

    public void setHeadTransactionId(int headTransactionId2) {
        this.headTransactionId = headTransactionId2;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(int companyId2) {
        this.companyId = companyId2;
    }

    public AddCustomerRequest(String email2, String name2, String billPostCode2, String phone2, Object currency2, Object portal2, String billState2, Object notes2, Object shipCity2, Object shipState2, Object shipPostCode2, String billCity2, String billCountry2, Object mobile2, String billAddress12, Object shipCont2, String billAddress22, Object accountNumber2, Object tollFree2, String shipCountry2, boolean isShippingSame2, Object fax2, Object shipAddress22, Object shipPhone2, Object shipAddress12, Object shipInstruct2) {
        this.email = email2;
        this.name = name2;
        this.billPostCode = billPostCode2;
        this.phone = phone2;
        this.currency = currency2;
        this.portal = portal2;
        this.billState = billState2;
        this.notes = notes2;
        this.shipCity = shipCity2;
        this.shipState = shipState2;
        this.shipPostCode = shipPostCode2;
        this.billCity = billCity2;
        this.billCountry = billCountry2;
        this.mobile = mobile2;
        this.billAddress1 = billAddress12;
        this.shipCont = shipCont2;
        this.billAddress2 = billAddress22;
        this.accountNumber = accountNumber2;
        this.tollFree = tollFree2;
        this.shipCountry = shipCountry2;
        this.isShippingSame = isShippingSame2;
        this.fax = fax2;
        this.shipAddress2 = shipAddress22;
        this.shipPhone = shipPhone2;
        this.shipAddress1 = shipAddress12;
        this.shipInstruct = shipInstruct2;
    }
}
