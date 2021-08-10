package com.akounto.accountingsoftware.response.chartaccount;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HeadType {
    @SerializedName("HeadSubTypes")
    private List<HeadSubType> HeadSubTypes;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f138Id;
    @SerializedName("Name")
    private String Name;

    public int getId() {
        return this.f138Id;
    }

    public void setId(int id) {
        this.f138Id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public List<HeadSubType> getHeadSubTypes() {
        return this.HeadSubTypes;
    }

    public void setHeadSubTypes(List<HeadSubType> headSubTypes) {
        this.HeadSubTypes = headSubTypes;
    }
}
