package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardData {
    @SerializedName("BankBalance")
    private List<BankBalanceItem> bankBalance;
    @SerializedName("CashFlow")
    private List<CashFlowItem> cashFlow;
    @SerializedName("CreationTime")
    private String creationTime;
    @SerializedName("ExpenseBreakdown")
    private List<ExpenseBreakdownItem> expenseBreakdown;
    @SerializedName("InvoicePurchaseOverdues")
    private List<InvoicePurchaseOverduesItem> invoicePurchaseOverdues;
    @SerializedName("LastActivities")
    private List<LastActivitiesItem> lastActivities;
    @SerializedName("LastBankTransactions")
    private List<LastBankTransactionsItem> lastBankTransactions;
    @SerializedName("ProfitLoss")
    private List<ProfitLossItem> profitLoss;

    public List<InvoicePurchaseOverduesItem> getInvoicePurchaseOverdues() {
        return this.invoicePurchaseOverdues;
    }

    public List<BankBalanceItem> getBankBalance() {
        return this.bankBalance;
    }

    public List<ExpenseBreakdownItem> getExpenseBreakdown() {
        return this.expenseBreakdown;
    }

    public List<CashFlowItem> getCashFlow() {
        return this.cashFlow;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public List<LastBankTransactionsItem> getLastBankTransactions() {
        return this.lastBankTransactions;
    }

    public List<ProfitLossItem> getProfitLoss() {
        return this.profitLoss;
    }

    public List<LastActivitiesItem> getLastActivities() {
        return this.lastActivities;
    }
}
