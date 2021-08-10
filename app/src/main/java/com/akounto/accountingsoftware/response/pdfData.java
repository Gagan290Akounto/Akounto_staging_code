package com.akounto.accountingsoftware.response;

import com.google.gson.annotations.SerializedName;

public class pdfData {
    @SerializedName("PDFData")
    private String PDFData;

    public String getPDFData() {
        return this.PDFData;
    }

    public void setPDFData(String PDFData2) {
        this.PDFData = PDFData2;
    }
}
