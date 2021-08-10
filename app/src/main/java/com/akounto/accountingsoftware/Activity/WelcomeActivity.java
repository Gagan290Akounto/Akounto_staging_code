package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.adapter.SliderPagerAdapter;
import com.akounto.accountingsoftware.util.UiUtil;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    ReferrerDetails response = null;
    InstallReferrerClient referrerClient;
    private ViewPager viewPager;
    private SliderPagerAdapter myViewPagerAdapter;
    private com.google.android.material.tabs.TabLayout dotsLayout;
    private ImageView[] dots;
    private int[] layouts;
    private TextView btnSignUp, btnSignIn;
    //private PrefManager prefManager;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 7000; // time in milliseconds between successive task executions.
    int current = 0;
    Bundle b = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        try {
            Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                }
            }).check();
            b = new Bundle();
            b.putString(Constant.CATEGORY, "welcome");
            b.putString(Constant.ACTION, "welcome_screen");
            SplashScreenActivity.sendEvent("welcome_screenview", b);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            dotsLayout = (com.google.android.material.tabs.TabLayout) findViewById(R.id.tab_layout);
            dotsLayout.setupWithViewPager(viewPager, true);
            btnSignIn = (TextView) findViewById(R.id.btn_signin);
            btnSignUp = (TextView) findViewById(R.id.btn_signup);
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b = new Bundle();
                    b.putString(Constant.CATEGORY, "welcome");
                    b.putString(Constant.ACTION, "click_sign_in");
                    SplashScreenActivity.sendEvent("welcome_click_sign_in", b);
                    launchHomeScreen();
                }
            });
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b = new Bundle();
                    b.putString(Constant.CATEGORY, "welcome");
                    b.putString(Constant.ACTION, "click_sign_up");
                    SplashScreenActivity.sendEvent("welcome_click_sign_up", b);
                    startActivity(new Intent(WelcomeActivity.this, SIgnUpStep0.class));
                }
            });
            // layouts of welcome sliders
            layouts = new int[]{
                    R.layout.welcome_slide1,
                    R.layout.welcome_slide2,
                    R.layout.welcome_slide3
            };

            myViewPagerAdapter = new SliderPagerAdapter(layouts, getApplicationContext());
            viewPager.setAdapter(myViewPagerAdapter);

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (current == layouts.length) {
                        current = 0;
                    }
                    viewPager.setCurrentItem(current++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);

            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established.
                            try {
                                response = referrerClient.getInstallReferrer();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            LoginRepo.prinLogs(referrerUrl, 4, "referrer_OK");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app.
                            try {
                                response = referrerClient.getInstallReferrer();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            String referrerUrl2 = response.getInstallReferrer();
                            LoginRepo.prinLogs(referrerUrl2, 4, "referrer_FEATURE_NOT_SUPPORTED");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection couldn't be established.
                            try {
                                response = referrerClient.getInstallReferrer();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            String referrerUrl3 = response.getInstallReferrer();
                            LoginRepo.prinLogs(referrerUrl3, 4, "SERVICE_UNAVAILABLE");
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });

        } catch (Exception e) {
        }
    }

    private void launchHomeScreen() {
        UiUtil.SetFirstLogin(getApplicationContext(), true);
        startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));
        finish();
    }
}
