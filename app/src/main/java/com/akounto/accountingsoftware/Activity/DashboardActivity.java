package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.akounto.accountingsoftware.Activity.Accounting.TransactionsActivity;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceList;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.RegisterBank.Bank;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MainMenu;
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment;
import com.akounto.accountingsoftware.Activity.fragment.HomeDashboardFragment;
import com.akounto.accountingsoftware.Activity.fragment.InvoicesFragment;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    TextView helloNorman;
    Context mContext;
    SignInResponse signInResponse;
    FrameLayout fl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_parent_layout);
        mContext = this;
        this.signInResponse = UiUtil.getUserDetails(this);

        this.helloNorman = findViewById(R.id.tv_company_name);

        findViewById(R.id.bell).setOnClickListener(view -> DashboardActivity.this.lambda$onCreate$5$DashboardActivity(view));
        findViewById(R.id.notification).setOnClickListener(view -> DashboardActivity.this.lambda$onCreate$6$DashboardActivity(view));
        findViewById(R.id.footer_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragmentNotToStack(new HomeDashboardFragment());
            }
        });
        findViewById(R.id.footer_invoice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addFragmentNotToStack(new InvoicesFragment());
                startActivity(new Intent(getApplicationContext(), InvoiceList.class));
            }
        });
        findViewById(R.id.footer_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragmentNotToStack(new MoreFragment());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fl = findViewById(R.id.content_frame);
        addFragmentNotToStack(new HomeDashboardFragment());
        //findViewById(R.id.settingLayout).setOnClickListener(view -> DashboardActivity.this.lambda$onCreate$8$DashboardActivity(view));
        try {
            helloNorman.setText(AppSingle.getInstance().getComp_name());
        } catch (Exception e) {
        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changename(String name) {
        try {
            helloNorman.setText(name);
        } catch (Exception e) {
        }
    }

    public void lambda$onCreate$5$DashboardActivity(View click) {
        //startActivity(new Intent(getApplicationContext(), NotifcationActivity.class));
        UiUtil.showToast(mContext, "Coming Soon");

    }

    public void lambda$onCreate$6$DashboardActivity(View clickLogout) {
        //startActivity(new Intent(this, NotifcationActivity.class));
        AddFragments.addFragmentToDrawerActivity(this, null, MainMenu.class);
    }

    public void lambda$onCreate$8$DashboardActivity(View v) {
        startActivity(new Intent(this, SettingActivity.class));
    }



    public void showDialog() {

        Dialog dialog = new Dialog(mContext);
        if (!dialog.isShowing()) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dilog_bank_import);
            Button btn_import = dialog.findViewById(R.id.btn_import);
            Button btn_cancle = dialog.findViewById(R.id.btn_cancel);
            TextView descp=dialog.findViewById(R.id.descp);
            descp.setText("Do you really want to Exit!");
            btn_import.setText("Ok");
            btn_import.setOnClickListener(v -> {
                dialog.dismiss();
                close();
                finish();
            });
            btn_cancle.setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.show();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void addFragmentToStack(Fragment fragment) {
        try {
            if (fl == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(null);
            ft.add(fl.getId(), fragment, fragment.getClass().getName());
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        } catch (Exception e) {
        }
    }





    public void addFragmentNotToStack(Fragment fragment) {
        try {
            if (fl == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(null);
            ft.add(fl.getId(), fragment, fragment.getClass().getName());
            if (false)
                ft.addToBackStack(fragment.getClass().getName());
            //else
                //clearBackStack();
            ft.commit();
        } catch (Exception e) {
        }
    }

    public void addFragmentBackAllow(Fragment fragment) {
        try {
            if (fl == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(null);
            ft.add(fl.getId(), fragment, fragment.getClass().getName());

            ft.addToBackStack(fragment.getClass().getName());

            ft.commit();
        } catch (Exception e) {
        }
    }


    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
         finish();
        } else {
            super.onBackPressed();
            clearBackStack();
            showDialog();
        }
    }
    public void clearBackStack() {
        try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }
    private void close() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    @Override
    protected void onDestroy(){
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
}
