package com.akounto.accountingsoftware.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDelete {

    @SerializedName("UserId")
    @Expose
    String userId;

    public UserDelete(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
