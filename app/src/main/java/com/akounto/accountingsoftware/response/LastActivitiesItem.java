package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class LastActivitiesItem {
    @SerializedName("ActionType")
    private int actionType;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Created")
    private String created;
    @SerializedName("Id")

    /* renamed from: id */
    private int f119id;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("TableEnumType")
    private int tableEnumType;
    @SerializedName("TableTransactionId")
    private int tableTransactionId;
    @SerializedName("TableTransactionValue")
    private String tableTransactionValue;
    @SerializedName("TransactionHeadName")
    private String transactionHeadName;
    @SerializedName("UserName")
    private String userName;

    public int getTableTransactionId() {
        return this.tableTransactionId;
    }

    public String getTableTransactionValue() {
        return this.tableTransactionValue;
    }

    public int getActionType() {
        return this.actionType;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public String getTransactionHeadName() {
        return this.transactionHeadName;
    }

    public int getTableEnumType() {
        return this.tableEnumType;
    }

    public int getId() {
        return this.f119id;
    }

    public String getCreated() {
        return this.created;
    }
}
