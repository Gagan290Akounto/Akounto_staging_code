package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class EffectiveTax {
    @SerializedName("EffectiveDate")
    private String EffectiveDate;
    @SerializedName("Rate")
    private int Rate;

    public String getEffectiveDate() {
        return this.EffectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.EffectiveDate = effectiveDate;
    }

    public int getRate() {
        return this.Rate;
    }

    public void setRate(int rate) {
        this.Rate = rate;
    }

    public EffectiveTax(int Rate2) {
        this.Rate = Rate2;
    }
}
