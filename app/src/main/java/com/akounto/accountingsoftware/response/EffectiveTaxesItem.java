package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class EffectiveTaxesItem implements Serializable {
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("EffectiveDate")
    private String effectiveDate;
    @SerializedName("Id")

    /* renamed from: id */
    private int f115id;
    @SerializedName("Rate")
    private final double rate;
    @SerializedName("TaxId")
    private int taxId;

    public int getCompanyId() {
        return this.companyId;
    }

    public double getRate() {
        return this.rate;
    }

    public int getId() {
        return this.f115id;
    }

    public int getTaxId() {
        return this.taxId;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public EffectiveTaxesItem(double rate2) {
        this.rate = rate2;
    }

    public EffectiveTaxesItem(double rate2, String effectiveDate2) {
        this.rate = rate2;
        this.effectiveDate = effectiveDate2;
    }
}
