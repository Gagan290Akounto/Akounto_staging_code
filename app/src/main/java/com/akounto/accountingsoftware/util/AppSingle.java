package com.akounto.accountingsoftware.util;

import android.app.Application;

import com.akounto.accountingsoftware.Activity.DashboardActivity;

/**
 * Created by root on 12-12-2016.
 */

public class AppSingle extends Application {
    private DashboardActivity activity;
    private static AppSingle _app;
    private String comp_id,access_token,comp_name,currency,cur_code,email;

    public static AppSingle getInstance() {
        return _app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _app = this;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCur_code() {
        return cur_code;
    }

    public void setCur_code(String cur_code) {
        this.cur_code = cur_code;
    }

    public DashboardActivity getActivity() {
        return activity;
    }

    public void setActivity(DashboardActivity activity) {
        this.activity = activity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
