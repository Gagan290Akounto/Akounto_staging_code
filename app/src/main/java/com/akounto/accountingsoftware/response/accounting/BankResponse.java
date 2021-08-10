package com.akounto.accountingsoftware.response.accounting;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BankResponse {
    @SerializedName("Banks")
    private List<BanksItem> banks;
    @SerializedName("DefaultTransactionHead")
    private DefaultTransactionHead defaultTransactionHead;

    public DefaultTransactionHead getDefaultTransactionHead() {
        return this.defaultTransactionHead;
    }

    public List<BanksItem> getBanks() {
        return this.banks;
    }
}
