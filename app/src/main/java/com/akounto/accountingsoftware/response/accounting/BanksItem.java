package com.akounto.accountingsoftware.response.accounting;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BanksItem implements Serializable {
    @SerializedName("AccessToken")
    private Object accessToken;
    @SerializedName("BankAccounts")
    private BankAccounts bankAccounts;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Created")
    private String created;
    @SerializedName("Id")

    /* renamed from: id */
    private int f135id;
    @SerializedName("InstitutionId")
    private String institutionId;
    @SerializedName("InstitutionName")
    private String institutionName;
    @SerializedName("ItemId")
    private String itemId;

    public int getCompanyId() {
        return this.companyId;
    }

    public String getInstitutionName() {
        return this.institutionName;
    }

    public String getInstitutionId() {
        return this.institutionId;
    }

    public Object getAccessToken() {
        return this.accessToken;
    }

    public BankAccounts getBankAccounts() {
        return this.bankAccounts;
    }

    public int getId() {
        return this.f135id;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getCreated() {
        return this.created;
    }
}
