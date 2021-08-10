package com.akounto.accountingsoftware.model;

public class TaxSummaryList {

    /* renamed from: id */
    int f95id;
    String taxName;
    double taxValue;

    public TaxSummaryList(String taxName2, double taxValue2, int id) {
        this.taxName = taxName2;
        this.taxValue = taxValue2;
        this.f95id = id;
    }

    public int getId() {
        return this.f95id;
    }

    public void setId(int id) {
        this.f95id = id;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public void setTaxName(String taxName2) {
        this.taxName = taxName2;
    }

    public double getTaxValue() {
        return this.taxValue;
    }

    public void setTaxValue(double taxValue2) {
        this.taxValue = taxValue2;
    }

    public TaxSummaryList(String taxName2, double taxValue2) {
        this.taxName = taxName2;
        this.taxValue = taxValue2;
    }
}
