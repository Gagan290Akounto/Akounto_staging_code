package com.akounto.accountingsoftware.util;

import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillPriceCalculator {

    private List<PurchaseItem> items;
    double sub_totel = 0.0;
    double totel = 0.0;
    double tax_totel = 0.0;
    HashMap<String, Double> taxamount = new HashMap<String, Double>();
    List<ProductServiceTaxesItem> taxs = new ArrayList<>();
    List<ProductServiceTaxesItem> taxes = new ArrayList<>();

    public BillPriceCalculator(List<PurchaseItem> items) {
        this.items = items;
        calSubPrice();
        calTaxPrice();
        getTaxSummery();
        calTotal();
    }

    /*private void getTaxSummery() {
        if ((taxs != null)) {
            taxes = new ArrayList<>();
            Map<String, Double> counting = taxs.stream().collect(Collectors.groupingBy(ProductServiceTaxesItem::getTaxName, Collectors.summingDouble(ProductServiceTaxesItem::getAmount)));
            List<String> valueList = new ArrayList(counting.keySet());
            Map<String, List<ProductServiceTaxesItem>> txtlist = taxs.stream().collect(Collectors.groupingBy(ProductServiceTaxesItem::getTaxName));
            for (int i = 0; i < counting.size(); i++) {
                ProductServiceTaxesItem tm = txtlist.get(valueList.get(i)).get(0);
                tm.setAmount(counting.get(valueList.get(i)));
                tax_totel = tax_totel + counting.get(valueList.get(i));
                taxes.add(tm);
            }
        }
    }
    private void calSubPrice() {
        try {
            for (int i = 0; i < items.size(); i++) {
                sub_totel = sub_totel + items.get(i).getQuantity() * items.get(i).getPrice();
            }
        } catch (Exception e) {
        }
    }

    private void calTaxPrice() {
        try {
            for (int i = 0; i < items.size(); i++) {
                double amount = 0.0;
                ProductServiceTaxesItem temp;
                for (int j = 0; j < items.get(i).getProductServiceTaxes().size(); j++) {
                    amount = amount + (((items.get(i).getPrice()*(double)items.get(i).getQuantity()) / 100.0f) * items.get(i).getProductServiceTaxes().get(j).getRate());
                    temp = items.get(i).getProductServiceTaxes().get(j);
                    temp.setAmount(((items.get(i).getPrice()*(double) items.get(i).getQuantity()) / 100.0f) * items.get(i).getProductServiceTaxes().get(j).getRate());
                    taxs.add(temp);
                }
                taxamount.put(items.get(i).getName(), amount);
            }
        } catch (Exception e) {
        }
    }

    private void calTotal() {
        double temp = 0.0;
        try {
            for (int i = 0; i < taxamount.size(); i++) {
                temp = temp + taxamount.get(items.get(i).getName());
            }
        } catch (Exception e) {
        }
        totel = sub_totel + tax_totel;
    }

    public double getSub_totel() {
        return sub_totel;
    }

    public void setSub_totel(double sub_totel) {
        this.sub_totel = sub_totel;
    }

    public double getTotel() {
        return totel;
    }

    public void setTotel(double totel) {
        this.totel = totel;
    }

    public HashMap<String, Double> getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(HashMap<String, Double> taxamount) {
        this.taxamount = taxamount;
    }

    public List<ProductServiceTaxesItem> getTaxs() {
        return taxs;
    }

    public void setTaxs(List<ProductServiceTaxesItem> taxs) {
        this.taxs = taxs;
    }

    public List<ProductServiceTaxesItem> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ProductServiceTaxesItem> taxes) {
        this.taxes = taxes;
    }*/
    private void getTaxSummery() {
        if ((taxs != null)) {
            taxes = new ArrayList<>();
            Map<String, Double> counting = taxs.stream().collect(Collectors.groupingBy(ProductServiceTaxesItem::getTaxName, Collectors.summingDouble(ProductServiceTaxesItem::getAmount)));
            List<String> valueList = new ArrayList(counting.keySet());
            Map<String, List<ProductServiceTaxesItem>> txtlist = taxs.stream().collect(Collectors.groupingBy(ProductServiceTaxesItem::getTaxName));
            for (int i = 0; i < counting.size(); i++) {
                try {
                    ProductServiceTaxesItem tm = txtlist.get(valueList.get(i)).get(0);
                    tm.setAmount(counting.get(valueList.get(i)));
                    tax_totel = tax_totel + counting.get(valueList.get(i));
                    taxes.add(tm);
                } catch (Exception e) {
                }
            }
        }
    }

    private void calSubPrice() {
        try {
            for (int i = 0; i < items.size(); i++) {
                sub_totel = sub_totel + items.get(i).getQuantity() * items.get(i).getPrice();
            }
        } catch (Exception e) {
        }
    }

    private void calTaxPrice() {
        try {
            for (int i = 0; i < items.size(); i++) {
                double amount = 0.0;
                ProductServiceTaxesItem temp;
                if (items.get(i).getProductServiceTaxes() != null) {
                    for (int j = 0; j < items.get(i).getProductServiceTaxes().size(); j++) {
                        try {
                            amount = amount + (((items.get(i).getPrice() * items.get(i).getQuantity()) / 100.0f) * items.get(i).getProductServiceTaxes().get(j).getRate());
                            temp = items.get(i).getProductServiceTaxes().get(j);
                            temp.setAmount(((items.get(i).getPrice() * items.get(i).getQuantity()) / 100.0f) * items.get(i).getProductServiceTaxes().get(j).getRate());
                            taxs.add(temp);
                        } catch (Exception e) {
                        }
                    }
                    taxamount.put(items.get(i).getName(), amount);
                }
            }
        } catch (Exception e) {
        }
    }

    private void calTotal() {
        double temp = 0.0;
        try {
            for (int i = 0; i < taxamount.size(); i++) {
                temp = temp + taxamount.get(items.get(i).getName());
            }
        } catch (Exception e) {
        }
        totel = sub_totel + tax_totel;
    }

    public double getSub_totel() {
        return sub_totel;
    }

    public void setSub_totel(double sub_totel) {
        this.sub_totel = sub_totel;
    }

    public double getTotel() {
        return totel;
    }

    public void setTotel(double totel) {
        this.totel = totel;
    }

    public HashMap<String, Double> getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(HashMap<String, Double> taxamount) {
        this.taxamount = taxamount;
    }

    public List<ProductServiceTaxesItem> getTaxs() {
        return taxs;
    }

    public void setTaxs(List<ProductServiceTaxesItem> taxs) {
        this.taxs = taxs;
    }

    public List<ProductServiceTaxesItem> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ProductServiceTaxesItem> taxes) {
        this.taxes = taxes;
    }
}
