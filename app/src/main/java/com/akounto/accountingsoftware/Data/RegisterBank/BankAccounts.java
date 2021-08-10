
package com.akounto.accountingsoftware.Data.RegisterBank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankAccounts {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("BankId")
    @Expose
    private Integer bankId;
    @SerializedName("HeadTransactionId")
    @Expose
    private Integer headTransactionId;
    @SerializedName("AccountId")
    @Expose
    private String accountId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Mask")
    @Expose
    private String mask;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("SubType")
    @Expose
    private Object subType;
    @SerializedName("CurrentBalance")
    @Expose
    private Double currentBalance;
    @SerializedName("AvailableBalance")
    @Expose
    private Double availableBalance;
    @SerializedName("Currency")
    @Expose
    private String currency;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("IsAutoImport")
    @Expose
    private Boolean isAutoImport;
    @SerializedName("LastTransactionFetchedOn")
    @Expose
    private String lastTransactionFetchedOn;
    @SerializedName("Transactions")
    @Expose
    private Object transactions;
    @SerializedName("IsFirtUpload")
    @Expose
    private Boolean isFirtUpload;

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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getHeadTransactionId() {
        return headTransactionId;
    }

    public void setHeadTransactionId(Integer headTransactionId) {
        this.headTransactionId = headTransactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getSubType() {
        return subType;
    }

    public void setSubType(Object subType) {
        this.subType = subType;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getIsAutoImport() {
        return isAutoImport;
    }

    public void setIsAutoImport(Boolean isAutoImport) {
        this.isAutoImport = isAutoImport;
    }

    public String getLastTransactionFetchedOn() {
        return lastTransactionFetchedOn;
    }

    public void setLastTransactionFetchedOn(String lastTransactionFetchedOn) {
        this.lastTransactionFetchedOn = lastTransactionFetchedOn;
    }

    public Object getTransactions() {
        return transactions;
    }

    public void setTransactions(Object transactions) {
        this.transactions = transactions;
    }

    public Boolean getIsFirtUpload() {
        return isFirtUpload;
    }

    public void setIsFirtUpload(Boolean isFirtUpload) {
        this.isFirtUpload = isFirtUpload;
    }

}
