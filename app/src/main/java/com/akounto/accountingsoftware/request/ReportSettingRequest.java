
package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportSettingRequest {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("IsLogoDisplay")
    @Expose
    private Boolean isLogoDisplay;
    @SerializedName("InvoiceLayout")
    @Expose
    private Integer invoiceLayout;
    @SerializedName("PaymentTermType")
    @Expose
    private Integer paymentTermType;
    @SerializedName("DefaultTitle")
    @Expose
    private String defaultTitle;
    @SerializedName("AcentColor")
    @Expose
    private String acentColor;
    @SerializedName("ItemsType")
    @Expose
    private Integer itemsType;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("UnitsType")
    @Expose
    private Integer unitsType;
    @SerializedName("UnitName")
    @Expose
    private String unitName;
    @SerializedName("PriceType")
    @Expose
    private Integer priceType;
    @SerializedName("PriceName")
    @Expose
    private String priceName;
    @SerializedName("LogoURL")
    @Expose
    private String logoURL;
    @SerializedName("LogoData")
    @Expose
    private Object logoData;
    @SerializedName("InvoiceInitialNo")
    @Expose
    private Integer invoiceInitialNo;
    @SerializedName("InvoiceNoPrefix")
    @Expose
    private String invoiceNoPrefix;
    @SerializedName("InvoiceNoSuffix")
    @Expose
    private String invoiceNoSuffix;

    public ReportSettingRequest(Integer id, Integer companyId, Boolean isLogoDisplay, Integer invoiceLayout, Integer paymentTermType, String defaultTitle, String acentColor, Integer itemsType, String itemName, Integer unitsType, String unitName, Integer priceType, String priceName, String logoURL, Object logoData, Integer invoiceInitialNo, String invoiceNoPrefix, String invoiceNoSuffix) {
        this.id = id;
        this.companyId = companyId;
        this.isLogoDisplay = isLogoDisplay;
        this.invoiceLayout = invoiceLayout;
        this.paymentTermType = paymentTermType;
        this.defaultTitle = defaultTitle;
        this.acentColor = acentColor;
        this.itemsType = itemsType;
        this.itemName = itemName;
        this.unitsType = unitsType;
        this.unitName = unitName;
        this.priceType = priceType;
        this.priceName = priceName;
        this.logoURL = logoURL;
        this.logoData = logoData;
        this.invoiceInitialNo = invoiceInitialNo;
        this.invoiceNoPrefix = invoiceNoPrefix;
        this.invoiceNoSuffix = invoiceNoSuffix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getLogoDisplay() {
        return isLogoDisplay;
    }

    public void setLogoDisplay(Boolean logoDisplay) {
        isLogoDisplay = logoDisplay;
    }

    public Integer getInvoiceLayout() {
        return invoiceLayout;
    }

    public void setInvoiceLayout(Integer invoiceLayout) {
        this.invoiceLayout = invoiceLayout;
    }

    public Integer getPaymentTermType() {
        return paymentTermType;
    }

    public void setPaymentTermType(Integer paymentTermType) {
        this.paymentTermType = paymentTermType;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public String getAcentColor() {
        return acentColor;
    }

    public void setAcentColor(String acentColor) {
        this.acentColor = acentColor;
    }

    public Integer getItemsType() {
        return itemsType;
    }

    public void setItemsType(Integer itemsType) {
        this.itemsType = itemsType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getUnitsType() {
        return unitsType;
    }

    public void setUnitsType(Integer unitsType) {
        this.unitsType = unitsType;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public Object getLogoData() {
        return logoData;
    }

    public void setLogoData(Object logoData) {
        this.logoData = logoData;
    }

    public Integer getInvoiceInitialNo() {
        return invoiceInitialNo;
    }

    public void setInvoiceInitialNo(Integer invoiceInitialNo) {
        this.invoiceInitialNo = invoiceInitialNo;
    }

    public String getInvoiceNoPrefix() {
        return invoiceNoPrefix;
    }

    public void setInvoiceNoPrefix(String invoiceNoPrefix) {
        this.invoiceNoPrefix = invoiceNoPrefix;
    }

    public String getInvoiceNoSuffix() {
        return invoiceNoSuffix;
    }

    public void setInvoiceNoSuffix(String invoiceNoSuffix) {
        this.invoiceNoSuffix = invoiceNoSuffix;
    }
}
