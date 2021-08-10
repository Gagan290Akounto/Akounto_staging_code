package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.ViewModel.BankListViewModel;
import com.akounto.accountingsoftware.util.UiUtil;
import com.plaid.link.Plaid;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkError;
import com.plaid.link.result.LinkResultHandler;
import com.plaid.link.result.LinkSuccessMetadata;

import kotlin.Unit;

public class SignUpStep3 extends AppCompatActivity {

    private Context mContext;
    private BankListViewModel model;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signupsetp3);
        try {
            mContext = this;
            model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(BankListViewModel.class);
            findViewById(R.id.continueButton).setOnClickListener(v -> {
                model.linkBank(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext)).observe(this, bankLinkData -> {
                    if (bankLinkData.getStatus() == 0) {
                        Toast.makeText(getApplicationContext(), bankLinkData.getBankData().getLinkToken(), Toast.LENGTH_SHORT).show();
                        onLinkTokenSuccess(bankLinkData.getBankData().getLinkToken());
                    } else {
                        Toast.makeText(getApplicationContext(), bankLinkData.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            findViewById(R.id.skipButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignUpStep3.this.getApplicationContext(), DashboardActivity.class);
                    SignUpStep3.this.startActivity(intent);
                    SignUpStep3.this.finish();
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
            Log.i(SignUpStep3.class.getSimpleName(), "Not handled");
        }
    }

    private void onLinkTokenSuccess(String token) {
        Plaid.create(
                getApplication(),
                new LinkTokenConfiguration.Builder()
                        .token(token)
                        .build())
                .open(this);
    }

    private final LinkResultHandler myPlaidResultHandler = new LinkResultHandler(
            linkSuccess -> {
                LinkSuccessMetadata metadata = linkSuccess.getMetadata();
                String publicToken = linkSuccess.getPublicToken();
                String institutionId = metadata.getInstitution().getId();
                String institutionName = metadata.getInstitution().getName();
/*
                model.registerBank(mContext, publicToken, institutionId, institutionName).observe(this, new Observer<BankAccountData>() {
                    @Override
                    public void onChanged(BankAccountData bankAccountData) {
                        if (bankAccountData.getStatus() == 0) {
                            Bundle b=new Bundle();
                            b.putString(Constant.CATEGORY,"SignUp");
                            b.putString(Constant.ACTION,"connect_bank");
                            SplashScreenActivity.sendEvent("evtSignUpConnectBank",b);
                            Intent intent = new Intent(SignUpStep3.this.getApplicationContext(), DashboardActivity.class);
                            SignUpStep3.this.startActivity(intent);
                            SignUpStep3.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), bankAccountData.getStatusMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
                return Unit.INSTANCE;
            },
            linkExit -> {
                LinkError error = linkExit.getError();
                try {
                    String displayMessage = error.getDisplayMessage();
                    Toast.makeText(this, displayMessage, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpStep3.this.getApplicationContext(), DashboardActivity.class);
                    SignUpStep3.this.startActivity(intent);
                    SignUpStep3.this.finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                return Unit.INSTANCE;
            }
    );

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpStep3.this,SignInActivity.class));
    }
}
