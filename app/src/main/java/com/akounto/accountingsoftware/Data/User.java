package com.akounto.accountingsoftware.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Marks a class as an entity. This class will have a mapping SQLite table in the database.
@Entity(tableName = "users")
public class User {
    //Marks a field in an Entity as the primary key.
    @PrimaryKey(autoGenerate = true)
    public int uid=0;
    //Allows specific customization about the column associated with this field.
    @ColumnInfo(name = "access_token")
    public String accesstoken;
    @ColumnInfo(name = "token_type")
    public String tokentype;
    @ColumnInfo(name = "expires_in")
    public Integer expiresin;
    @ColumnInfo(name = "user_name")
    public String username;
    @ColumnInfo(name = "first_name")
    public String firstname;
    @ColumnInfo(name = "last_name")
    public String lastname;
    @ColumnInfo(name = "user_details")
    public String userdetails;

    public User(String accesstoken, String tokentype, Integer expiresin, String username, String firstname, String lastname, String userdetails) {
        this.accesstoken = accesstoken;
        this.tokentype = tokentype;
        this.expiresin = expiresin;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userdetails = userdetails;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getTokentype() {
        return tokentype;
    }

    public void setTokentype(String tokentype) {
        this.tokentype = tokentype;
    }

    public Integer getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(Integer expiresin) {
        this.expiresin = expiresin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }
}
