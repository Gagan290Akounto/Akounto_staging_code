
package com.akounto.accountingsoftware.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Business {

    @SerializedName("Id")//An annotation that indicates this member should be serialized to JSON with the provided name value as its field name.
    @Expose//An annotation that indicates this member should be exposed for JSON serialization or deserialization.
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Roles")
    @Expose
    private List<Integer> roles = null;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("UName")
    @Expose
    private String uName;
    @SerializedName("UId")
    @Expose
    private String uId;
    @SerializedName("TimeZone")
    @Expose
    private Object timeZone;
    @SerializedName("CurrencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("FinancialYearStart")
    @Expose
    private String financialYearStart;
    @SerializedName("FinancialYearEnd")
    @Expose
    private String financialYearEnd;
    @SerializedName("DefaultBusiness")
    @Expose
    private Boolean defaultBusiness;
    @SerializedName("Country")
    @Expose
    private Integer country;
    @SerializedName("AccountingBasisType")
    @Expose
    private Integer accountingBasisType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public Object getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Object timeZone) {
        this.timeZone = timeZone;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getFinancialYearStart() {
        return financialYearStart;
    }

    public void setFinancialYearStart(String financialYearStart) {
        this.financialYearStart = financialYearStart;
    }

    public String getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(String financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public Boolean getDefaultBusiness() {
        return defaultBusiness;
    }

    public void setDefaultBusiness(Boolean defaultBusiness) {
        this.defaultBusiness = defaultBusiness;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getAccountingBasisType() {
        return accountingBasisType;
    }

    public void setAccountingBasisType(Integer accountingBasisType) {
        this.accountingBasisType = accountingBasisType;
    }

}
