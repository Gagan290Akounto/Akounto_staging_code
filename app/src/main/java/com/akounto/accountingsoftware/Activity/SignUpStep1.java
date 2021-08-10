package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;

public class SignUpStep1 extends AppCompatActivity {

    private EditText f_n, l_n, p_n;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signupsetp1);
        try {
            f_n = findViewById(R.id.first_name);
            l_n = findViewById(R.id.last_name);
            p_n = findViewById(R.id.edt_phone_number);
            findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValid()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"sign_up");
                        b.putString(Constant.ACTION,"personal_info");
                        b.putString(Constant.PHONE,p_n.getText().toString());
                        SplashScreenActivity.sendEvent("sign_up_personal_info",b);
                        Intent i = new Intent(SignUpStep1.this, SignUpStep2.class);
                        i.putExtra(UiConstants.FIRST_NAME, f_n.getText().toString());
                        i.putExtra(UiConstants.LAST_NAME, l_n.getText().toString());
                        i.putExtra(UiConstants.PHONE_NUMBER, p_n.getText().toString());
                        startActivity(i);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private boolean isValid() {
        if (f_n.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter first name");
            return false;
        } else if (f_n.getText().toString().length() < 2) {
            UiUtil.showToast(this, "Please enter first name min two character");
            return false;
        } else if (l_n.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter last name");
            return false;
        } else if (l_n.getText().toString().length() < 2) {
            UiUtil.showToast(this, "Please enter last name min two character");
            return false;
        } else if (p_n.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter valid phone number");
            return false;
        } else if (p_n.getText().toString().length() < 10) {
            UiUtil.showToast(this, "Please enter valid phone number");
            return false;
        } else {
            return true;
        }
    }
}
