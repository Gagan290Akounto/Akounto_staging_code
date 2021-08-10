package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SaleTax implements Serializable {
    @SerializedName("AccountTaxId")
    private Object accountTaxId;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("CurrentRate")
    private double currentRate;
    @SerializedName("Description")
    private Object description;
    @SerializedName("EffectiveTaxes")
    private List<EffectiveTaxesItem> effectiveTaxes;
    @SerializedName("HeadId")
    private int headId;
    @SerializedName("HeadName")
    private String headName;
    @SerializedName("Id")
    private int id;
    @SerializedName("IsCompoundTax")
    private boolean isCompoundTax;
    @SerializedName("IsRecoverableTax")
    private boolean isRecoverableTax;
    @SerializedName("Name")
    private String name;
    @SerializedName("SubHeadsId")
    private int subHeadId;
    @SerializedName("SubHeadName")
    private String subHeadName;

    public int getHeadId() {
        return this.headId;
    }

    public Object getDescription() {
        return this.description;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public String getSubHeadName() {
        return this.subHeadName;
    }

    public int getId() {
        return this.id;
    }

    public List<EffectiveTaxesItem> getEffectiveTaxes() {
        return this.effectiveTaxes;
    }

    public Object getAccountTaxId() {
        return this.accountTaxId;
    }

    public String getHeadName() {
        return this.headName;
    }

    public boolean isIsCompoundTax() {
        return this.isCompoundTax;
    }

    public boolean isIsRecoverableTax() {
        return this.isRecoverableTax;
    }

    public int getSubHeadId() {
        return this.subHeadId;
    }

    public String getName() {
        return this.name;
    }

    public double getCurrentRate() {
        return this.currentRate;
    }

    public SaleTax() {
    }

    public SaleTax(Object description2, int id, List<EffectiveTaxesItem> effectiveTaxes2, Object accountTaxId2, boolean isCompoundTax2, boolean isRecoverableTax2, int subHeadId2, String name2) {
        this.description = description2;
        this.id = id;
        this.effectiveTaxes = effectiveTaxes2;
        this.accountTaxId = accountTaxId2;
        this.isCompoundTax = isCompoundTax2;
        this.isRecoverableTax = isRecoverableTax2;
        this.subHeadId = subHeadId2;
        this.name = name2;
    }

    public SaleTax(int id, String name2) {
        this.id = id;
        this.name = name2;
    }
}
