package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.ViewModel.LoginViewModel;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SIgnUpStep0 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText emailET;
    private EditText passwordET;
    TextView tv_error, password_error;
    LinearLayout password_ll;
    RelativeLayout email_ll;
    ImageView mail_ckeck;
    CheckBox checkBox_tnc;
    Context mContext;
    private LoginViewModel model;
    private LifecycleOwner owner;
    boolean success = true;
    LinearLayout back;
    private SignInButton signInButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient = null;
    private static final int RC_SIGN_IN = 1;
    private String fname, email,lname;
    private String idToken;

    protected void setGooglePlusButtonText(SignInButton signInButton) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign up with Google");
                return;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signupsetp0);
        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mContext = this;
            owner = this;
            signInButton = findViewById(R.id.sign_up_button);
            setGooglePlusButtonText(signInButton);
            Bundle b = new Bundle();
            b.putString(Constant.CATEGORY, "sign_up");
            b.putString(Constant.ACTION, "sign_up0_screen_view");
            SplashScreenActivity.sendEvent("sign_up_screen_view", b);
            tv_error = findViewById(R.id.tv_error);
            password_error = findViewById(R.id.password_error);
            email_ll = findViewById(R.id.email_ll);
            password_ll = findViewById(R.id.password_ll);
            back = findViewById(R.id.back);
            this.emailET = findViewById(R.id.emailET);
            this.passwordET = findViewById(R.id.passwordET);

            mail_ckeck = findViewById(R.id.mail_ckeck);
            checkBox_tnc = findViewById(R.id.checkBox_tnc);
            emailET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!UiUtil.isValidEmail(emailET.getText().toString())) {
                        mail_ckeck.setVisibility(View.VISIBLE);
                        Log.e("Checked ::", "true" + s);
                        Bundle b = new Bundle();
                        b.putString(Constant.CATEGORY, "sign_up");
                        b.putString(Constant.ACTION, "capture_email");
                        b.putString(Constant.EMAIL, emailET.getText().toString());
                        SplashScreenActivity.sendEvent("sign_up0_screen_capture_email", b);
                    } else {
                        mail_ckeck.setVisibility(View.GONE);
                        Log.e("Checked ::", "false" + s);
                    }
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SIgnUpStep0.this, WelcomeActivity.class));
                }
            });
            findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    reset();
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "sign_up");
                    b.putString(Constant.ACTION, "button_click");
                    b.putString(Constant.EMAIL, emailET.getText().toString());
                    SplashScreenActivity.sendEvent("sign_up0_button_click", b);

                    if (isValid()) {
                        checkEmail(mContext, emailET.getText().toString());
                    } else {
                        Bundle b1 = new Bundle();
                        b1.putString(Constant.CATEGORY, "sign_up");
                        b1.putString(Constant.ACTION, "email_verify_fail");
                        b1.putString(Constant.EMAIL, emailET.getText().toString());
                        SplashScreenActivity.sendEvent("verify_email_validtion_error", b1);

                    }
                }
            });
            findViewById(R.id.loginText).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    startActivity(new Intent(SIgnUpStep0.this, SignInActivity.class));
                }
            });

            findViewById(R.id.tnc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), TnCActivity.class);
                    intent.putExtra(Constant.LAUNCH_TYPE, "1");
                    startActivity(intent);
                }
            });
            findViewById(R.id.pp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), TnCActivity.class);
                    intent.putExtra(Constant.LAUNCH_TYPE, "2");
                    startActivity(intent);
                }
            });
            model = new ViewModelProviders().of(this).get(LoginViewModel.class);
            firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            authStateListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            };
          /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id2))//you can also use R.string.default_web_client_id
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                    .requestServerAuthCode(getString(R.string.web_client_id2))
                    .requestEmail()
                    .build();*/
            GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id2))//you can also use R.string.default_web_client_id
                    .requestEmail()
                    .build();
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //UiUtil.showToast(mContext,"signInButton");
                    UiUtil.showProgressDialogue(mContext, "", "Loading..");
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    SIgnUpStep0.this.startActivityForResult(intent, RC_SIGN_IN);
                }
            });
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign Up virfy Email");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        success = true;
        emailET.setText("");
        passwordET.setText("");
    }

    public void checkEmail(Context mContext, String email) {

        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        Api api = ApiUtils.getAPIService();
        api.checkEmailExistRequest(Constant.X_SIGNATURE, email).enqueue(new Callback<CheckEmailData>() {
            @Override
            public void onResponse(Call<CheckEmailData> call, Response<CheckEmailData> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            if (!response.body().isData()) {

                                if (success) {
                                    success = false;
                                    tv_error.setVisibility(View.GONE);
                                    Bundle b = new Bundle();
                                    b.putString(Constant.CATEGORY, "sign_up");
                                    b.putString(Constant.ACTION, "email_verify_success");
                                    b.putString(Constant.EMAIL, emailET.getText().toString());
                                    SplashScreenActivity.sendEvent("sign_up0_verify_email_success", b);

                                    RegisterBusiness registerBusiness = new RegisterBusiness();
                                    User user = new User();
                                    user.setEmail(emailET.getText().toString());
                                    user.setPassword(passwordET.getText().toString());
                                    user.setRole("Admin");
                                    registerBusiness.setUser(user);
                                    LocalManager.getInstance().setRegisterBusiness(registerBusiness);
                                    startActivity(new Intent(SIgnUpStep0.this, SignUpDetails.class));
                                }
                            } else {
                                tv_error.setVisibility(View.VISIBLE);
                                tv_error.setText("Email already exists.");
                                email_ll.setBackgroundResource(R.drawable.error);
                                Bundle b = new Bundle();
                                b.putString(Constant.CATEGORY, "sign_up");
                                b.putString(Constant.ACTION, "email_verify_fail");
                                b.putString(Constant.CAUSES, "email already exsit");
                                SplashScreenActivity.sendEvent("sign_up0_verify_email_fail", b);


                            }
                        } else {
                            Bundle b = new Bundle();
                            b.putString(Constant.CATEGORY, "sign_up");
                            b.putString(Constant.ACTION, "email_verify_fail");
                            b.putString(Constant.EMAIL, emailET.getText().toString());
                            SplashScreenActivity.sendEvent("sign_up0_verify_email_fail", b);
                            tv_error.setVisibility(View.VISIBLE);
                            tv_error.setText("Email already exists.");
                            email_ll.setBackgroundResource(R.drawable.error);
                            //Toast.makeText(SIgnUpStep0.this, response.body().getTransactionStatus().getError().getDescription(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        tv_error.setVisibility(View.VISIBLE);
                        tv_error.setText("Email is not valid.");
                        email_ll.setBackgroundResource(R.drawable.error);
                        Bundle b = new Bundle();
                        b.putString(Constant.CATEGORY, "sign_up");
                        b.putString(Constant.ACTION, "email_verify_fail");
                        b.putString(Constant.EMAIL, emailET.getText().toString());
                        SplashScreenActivity.sendEvent("sign_up0_verify_email_fail", b);
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "sign_up");
                    b.putString(Constant.ACTION, "email_verify_fail");
                    b.putString(Constant.EMAIL, emailET.getText().toString());
                    SplashScreenActivity.sendEvent("sign_up0_verify_email_fail", b);
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText(e.getMessage());
                    email_ll.setBackgroundResource(R.drawable.error);
                    LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign Up virfy Email");
                }
            }

            @Override
            public void onFailure(Call<CheckEmailData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Bundle b = new Bundle();
                b.putString(Constant.CATEGORY, "sign_up");
                b.putString(Constant.ACTION, "email_verify_fail");
                b.putString(Constant.EMAIL, emailET.getText().toString());
                SplashScreenActivity.sendEvent("sign_up0_verify_email_fail", b);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText(t.getMessage());
                email_ll.setBackgroundResource(R.drawable.error);
            }
        });
    }

    private boolean isValid() {
        if (UiUtil.isValidEmail(this.emailET.getText().toString())) {
            //UiUtil.showToast(this, "Please enter valid email");
            emailET.requestFocus();
            tv_error.setText("Please enter valid email");
            tv_error.setVisibility(View.VISIBLE);
            email_ll.setBackgroundResource(R.drawable.error);
            return false;
        } else if (this.passwordET.getText().toString().length() == 0) {
            //UiUtil.showToast(this, "Please enter valid password");
            passwordET.requestFocus();
            password_error.setText("Please enter valid password.");
            password_error.setVisibility(View.VISIBLE);
            password_ll.setBackgroundResource(R.drawable.error);
            return false;
        } else if (this.passwordET.getText().toString().length() < 6) {
            //UiUtil.showToast(this, "Password must be more the six characters");
            passwordET.requestFocus();
            password_error.setText("Password must be more the six characters.");
            password_error.setVisibility(View.VISIBLE);
            password_ll.setBackgroundResource(R.drawable.error);
            return false;
        } else if (!checkBox_tnc.isChecked()) {
            UiUtil.showToast(this, "Please check Terms & Conditions");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                handleSignInResult(result);
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            UiUtil.cancelProgressDialogue();
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                idToken = account.getIdToken();
                fname = account.getDisplayName();
                Log.e("F NAME",fname);
                lname = account.getFamilyName();
                Log.e("F NAME",lname);
                email = account.getEmail();
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(credential, account);
            } else {
                // Google Sign In failed, update UI appropriately
                Log.e("TAG", "Login Unsuccessful. " + result.getStatus().getStatusMessage());
                Toast.makeText(this, "Login Unsuccessful" + result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential, GoogleSignInAccount account) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
            if (task.isSuccessful()) {
                model.checkEmail(mContext, task.getResult().getUser().getEmail()).observe(SIgnUpStep0.this, new Observer<CheckEmailData>() {
                    @Override
                    public void onChanged(CheckEmailData checkEmailData) {
                       /* if (external_sign_up) {
                            external_sign_up = false;*/
                        try {
                            if (checkEmailData.getStatus() == 0) {
                                if (!checkEmailData.isData()) {
                                    Intent mainIntent = new Intent(SIgnUpStep0.this, ExternalSignUp.class);
                                    mainIntent.putExtra(Constant.FIRST_NAME, task.getResult().getUser().getDisplayName());
                                    mainIntent.putExtra(Constant.LAST_NAME, lname);
                                    mainIntent.putExtra(Constant.EMAIL, task.getResult().getUser().getEmail());
                                    mainIntent.putExtra(Constant.ID_TOKEN, account.getIdToken());
                                    SIgnUpStep0.this.startActivity(mainIntent);
                                } else {
                                    model.extLogin(mContext, "Google", account.getIdToken()).observe(owner, userDetails -> {
                                        UserDetails ud;
                                        if (userDetails.getStatus() == 0) {
                                            try {
                                                Bundle b = new Bundle();
                                                b.putString(Constant.CATEGORY, "profile");
                                                b.putString(Constant.ACTION, "signin_social");
                                                SplashScreenActivity.sendEvent("profile_signin_social", b);
                                                UiUtil.addLoginToSharedPref(SIgnUpStep0.this, true);
                                                UiUtil.addUserDetails(SIgnUpStep0.this, userDetails);
                                                Intent mainIntent = new Intent(SIgnUpStep0.this, DashboardActivity.class);
                                                SIgnUpStep0.this.startActivity(mainIntent);
                                            } catch (Exception e) {
                                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(SIgnUpStep0.this, userDetails.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(SIgnUpStep0.this, "Fail to get response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
                        }
                        /* }*/
                    }
                });
            } else {
                Log.w("TAG", "signInWithCredential" + task.getException().getMessage());
                task.getException().printStackTrace();
                Toast.makeText(SIgnUpStep0.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        try {
            if (authStateListener != null) {
                FirebaseAuth.getInstance().signOut();
            }
            firebaseAuth.addAuthStateListener(authStateListener);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            if (authStateListener != null) {
                FirebaseAuth.getInstance().signOut();
            }
            firebaseAuth.addAuthStateListener(authStateListener);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
        super.onStop();
    }
    /*

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (authStateListener != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (authStateListener != null) {
                FirebaseAuth.getInstance().signOut();
            }
            firebaseAuth.addAuthStateListener(authStateListener);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (authStateListener != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }
*/

    private void reset() {
        email_ll.setBackgroundResource(R.drawable.new_light_blue);
        password_ll.setBackgroundResource(R.drawable.new_light_blue);

        tv_error.setVisibility(View.GONE);
        password_error.setVisibility(View.GONE);
    }
}