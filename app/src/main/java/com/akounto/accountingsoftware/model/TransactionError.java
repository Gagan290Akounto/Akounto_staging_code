package com.akounto.accountingsoftware.model;

import com.google.gson.annotations.SerializedName;

public class TransactionError {
    @SerializedName("Code")
    private String code;
    @SerializedName("Description")
    private String description;

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
