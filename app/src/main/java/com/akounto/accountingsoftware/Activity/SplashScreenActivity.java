package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.util.LogsPrint;
import com.akounto.accountingsoftware.util.UiUtil;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private Context mContext;
    public static FirebaseAnalytics mFirebaseAnalytics;
    private int i = 0;
    int a[] = {1, 2};
    InstallReferrerClient referrerClient;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            ReferrerDetails response = null;
                            try {
                                response = referrerClient.getInstallReferrer();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            LoginRepo.prinLogs(referrerUrl+"\n"+referrerClickTime+"\n"+appInstallTime+instantExperienceLaunched, 5, "SplashScreenActivity GOOGLE API referrer");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            LoginRepo.prinLogs("FEATURE_NOT_SUPPORTED", 5, "SplashScreenActivity GOOGLE API referrer");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            LoginRepo.prinLogs("SERVICE_UNAVAILABLE", 5, "SplashScreenActivity GOOGLE API referrer");
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {

                }
            });
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "SplashScreenActivity");
        }
        try {
            this.imageView = findViewById(R.id.app_logo);
            this.imageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        if (i == 0) {
                            i++;
                            //if (!UiUtil.isLoggedin(SplashScreenActivity.this)) {
                            if (UiUtil.isFirstLogin(getApplicationContext())) {
                                Log.e("Count %s", String.valueOf(i));
                                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                            } else {
                                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
                            }/*} else {
                                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                            }*/
                            SplashScreenActivity.this.finish();
                        }
                    } catch (Exception e) {
                        LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "SplashScreenActivity");
                        //LoginRepo.prinLogs(""+Log.getStackTraceString(e),5,"SplashScreenActivity");
                    }
                }
            }, 2000);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "SplashScreenActivity");
        }
    }

    public static void sendEvent(String event_name, Bundle b) {
        try {
            if (Constant.ANALATICS_ON && b != null)
                SplashScreenActivity.mFirebaseAnalytics.logEvent(event_name, b);
            else if (Constant.ANALATICS_ON) {
                SplashScreenActivity.mFirebaseAnalytics.logEvent(event_name, new Bundle());
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "SplashScreenActivity");
        }
    }
}
