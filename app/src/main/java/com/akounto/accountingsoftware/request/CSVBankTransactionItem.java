package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class CSVBankTransactionItem {
    @SerializedName("Date")
    private String date;
    @SerializedName("Deposits")
    private int deposits;
    @SerializedName("Description")
    private String description;
    @SerializedName("IsValid")
    private boolean isValid;
    @SerializedName("Withdrawals")
    private int withdrawals;

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDeposits(int deposits2) {
        this.deposits = deposits2;
    }

    public int getDeposits() {
        return this.deposits;
    }

    public void setWithdrawals(int withdrawals2) {
        this.withdrawals = withdrawals2;
    }

    public int getWithdrawals() {
        return this.withdrawals;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public String getDate() {
        return this.date;
    }

    public void setIsValid(boolean isValid2) {
        this.isValid = isValid2;
    }

    public boolean isIsValid() {
        return this.isValid;
    }

    public CSVBankTransactionItem(String description2, int deposits2, int withdrawals2, String date2, boolean isValid2) {
        this.description = description2;
        this.deposits = deposits2;
        this.withdrawals = withdrawals2;
        this.date = date2;
        this.isValid = isValid2;
    }
}
