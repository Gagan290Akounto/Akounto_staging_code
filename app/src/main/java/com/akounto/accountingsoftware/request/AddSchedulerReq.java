package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.SerializedName;

public class AddSchedulerReq {
    @SerializedName("Aggrement")
    private String Aggrement;
    @SerializedName("AggrementAmount")
    private Double AggrementAmount;
    @SerializedName("Description")
    private String Description;
    @SerializedName("EveryDays")
    private int EveryDays;
    @SerializedName("InvoiceAmount")
    private Double InvoiceAmount;
    @SerializedName("InvoiceEndOn")
    private String InvoiceEndOn;
    @SerializedName("InvoiceFirstOn")
    private String InvoiceFirstOn;
    @SerializedName("InvoiceId")
    private int InvoiceId;
    @SerializedName("IsInvoiceSendAuto")
    private Boolean IsInvoiceSendAuto;
    @SerializedName("MonthDay")
    private int MonthDay;
    @SerializedName("NoInvoice")
    private int NoInvoice;
    @SerializedName("NoInvoiceCreated")
    private int NoInvoiceCreated;
    @SerializedName("ScheduleEndType")
    private int ScheduleEndType;
    @SerializedName("ScheduleType")
    private int ScheduleType;
    @SerializedName("TimeZone")
    private String TimeZone;
    @SerializedName("TotalInvoiceAmountCreated")
    private int TotalInvoiceAmountCreated;
    @SerializedName("WeekDay")
    private int WeekDay;
    @SerializedName("YearMonth")
    private int YearMonth;

    public int getInvoiceId() {
        return this.InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.InvoiceId = invoiceId;
    }

    public int getScheduleType() {
        return this.ScheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.ScheduleType = scheduleType;
    }

    public int getWeekDay() {
        return this.WeekDay;
    }

    public void setWeekDay(int weekDay) {
        this.WeekDay = weekDay;
    }

    public int getMonthDay() {
        return this.MonthDay;
    }

    public void setMonthDay(int monthDay) {
        this.MonthDay = monthDay;
    }

    public int getYearMonth() {
        return this.YearMonth;
    }

    public void setYearMonth(int yearMonth) {
        this.YearMonth = yearMonth;
    }

    public int getEveryDays() {
        return this.EveryDays;
    }

    public void setEveryDays(int everyDays) {
        this.EveryDays = everyDays;
    }

    public String getInvoiceFirstOn() {
        return this.InvoiceFirstOn;
    }

    public void setInvoiceFirstOn(String invoiceFirstOn) {
        this.InvoiceFirstOn = invoiceFirstOn;
    }

    public int getScheduleEndType() {
        return this.ScheduleEndType;
    }

    public void setScheduleEndType(int scheduleEndType) {
        this.ScheduleEndType = scheduleEndType;
    }

    public int getNoInvoice() {
        return this.NoInvoice;
    }

    public void setNoInvoice(int noInvoice) {
        this.NoInvoice = noInvoice;
    }

    public String getInvoiceEndOn() {
        return this.InvoiceEndOn;
    }

    public void setInvoiceEndOn(String invoiceEndOn) {
        this.InvoiceEndOn = invoiceEndOn;
    }

    public String getTimeZone() {
        return this.TimeZone;
    }

    public void setTimeZone(String timeZone) {
        this.TimeZone = timeZone;
    }

    public Boolean getInvoiceSendAuto() {
        return this.IsInvoiceSendAuto;
    }

    public void setInvoiceSendAuto(Boolean invoiceSendAuto) {
        this.IsInvoiceSendAuto = invoiceSendAuto;
    }

    public Double getInvoiceAmount() {
        return this.InvoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.InvoiceAmount = invoiceAmount;
    }

    public Double getAggrementAmount() {
        return this.AggrementAmount;
    }

    public void setAggrementAmount(Double aggrementAmount) {
        this.AggrementAmount = aggrementAmount;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getAggrement() {
        return this.Aggrement;
    }

    public void setAggrement(String aggrement) {
        this.Aggrement = aggrement;
    }

    public int getNoInvoiceCreated() {
        return this.NoInvoiceCreated;
    }

    public void setNoInvoiceCreated(int noInvoiceCreated) {
        this.NoInvoiceCreated = noInvoiceCreated;
    }

    public int getTotalInvoiceAmountCreated() {
        return this.TotalInvoiceAmountCreated;
    }

    public void setTotalInvoiceAmountCreated(int totalInvoiceAmountCreated) {
        this.TotalInvoiceAmountCreated = totalInvoiceAmountCreated;
    }
}
