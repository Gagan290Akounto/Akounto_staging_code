package com.akounto.accountingsoftware.response.accounting;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class BankAccounts implements Serializable {
    @SerializedName("AccountId")
    private String accountId;
    @SerializedName("AvailableBalance")
    private double availableBalance;
    @SerializedName("BankId")
    private int bankId;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Created")
    private String created;
    @SerializedName("Currency")
    private String currency;
    @SerializedName("CurrentBalance")
    private double currentBalance;
    @SerializedName("HeadTransactionId")
    private int headTransactionId;
    @SerializedName("Id")

    /* renamed from: id */
    private int f134id;
    @SerializedName("IsAutoImport")
    private boolean isAutoImport;
    @SerializedName("IsFirtUpload")
    private boolean isFirtUpload;
    @SerializedName("LastTransactionFetchedOn")
    private String lastTransactionFetchedOn;
    @SerializedName("Mask")
    private String mask;
    @SerializedName("Name")
    private String name;
    @SerializedName("SubType")
    private String subType;
    @SerializedName("Transactions")
    private Object transactions;
    @SerializedName("Type")
    private int type;

    public String getAccountId() {
        return this.accountId;
    }

    public String getSubType() {
        return this.subType;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public String getMask() {
        return this.mask;
    }

    public double getCurrentBalance() {
        return this.currentBalance;
    }

    public String getLastTransactionFetchedOn() {
        return this.lastTransactionFetchedOn;
    }

    public boolean isIsAutoImport() {
        return this.isAutoImport;
    }

    public String getName() {
        return this.name;
    }

    public String getCreated() {
        return this.created;
    }

    public Object getTransactions() {
        return this.transactions;
    }

    public int getType() {
        return this.type;
    }

    public double getAvailableBalance() {
        return this.availableBalance;
    }

    public int getHeadTransactionId() {
        return this.headTransactionId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public int getId() {
        return this.f134id;
    }

    public boolean isIsFirtUpload() {
        return this.isFirtUpload;
    }

    public int getBankId() {
        return this.bankId;
    }
}
