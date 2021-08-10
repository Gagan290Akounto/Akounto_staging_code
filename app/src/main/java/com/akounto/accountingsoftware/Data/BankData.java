
package com.akounto.accountingsoftware.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankData {

    @SerializedName("LinkToken")
    @Expose
    private String linkToken;
    @SerializedName("Environment")
    @Expose
    private String environment;
    @SerializedName("Expiry")
    @Expose
    private String expiry;

    public String getLinkToken() {
        return linkToken;
    }

    public void setLinkToken(String linkToken) {
        this.linkToken = linkToken;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

}
