
package com.akounto.accountingsoftware.response.Taxs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.response.EffectiveTax;
public class Data {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("HeadId")
    @Expose
    private Integer headId;
    @SerializedName("SubHeadsId")
    @Expose
    private Integer subHeadsId;
    @SerializedName("AccountTaxId")
    @Expose
    private Object accountTaxId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Currency")
    @Expose
    private Object currency;
    @SerializedName("IsCompoundTax")
    @Expose
    private Boolean isCompoundTax;
    @SerializedName("IsRecoverableTax")
    @Expose
    private Boolean isRecoverableTax;
    @SerializedName("UserName")
    @Expose
    private Object userName;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("CurrentRate")
    @Expose
    private Double currentRate;
    @SerializedName("EffectiveTaxes")
    @Expose
    private List<EffectiveTax> effectiveTaxes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public Integer getSubHeadsId() {
        return subHeadsId;
    }

    public void setSubHeadsId(Integer subHeadsId) {
        this.subHeadsId = subHeadsId;
    }

    public Object getAccountTaxId() {
        return accountTaxId;
    }

    public void setAccountTaxId(Object accountTaxId) {
        this.accountTaxId = accountTaxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Boolean getIsCompoundTax() {
        return isCompoundTax;
    }

    public void setIsCompoundTax(Boolean isCompoundTax) {
        this.isCompoundTax = isCompoundTax;
    }

    public Boolean getIsRecoverableTax() {
        return isRecoverableTax;
    }

    public void setIsRecoverableTax(Boolean isRecoverableTax) {
        this.isRecoverableTax = isRecoverableTax;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(Double currentRate) {
        this.currentRate = currentRate;
    }

    public List<EffectiveTax> getEffectiveTaxes() {
        return effectiveTaxes;
    }

    public void setEffectiveTaxes(List<EffectiveTax> effectiveTaxes) {
        this.effectiveTaxes = effectiveTaxes;
    }

}
