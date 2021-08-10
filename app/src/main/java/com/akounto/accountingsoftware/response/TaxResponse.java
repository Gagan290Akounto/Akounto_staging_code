package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TaxResponse {
    @SerializedName("AccountTaxId")
    private String AccountTaxId;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("Description")
    private String Description;
    @SerializedName("EffectiveTaxes")
    private List<EffectiveTax> EffectiveTaxes;
    @SerializedName("HeadId")
    private int HeadId;
    @SerializedName("HeadName")
    private String HeadName;
    @SerializedName("Id")
    private int Id;
    @SerializedName("IsCompoundTax")
    private Boolean IsCompoundTax;
    @SerializedName("IsRecoverableTax")
    private Boolean IsRecoverableTax;
    @SerializedName("Name")
    private String Name;
    @SerializedName("SubHeadId")
    private int SubHeadId;
    @SerializedName("SubHeadName")
    private String SubHeadName;

    public String getAccountTaxId() {
        return this.AccountTaxId;
    }

    public void setAccountTaxId(String accountTaxId) {
        this.AccountTaxId = accountTaxId;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public List<EffectiveTax> getEffectiveTaxes() {
        return this.EffectiveTaxes;
    }

    public void setEffectiveTaxes(List<EffectiveTax> effectiveTaxes) {
        this.EffectiveTaxes = effectiveTaxes;
    }

    public int getHeadId() {
        return this.HeadId;
    }

    public void setHeadId(int headId) {
        this.HeadId = headId;
    }

    public String getHeadName() {
        return this.HeadName;
    }

    public void setHeadName(String headName) {
        this.HeadName = headName;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public Boolean getCompoundTax() {
        return this.IsCompoundTax;
    }

    public void setCompoundTax(Boolean compoundTax) {
        this.IsCompoundTax = compoundTax;
    }

    public Boolean getRecoverableTax() {
        return this.IsRecoverableTax;
    }

    public void setRecoverableTax(Boolean recoverableTax) {
        this.IsRecoverableTax = recoverableTax;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSubHeadName() {
        return this.SubHeadName;
    }

    public void setSubHeadName(String subHeadName) {
        this.SubHeadName = subHeadName;
    }

    public int getSubHeadId() {
        return this.SubHeadId;
    }

    public void setSubHeadId(int subHeadId) {
        this.SubHeadId = subHeadId;
    }
}
