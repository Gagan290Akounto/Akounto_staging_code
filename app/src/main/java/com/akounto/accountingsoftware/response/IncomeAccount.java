package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class IncomeAccount {
    @SerializedName("AccountId")
    private String accountId;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")

    /* renamed from: id */
    private int f117id;
    @SerializedName("Name")
    private String name;
    @SerializedName("SubHeadId")
    private int subHeadId;

    public String getDescription() {
        return this.description;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public int getId() {
        return this.f117id;
    }

    public int getSubHeadId() {
        return this.subHeadId;
    }

    public String getName() {
        return this.name;
    }
}
