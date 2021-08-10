
package com.akounto.accountingsoftware.Data.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastActivity {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("TableTransactionId")
    @Expose
    private Integer tableTransactionId;
    @SerializedName("TableTransactionValue")
    @Expose
    private String tableTransactionValue;
    @SerializedName("TableEnumType")
    @Expose
    private Integer tableEnumType;
    @SerializedName("ActionType")
    @Expose
    private Integer actionType;
    @SerializedName("TransactionHeadName")
    @Expose
    private String transactionHeadName;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Created")
    @Expose
    private String created;

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

    public Integer getTableTransactionId() {
        return tableTransactionId;
    }

    public void setTableTransactionId(Integer tableTransactionId) {
        this.tableTransactionId = tableTransactionId;
    }

    public String getTableTransactionValue() {
        return tableTransactionValue;
    }

    public void setTableTransactionValue(String tableTransactionValue) {
        this.tableTransactionValue = tableTransactionValue;
    }

    public Integer getTableEnumType() {
        return tableEnumType;
    }

    public void setTableEnumType(Integer tableEnumType) {
        this.tableEnumType = tableEnumType;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getTransactionHeadName() {
        return transactionHeadName;
    }

    public void setTransactionHeadName(String transactionHeadName) {
        this.transactionHeadName = transactionHeadName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
