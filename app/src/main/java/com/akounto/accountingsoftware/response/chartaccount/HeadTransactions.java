package com.akounto.accountingsoftware.response.chartaccount;

import com.google.gson.annotations.SerializedName;

public class HeadTransactions {
    @SerializedName("AccountId")
    private String AccountId;
    @SerializedName("Description")
    private String Description;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f137Id;
    @SerializedName("Name")
    private String Name;
    @SerializedName("SubHeadId")
    private int SubHeadId;

    public int getId() {
        return this.f137Id;
    }

    public void setId(int id) {
        this.f137Id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getAccountId() {
        return this.AccountId;
    }

    public void setAccountId(String accountId) {
        this.AccountId = accountId;
    }

    public int getSubHeadId() {
        return this.SubHeadId;
    }

    public void setSubHeadId(int subHeadId) {
        this.SubHeadId = subHeadId;
    }
}
