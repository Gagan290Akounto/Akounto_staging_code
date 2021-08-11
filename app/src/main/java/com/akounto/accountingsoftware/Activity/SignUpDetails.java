package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CountryData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.Listner.IDialogListClickListener;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.adapter.AdapterDialogListItem;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.response.SignUp.SignUpResponse;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpDetails extends AppCompatActivity {
    int selectedPos = 0;
    private EditText f_n, l_n, p_n, b_name;
    TextView edt_phone_number_tooltip,f_n_error, l_n_error, p_n_error, b_name_error, c_care, deals_with_business_error, type_business_error, phone_code_tv;
    private LinearLayout next, aboutUs, aboutBusiness, nxt_back, back, f_n_ll, l_n_ll, p_n_ll, b_name_ll, dealsWithBusiness_error, typeBusiness_error;
    private String fist = "", last = "", phone = "", businessType = "", dealsWith = "", business_name;
    private int businessTypeId = 0, dealsWithId = 0;
    RegisterBusiness registerBusiness;
    int selectedCountry = 0;
    String countryCode = "US";
    String selectedCurrencyId = "USD";
    Spinner countrySpinner;
    Spinner businessCurrencySpinner;
    ImageView couFlagSpinner;
    List<String> countryListForSpinner = new ArrayList();
    List<String> countryListFoCurrency = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    Map<String, String> currencyMap = new HashMap();
    Map<Integer, String> countryMap = new HashMap();
    Map<Integer, String> dealsMap = new HashMap();
    List<String> dealsinForSpinner = new ArrayList();
    Map<Integer, String> typeBusnisMap = new HashMap();
    List<String> typeBusniSpinner = new ArrayList();
    ArrayList<CountryData> countryData = new ArrayList<>();

    private Spinner typeBusinessSpinner;
    private Spinner dealsWithBusinessSpinner;
    Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup1);
        try {
            mContext = this;
            registerBusiness = LocalManager.getInstance().getRegisterBusiness();
            Bundle b = new Bundle();
            b.putString(Constant.CATEGORY, "sign_up");
            b.putString(Constant.ACTION, "screen_view");
            b.putString(Constant.COMPANY, business_name);
            SplashScreenActivity.sendEvent("sign_up2_screen_view", b);
            next = findViewById(R.id.nxt_done);
            nxt_back = findViewById(R.id.nxt_back);
            aboutUs = findViewById(R.id.about_us);
            edt_phone_number_tooltip = findViewById(R.id.edt_phone_number_tooltip);
            back = findViewById(R.id.back);
            aboutBusiness = findViewById(R.id.about_business);
            countrySpinner = findViewById(R.id.countrySpinner);
            businessCurrencySpinner = findViewById(R.id.businessCurrencySpinner);
            couFlagSpinner = findViewById(R.id.phone_code_spiner);
            f_n = findViewById(R.id.first_name);
            l_n = findViewById(R.id.last_name);
            p_n = findViewById(R.id.edt_phone_number);
            b_name = findViewById(R.id.business_name);
            c_care = findViewById(R.id.cusmoter_care);
            phone_code_tv = findViewById(R.id.phone_code_tv);
            f_n_error = findViewById(R.id.first_name_error);
            l_n_error = findViewById(R.id.last_name_error);
            p_n_error = findViewById(R.id.edt_phone_number_error);
            b_name_error = findViewById(R.id.business_name_error);
            deals_with_business_error = findViewById(R.id.deals_with_business_error);
            type_business_error = findViewById(R.id.type_business_error);
            dealsWithBusiness_error = findViewById(R.id.dealsWithBusiness_error);
            typeBusiness_error = findViewById(R.id.typeBusiness_error);
            f_n_ll = findViewById(R.id.first_name_ll);
            l_n_ll = findViewById(R.id.last_name_ll);
            p_n_ll = findViewById(R.id.phone_number_ll);
            b_name_ll = findViewById(R.id.business_name_ll);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUpDetails.this, SIgnUpStep0.class));
                }
            });
            c_care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:+18332568686"));
                        startActivity(callIntent);
                    } catch (Exception e) {
                    }
                }
            });
            settypeBusinessSpinner();
            try {
                fetchCountries();
                fetchCurrencies();
            } catch (Exception e) {
                e.printStackTrace();
                Bundle ab = new Bundle();
                b.putString(Constant.CATEGORY, "sign_up");
                b.putString(Constant.ACTION, "Exception");
                b.putString(Constant.COMPANY, business_name);
                SplashScreenActivity.sendEvent("sign_up_Exception", b);
                LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
            }

            findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset();
                    User user;
                    fist = f_n.getText().toString().trim();
                    last = l_n.getText().toString().trim();
                    phone = p_n.getText().toString().trim();
                    try {
                        user = registerBusiness.getUser();
                        user.setFirstName(fist.trim());
                        user.setLastName(last.trim());
                    } catch (Exception e) {
                        user = new User();
                        user.setFirstName("NA");
                        user.setLastName("NA");
                        LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
                    }
                    registerBusiness.setUser(user);
                    registerBusiness.setBusinessName(b_name.getText().toString().trim());
                    registerBusiness.setBusinessEntity(businessType);
                    registerBusiness.setBusinessEntityId(businessTypeId);
                    registerBusiness.setIndustryTypeId(dealsWithId);
                    registerBusiness.setIndustryTypeName(dealsWith);
                    registerBusiness.setPhone(phone);
                    registerBusiness.setCountry(selectedCountry);
                    registerBusiness.setBusinessCurrency(selectedCurrencyId);
                    registerBusiness.setPhoneCode(phone_code_tv.getText().toString());
                    business_name = b_name.getText().toString();
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "sign_up");
                    b.putString(Constant.ACTION, "business_info_click");
                    b.putString(Constant.COMPANY, business_name);
                    SplashScreenActivity.sendEvent("sign_up_business_info_click", b);
                    if (isValidAbout()) {
                        UiUtil.showProgressDialogue(mContext, "", "Please wait...");
                        JsonObject requet = UiUtil.getRegRequst(registerBusiness);
                        Api api = ApiUtils.getAPIService();
                        api.register(Constant.X_SIGNATURE, requet).enqueue(new Callback<SignUpResponse>() {
                            @Override
                            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                                UiUtil.cancelProgressDialogue();

                                SignUpResponse responsed = response.body();
                                try {
                                    if (responsed.getTransactionStatus().getIsSuccess()) {
                                        AppSingle.getInstance().setEmail(response.body().getData().getUserName());
                                        UiUtil.showToast(mContext, "Register successfully");
                                        UiUtil.addLoginToSharedPref(mContext, true);
                                        UiUtil.addUserDetails(mContext, responsed);
                                        AppSingle.getInstance().setComp_name(new Gson().fromJson(responsed.getData().getUserDetails(), UserDetails.class).getActiveBusiness().getName());
                                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Bundle b = new Bundle();
                                        b.putString(Constant.CATEGORY, "sign_up");
                                        b.putString(Constant.ACTION, "business_info_success");
                                        b.putString(Constant.COMPANY, business_name);
                                        SplashScreenActivity.sendEvent("sign_up_register_success", b);
                                    } else {
                                        Bundle b = new Bundle();
                                        b.putString(Constant.CATEGORY, "sign_up");
                                        b.putString(Constant.ACTION, "business_info_fail");
                                        b.putString(Constant.COMPANY, business_name);
                                        SplashScreenActivity.sendEvent("sign_up_register_fail", b);
                                        if (!responsed.getTransactionStatus().getIsSuccess()) {
                                            UiUtil.showToast(mContext, ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                                        } else {
                                            UiUtil.showToast(mContext, "Not able to send register request");
                                        }
                                        LoginRepo.prinLogs(requet.toString() + "\n" + response.body().toString(), 5, "Fail|Sign up");
                                    }
                                } catch (Exception e) {
                                    Bundle b = new Bundle();
                                    b.putString(Constant.CATEGORY, "sign_up");
                                    b.putString(Constant.ACTION, "business_info_fail");
                                    b.putString(Constant.COMPANY, business_name);
                                    SplashScreenActivity.sendEvent("sign_up_register_fail", b);
                                    UiUtil.showToast(mContext, "Something went wrong");
                                    LoginRepo.prinLogs(requet.toString(), 5, "Fail Internal Exception|Sign up");
                                }
                            }

                            @Override
                            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                                UiUtil.cancelProgressDialogue();
                                UiUtil.showToast(mContext, "Not able to send register request");
                                Bundle b = new Bundle();
                                b.putString(Constant.CATEGORY, "sign_up");
                                b.putString(Constant.ACTION, "business_info_fail");
                                SplashScreenActivity.sendEvent("sign_up_register_network_fail", b);
                                LoginRepo.prinLogs("" + Log.getStackTraceString(t), 5, "Sign up");
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            Bundle b = new Bundle();
            b.putString(Constant.CATEGORY, "sign_up");
            b.putString(Constant.ACTION, "Exception");
            b.putString(Constant.CAUSES, e.getMessage());
            SplashScreenActivity.sendEvent("sign_up_Exception", b);
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
        }
        setfilter(countryCode);
        Picasso.with(mContext).load(Constant.IMG_URL+"US.png").into(couFlagSpinner);
        couFlagSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListDialog("Select Country", selectedPos, countryData, new IDialogListClickListener() {
                    @Override
                    public void onClick(CountryData data, int selectedPos) {
                        countryCode = data.getCountryCode().trim();
                        phone_code_tv.setText("+" + data.getPhoneCode());
                        Picasso.with(mContext).load(Constant.IMG_URL + data.getCountryCode() + ".png").into(couFlagSpinner);
                        setfilter(countryCode);
                    }
                });
            }
        });
    }

    private void settypeBusinessSpinner() throws JSONException {
        this.typeBusinessSpinner = findViewById(R.id.typeBusinessSpinner);
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("type_of_business.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.typeBusnisMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.typeBusniSpinner.add(jsonObject.getString("Name"));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.dropdown_text, typeBusniSpinner);
        dataAdapter.setDropDownViewResource(R.layout.dropdown_text);
        this.typeBusinessSpinner.setAdapter(dataAdapter);
        this.typeBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                businessTypeId = pos;
                businessType = typeBusniSpinner.get(pos);
                Log.e("selected", typeBusniSpinner.get(pos));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.dealsWithBusinessSpinner = findViewById(R.id.dealsWithBusinessSpinner);

        String loadJSONFromAsset1 = JsonUtils.loadJSONFromAsset("deals_in.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset1);
        JSONArray jsonArray1 = new JSONArray(loadJSONFromAsset1);
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject jsonObject = jsonArray1.getJSONObject(i);
            this.dealsMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.dealsinForSpinner.add(jsonObject.getString("Name"));
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.dropdown_text, dealsinForSpinner);
        dataAdapter2.setDropDownViewResource(R.layout.dropdown_text);
        this.dealsWithBusinessSpinner.setAdapter(dataAdapter2);
        this.dealsWithBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                dealsWithId = pos;
                dealsWith = dealsinForSpinner.get(pos);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        CountryData data;
        for (int i = 0; i < jsonArray.length(); i++) {
            data = new CountryData();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
            this.countryListFoCurrency.add(jsonObject.getString("Currency"));
            data.setId(jsonObject.get("Id").toString());
            data.setName(jsonObject.getString("Name"));
            data.setCurrency(jsonObject.getString("Currency"));
            data.setPhoneCode(jsonObject.getString("PhoneCode"));
            data.setCountryCode(jsonObject.getString("CountryCode"));
            countryData.add(data);
        }
        ArrayAdapter<String> dataAdaptercountry = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_text, countryListForSpinner);
        dataAdaptercountry.setDropDownViewResource(R.layout.dropdown_text);
        countrySpinner.setAdapter(dataAdaptercountry);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = getCountryId(countryListForSpinner.get(i));
                Log.e("Country ::", countryListFoCurrency.get(i));
                businessCurrencySpinner.setSelection(searchIndexCur(countryListFoCurrency.get(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getCountryId(String selectedCountry2) {
        for (Map.Entry<Integer, String> map : this.countryMap.entrySet()) {
            if (map.getValue().equals(selectedCountry2)) {
                return map.getKey().intValue();
            }
        }
        return 0;
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String currency = jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name");
            this.currencyMap.put((String) jsonObject.get("Id"), currency);
            this.currencyListForSpinner.add(currency);
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencyListForSpinner2) {
        ArrayAdapter<String> dataAdapterCurrency = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_text, currencyListForSpinner2);
        dataAdapterCurrency.setDropDownViewResource(R.layout.dropdown_text);
        businessCurrencySpinner.setAdapter(dataAdapterCurrency);
        businessCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrencyId = currencyListForSpinner2.get(i).substring(0, 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int searchIndexCur(String cur_id) {
        int i = 0;
        for (int c = 0; c < currencyListForSpinner.size(); c++) {
            if (currencyListForSpinner.get(c).contains(cur_id)) {
                i = c;
            }
        }
        return i;
    }

    private boolean isValidAbout() {
        boolean response = true;
        String focusfield = "";
        if (f_n.getText().toString().trim().length() == 0) {
            f_n_error.setVisibility(View.VISIBLE);
            f_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                f_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (f_n.getText().toString().trim().length() < 2) {
            //UiUtil.showToast(this, "Please enter first name min two character");
            f_n_error.setVisibility(View.VISIBLE);
            f_n_error.setText("Please enter first name min 2 character");
            f_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                f_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        if (l_n.getText().toString().trim().length() == 0) {
            //UiUtil.showToast(this, "Please enter last name");
            l_n_error.setVisibility(View.VISIBLE);
            l_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                l_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (l_n.getText().toString().trim().length() < 2) {
            //UiUtil.showToast(this, "Please enter last name min two character");
            l_n_error.setVisibility(View.VISIBLE);
            l_n_error.setText("Please enter last name min 2 character");
            l_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                l_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        String phoneNumber = p_n.getText().toString().trim();
        if (phoneNumber.length() == 0) {
            //UiUtil.showToast(this, "Please enter valid phone number");
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                p_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (((countryCode.equalsIgnoreCase("US") || countryCode.equalsIgnoreCase("CA")) && phoneNumber.length() != 10)
                || (!countryCode.equalsIgnoreCase("US") && !countryCode.equalsIgnoreCase("CA") && (phoneNumber.length() < 10 || phoneNumber.length() > 12))) {
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                p_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        if (b_name.getText().toString().trim().length() == 0) {
            response = false;
            b_name_error.setVisibility(View.VISIBLE);
            b_name_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                b_name.requestFocus();
                focusfield = "1";
            }
        } else if (b_name.getText().toString().trim().length() < 2) {
            b_name_error.setVisibility(View.VISIBLE);
            b_name_error.setText("Please enter business name min 2 character");
            b_name_ll.setBackgroundResource(R.drawable.error);
            response = false;
            if (focusfield.equalsIgnoreCase("")) {
                b_name.requestFocus();
                focusfield = "1";
            }
        }
        if (dealsWithId == 0) {
            dealsWithBusiness_error.setBackgroundResource(R.drawable.error);
            deals_with_business_error.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield.equalsIgnoreCase("")) {
                dealsWithBusiness_error.requestFocus();
                focusfield = "1";
            }
        }

        if (businessTypeId == 0) {
            typeBusiness_error.setBackgroundResource(R.drawable.error);
            type_business_error.setVisibility(View.VISIBLE);
            if (focusfield.equalsIgnoreCase("")) {
                typeBusiness_error.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        return response;
    }

    private boolean isValidBusiness() {
        boolean response = true;
        String focusfield = "";

        return response;
    }

    private void reset() {
        try {
            f_n_ll.setBackgroundResource(R.drawable.new_light_blue);
            l_n_ll.setBackgroundResource(R.drawable.new_light_blue);
            p_n_ll.setBackgroundResource(R.drawable.new_light_blue);
            b_name_ll.setBackgroundResource(R.drawable.new_light_blue);
            dealsWithBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            typeBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            f_n_error.setVisibility(View.GONE);
            l_n_error.setVisibility(View.GONE);
            p_n_error.setVisibility(View.GONE);
            b_name_error.setVisibility(View.GONE);
            deals_with_business_error.setVisibility(View.GONE);
            type_business_error.setVisibility(View.GONE);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
        }
    }

    protected void openListDialog(String title, int selectedPos, final List<CountryData> listData, final IDialogListClickListener listener) {
        com.akounto.accountingsoftware.databinding.MobileCodeDilogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.mobile_code_dilog, null, false);
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        binding.txtViewTitle.setText(title);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayoutManager lLayout = new LinearLayoutManager(mContext);
        RecyclerView rView = binding.rvList;
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        AdapterDialogListItem adapter = new AdapterDialogListItem(mContext, listData, selectedPos);
        rView.setAdapter(adapter);
        adapter.registerOnItemClickListener(new AdapterDialogListItem.IonItemSelect() {
            @Override
            public void onItemSelect(int position) {
                if (listener != null)
                    listener.onClick(listData.get(position), position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setfilter(String cd) {
        p_n.setText("");
     /*   p_n.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    edt_phone_number_tooltip.setVisibility(View.VISIBLE);
                }else{
                    edt_phone_number_tooltip.setVisibility(View.GONE);
                }
            }
        });*/
        if (countryCode.equals("US") || countryCode.equals("CA")) {
            p_n_error.setText("Contact number should be 10 digit number.");
            edt_phone_number_tooltip.setText("** Number must contain 10 digit without prefixing 0 & 1.\n" +
                    "** Landline number need area code.");
            UiUtil.removeAll(p_n);
            p_n.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            p_n.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0") || x.startsWith("1")) {
                        p_n.setText("");
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        } else {
            p_n_error.setText("Contact number should be 10 to 12 digit number.");
            edt_phone_number_tooltip.setText("** Contact number without prefixing 0.\n" +
                    "** Landline number need area code.");
            UiUtil.removeAll(p_n);
            p_n.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            p_n.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0")) {
                        p_n.setText("");
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        }

    }
}
