package com.akounto.accountingsoftware.response;

public class SignInResponse {
    private String FirstName;
    private String LastName;
    private String UserDetails;
    private String UserName;
    private String expires;
    private String access_token;

    public SignInResponse(String firstName, String lastName, String userDetails, String userName, String access_token, String expires) {
        FirstName = firstName;
        LastName = lastName;
        UserDetails = userDetails;
        UserName = userName;
        this.access_token = access_token;
        this.expires=expires;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token2) {
        this.access_token = access_token2;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getUserDetails() {
        return this.UserDetails;
    }

    public void setUserDetails(String userDetails) {
        this.UserDetails = userDetails;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
