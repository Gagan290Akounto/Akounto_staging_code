
package com.akounto.accountingsoftware.Data.RegisterBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bank {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("AccessToken")
    @Expose
    private Object accessToken;
    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("InstitutionId")
    @Expose
    private String institutionId;
    @SerializedName("InstitutionName")
    @Expose
    private String institutionName;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("BankAccounts")
    @Expose
    private BankAccounts bankAccounts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Object accessToken) {
        this.accessToken = accessToken;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public BankAccounts getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(BankAccounts bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

}
