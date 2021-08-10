package com.akounto.accountingsoftware.util;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.akounto.accountingsoftware.Data.ItemPriceSummary;
import com.akounto.accountingsoftware.Data.TempTaxes;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SellPurchaseBusiness {

    private ItemPriceSummary priceSummary = null;

    public SellPurchaseBusiness() {
        this.priceSummary = new ItemPriceSummary();
    }

    public void setInvoice(InvoiceDetails _collection, String _companyCurrency) {
        List<TempTaxes> combindTaxes = new ArrayList<>();
        TempTaxes tempTaxes = null;
        if ((_collection.getInvoiceTransaction() != null)) {
            for (int i = 0; (i<= (_collection.getInvoiceTransaction().size() - 1)); i++) {
                this.priceSummary.setItemTotal((this.priceSummary.getItemTotal() + (_collection.getInvoiceTransaction().get(i).getPrice() * _collection.getInvoiceTransaction().get(i).getQuantity())));
                if (((_collection.getInvoiceTransaction().get(i).getTaxes() != null)
                        && (_collection.getInvoiceTransaction().get(i).getTaxes().size() > 0))) {
                    for (int t = 0; (t <= (_collection.getInvoiceTransaction().get(i).getTaxes().size() - 1)); t++) {
                        tempTaxes = new TempTaxes();
                        tempTaxes.Amount = ((_collection.getInvoiceTransaction().get(i).getPrice() * _collection.getInvoiceTransaction().get(i).getQuantity())
                                * (_collection.getInvoiceTransaction().get(i).getTaxes().get(t).getRate() / 100));
                        tempTaxes.Name = _collection.getInvoiceTransaction().get(i).getTaxes().get(t).getName();
                        tempTaxes.TransactionHeadTaxId = _collection.getInvoiceTransaction().get(i).getTaxes().get(t).getHeadTransactionTexId();
                        tempTaxes.Rate = _collection.getInvoiceTransaction().get(i).getTaxes().get(t).getRate();
                        combindTaxes.add(tempTaxes);
                    }
                }
            }
            this.priceSummary.setTaxes(combindTaxes);
            this.priceSummary.setCustExchangeRate(_collection.getCustExchangeRate());
            this.priceSummary.setCompanyCurrency(_companyCurrency);
            this.priceSummary.setCustCurrency(_collection.getCustCurrency());
            this.priceSummary.setTotalProduct(_collection.getInvoiceTransaction().size());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<TempTaxes> getTaxSummery() {
        List<TempTaxes> taxes = null;
        if ((priceSummary.getTaxes() != null)) {
            taxes = new ArrayList<>();
            Map<String, Double> counting = priceSummary.getTaxes().stream().collect(Collectors.groupingBy(TempTaxes::getName, Collectors.summingDouble(TempTaxes::getAmount)));
            List<String> valueList = new ArrayList(counting.keySet());
            Map<String, List<TempTaxes>> txtlist = priceSummary.getTaxes().stream().collect(Collectors.groupingBy(TempTaxes::getName));
            for (int i = 0; i < counting.size(); i++) {
                TempTaxes tm = txtlist.get(valueList.get(i)).get(0);
                tm.setAmount(counting.get(valueList.get(i)));
                taxes.add(tm);
            }
        }
        return taxes;
    }

    public double getTotal() {
        return this.priceSummary.getItemTotal();
    }

    public  double getCustomerInvoiceTotal() {
        double total = this.priceSummary.getItemTotal();
        if ((this.priceSummary.getTaxes() != null)) {
            for (int i = 0; i < this.priceSummary.getTaxes().size(); i++) {
                total = total + this.priceSummary.getTaxes().get(i).getAmount();
            }
        }
        return total;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public final String getRatiofCurreny() {
        String response = "";
        double total = this.priceSummary.getItemTotal();

        if (this.priceSummary.getTaxes() != null) {
            if ((this.priceSummary.getTaxes() != null)) {
                Map<String, Double> counting = this.priceSummary.getTaxes().stream().collect(Collectors.groupingBy(TempTaxes::getName, Collectors.summingDouble(TempTaxes::getAmount)));
            }
        }
        total = (total * this.priceSummary.getCustExchangeRate());
        response = String.format("Currency conversion:{0} ({1}) at {2}", Math.round(total), this.priceSummary.getCustCurrency(), Math.round(priceSummary.getCustExchangeRate()));
        return response;
    }
/*    public GetBillRateofCurreny() {
        String total = this.response.ItemTotal;
        this.response.Taxes.forEach(function (obj) {
            total += obj.Amount;
        });
        total = (total *  this.response.ExchangeRate);
        return total;

    }*/
    public final boolean IsShowConversion() {
        boolean response = false;
        if ((!isNullOrBlank(this.priceSummary.getCustCurrency())
                && (!isNullOrBlank(this.priceSummary.getCompanyCurrency())
                && (!this.priceSummary.getCustCurrency().equals(this.priceSummary.getCompanyCurrency())
                && ((this.priceSummary.getTotalProduct() > 0)
                && (this.priceSummary.getItemTotal() != 0)))))) {
            response = true;
        }

        return response;
    }

    public static boolean isNullOrBlank(String param) {
        return param == null || param.trim().length() == 0;
    }

    public ItemPriceSummary getPriceSummary() {
        return priceSummary;
    }
}
