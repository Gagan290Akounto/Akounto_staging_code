package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Schedule implements Serializable {
    @SerializedName("Aggrement")
    private String aggrement;
    @SerializedName("AggrementAmount")
    private double aggrementAmount;
    @SerializedName("BaseUtcOffset")
    private int baseUtcOffset;
    @SerializedName("CompanyId")
    private int companyId;
    @SerializedName("Description")
    private String description;
    @SerializedName("EveryDays")
    private int everyDays;
    @SerializedName("InvoiceAmount")
    private double invoiceAmount;
    @SerializedName("InvoiceEndOn")
    private Object invoiceEndOn;
    @SerializedName("InvoiceFirstOn")
    private String invoiceFirstOn;
    @SerializedName("InvoiceId")
    private int invoiceId;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("IsInvoiceSendAuto")
    private boolean isInvoiceSendAuto;
    @SerializedName("MonthDay")
    private int monthDay;
    @SerializedName("NoInvoice")
    private int noInvoice;
    @SerializedName("NoInvoiceCreated")
    private int noInvoiceCreated;
    @SerializedName("ScheduleEndType")
    private int scheduleEndType;
    @SerializedName("ScheduleType")
    private int scheduleType;
    @SerializedName("TimeZone")
    private String timeZone;
    @SerializedName("TotalInvoiceAmountCreated")
    private double totalInvoiceAmountCreated;
    @SerializedName("WeekDay")
    private int weekDay;
    @SerializedName("YearMonth")
    private int yearMonth;

    public int getScheduleType() {
        return this.scheduleType;
    }

    public String getInvoiceFirstOn() {
        return this.invoiceFirstOn;
    }

    public String getDescription() {
        return this.description;
    }

    public int getNoInvoiceCreated() {
        return this.noInvoiceCreated;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public int getNoInvoice() {
        return this.noInvoice;
    }

    public int getMonthDay() {
        return this.monthDay;
    }

    public double getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public int getInvoiceId() {
        return this.invoiceId;
    }

    public int getWeekDay() {
        return this.weekDay;
    }

    public int getEveryDays() {
        return this.everyDays;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public double getAggrementAmount() {
        return this.aggrementAmount;
    }

    public int getYearMonth() {
        return this.yearMonth;
    }

    public int getBaseUtcOffset() {
        return this.baseUtcOffset;
    }

    public String getAggrement() {
        return this.aggrement;
    }

    public boolean isIsInvoiceSendAuto() {
        return this.isInvoiceSendAuto;
    }

    public double getTotalInvoiceAmountCreated() {
        return this.totalInvoiceAmountCreated;
    }

    public Object getInvoiceEndOn() {
        return this.invoiceEndOn;
    }

    public int getScheduleEndType() {
        return this.scheduleEndType;
    }
}
