package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CountryData;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.Listner.IDialogListClickListener;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.ViewModel.ProfileViewModel;
import com.akounto.accountingsoftware.adapter.AdapterDialogListItem;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExternalSignUp extends AppCompatActivity implements View.OnClickListener {
    String countryCode = "US";
    int selectedPos = 0;
    ArrayList<CountryData> countryData = new ArrayList<>();
    private ProfileViewModel model;
    private TextView submit, pre,phone_code_tv,edt_phone_number_tooltip;
    ImageView couFlagSpinner;
    private EditText b_name, m_number;
    private String email, id_token;
    private Spinner typeBusinessSpinner;
    private Spinner dealsWithBusinessSpinner;
    private Context mContext;
    private LoginData loginData;
    private LinearLayout p_n_ll, b_name_ll, dealsWithBusiness_error, typeBusiness_error, back;
    TextView p_n_error, b_name_error, c_care, deals_with_business_error, type_business_error;
    Map<Integer, String> typeBusnisMap = new HashMap();
    List<String> typeBusniSpinner = new ArrayList();
    Map<Integer, String> dealsMap = new HashMap();
    List<String> dealsinForSpinner = new ArrayList();
    private String businessType = "", dealsWith = "", business_name;
    private int businessTypeId = 0, dealsWithId = 0;
    int selectedCountry = 0;
    String selectedCurrencyId = "USD", first = "NA", last = "NA";
    Spinner countrySpinner;
    Spinner businessCurrencySpinner;
    List<String> countryListForSpinner = new ArrayList();
    List<String> countryListFoCurrency = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    Map<String, String> currencyMap = new HashMap();
    Map<Integer, String> countryMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_extlogin);
        try {
            mContext = this;
            Bundle b = new Bundle();
            b.putString(Constant.CATEGORY, "sign_up");
            b.putString(Constant.ACTION, "screenview");
            b.putString(Constant.EMAIL, email);
            SplashScreenActivity.sendEvent("social_signup_screenview", b);
            typeBusinessSpinner = findViewById(R.id.typeBusinessSpinner);
            dealsWithBusinessSpinner = findViewById(R.id.dealsWithBusinessSpinner);
            m_number = findViewById(R.id.ed_mobile);
            submit = findViewById(R.id.continueButton);
            pre = findViewById(R.id.previousButton);
            back = findViewById(R.id.back);
            edt_phone_number_tooltip = findViewById(R.id.edt_phone_number_tooltip);
            couFlagSpinner = findViewById(R.id.phone_code_spiner);
            phone_code_tv = findViewById(R.id.phone_code_tv);
            p_n_ll = findViewById(R.id.phone_number_ll);
            b_name_ll = findViewById(R.id.business_name_ll);
            c_care = findViewById(R.id.cusmoter_care);
            dealsWithBusiness_error = findViewById(R.id.dealsWithBusiness_error);
            typeBusiness_error = findViewById(R.id.typeBusiness_error);

            p_n_error = findViewById(R.id.edt_phone_number_error);
            b_name_error = findViewById(R.id.business_name_error);
            deals_with_business_error = findViewById(R.id.deals_with_business_error);
            type_business_error = findViewById(R.id.type_business_error);
            pre.setVisibility(View.GONE);
            b_name = findViewById(R.id.business_name);
            this.countrySpinner = findViewById(R.id.countrySpinner);
            this.businessCurrencySpinner = findViewById(R.id.businessCurrencySpinner);
            submit.setOnClickListener(this);
            String[] name=getIntent().getStringExtra(Constant.FIRST_NAME).split(" ");
            try {
                first = name[0];
                last =  name[name.length-1];
            }catch (Exception e){
            }
            email = getIntent().getStringExtra(Constant.EMAIL);
            id_token = getIntent().getStringExtra(Constant.ID_TOKEN);
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
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ExternalSignUp.this, SIgnUpStep0.class));
                }
            });
            settypeBusinessSpinner();

            model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);

            fetchCountries();
            fetchCurrencies();
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
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
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
        ////Collections.sort(countryListForSpinner);
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
                ExternalSignUp.this.businessTypeId = pos;
                ExternalSignUp.this.businessType = typeBusniSpinner.get(pos);
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
                ExternalSignUp.this.dealsWithId = pos;
                ExternalSignUp.this.dealsWith = dealsinForSpinner.get(pos);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle b = new Bundle();
        b.putString(Constant.CATEGORY, "sign_up");
        b.putString(Constant.ACTION, "onClick");
        b.putString(Constant.EMAIL, email);
        SplashScreenActivity.sendEvent("social_signup_onClick", b);
        String bn = b_name.getText().toString();
        reset();
        if (isValidAbout()) {

           // LoginRepo.prinLogs("" +JsonUtils.getExtRegRequst(m_number.getText().toString(), first,last, email, bn, id_token, String.valueOf(ExternalSignUp.this.dealsWithId), ExternalSignUp.this.dealsWith, String.valueOf(ExternalSignUp.this.businessTypeId), ExternalSignUp.this.businessType, "" + selectedCountry, "" + selectedCurrencyId), 5, "Sign up Socel");
            model.extReg(mContext, JsonUtils.getExtRegRequst(m_number.getText().toString(), first,last, email, bn, id_token, String.valueOf(ExternalSignUp.this.dealsWithId), ExternalSignUp.this.dealsWith, String.valueOf(ExternalSignUp.this.businessTypeId), ExternalSignUp.this.businessType, "" + selectedCountry, "" + selectedCurrencyId,phone_code_tv.getText().toString())).observe(this, userDetails -> {
                if (userDetails.getStatus() == 0) {
                    try {
                        Bundle b11 = new Bundle();
                        b11.putString(Constant.CATEGORY, "sign_up");
                        b11.putString(Constant.ACTION, "social");
                        b11.putString(Constant.EMAIL, email);
                        SplashScreenActivity.sendEvent("sign_up_social", b11);
                        UiUtil.addLoginToSharedPref(ExternalSignUp.this, true);
                        UiUtil.addUserDetails(ExternalSignUp.this, userDetails);
                        AppSingle.getInstance().setComp_name(new Gson().fromJson(userDetails.getData().getUserDetails(), UserDetails.class).getActiveBusiness().getName());
                        AppSingle.getInstance().setEmail(email);
                        Intent mainIntent = new Intent(ExternalSignUp.this, DashboardActivity.class);
                        ExternalSignUp.this.startActivity(mainIntent);
                    } catch (Exception e) {
                        LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up Socel");
                        Bundle b11 = new Bundle();
                        b11.putString(Constant.CATEGORY, "sign_up");
                        b11.putString(Constant.ACTION, "social");
                        b11.putString(Constant.EMAIL, email);
                        SplashScreenActivity.sendEvent("sign_up_social_fail", b11);
                    }
                } else {
                    Toast.makeText(ExternalSignUp.this, userDetails.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    Bundle b11 = new Bundle();
                    b11.putString(Constant.CATEGORY, "sign_up");
                    b11.putString(Constant.ACTION, "social");
                    b11.putString(Constant.EMAIL, email);
                    SplashScreenActivity.sendEvent("sign_up_social_fail", b11);
                }
            });
        }

    }


    private boolean isValidAbout() {
        boolean response = true;
        String focusfield = "";
        String phoneNumber = m_number.getText().toString().trim();
        if (phoneNumber.length() == 0) {
            //UiUtil.showToast(this, "Please enter valid phone number");
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                m_number.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (((countryCode.equalsIgnoreCase("US") || countryCode.equalsIgnoreCase("CA")) && phoneNumber.length() != 10)
                || (!countryCode.equalsIgnoreCase("US") && !countryCode.equalsIgnoreCase("CA") && (phoneNumber.length() < 10 || phoneNumber.length() > 12))) {
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                m_number.requestFocus();
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

    private void reset() {
        try {
            b_name_ll.setBackgroundResource(R.drawable.new_light_blue);
            p_n_ll.setBackgroundResource(R.drawable.new_light_blue);
            dealsWithBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            typeBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            b_name_error.setVisibility(View.GONE);
            p_n_error.setVisibility(View.GONE);
            deals_with_business_error.setVisibility(View.GONE);
            type_business_error.setVisibility(View.GONE);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "create bussiness");
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
        m_number.setText("");
        if (countryCode.equals("US") || countryCode.equals("CA")) {
            p_n_error.setText("Contact number should be 10 digit number.");
            edt_phone_number_tooltip.setText("** Number must contain 10 digit without prefixing 0 & 1.\n" +
                    "** Landline number need area code.");
            UiUtil.removeAll(m_number);
            m_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            m_number.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0") || x.startsWith("1")) {
                        m_number.setText("");
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
            UiUtil.removeAll(m_number);
            m_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            m_number.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0")) {
                        m_number.setText("");
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
