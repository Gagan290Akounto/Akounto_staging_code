package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendMailRequest {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("From")
    @Expose
    private List<String> from = null;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSendCopyMyself")
    @Expose
    private Boolean isSendCopyMyself;
    @SerializedName("IsAttachedPDF")
    @Expose
    private Boolean isAttachedPDF;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("MailType")
    @Expose
    private Integer mailType;
    @SerializedName("DownloadLink")
    @Expose
    private String downloadLink;
    @SerializedName("Recipient")
    @Expose
    private List<String> recipient = null;

    public SendMailRequest(Integer id, List<String> from, String message, Boolean isSendCopyMyself, Boolean isAttachedPDF, String subject, Integer mailType, String downloadLink, List<String> recipient) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.isSendCopyMyself = isSendCopyMyself;
        this.isAttachedPDF = isAttachedPDF;
        this.subject = subject;
        this.mailType = mailType;
        this.downloadLink = downloadLink;
        this.recipient = recipient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getFrom() {
        return from;
    }

    public void setFrom(List<String> from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSendCopyMyself() {
        return isSendCopyMyself;
    }

    public void setSendCopyMyself(Boolean sendCopyMyself) {
        isSendCopyMyself = sendCopyMyself;
    }

    public Boolean getAttachedPDF() {
        return isAttachedPDF;
    }

    public void setAttachedPDF(Boolean attachedPDF) {
        isAttachedPDF = attachedPDF;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getMailType() {
        return mailType;
    }

    public void setMailType(Integer mailType) {
        this.mailType = mailType;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public List<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<String> recipient) {
        this.recipient = recipient;
    }
}
