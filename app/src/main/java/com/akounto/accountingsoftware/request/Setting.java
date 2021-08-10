package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("AccountingBasisType")
    String accountingBasisType;

    public Setting(String accountingBasisType) {
        this.accountingBasisType = accountingBasisType;
    }
}
