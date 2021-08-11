
package com.akounto.accountingsoftware.Data.Dashboard;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.response.ProfitLossItem;

public class Data {

    @SerializedName("CreationTime")
    @Expose
    private String creationTime;
    @SerializedName("CashFlow")
    @Expose
    private List<CashFlow> cashFlow = null;
    @SerializedName("ProfitLoss")
    @Expose
    private ProfitLossItem profitLoss;
    @SerializedName("InvoicePurchaseOverdues")
    @Expose
    private List<InvoicePurchaseOverdue> invoicePurchaseOverdues = null;
    @SerializedName("LastBankTransactions")
    @Expose
    private List<LastBankTransaction> lastBankTransactions = null;
    @SerializedName("LastActivities")
    @Expose
    private List<LastActivity> lastActivities = null;
    @SerializedName("BankBalance")
    @Expose
    private List<BankBalance> bankBalance = null;
    @SerializedName("ExpenseBreakdown")
    @Expose
    private List<ExpenseBreakdown> expenseBreakdown = null;
    @SerializedName("ProjectedProfits")
    @Expose
    private List<ProjectedProfit> projectedProfits = null;

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public List<CashFlow> getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(List<CashFlow> cashFlow) {
        this.cashFlow = cashFlow;
    }

    public ProfitLossItem getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(ProfitLossItem profitLoss) {
        this.profitLoss = profitLoss;
    }

    public List<InvoicePurchaseOverdue> getInvoicePurchaseOverdues() {
        return invoicePurchaseOverdues;
    }

    public void setInvoicePurchaseOverdues(List<InvoicePurchaseOverdue> invoicePurchaseOverdues) {
        this.invoicePurchaseOverdues = invoicePurchaseOverdues;
    }

    public List<LastBankTransaction> getLastBankTransactions() {
        return lastBankTransactions;
    }

    public void setLastBankTransactions(List<LastBankTransaction> lastBankTransactions) {
        this.lastBankTransactions = lastBankTransactions;
    }

    public List<LastActivity> getLastActivities() {
        return lastActivities;
    }

    public void setLastActivities(List<LastActivity> lastActivities) {
        this.lastActivities = lastActivities;
    }

    public List<BankBalance> getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(List<BankBalance> bankBalance) {
        this.bankBalance = bankBalance;
    }

    public List<ExpenseBreakdown> getExpenseBreakdown() {
        return expenseBreakdown;
    }

    public void setExpenseBreakdown(List<ExpenseBreakdown> expenseBreakdown) {
        this.expenseBreakdown = expenseBreakdown;
    }

    public List<ProjectedProfit> getProjectedProfits() {
        return projectedProfits;
    }

    public void setProjectedProfits(List<ProjectedProfit> projectedProfits) {
        this.projectedProfits = projectedProfits;
    }

}
