package com.akounto.accountingsoftware.Data;

import java.util.List;

public class ItemPriceSummary {

    private double CustExchangeRate;
    private String CompanyCurrency;
    private String CustCurrency;
    private double ItemTotal ;
    private List<TempTaxes> Taxes;
    private double TotalProduct ;
    private double ExchangeRate ;

    public double getCustExchangeRate() {
        return CustExchangeRate;
    }

    public void setCustExchangeRate(double custExchangeRate) {
        CustExchangeRate = custExchangeRate;
    }

    public String getCompanyCurrency() {
        return CompanyCurrency;
    }

    public void setCompanyCurrency(String companyCurrency) {
        CompanyCurrency = companyCurrency;
    }

    public String getCustCurrency() {
        return CustCurrency;
    }

    public void setCustCurrency(String custCurrency) {
        CustCurrency = custCurrency;
    }

    public double getItemTotal() {
        return ItemTotal;
    }

    public void setItemTotal(double itemTotal) {
        ItemTotal = itemTotal;
    }

    public List<TempTaxes> getTaxes() {
        return Taxes;
    }

    public void setTaxes(List<TempTaxes> taxes) {
        Taxes = taxes;
    }

    public double getTotalProduct() {
        return TotalProduct;
    }

    public void setTotalProduct(double totalProduct) {
        TotalProduct = totalProduct;
    }

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

}
