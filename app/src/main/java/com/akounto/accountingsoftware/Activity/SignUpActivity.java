package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;

public class SignUpActivity extends AppCompatActivity {
    EditText emailET;
    EditText passwordET;
    EditText rePasswordET;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_signup1st);
        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SignUpActivity.this.lambda$onCreate$0$SignUpActivity(view);
            }
        });
        findViewById(R.id.loginText).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SignUpActivity.this.lambda$onCreate$1$SignUpActivity(view);
            }
        });
        inItUi();
    }

    public /* synthetic */ void lambda$onCreate$0$SignUpActivity(View click) {
        if (isValid()) {
            RegisterBusiness registerBusiness = new RegisterBusiness();
            User user = new User();
            user.setEmail(this.emailET.getText().toString());
            user.setPassword(this.passwordET.getText().toString());
            user.setRole("Admin");
            registerBusiness.setUser(user);
            LocalManager.getInstance().setRegisterBusiness(registerBusiness);
            startActivity(new Intent(this, GetStartedActivity.class));
        }
    }

    public /* synthetic */ void lambda$onCreate$1$SignUpActivity(View click) {
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void inItUi() {
        this.emailET = findViewById(R.id.emailET);
        this.passwordET = findViewById(R.id.passwordET);
        this.rePasswordET = findViewById(R.id.rePasswordET);
    }

    private boolean isValid() {
        if (this.emailET.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter valid email");
            return false;
        } else if (this.passwordET.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter valid password");
            return false;
        } else if (this.passwordET.getText().toString().length() == this.rePasswordET.getText().toString().length()) {
            return true;
        } else {
            UiUtil.showToast(this, "Password does not match");
            return false;
        }
    }
}
