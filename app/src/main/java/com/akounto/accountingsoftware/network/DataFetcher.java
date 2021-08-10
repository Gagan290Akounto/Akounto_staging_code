package com.akounto.accountingsoftware.network;

import android.content.Context;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.response.SignUpResponse;
import com.akounto.accountingsoftware.util.InternetDialog;
import java.lang.reflect.Type;
import java.util.List;

public class DataFetcher {
   /* public static void getAllclassrooms(Context context, Response.Listener<List> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String url, String token) {
        if (new InternetDialog(context).getInternetStatus(context)) {
            HelperVolley.CallGetApi(context, url, url, updateSuccessListener, List.class, errorListener, token);
        }
    }

    public static void getAllSubjectNames(Context context, Response.Listener<List> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String url, String token) {
        if (new InternetDialog(context).getInternetStatus(context)) {
            HelperVolley.CallGetApi(context, url, url, updateSuccessListener, List.class, errorListener, token);
        }
    }

    public static void login(Context context, String json, Response.Listener<SignInResponse> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String url) {
        if (new InternetDialog(context).getInternetStatus(context)) {
            HelperVolley.CallApiWithJson(context, url, url, updateSuccessListener, json, repClass, errorListener);
        }
    }

    public static void signUp(Context context, String json, Response.Listener<SignUpResponse> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String url) {
        if (new InternetDialog(context).getInternetStatus(context)) {
            HelperVolley.CallApiWithJson(context, url, url, updateSuccessListener, json, repClass, errorListener);
        }
    }*/
}
