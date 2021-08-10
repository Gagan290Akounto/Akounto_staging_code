package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddCompanyRequest {
    @SerializedName("BusinessCurrency")
    private String businessCurrency;
    @SerializedName("BusinessEntity")
    private String businessEntity;
    @SerializedName("BusinessEntityId")
    private String businessEntityId;
    @SerializedName("BusinessName")
    private String businessName;
    @SerializedName("Country")
    private String country;
    @SerializedName("IndustryTypeId")
    private String industryTypeId;
    @SerializedName("IndustryTypeName")
    private String industryTypeName;
    @SerializedName("FinancialDayEnd")
    private int financialDayEnd;
    @SerializedName("FinancialMonthEnd")
    private int financialMonthEnd;

    public void setIndustryTypeId(String industryTypeId2) {
        this.industryTypeId = industryTypeId2;
    }

    public String getIndustryTypeId() {
        return this.industryTypeId;
    }

    public void setBusinessEntityId(String businessEntityId2) {
        this.businessEntityId = businessEntityId2;
    }

    public String getBusinessEntityId() {
        return this.businessEntityId;
    }

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

    public void setIndustryTypeName(String industryTypeName2) {
        this.industryTypeName = industryTypeName2;
    }

    public String getIndustryTypeName() {
        return this.industryTypeName;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getCountry() {
        return this.country;
    }

    public void setBusinessEntity(String businessEntity2) {
        this.businessEntity = businessEntity2;
    }

    public String getBusinessEntity() {
        return this.businessEntity;
    }

    public AddCompanyRequest(String industryTypeId2) {
        this.industryTypeId = industryTypeId2;
    }

    public AddCompanyRequest(String industryTypeId2, String businessEntityId2, String businessCurrency2, String businessName2, String industryTypeName2, String country2, String businessCurrency,String businessEntity2,int financialDayEnd,int financialMonthEnd) {
        this.industryTypeId = industryTypeId2;
        this.businessEntityId = businessEntityId2;
        this.businessCurrency = businessCurrency2;
        this.businessName = businessName2;
        this.industryTypeName = industryTypeName2;
        this.country = country2;
        this.businessCurrency=businessCurrency;
        this.businessEntity = businessEntity2;
        this.financialMonthEnd=financialMonthEnd;
        this.financialDayEnd=financialDayEnd;
    }
}
