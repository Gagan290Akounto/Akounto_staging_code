package com.akounto.accountingsoftware.response.currency;

import com.google.gson.annotations.SerializedName;

public class CurrenucyData {
    @SerializedName("BusinessCurrency")
    private Currency BusinessCurrency;
    @SerializedName("ExchangeCurrency")
    private Currency ExchangeCurrency;

    public Currency getBusinessCurrency() {
        return this.BusinessCurrency;
    }

    public void setBusinessCurrency(Currency businessCurrency) {
        this.BusinessCurrency = businessCurrency;
    }

    public Currency getExchangeCurrency() {
        return this.ExchangeCurrency;
    }

    public void setExchangeCurrency(Currency exchangeCurrency) {
        this.ExchangeCurrency = exchangeCurrency;
    }
}
