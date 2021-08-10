package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InvoiceTransactionItem {
    @SerializedName("Description")
    private String description;
    @SerializedName("Price")
    private int price;
    @SerializedName("Id")
    private String Id;
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductTransactionHeadId")
    private int productTransactionHeadId;
    @SerializedName("Quantity")
    private int quantity;
    @SerializedName("Taxes")
    private final List<RecurringInvoiceSalesTaxItem> taxes;

    public void setDescription(String description2) {
        this.description = description2;
    }

    public void setProductName(String productName2) {
        this.productName = productName2;
    }

    public void setPrice(int price2) {
        this.price = price2;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public void setProductId(int productId2) {
        this.productId = productId2;
    }

    public void setProductTransactionHeadId(int productTransactionHeadId2) {
        this.productTransactionHeadId = productTransactionHeadId2;
    }
    public InvoiceTransactionItem(String description2, String productName2, List<RecurringInvoiceSalesTaxItem> taxes2, int price2, int quantity2, int productId2, int productTransactionHeadId2) {
        this.description = description2;
        this.productName = productName2;
        this.taxes = taxes2;
        this.price = price2;
        this.quantity = quantity2;
        this.productId = productId2;
        this.productTransactionHeadId = productTransactionHeadId2;
    }
    public InvoiceTransactionItem(String Id,String description2, String productName2, List<RecurringInvoiceSalesTaxItem> taxes2, int price2, int quantity2, int productId2, int productTransactionHeadId2) {
        this.Id=Id;
        this.description = description2;
        this.productName = productName2;
        this.taxes = taxes2;
        this.price = price2;
        this.quantity = quantity2;
        this.productId = productId2;
        this.productTransactionHeadId = productTransactionHeadId2;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
