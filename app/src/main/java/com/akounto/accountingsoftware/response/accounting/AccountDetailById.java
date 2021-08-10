package com.akounto.accountingsoftware.response.accounting;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class AccountDetailById implements Serializable {
    @SerializedName("AccountTaxId")
    private String accountTaxId;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Created")
    private String created;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("CurrentRate")
    private double currentRate;
    @SerializedName("Description")
    private String description;
    @SerializedName("EffectiveTaxes")
    private List<Object> effectiveTaxes;
    @SerializedName("HeadId")
    private int headId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f133id;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("IsCompoundTax")
    private boolean isCompoundTax;
    @SerializedName("IsRecoverableTax")
    private boolean isRecoverableTax;
    @SerializedName("Name")
    private String name;
    @SerializedName("SubHeadsId")
    private int subHeadsId;
    @SerializedName("UserName")
    private String userName;

    public int getHeadId() {
        return this.headId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public int getSubHeadsId() {
        return this.subHeadsId;
    }

    public double getCurrentRate() {
        return this.currentRate;
    }

    public String getAccountTaxId() {
        return this.accountTaxId;
    }

    public boolean isIsCompoundTax() {
        return this.isCompoundTax;
    }

    public boolean isIsRecoverableTax() {
        return this.isRecoverableTax;
    }

    public String getName() {
        return this.name;
    }

    public String getCreated() {
        return this.created;
    }

    public String getCurrency() {
        return this.currency;
    }

    public int getId() {
        return this.f133id;
    }

    public List<Object> getEffectiveTaxes() {
        return this.effectiveTaxes;
    }
}
