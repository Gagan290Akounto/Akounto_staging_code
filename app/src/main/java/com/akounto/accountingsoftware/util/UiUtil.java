package com.akounto.accountingsoftware.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.akounto.accountingsoftware.Activity.SignInActivity;
import com.akounto.accountingsoftware.Data.Dashboard.CashFlow;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.Dashboard.ExpenseBreakdown;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.request.AddBillTax;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.response.SignUp.SignUpResponse;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.akounto.accountingsoftware.Data.Dashboard.CashFlow;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.Dashboard.ExpenseBreakdown;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.SignInActivity;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.request.AddBillTax;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.response.SignUp.SignUpResponse;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.akounto.accountingsoftware.response.TaxResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UiUtil {
    private static final int PRIVATE_MODE = 0;
    public static final char[] f142c = {'k', 'm', 'b', 't'};
    private static SharedPreferences.Editor editor;
    public static ProgressDialog pDialogue;
    private static SharedPreferences pref;

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }
    public static List<Product> reset_list(List<Product> list) {
        List<Product> temp = list;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDiscountType() != 0) {
                if (list.get(i).getDiscount() != 0) {
                    try {
                        temp.set(i, setDiscountedPrice(list.get(i), list.get(i).getDiscountType(), "" + list.get(i).getDiscount(), "" + list.get(i).getPrice()));
                    } catch (Exception e) {
                        // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Toast.makeText(getApplicationContext(), "Discount value is empty", Toast.LENGTH_LONG).show();
                    temp.get(i).setPriceAfterDiscount(list.get(i).getPrice());
                }
            } else {
                temp.get(i).setPriceAfterDiscount(list.get(i).getPrice());
            }
        }
        return temp;
    }

    public static Product setDiscountedPrice(Product temp, int type, String value, String amount) {
        Product item = temp;
        float am = 0;
        float val = 0;
        try {
            am = Float.parseFloat(amount);
            val = Float.parseFloat(value);
        } catch (Exception e) {
            item.setPriceAfterDiscount(am);
        }
        if (type == 2) {
            item.setPriceAfterDiscount(am - ((am * val) / 100f));
        } else if (type == 1) {
            item.setPriceAfterDiscount(am - val);
        } else {
            item.setPriceAfterDiscount(am);
        }
        return item;
    }

    public static List<Product> priceAfterDiscount(List<Product> list, int dis_type, double dis_value) {
        List<Product> temp = list;
        if (dis_type == 1) {
            for (int i = 0; i < list.size(); i++) {
                try {
                    double am = list.get(i).getPrice();
                    double pad = list.get(i).getPriceAfterDiscount();
                    double price_after_discount = (pad - ((pad * dis_value) / 100f));
                    temp.get(i).setPriceAfterDiscount(price_after_discount);
                } catch (Exception e) {
                }
            }
        } else if (dis_type == 2) {
            for (int i = 0; i < list.size(); i++) {
                temp.get(0).setPriceAfterDiscount(list.get(i).getPriceAfterDiscount() - dis_value);
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                temp.get(0).setPriceAfterDiscount(list.get(i).getPrice());
            }
        }
        return temp;
    }

    public static int getDisType(int i) {
        if (i == 1) {
            return 2;
        } else if (i == 2) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int getDisTypeRe(int i) {
        if (i == 1) {
            return 1;
        } else if (i == 2) {
            return 2;
        } else {
            return 0;
        }
    }

    public static void disableEditText2(EditText editText, Activity activity) {
        hideKeyboard(activity);
        editText.setText("");
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    public static void enableEditText2(EditText edit) {
        edit.setEnabled(true);
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showProgressDialogue(Context context, String tittle, String msg) {
        if (context != null) {
            ProgressDialog progressDialog = pDialogue;
            if (progressDialog == null || !progressDialog.isShowing()) {
                try {
                    ProgressDialog progressDialog2 = new ProgressDialog(context);
                    pDialogue = progressDialog2;
                    progressDialog2.setTitle(tittle);
                    pDialogue.setMessage(msg);
                    pDialogue.setCanceledOnTouchOutside(false);
                    pDialogue.show();
                } catch (Exception e) {
                }
            } else {
                try {
                    pDialogue.setTitle(tittle);
                    pDialogue.setMessage(msg);
                } catch (Exception e) {
                }
            }
            try {
                pDialogue.setCanceledOnTouchOutside(true);
                pDialogue.setCancelable(true);
            } catch (Exception e) {
            }
        }
    }

    public static JsonObject getRegRequst(RegisterBusiness business) {
        JsonObject main = new JsonObject();
        try {
            JsonObject rest_data = new JsonObject();
            rest_data.addProperty("FirstName", business.getUser().getFirstName());
            rest_data.addProperty("LastName", business.getUser().getLastName());
            rest_data.addProperty("Email", business.getUser().getEmail());
            rest_data.addProperty("Password", business.getUser().getPassword());
            rest_data.addProperty("Role", "Admin");
            main.add("user", rest_data);
            main.addProperty("BusinessName", business.getBusinessName());
            main.addProperty("IndustryTypeId", business.getIndustryTypeId());
            main.addProperty("IndustryTypeName", business.getIndustryTypeName());
            main.addProperty("BusinessEntityId", business.getBusinessEntityId());
            main.addProperty("BusinessEntity", business.getBusinessEntity());
            main.addProperty("Country", business.getCountry());
            main.addProperty("BusinessCurrency", business.getBusinessCurrency());
            main.addProperty("PhoneCode", business.getPhoneCode());
            main.addProperty("Phone", business.getPhone());
        } catch (Exception e) {
            Log.e("Error :: ", e.getMessage());
        }
        return main;
    }

    public static void cancelProgressDialogue() {
        try {
            ProgressDialog progressDialog = pDialogue;
            if (progressDialog != null && progressDialog.isShowing()) {
                pDialogue.dismiss();
                pDialogue = null;
            }
        } catch (Exception e) {
        }
    }

    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLog(String tag, String msg) {
        for (int i = 0; i < msg.length(); i += 2048) {
            Log.d(tag, msg.substring(i, Math.min(msg.length(), i + 2048)));
        }
    }

    public static void addLoginToSharedPref(Context context, Boolean isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putBoolean(UiConstants.IS_LOGGEDIN, isLoogedIn.booleanValue());
        editor.commit();
    }

    public static Boolean isLoggedin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return Boolean.valueOf(sharedPreferences.getBoolean(UiConstants.IS_LOGGEDIN, false));
    }

    public static void addUserDetails(Context context, SignInResponse isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putString(UiConstants.USR_DETAILS, new Gson().toJson(isLoogedIn));
        editor.commit();
    }

    public static void addUserDetails(Context context, SignUpResponse isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        SignInResponse signInResponse = new SignInResponse(isLoogedIn.getData().getFirstName(), isLoogedIn.getData().getLastName(), isLoogedIn.getData().getUserDetails(), isLoogedIn.getData().getUserName(), isLoogedIn.getData().getAccessToken(), isLoogedIn.getData().getExpiresIn().toString());
        edit.putString(UiConstants.USR_DETAILS, new Gson().toJson(signInResponse));
        editor.commit();
    }

    public static void addUserDetails(Context context, LoginData isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        SignInResponse signInResponse = new SignInResponse(isLoogedIn.getFirstName(), isLoogedIn.getLastName(), isLoogedIn.getUserDetails(), isLoogedIn.getUserName(), isLoogedIn.getAccessToken(), isLoogedIn.getExpiresIn().toString());
        edit.putString(UiConstants.USR_DETAILS, new Gson().toJson(signInResponse));
        editor.commit();
    }

    public static void addUserDetails(Context context, SoclData isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        SignInResponse signInResponse = new SignInResponse(isLoogedIn.getData().getFirstName(), isLoogedIn.getData().getLastName(), isLoogedIn.getData().getUserDetails(), isLoogedIn.getData().getUserName(), isLoogedIn.getData().getAccessToken(), isLoogedIn.getData().getExpiresIn().toString());
        edit.putString(UiConstants.USR_DETAILS, new Gson().toJson(signInResponse));
        editor.commit();
    }

    public static void SetFirstLogin(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putBoolean(UiConstants.FIRST_LOGIN, isFirstTime);
        editor.commit();
    }

    public static void addAcccessToken(Context context, SignInResponse isLoogedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putString(UiConstants.LOGIN, isLoogedIn.getAccess_token());
        editor.commit();
    }

    public static boolean isFirstLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return sharedPreferences.getBoolean(UiConstants.FIRST_LOGIN, false);
    }

    public static String getBussinessCurren(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class).getActiveBusiness().getCurrency();
    }

    public static String getBussinessCurrenSymbul(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class).getActiveBusiness().getCurrencySymbol();
    }

    public static int getAccountingType(Context context) {
        int a = -1;
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
            pref = sharedPreferences;
            a = new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class).getActiveBusiness().getAccountingBasisType();
        } catch (Exception e) {
        }
        return a;
    }

    public static String getUserName(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
            pref = sharedPreferences;
            return new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), LoginData.class).getUserDetails(), UserDetails.class).getActiveBusiness().getName();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getActiveBussinessCurren(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class).getActiveBusiness().getCurrencySymbol();
    }

    public static String getAcccessToken(Context context) {
        String at = "";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
            pref = sharedPreferences;
            at = new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getAccess_token();
        } catch (Exception e) {
        }
        return at;
    }

    public static String getFirstName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getFirstName();
    }

    public static String getLastName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getLastName();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserName();
    }

    public static String getExpireIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return String.valueOf(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getExpires());
    }

    public static String getComp_Id(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class).getActiveBusiness().getId().toString();
    }

    //
    public static SignInResponse getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        return new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class);
    }

    public static UserDetails getUserDetail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.akounto.accountingsoftware", PRIVATE_MODE);
        pref = sharedPreferences;
        UserDetails userDetails = new Gson().fromJson(new Gson().fromJson(sharedPreferences.getString(UiConstants.USR_DETAILS, ""), SignInResponse.class).getUserDetails(), UserDetails.class);
        return userDetails;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /* public static boolean isValidEmail(String email){

           return Patterns.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                   + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
       }*/
    public static String loadJSONFromAsset(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void moveToSignUp(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static String coolFormat(double n, int iteration) {
        Object obj;
        double d = (double) (((long) n) / 100);
        Double.isNaN(d);
        double d2 = d / 10.0d;
        boolean isRound = (d2 * 10.0d) % 10.0d == Utils.DOUBLE_EPSILON;
        if (d2 >= 1000.0d) {
            return coolFormat(d2, iteration + 1);
        }
        StringBuilder sb = new StringBuilder();
        if (d2 > 99.9d || isRound || d2 > 9.99d) {
            obj = Integer.valueOf((((int) d2) * 10) / 10);
        } else {
            obj = d2 + "";
        }
        sb.append(obj);
        sb.append("");
        sb.append(f142c[iteration]);
        return sb.toString();
    }

    public static void setExpenseBreakdownGraph(Context mContext, PieChart peiChart, DashboardData dbdata) {
        try {
            peiChart.getLegend().setEnabled(false);
            peiChart.setUsePercentValues(true);
            peiChart.setClickable(false);
            peiChart.getDescription().setEnabled(false);
            peiChart.setExtraOffsets(5.0f, 10.0f, 5.0f, 5.0f);
            peiChart.setDragDecelerationFrictionCoef(0.95f);
            peiChart.setDrawHoleEnabled(false);
            peiChart.setTransparentCircleColor(-1);
            peiChart.setTransparentCircleAlpha(110);
            peiChart.setRotationEnabled(true);
            peiChart.setHighlightPerTapEnabled(true);
            peiChart.animateY(1400, Easing.EaseInOutQuad);
            peiChart.setEntryLabelColor(mContext.getResources().getColor(R.color.text_color_blue));
            peiChart.setEntryLabelTextSize(12.0f);
            Typeface poppins_semibold = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_semibold.ttf");
            peiChart.setEntryLabelTypeface(poppins_semibold);
            ArrayList<PieEntry> expenses = new ArrayList<>();
            for (ExpenseBreakdown expenseBreakdownItem : dbdata.getData().getExpenseBreakdown()) {
                expenses.add(new PieEntry(Float.parseFloat(expenseBreakdownItem.getAmount().toString()), expenseBreakdownItem.getName()));
            }
            PieDataSet pieDataSet = new PieDataSet(expenses, "Expense Breakdown");
            pieDataSet.setDrawIcons(false);
            pieDataSet.setSliceSpace(0.5f);
            pieDataSet.setSelectionShift(5.0f);
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#f8b886"));
            colors.add(Color.parseColor("#c96666"));
            colors.add(Color.parseColor("#66c2c9"));
            colors.add(Color.parseColor("#6677c9"));
            colors.add(Color.parseColor("#6693c9"));
            for (int c : ColorTemplate.LIBERTY_COLORS) {
                colors.add(Integer.valueOf(c));
            }
            colors.add(Integer.valueOf(ColorTemplate.getHoloBlue()));
            pieDataSet.setColors((List<Integer>) colors);
            pieDataSet.setValueLinePart1OffsetPercentage(80.0f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.5f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setUsingSliceColorAsValueLineColor(true);
            pieDataSet.setValueLineWidth(2.0f);
            pieDataSet.setValueFormatter(new PercentageValueFormatter());
            pieDataSet.setValueLineVariableLength(true);
            PieData data = new PieData(pieDataSet);
            data.setValueTypeface(poppins_semibold);
            data.setValueTextSize(16.0f);
            data.setValueTextColor(mContext.getResources().getColor(R.color.text_color));
            if (isPieChartDataEmpty(expenses)) {
                peiChart.setData(data);
                peiChart.highlightValues((Highlight[]) null);
                peiChart.invalidate();
            }
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }

    public static void setCashFlowGraph(Context mContext, BarChart barChart, DashboardData dbdata) {
        try {
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.getAxisLeft().setDrawAxisLine(false);
            barChart.getXAxis().setDrawAxisLine(false);
            barChart.getAxisRight().setEnabled(false);
            Legend l = barChart.getLegend();
            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");
            float groupSpace = 0.2f;
            float barSpace = 0.08f;
            float barWidth = 0.32f;
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setYOffset(10.0f);
            l.setXOffset(0.0f);
            l.setYEntrySpace(0.0f);
            l.setTextSize(10.0f);
            ArrayList<BarEntry> incomeValues = new ArrayList<>();
            ArrayList<BarEntry> outComeValues = new ArrayList<>();
            ArrayList<String> xAxisLabels = new ArrayList<>();
            int i = 0;
            for (CashFlow cashFlowItem : dbdata.getData().getCashFlow()) {
                incomeValues.add(new BarEntry((float) i, Float.parseFloat(cashFlowItem.getCredit().toString())));
                outComeValues.add(new BarEntry((float) i, Float.parseFloat(cashFlowItem.getDredit().toString())));
                xAxisLabels.add(cashFlowItem.getLabel());
                i++;
            }
            int xAxisItemsCount = Math.max(incomeValues.size(), outComeValues.size());
            IndexAxisValueFormatter custom = new IndexAxisValueFormatter((Collection<String>) xAxisLabels);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setCenterAxisLabels(true);
            xAxis.setValueFormatter(custom);
            xAxis.setLabelCount(xAxisItemsCount);
            xAxis.setLabelRotationAngle(270.0f);
            xAxis.setDrawGridLines(false);
            xAxis.setTypeface(poppins_regular);
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);
            leftAxis.setSpaceTop(35.0f);
            leftAxis.setAxisMinimum(0.0f);
            leftAxis.setTypeface(poppins_regular);
            leftAxis.setValueFormatter(new LargeValueFormatter());
            if (barChart.getData() == null || (barChart.getData()).getDataSetCount() <= 0) {
                if (UiUtil.getAccountingType(mContext) == 2) {
                    BarDataSet inCome = new BarDataSet(incomeValues, "Income");
                    inCome.setColor(Color.parseColor("#1a8b8c"));//green
                    BarDataSet outCome = new BarDataSet(outComeValues, "Expense");
                    outCome.setColor(Color.parseColor("#f68477"));//red
                    barChart.setData(new BarData(outCome, inCome));
                    BarDataSet barDataSet = outCome;
                } else {
                    BarDataSet inCome = new BarDataSet(incomeValues, "Inflow");
                    inCome.setColor(Color.parseColor("#1a8b8c"));//green
                    BarDataSet outCome = new BarDataSet(outComeValues, "Outflow");
                    outCome.setColor(Color.parseColor("#f68477"));//red
                    barChart.setData(new BarData(outCome, inCome));
                    BarDataSet barDataSet = outCome;
                }
            } else {
                ((BarDataSet) barChart.getData().getDataSetByIndex(0)).setValues(incomeValues);
                ((BarDataSet) barChart.getData().getDataSetByIndex(1)).setValues(outComeValues);
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            }
            for (IBarDataSet set : barChart.getData().getDataSets()) {
                set.setDrawValues(false);
            }
            barChart.getData().setHighlightEnabled(false);
            barChart.animateXY(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
            barChart.getBarData().setBarWidth(barWidth);
            barChart.getXAxis().setAxisMinimum(0.0f);
            barChart.getXAxis().setAxisMaximum((barChart.getBarData().getGroupWidth(groupSpace, barSpace) * ((float) xAxisItemsCount)) + 0.0f);
            barChart.groupBars(0.0f, groupSpace, barSpace);
            barChart.invalidate();
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }

    public static boolean isPieChartDataEmpty(ArrayList<PieEntry> pieEntries) {
        boolean isPieChartDataEmpty = true;
        Iterator<PieEntry> it = pieEntries.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().getValue() != 0.0f) {
                    isPieChartDataEmpty = false;
                    break;
                }
            } else {
                break;
            }
        }
        return !isPieChartDataEmpty;
    }

    private static class PercentageValueFormatter extends ValueFormatter {
        private final DecimalFormat mFormat = new DecimalFormat("###,###,##0.0");

        public String getPieLabel(float value, PieEntry pieEntry) {
            return this.mFormat.format((double) value).replace(",", ".") + " %";
        }
    }


    public static List<ProductServiceTaxesItem> changeTaxResponse(List<TaxResponse> orginal) {
        List<ProductServiceTaxesItem> converted = new ArrayList<>();
        try {
            for (int i = 0; i < orginal.size(); i++) {
                ProductServiceTaxesItem temp = new ProductServiceTaxesItem();
                temp.setSelected(false);
                try {
                    temp.setTaxId(orginal.get(i).getId());
                } catch (Exception e) {
                }
                try {
                    temp.setTaxName(orginal.get(i).getName());
                } catch (Exception e) {
                }
                try {
                    temp.setRate(orginal.get(i).getEffectiveTaxes().get(0).getRate());
                } catch (Exception e) {
                }
                converted.add(temp);
            }
        } catch (Exception e) {
        }
        return converted;
    }

    public static ProductServiceTaxesItem changeTaxResponse(TaxResponse orginal) {

        ProductServiceTaxesItem temp = new ProductServiceTaxesItem();
        temp.setSelected(false);
        try {
            temp.setTaxId(orginal.getId());
        } catch (Exception e) {
        }
        try {
            temp.setTaxName(orginal.getName());
        } catch (Exception e) {
        }
        try {
            temp.setRate(orginal.getEffectiveTaxes().get(0).getRate());
        } catch (Exception e) {
        }


        return temp;
    }

    public static List<ProductServiceTaxesItem> makeCompatible(List<ProductServiceTaxesItem> orignal, List<ProductServiceTaxesItem> selected) {
        try {
            for (int i = 0; i < orignal.size(); i++) {
                orignal.get(i).setSelected(isAvilable(orignal.get(i).getTaxId(), selected));
            }
        } catch (Exception e) {
        }
        return orignal;
    }

    public static boolean isAvilable(int id, List<ProductServiceTaxesItem> list) {
        boolean result = false;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTaxId() == id) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }


    public static List<ProductServiceTaxesItem> trasforme(List<ProductServiceTaxesItem> taxs) {
        List<ProductServiceTaxesItem> temp = new ArrayList<>();
        for (int i = 0; i < taxs.size(); i++) {
            ProductServiceTaxesItem taxesItem = new ProductServiceTaxesItem();
            taxesItem.setId(taxs.get(i).getId());
            taxesItem.setTaxId(taxs.get(i).getHeadTransactionTexId());
            taxesItem.setTaxName(taxs.get(i).getName());
            taxesItem.setRate(taxs.get(i).getRate());
            temp.add(taxesItem);
        }
        return temp;
    }

    public static List<ProductServiceTaxesItem> trasformeBill(List<AddBillTax> taxs) {
        List<ProductServiceTaxesItem> temp = new ArrayList<>();
        try {
            for (int i = 0; i < taxs.size(); i++) {
                ProductServiceTaxesItem taxesItem = new ProductServiceTaxesItem();
                taxesItem.setId(taxs.get(i).getId());
                taxesItem.setTaxId(taxs.get(i).getHeadTransactionTexId());
                taxesItem.setTaxName(taxs.get(i).getName());
                taxesItem.setRate(taxs.get(i).getRate());
                temp.add(taxesItem);
            }
        } catch (Exception e) {
        }
        return temp;
    }

    public static com.akounto.accountingsoftware.model.Currency getcurancy(String id, List<com.akounto.accountingsoftware.model.Currency> list) {
        com.akounto.accountingsoftware.model.Currency c = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(id)) {
                c = list.get(i);
                break;
            }
        }
        return c;
    }

    public static int getcurancyindex(String id, List<Currency> list) {
        int c = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(id)) {
                c = i;
                break;
            }
        }
        return c;
    }

    public static void removeAll(EditText editText) {
        try {
            Field field = findField("mListeners", editText.getClass());
            if (field != null) {
                field.setAccessible(true);
                ArrayList<TextWatcher> list = (ArrayList<TextWatcher>) field.get(editText); //IllegalAccessException
                if (list != null) {
                    list.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Field findField(String name, Class<?> type) {
        for (Field declaredField : type.getDeclaredFields()) {
            if (declaredField.getName().equals(name)) {
                return declaredField;
            }
        }
        if (type.getSuperclass() != null) {
            return findField(name, type.getSuperclass());
        }
        return null;
    }
}
