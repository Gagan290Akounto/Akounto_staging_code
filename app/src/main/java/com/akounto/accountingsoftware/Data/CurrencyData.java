
package com.akounto.accountingsoftware.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyData {

    @SerializedName("Currency")
    @Expose
    private List<Currency> currency = null;

    public List<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(List<Currency> currency) {
        this.currency = currency;
    }

}
