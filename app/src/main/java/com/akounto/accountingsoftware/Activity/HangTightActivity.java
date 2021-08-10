package com.akounto.accountingsoftware.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.akounto.accountingsoftware.R;
import com.google.gson.Gson;
import cn.pedant.SweetAlert.SweetAlertDialog;
import lal.adhish.gifprogressbar.GifView;

public class HangTightActivity extends AppCompatActivity {

    GifView progressGif;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_hang_tight);
        GifView gifView = findViewById(R.id.progressGif);
        this.progressGif = gifView;
        gifView.setImageResource(R.drawable.hangon);
        sendDataToServer();
    }

    private void sendDataToServer() {
        //DataFetcher.signUp(this, new Gson().toJson(LocalManager.getInstance().getRegisterBusiness()), loginResponse(), HangTightActivity.class, updateErrorListener(), "https://beta.api.akounto.com/api/profile/register-business");
    }

    /* access modifiers changed from: private */
    public void showDialogue() {
        new SweetAlertDialog(this, 2).setTitleText("Success").setContentText("Check you email and confirm your email").setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(SweetAlertDialog sDialog) {
                Intent intent = new Intent(HangTightActivity.this.getApplicationContext(), SignInActivity.class);
                intent.setFlags(335577088);
                HangTightActivity.this.startActivity(intent);
                HangTightActivity.this.finish();
            }
        }).show();
    }
/*
    private Response.Listener<SignUpResponse> loginResponse() {
        return new Response.Listener<SignUpResponse>() {
            public void onResponse(SignUpResponse response) {
                Log.d("222222", "2222222");
                HangTightActivity.this.showDialogue();
            }
        };
    }

    private Response.ErrorListener updateErrorListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("3333333", "333333");
                UiUtil.cancelProgressDialogue();
                error.printStackTrace();
                HangTightActivity.this.showDialogue();
            }
        };
    }*/
}
