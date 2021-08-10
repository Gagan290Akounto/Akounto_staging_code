
package com.akounto.accountingsoftware.response.ReportSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

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
    @SerializedName("EstimateTitle")
    @Expose
    private String estimateTitle;
    @SerializedName("SubHeading")
    @Expose
    private String subHeading;
    @SerializedName("DefaultFooter")
    @Expose
    private Object defaultFooter;
    @SerializedName("DefaultNotes")
    @Expose
    private Object defaultNotes;
    @SerializedName("FontSize")
    @Expose
    private String fontSize;
    @SerializedName("FontFamily")
    @Expose
    private String fontFamily;
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
    @SerializedName("AmountType")
    @Expose
    private Integer amountType;
    @SerializedName("AmountName")
    @Expose
    private String amountName;
    @SerializedName("IsItemShow")
    @Expose
    private Boolean isItemShow;
    @SerializedName("IsDescriptionShow")
    @Expose
    private Boolean isDescriptionShow;
    @SerializedName("IsUnitShow")
    @Expose
    private Boolean isUnitShow;
    @SerializedName("IsPriceShow")
    @Expose
    private Boolean isPriceShow;
    @SerializedName("IsAmountShow")
    @Expose
    private Boolean isAmountShow;
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

    public Boolean getIsLogoDisplay() {
        return isLogoDisplay;
    }

    public void setIsLogoDisplay(Boolean isLogoDisplay) {
        this.isLogoDisplay = isLogoDisplay;
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

    public String getEstimateTitle() {
        return estimateTitle;
    }

    public void setEstimateTitle(String estimateTitle) {
        this.estimateTitle = estimateTitle;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public Object getDefaultFooter() {
        return defaultFooter;
    }

    public void setDefaultFooter(Object defaultFooter) {
        this.defaultFooter = defaultFooter;
    }

    public Object getDefaultNotes() {
        return defaultNotes;
    }

    public void setDefaultNotes(Object defaultNotes) {
        this.defaultNotes = defaultNotes;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
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

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public String getAmountName() {
        return amountName;
    }

    public void setAmountName(String amountName) {
        this.amountName = amountName;
    }

    public Boolean getIsItemShow() {
        return isItemShow;
    }

    public void setIsItemShow(Boolean isItemShow) {
        this.isItemShow = isItemShow;
    }

    public Boolean getIsDescriptionShow() {
        return isDescriptionShow;
    }

    public void setIsDescriptionShow(Boolean isDescriptionShow) {
        this.isDescriptionShow = isDescriptionShow;
    }

    public Boolean getIsUnitShow() {
        return isUnitShow;
    }

    public void setIsUnitShow(Boolean isUnitShow) {
        this.isUnitShow = isUnitShow;
    }

    public Boolean getIsPriceShow() {
        return isPriceShow;
    }

    public void setIsPriceShow(Boolean isPriceShow) {
        this.isPriceShow = isPriceShow;
    }

    public Boolean getIsAmountShow() {
        return isAmountShow;
    }

    public void setIsAmountShow(Boolean isAmountShow) {
        this.isAmountShow = isAmountShow;
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
