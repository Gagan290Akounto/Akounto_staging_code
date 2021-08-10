package com.akounto.accountingsoftware.request;

import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddSaleTaxRequest {
    @SerializedName("AccountTaxId")
    private Object accountTaxId;
    @SerializedName("Description")
    private final String description;
    @SerializedName("EffectiveTaxes")
    private final List<EffectiveTaxesItem> effectiveTaxes;
    @SerializedName("IsCompoundTax")
    private final Boolean isCompoundTax;
    @SerializedName("IsRecoverableTax")
    private final Boolean isRecoverableTax;
    @SerializedName("Name")
    private final String name;

    public AddSaleTaxRequest(String name2, String description2, Boolean isCompoundTax2, Boolean isRecoverableTax2, List<EffectiveTaxesItem> effectiveTaxes2) {
        this.name = name2;
        this.description = description2;
        this.isCompoundTax = isCompoundTax2;
        this.isRecoverableTax = isRecoverableTax2;
        this.effectiveTaxes = effectiveTaxes2;
    }

    public AddSaleTaxRequest(String name2, String description2, Boolean isCompoundTax2, Boolean isRecoverableTax2, Object accountTaxId2, List<EffectiveTaxesItem> effectiveTaxes2) {
        this.name = name2;
        this.description = description2;
        this.isCompoundTax = isCompoundTax2;
        this.isRecoverableTax = isRecoverableTax2;
        this.accountTaxId = accountTaxId2;
        this.effectiveTaxes = effectiveTaxes2;
    }
}
