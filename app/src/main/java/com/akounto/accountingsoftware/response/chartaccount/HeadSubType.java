package com.akounto.accountingsoftware.response.chartaccount;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HeadSubType {
    @SerializedName("HeadTransactions")
    private List<HeadTransactions> HeadTransactions;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f136Id;
    @SerializedName("Name")
    private String Name;

    public int getId() {
        return this.f136Id;
    }

    public void setId(int id) {
        this.f136Id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public List<HeadTransactions> getHeadTransactions() {
        return this.HeadTransactions;
    }

    public void setHeadTransactions(List<HeadTransactions> headTransactions) {
        this.HeadTransactions = headTransactions;
    }
}
