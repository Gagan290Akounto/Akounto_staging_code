package com.akounto.accountingsoftware.Constants;

import com.akounto.accountingsoftware.R;

public final class Constant {

    //UAT
    public static final boolean ANALATICS_ON = false;
    public static final String BASE_URL = "https://beta.api.akounto.com/";
    public static final String X_SIGNATURE = "20FCC48BAC3E4B6496FDE12BFA1E93C3";
    public static final String WebURL = "http://staging.akounto.com";

    //Pre Production
/*        public static final boolean ANALATICS_ON = false;
    public static final String BASE_URL = "https://api.akounto.com/";
    public static final String X_SIGNATURE = "F1EF64264FF54BA581FC0296825DA82B";
    public static final String WebURL = "https://app.akounto.com";*/

    //Production
/*    public static final boolean ANALATICS_ON = true;
    public static final String BASE_URL = "https://api.akounto.com/";
    public static final String X_SIGNATURE = "F1EF64264FF54BA581FC0296825DA82B";
    public static final String WebURL = "https://app.akounto.com";*/

    public static final String IMG_URL = "https://app.akounto.com/assets/flags/";
    public static final String GRANT_TYPE = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String ID_TOKEN = "id_token";
    public static final String EMAIL = "email";
    public static final String LAUNCH_TYPE = "launch_type";
    public static final String BANK_ID = "bank_id";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASWORD = "password";
    public static final int due_val[] = {15, 30, 45, 60, 90};
    public static final int images[][] = {{R.drawable.classical_1, R.drawable.default_1, R.drawable.minimal_1}, {R.drawable.classical_2, R.drawable.default_2, R.drawable.minimal_2}, {R.drawable.classical_3, R.drawable.default_3, R.drawable.minimal_3}, {R.drawable.classical_4, R.drawable.default_4, R.drawable.minimal_4}, {R.drawable.classical_5, R.drawable.default_5, R.drawable.minimal_5}};
    public static final String color[] = {"#5a53fd", "#1a8b8c", "#6f42c1", "#fb3c56", "#ffc107"};
    public static final int days_month[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final String CUSTOMER_DATE = "CUSTOMER_DATE";
    public static final boolean DEBUG = true;
    public static final String IS_EDIT = "is_edit";
    public static final String IS_LOGGEDIN = "is_loggedin";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_name";
    public static final String LOGIN = "token";

    ////////////////////////////////
    public static final String EVENT = "event";
    public static final String CATEGORY = "category";
    public static final String ACTION = "action";
    public static final String CAUSES = "causes";
    public static final String PHONE = "phone";
    public static final String COMPANY = "company";
    public static final String ACCOUNT_NUMBER = "account_no";

}
