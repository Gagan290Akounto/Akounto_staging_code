package com.akounto.accountingsoftware.response.saletax;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SaleTaxResponseData {
    @SerializedName("SalesTaxTransactions")
    private List<SalesTaxTransaction> SalesTaxTransactions;
}
