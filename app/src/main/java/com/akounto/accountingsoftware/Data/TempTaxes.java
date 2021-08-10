package com.akounto.accountingsoftware.Data;

public class TempTaxes {

    public double Rate;

    public int TransactionHeadTaxId;

    public String Name;

    public double Amount;

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public int getTransactionHeadTaxId() {
        return TransactionHeadTaxId;
    }

    public void setTransactionHeadTaxId(int transactionHeadTaxId) {
        TransactionHeadTaxId = transactionHeadTaxId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
