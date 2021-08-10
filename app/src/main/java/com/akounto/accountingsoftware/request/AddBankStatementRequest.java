package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AddBankStatementRequest {
    @SerializedName("BankAccountId")
    private int bankAccountId;
    @SerializedName("CSVBankTransaction")
    private List<CSVBankTransactionItem> cSVBankTransaction;
    @SerializedName("OpeningBalance")
    private int openingBalance;

    public void setOpeningBalance(int openingBalance2) {
        this.openingBalance = openingBalance2;
    }

    public int getOpeningBalance() {
        return this.openingBalance;
    }

    public void setBankAccountId(int bankAccountId2) {
        this.bankAccountId = bankAccountId2;
    }

    public int getBankAccountId() {
        return this.bankAccountId;
    }

    public void setCSVBankTransaction(List<CSVBankTransactionItem> cSVBankTransaction2) {
        this.cSVBankTransaction = cSVBankTransaction2;
    }

    public List<CSVBankTransactionItem> getCSVBankTransaction() {
        return this.cSVBankTransaction;
    }

    public AddBankStatementRequest(int openingBalance2, int bankAccountId2, List<CSVBankTransactionItem> cSVBankTransaction2) {
        this.openingBalance = openingBalance2;
        this.bankAccountId = bankAccountId2;
        this.cSVBankTransaction = cSVBankTransaction2;
    }
}
