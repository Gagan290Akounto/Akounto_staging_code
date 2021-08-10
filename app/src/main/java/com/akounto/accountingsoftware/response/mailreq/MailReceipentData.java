package com.akounto.accountingsoftware.response.mailreq;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MailReceipentData {
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("DownloadLink")
    private String DownloadLink;
    @SerializedName("From")
    private List<String> From;
    @SerializedName("Id")

    /* renamed from: Id */
    private int f139Id;
    @SerializedName("IsAttachedPDF")
    private Boolean IsAttachedPDF;
    @SerializedName("IsSendCopyMyself")
    private Boolean IsSendCopyMyself;
    @SerializedName("MailType")
    private int MailType;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Recipient")
    private List<String> Recipient;
    @SerializedName("Remarks")
    private String Remarks;
    @SerializedName("Subject")
    private String Subject;
    @SerializedName("UserName")
    private String UserName;

    public int getId() {
        return this.f139Id;
    }

    public void setId(int id) {
        this.f139Id = id;
    }

    public int getCompanyId() {
        return this.CompanyId;
    }

    public void setCompanyId(int companyId) {
        this.CompanyId = companyId;
    }

    public int getMailType() {
        return this.MailType;
    }

    public void setMailType(int mailType) {
        this.MailType = mailType;
    }

    public String getSubject() {
        return this.Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getDownloadLink() {
        return this.DownloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.DownloadLink = downloadLink;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getRemarks() {
        return this.Remarks;
    }

    public void setRemarks(String remarks) {
        this.Remarks = remarks;
    }

    public Boolean getSendCopyMyself() {
        return this.IsSendCopyMyself;
    }

    public void setSendCopyMyself(Boolean sendCopyMyself) {
        this.IsSendCopyMyself = sendCopyMyself;
    }

    public Boolean getAttachedPDF() {
        return this.IsAttachedPDF;
    }

    public void setAttachedPDF(Boolean attachedPDF) {
        this.IsAttachedPDF = attachedPDF;
    }

    public List<String> getRecipient() {
        return this.Recipient;
    }

    public void setRecipient(List<String> recipient) {
        this.Recipient = recipient;
    }

    public List<String> getFrom() {
        return this.From;
    }

    public void setFrom(List<String> from) {
        this.From = from;
    }
}
