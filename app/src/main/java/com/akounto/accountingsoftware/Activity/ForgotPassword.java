package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.model.ForgotPasswordData;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    TextView email, submit,email_error,go_back,tv_success;
    String emal = "";
    Context mContext;
    RelativeLayout success_alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot_password);
        try {
            mContext = this;
            email = findViewById(R.id.email_forget);
            submit = findViewById(R.id.forgotButton);
            email_error = findViewById(R.id.email_error);
            success_alert = findViewById(R.id.success_alert);
            tv_success = findViewById(R.id.tv_success);
            go_back = findViewById(R.id.go_back);
            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!UiUtil.isValidEmail(email.getText().toString())) {
                        email_error.setVisibility(View.GONE);
                    } else {
                        email_error.setVisibility(View.VISIBLE);
                    }
                }
            });
            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emal = email.getText().toString();
                    email_error.setVisibility(View.GONE);
                    if (!UiUtil.isValidEmail(emal)) {
                        UiUtil.showProgressDialogue(mContext, "", "Please wait...");
                        JsonObject requet = new JsonParser().parse("{ \"Email\":\"" + emal + "\"}").getAsJsonObject();
                        Api api = ApiUtils.getAPIService();
                        api.fogotPasswordRequest(Constant.X_SIGNATURE, requet).enqueue(new Callback<ForgotPasswordData>() {
                            @Override
                            public void onResponse(Call<ForgotPasswordData> call, Response<ForgotPasswordData> response) {
                                UiUtil.cancelProgressDialogue();
                                ForgotPasswordData passwordData = response.body();
                                try {
                                    if (passwordData.isData()) {
                                        Bundle b = new Bundle();
                                        b.putString(Constant.CATEGORY, "profile");
                                        b.putString(Constant.ACTION, "forgot_password");
                                        SplashScreenActivity.mFirebaseAnalytics.logEvent("profile_forgot_password", b);
                                        success_alert.setVisibility(View.VISIBLE);
                                        tv_success.setText("Check your email\n" +
                                                "If we find " + emal + " in our system, we will send" +
                                                "you an email with a link to reset your password.\n" +
                                                "\n" +
                                                "If you don't receive the email, check your\n" +
                                                "spam folder or contact us.");

                                    } else {
                                        success_alert.setVisibility(View.GONE);
                                        if (!passwordData.getTransactionStatus().getIsSuccess()) {
                                           // UiUtil.showToast(mContext, ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                                            email_error.setVisibility(View.VISIBLE);
                                            email_error.setText(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                                        }
                                    }
                                } catch (Exception e) {
                                    success_alert.setVisibility(View.GONE);
                                    email_error.setVisibility(View.VISIBLE);
                                    email_error.setText("Not able to send forgot password request");
                                }
                            }

                            @Override
                            public void onFailure(Call<ForgotPasswordData> call, Throwable t) {
                                UiUtil.cancelProgressDialogue();
                                success_alert.setVisibility(View.GONE);
                                email_error.setVisibility(View.VISIBLE);
                                email_error.setText("Not able to send forgot password request");
                            }
                        });
                    } else {
                        email_error.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}
