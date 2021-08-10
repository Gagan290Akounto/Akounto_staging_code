package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.response.SignUp.SignUpResponse;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpStep2 extends AppCompatActivity {

    private String fist = "", last = "", phone = "", businessType = "", dealsWith = "", business_name;
    private int businessTypeId = 0, dealsWithId = 0;
    EditText b_name;
    RegisterBusiness registerBusiness;
    private Spinner typeBusinessSpinner;
    private Spinner dealsWithBusinessSpinner;
    Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signupsetp2);
        try {
            mContext = this;
            b_name = findViewById(R.id.business_name);
            this.registerBusiness = LocalManager.getInstance().getRegisterBusiness();
            fist = getIntent().getStringExtra(UiConstants.FIRST_NAME);
            last = getIntent().getStringExtra(UiConstants.LAST_NAME);
            phone = getIntent().getStringExtra(UiConstants.PHONE_NUMBER);
            settypeBusinessSpinner();

            findViewById(R.id.previousButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignUpStep2.this.finish();
                }
            });
            findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user;
                    try {
                        user = registerBusiness.getUser();
                        user.setFirstName(fist);
                        user.setLastName(last);
                    } catch (Exception e) {
                        user = new User();
                        user.setFirstName("NA");
                        user.setLastName("NA");
                    }
                    SignUpStep2.this.registerBusiness.setUser(user);
                    SignUpStep2.this.registerBusiness.setBusinessName(SignUpStep2.this.b_name.getText().toString());
                    SignUpStep2.this.registerBusiness.setBusinessEntity(SignUpStep2.this.businessType);
                    SignUpStep2.this.registerBusiness.setBusinessEntityId(SignUpStep2.this.businessTypeId + 1);
                    SignUpStep2.this.registerBusiness.setIndustryTypeId(SignUpStep2.this.dealsWithId + 1);
                    SignUpStep2.this.registerBusiness.setIndustryTypeName(SignUpStep2.this.dealsWith);
                    SignUpStep2.this.registerBusiness.setPhone(phone);
                    SignUpStep2.this.registerBusiness.setCountry(1);
                    SignUpStep2.this.registerBusiness.setBusinessCurrency("USD");
                    Log.d("Register :: ", UiUtil.getRegRequst(registerBusiness).toString());
                    business_name = b_name.getText().toString();
                    if (isValid()) {
                        UiUtil.showProgressDialogue(mContext, "", "Please wait...");
                        JsonObject requet = UiUtil.getRegRequst(registerBusiness);
                        Api api = ApiUtils.getAPIService();
                        api.register(Constant.X_SIGNATURE, requet).enqueue(new Callback<SignUpResponse>() {
                            @Override
                            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                                UiUtil.cancelProgressDialogue();
                                Bundle b=new Bundle();
                                b.putString(Constant.CATEGORY,"sign_up");
                                b.putString(Constant.ACTION,"business_info");
                                b.putString(Constant.COMPANY,business_name);
                                SplashScreenActivity.sendEvent("sign_up_connect_bank",b);
                                SignUpResponse responsed = response.body();
                                try {
                                    if (responsed.getTransactionStatus().getIsSuccess()) {
                                        UiUtil.showToast(mContext, "Register successfully");
                                        UiUtil.addLoginToSharedPref(SignUpStep2.this, true);
                                        UiUtil.addUserDetails(SignUpStep2.this, responsed);
                                        Intent intent = new Intent(SignUpStep2.this.getApplicationContext(), SignUpStep3.class);
                                        SignUpStep2.this.startActivity(intent);
                                        SignUpStep2.this.finish();
                                    } else {
                                        if (!responsed.getTransactionStatus().getIsSuccess()) {
                                            UiUtil.showToast(mContext, ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                                        } else {
                                            UiUtil.showToast(mContext, "Not able to send register request");
                                        }
                                    }
                                } catch (Exception e) {
                                    UiUtil.showToast(mContext, "Something went wrong");
                                }
                            }

                            @Override
                            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                                UiUtil.cancelProgressDialogue();
                                UiUtil.showToast(mContext, "Not able to send register request");
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private boolean isValid() {
        if (b_name.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter business name");
            return false;
        } else if (b_name.getText().toString().length() < 2) {
            UiUtil.showToast(this, "Please enter business name min two character");
            return false;
        } else {
            return true;
        }
    }

    private void settypeBusinessSpinner() {
        this.typeBusinessSpinner = findViewById(R.id.typeBusinessSpinner);
        final List<String> finalCategories = new ArrayList<>();
        finalCategories.add("LLC");
        finalCategories.add("Non Profit Organization");
        finalCategories.add("Partnership or LLP");
        finalCategories.add("Proprietorship");
        finalCategories.add("Others");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, finalCategories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        this.typeBusinessSpinner.setAdapter(dataAdapter);
        this.typeBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                SignUpStep2.this.businessTypeId = pos;
                SignUpStep2.this.businessType = finalCategories.get(pos);
                Log.e("pospospos", finalCategories.get(pos));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.dealsWithBusinessSpinner = findViewById(R.id.dealsWithBusinessSpinner);
        final List<String> finalCategories2 = new ArrayList<>();
        finalCategories2.add("Book-keeping");
        finalCategories2.add("Others");
        finalCategories2.add("Construction");
        finalCategories2.add("Financial Services");
        finalCategories2.add("Health Services");
        finalCategories2.add("Information Technology Services");
        finalCategories2.add("Marketing Services");
        finalCategories2.add("Medical Services");
        finalCategories2.add("Non Profit Organizations");
        finalCategories2.add("Photography and creative services");
        finalCategories2.add("Real Estate");
        finalCategories2.add("Retailers and Resellers");
        finalCategories2.add("Others");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, finalCategories2);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        this.dealsWithBusinessSpinner.setAdapter(dataAdapter2);
        this.dealsWithBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                SignUpStep2.this.dealsWithId = pos;
                SignUpStep2.this.dealsWith = finalCategories2.get(pos);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}