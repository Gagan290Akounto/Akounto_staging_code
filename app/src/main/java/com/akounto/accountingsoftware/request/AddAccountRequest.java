package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddAccountRequest {
    @SerializedName("AccountTaxId")
    private String accountTaxId;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")

    /* renamed from: id */
    private final int f96id;
    @SerializedName("IsRecoverableTax")
    private boolean isRecoverableTax = true;
    @SerializedName("Name")
    private String name;
    @SerializedName("SubHeadsId")
    private String subHeadsId;

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSubHeadsId(String subHeadsId2) {
        this.subHeadsId = subHeadsId2;
    }

    public String getSubHeadsId() {
        return this.subHeadsId;
    }

    public void setCurrency(String currency2) {
        this.currency = currency2;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setAccountTaxId(String accountTaxId2) {
        this.accountTaxId = accountTaxId2;
    }

    public String getAccountTaxId() {
        return this.accountTaxId;
    }

    public void setIsRecoverableTax(boolean isRecoverableTax2) {
        this.isRecoverableTax = isRecoverableTax2;
    }

    public boolean isIsRecoverableTax() {
        return this.isRecoverableTax;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public AddAccountRequest(String description2, String subHeadsId2, String currency2, String accountTaxId2, String name2, int id) {
        this.description = description2;
        this.subHeadsId = subHeadsId2;
        this.currency = currency2;
        this.accountTaxId = accountTaxId2;
        this.name = name2;
        this.f96id = id;
    }
}
