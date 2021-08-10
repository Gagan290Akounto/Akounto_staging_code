package com.akounto.accountingsoftware.model;

import com.google.gson.annotations.SerializedName;

public class Currency {
    @SerializedName("Id")

    /* renamed from: id */
    private String id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Symbol")
    private String symbol;

    public void setSymbol(String symbol2) {
        this.symbol = symbol2;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Currency(String symbol2, String id, String name2) {
        this.symbol = symbol2;
        this.id = id;
        this.name = name2;
    }

    public Currency() {
    }
}
