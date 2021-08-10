package com.akounto.accountingsoftware.model;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("Id")

    /* renamed from: id */
    private final int f93id;
    @SerializedName("Name")
    private final String name;

    public int getId() {
        return this.f93id;
    }

    public String getName() {
        return this.name;
    }

    public Country(int id, String name2) {
        this.f93id = id;
        this.name = name2;
    }
}
