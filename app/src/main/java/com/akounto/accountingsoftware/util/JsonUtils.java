package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by root on 24-07-2017.
 */

public class JsonUtils {

    public static String loadJSONFromAsset(String fileName, Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("json/" + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
   /* public static List<JsonData> getBusinessListobj() {
        List<JsonData> listcircel = new ArrayList<>();
        Gson gson = new Gson();
        JsonListData data = gson.fromJson(loadJSONFromAsset("business_type.json"), JsonListData.class);
        if (data != null && data.getData() != null) {
            for (JsonData jd : data.getData()) {
                listcircel.add(new JsonData(jd.getId(),jd.getName()));
            }
        }
        return listcircel;
    }
    public static List<String> getBusinessList() {
        List<String> listcircel = new ArrayList<>();
        Gson gson = new Gson();
        JsonListData data = gson.fromJson(loadJSONFromAsset("business_type.json"), JsonListData.class);
        if (data != null && data.getData() != null) {
            for (JsonData jd : data.getData()) {
                listcircel.add(jd.getName());
            }
        }
        return listcircel;
    }
    public static List<JsonData> getDealsWithListobj() {
        List<JsonData> listcircel = new ArrayList<>();
        Gson gson = new Gson();
        JsonListData data = gson.fromJson(loadJSONFromAsset("deals_with.json"), JsonListData.class);
        if (data != null && data.getData() != null) {
            for (JsonData jd : data.getData()) {
                listcircel.add(new JsonData(jd.getId(),jd.getName()));
            }
        }
        return listcircel;
    }
    public static List<String> getDealsWithList() {
        List<String> listcircel = new ArrayList<>();
        Gson gson = new Gson();
        JsonListData data = gson.fromJson(loadJSONFromAsset("deals_with.json"), JsonListData.class);
        if (data != null && data.getData() != null) {
            for (JsonData jd : data.getData()) {
                listcircel.add(jd.getName());
            }
        }
        return listcircel;
    }*/

    public static JsonObject getExtRegRequst(String mobile,String f_name,String l_name, String email, String businessName, String externalAccessToken, String industryTypeId, String industryTypeName, String businessEntityId, String businessEntity,String country, String currency,String phoneCode) {
        JsonObject main = new JsonObject();
        try {
            JsonObject rest_data = new JsonObject();
            rest_data.addProperty("FirstName", f_name);
            rest_data.addProperty("LastName", l_name);
            rest_data.addProperty("Email", email);
            main.add("user", rest_data);
            main.addProperty("BusinessName", businessName);
            main.addProperty("IndustryTypeId", industryTypeId);
            main.addProperty("IndustryTypeName", industryTypeName);
            main.addProperty("BusinessEntityId", businessEntityId);
            main.addProperty("BusinessEntity", businessEntity);
            main.addProperty("Country", country);
            main.addProperty("BusinessCurrency", currency);
            main.addProperty("Provider", "Google");
            main.addProperty("Phone",mobile);
            main.addProperty("PhoneCode",phoneCode);
            main.addProperty("IdToken", externalAccessToken);
        }catch(Exception e){
            Log.e("Error :: ",e.getMessage());
        }
        return main;
    }
    public static JsonObject getDashBoardRequst(String current_date, String start_date, String end_date, String graph_type, String accounting_type) {
        JsonObject main = new JsonObject();
        try {
            main.addProperty("CurrentDate", current_date);
            main.addProperty("StartDate", start_date);
            main.addProperty("EndDate", end_date);
            main.addProperty("GraphType", graph_type);
            main.addProperty("AccountingType", accounting_type);
        }catch(Exception e){
            Log.e("Error :: ",e.getMessage());
        }
        return main;
    }

    public static JsonObject getForgotRequst(String email) {
        JsonObject main = new JsonObject();
        try {
            main.addProperty("Email", email);
        }catch(Exception e){
            Log.e("Error :: ",e.getMessage());
        }
        return main;
    }
}
