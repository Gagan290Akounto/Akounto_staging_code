package com.akounto.accountingsoftware.request;

public class RegisterBusiness {


    private String Address;
    private String BusinessCurrency;
    private String BusinessDo;
    private String BusinessEntity;
    private int BusinessEntityId;
    private String BusinessName;
    private int Country;
    private String phone;
    private int IndustryTypeId;
    private String IndustryTypeName;
    private User User;
    private String PhoneCode;

    public String getBusinessName() {
        return this.BusinessName;
    }

    public void setBusinessName(String businessName) {
        this.BusinessName = businessName;
    }

    public User getUser() {
        return this.User;
    }

    public void setUser(User user) {
        this.User = user;
    }

    public int getIndustryTypeId() {
        return this.IndustryTypeId;
    }

    public void setIndustryTypeId(int industryTypeId) {
        this.IndustryTypeId = industryTypeId;
    }

    public String getIndustryTypeName() {
        return this.IndustryTypeName;
    }

    public void setIndustryTypeName(String industryTypeName) {
        this.IndustryTypeName = industryTypeName;
    }

    public int getBusinessEntityId() {
        return this.BusinessEntityId;
    }

    public void setBusinessEntityId(int businessEntityId) {
        this.BusinessEntityId = businessEntityId;
    }

    public String getBusinessEntity() {
        return this.BusinessEntity;
    }

    public void setBusinessEntity(String businessEntity) {
        this.BusinessEntity = businessEntity;
    }

    public int getCountry() {
        return this.Country;
    }

    public void setCountry(int country) {
        this.Country = country;
    }

    public String getBusinessCurrency() {
        return this.BusinessCurrency;
    }

    public void setBusinessCurrency(String businessCurrency) {
        this.BusinessCurrency = businessCurrency;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getBusinessDo() {
        return this.BusinessDo;
    }

    public void setBusinessDo(String businessDo) {
        this.BusinessDo = businessDo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCode() {
        return PhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        PhoneCode = phoneCode;
    }
}
