package com.akounto.accountingsoftware.request;

public class SignIn {
    private String Password;
    private String UserName;
    private String grant_type;

    public String getUsername() {
        return this.UserName;
    }

    public void setUsername(String username) {
        this.UserName = username;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getGrant_type() {
        return this.grant_type;
    }

    public void setGrant_type(String grant_type2) {
        this.grant_type = grant_type2;
    }
}
