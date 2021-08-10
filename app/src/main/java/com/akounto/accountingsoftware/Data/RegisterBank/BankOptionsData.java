
package com.akounto.accountingsoftware.Data.RegisterBank;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankOptionsData {

    @SerializedName("BankId")
    @Expose
    private Integer bankId;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("Accounts")
    @Expose
    private List<Account> accounts = null;

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}
