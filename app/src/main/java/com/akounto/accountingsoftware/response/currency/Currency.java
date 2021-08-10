package com.akounto.accountingsoftware.response.currency;

import com.google.gson.annotations.SerializedName;

public class Currency {
    @SerializedName("Id")
    private String id;
    @SerializedName("Code")
    private String Code;
    @SerializedName("ExchangeRate")
    private Double ExchangeRate;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Symbol")
    private String Symbol;

    public Currency(String code, Double exchangeRate, String name, String symbol) {
        Code = code;
        ExchangeRate = exchangeRate;
        Name = name;
        Symbol = symbol;
    }

    public Currency() {

    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public Double getExchangeRate() {
        return this.ExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.ExchangeRate = exchangeRate;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSymbol() {
        return this.Symbol;
    }

    public void setSymbol(String symbol) {
        this.Symbol = symbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
