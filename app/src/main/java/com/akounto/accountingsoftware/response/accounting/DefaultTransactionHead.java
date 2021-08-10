package com.akounto.accountingsoftware.response.accounting;

import com.google.gson.annotations.SerializedName;

public class DefaultTransactionHead {
    @SerializedName("UncategorizedCredit")
    private int uncategorizedCredit;
    @SerializedName("UncategorizedDebit")
    private int uncategorizedDebit;
    @SerializedName("UnderReviewCredit")
    private int underReviewCredit;
    @SerializedName("UnderReviewDebit")
    private int underReviewDebit;

    public int getUncategorizedDebit() {
        return this.uncategorizedDebit;
    }

    public int getUnderReviewDebit() {
        return this.underReviewDebit;
    }

    public int getUncategorizedCredit() {
        return this.uncategorizedCredit;
    }

    public int getUnderReviewCredit() {
        return this.underReviewCredit;
    }
}
