package com.akounto.accountingsoftware.util;

import java.util.ArrayList;
import java.util.List;

public class UiConstants {
    public static final String CUSTOMER_DATE = "CUSTOMER_DATE";
    public static final boolean DEBUG = true;
    public static final String IS_EDIT = "is_edit";
    public static final String IS_LOGGEDIN = "is_loggedin";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_name";
    public static final String PHONE_CODE = "phone_code";
    public static final String LOGIN = "token";
    public static final String FIRST_LOGIN = "first_login";
    //public static final String PREF_NAME = "com.akounto.android";
   // public static final String SERVER_IP = "https://beta.api.akounto.com/";
    //public static final String X_SIGNATURE = "20FCC48BAC3E4B6496FDE12BFA1E93C2";
    public static final String USR_DETAILS = "usr_details";

    public static List<String> getDashboardItemName() {
        List<String> itemname = new ArrayList<>();
        itemname.add("Professional invoicing");
        itemname.add("Better bookkeeping");
        itemname.add("Capture Receipts");
        itemname.add("CFO Services");
        return itemname;
    }

    public static List<String> getDashboardItemDesc() {
        List<String> itemname = new ArrayList<>();
        itemname.add("Create customizable documents that get you paid faster");
        itemname.add("Get insights by tracking income and expenses");
        itemname.add("Stay compliant by keeping accurate records");
        itemname.add("A Professional CFO will oversee your current bookkeeping");
        return itemname;
    }
}
