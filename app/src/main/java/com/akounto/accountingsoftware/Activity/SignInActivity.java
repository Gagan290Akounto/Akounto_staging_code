package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.UpdateResponse;
import com.akounto.accountingsoftware.ui.dashboard.activity.DashboardMainActivity;
import com.akounto.accountingsoftware.util.AppSingle;
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
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.ViewModel.LoginViewModel;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.subhrajyoti.passwordview.PasswordView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText emailET;
    private EditText passwordET;
    RelativeLayout password_ll;
    private TextView signup,pass_show_hide;
    private TextView forgot_password;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient = null;
    private static final int RC_SIGN_IN = 1;
    private String name, email;
    private String idToken;
    private LoginViewModel model;
    private LifecycleOwner owner;
    private LinearLayout back;
    private TextView tv_error, password_error;
    private LinearLayout googlesign_dummy;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_signin);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mContext = this;
            owner = this;
            try {
                getUpdateDilog();
            } catch (Exception e) {
            }
            tv_error = findViewById(R.id.tv_error);
            googlesign_dummy = findViewById(R.id.sign_in_button1);
            password_ll = findViewById(R.id.password_ll);
            password_error = findViewById(R.id.password_error);
            this.emailET = findViewById(R.id.emailET);
            back = findViewById(R.id.sign_in_header);
            pass_show_hide = findViewById(R.id.tvShow);
            passwordET = findViewById(R.id.passwordET);
            signup = findViewById(R.id.text_signup);
            forgot_password = findViewById(R.id.txt_forgot_password);
            pass_show_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(pass_show_hide.getText().toString().equals("Show")){
                            passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pass_show_hide.setText("Hide");
                        } else{
                            passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pass_show_hide.setText("Show");
                        }

                }
            });
            firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtil.SetFirstLogin(getApplicationContext(), false);
                    startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));
                }
            });
            emailET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                   /* if (!UiUtil.isValidEmail(emailET.getText().toString())) {
                        mail_ckeck.setVisibility(View.VISIBLE);
                        Log.e("Checked ::", "true" + s);
                    } else {
                        mail_ckeck.setVisibility(View.GONE);
                        Log.e("Checked ::", "false" + s);
                    }*/
                }
            });

            firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    reset();
                    login();
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInActivity.this.startActivity(new Intent(SignInActivity.this, SignupOptions.class));
                }
            });

            forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInActivity.this.startActivity(new Intent(SignInActivity.this, ForgotPassword.class));
                }
            });
            //model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
            model = new ViewModelProviders().of(this).get(LoginViewModel.class);
            authStateListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            };
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id2))//you can also use R.string.default_web_client_id
                    .requestEmail()
                    .build();
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton = findViewById(R.id.sign_in_button);
           setGooglePlusButtonText(signInButton);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //UiUtil.showToast(mContext,"signInButton");
                    UiUtil.showProgressDialogue(mContext, "", "Loading..");
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    SignInActivity.this.startActivityForResult(intent, RC_SIGN_IN);
                }
            });
            googlesign_dummy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtil.showProgressDialogue(mContext, "", "Loading..");
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    SignInActivity.this.startActivityForResult(intent, RC_SIGN_IN);
                }
            });
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        emailET.setText("");
        //passwordET.setText("");
    }

    public void login() {
        try {
            String user = this.emailET.getText().toString();
            String pass = this.passwordET.getText().toString();
            if (isValid()) {
                loadLogin(mContext, user, pass);
            }
        } catch (Exception e) {
            emailET.requestFocus();
            tv_error.setText(e.getMessage());
            tv_error.setVisibility(View.VISIBLE);
            emailET.setBackgroundResource(R.drawable.error);
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
        }
    }

    public void loadLogin(Context mContext, String username, String pass) {

        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        Api api = ApiUtils.getAPIService();
        api.loginRequest(Constant.X_SIGNATURE, username, pass, Constant.GRANT_TYPE).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, retrofit2.Response<LoginData> response) {
                UiUtil.cancelProgressDialogue();
                LoginData loginData = response.body();
                try {
                    if (response.code() == 200) {
                        UiUtil.addLoginToSharedPref(SignInActivity.this, true);
                        UiUtil.addUserDetails(SignInActivity.this, loginData);
                        //Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                        Intent intent = new Intent(SignInActivity.this, DashboardMainActivity.class);
                        SignInActivity.this.startActivity(intent);
                        SignInActivity.this.finish();
                        AppSingle.getInstance().setComp_name(new Gson().fromJson(loginData.getUserDetails(), UserDetails.class).getActiveBusiness().getName());
                        Bundle b = new Bundle();
                        b.putString(Constant.CATEGORY, "profile");
                        b.putString(Constant.ACTION, "signin");
                        SplashScreenActivity.sendEvent("profile_signin", b);
                        AppSingle.getInstance().setEmail(username);
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        // Toast.makeText(mContext, error.getError_description(), Toast.LENGTH_SHORT).show();
                        emailET.requestFocus();
                        if (!error.getError_description().equalsIgnoreCase(""))
                            tv_error.setText(error.getError_description());
                        tv_error.setVisibility(View.VISIBLE);
                        emailET.setBackgroundResource(R.drawable.error);
                    }
                } catch (Exception e) {
                    //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    emailET.requestFocus();
                    tv_error.setText(e.getMessage());
                    tv_error.setVisibility(View.VISIBLE);
                    emailET.setBackgroundResource(R.drawable.error);
                    LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                // Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
                tv_error.setText(t.getMessage());
                tv_error.setVisibility(View.VISIBLE);
                emailET.setBackgroundResource(R.drawable.error);
                LoginRepo.prinLogs("" + Log.getStackTraceString(t), 5, "Sign in");
            }
        });
    }

    public void showErrorDialogue() {
        new SweetAlertDialog(this, 1).setTitleText("Oops...").setContentText("Fail to signin.Please try again with proper id and password").setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
    }

    protected void setGooglePlusButtonText(SignInButton signInButton) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign in with Google");
                return;
            }
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
                try {
                    idToken = account.getIdToken();
                    name = account.getDisplayName();
                    email = account.getEmail();
                } catch (Exception e) {

                }
                try {
                    //Toast.makeText(this, result.getSignInAccount().getServerAuthCode(), Toast.LENGTH_SHORT).show();
                    Log.w("Token :: ", result.getSignInAccount().getServerAuthCode());
                } catch (Exception e) {
                    Log.w("Token Error :: ", e.getLocalizedMessage());
                }
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
                model.checkEmail(mContext, task.getResult().getUser().getEmail()).observe(SignInActivity.this, new Observer<CheckEmailData>() {
                    @Override
                    public void onChanged(CheckEmailData checkEmailData) {
                       /* if (external_sign_up) {
                            external_sign_up = false;*/
                        try {
                            if (checkEmailData.getStatus() == 0) {
                                if (!checkEmailData.isData()) {
                                    Intent mainIntent = new Intent(SignInActivity.this, SocialSignUp1.class);
                                    mainIntent.putExtra(Constant.FIRST_NAME, task.getResult().getUser().getDisplayName());
                                    mainIntent.putExtra(Constant.EMAIL, task.getResult().getUser().getEmail());
                                    mainIntent.putExtra(Constant.ID_TOKEN, account.getIdToken());
                                    SignInActivity.this.startActivity(mainIntent);
                                } else {
                                    model.extLogin(mContext, "Google", account.getIdToken()).observe(owner, userDetails -> {
                                        UserDetails ud;
                                        if (userDetails.getStatus() == 0) {
                                            try {
                                                if (userDetails.getData() != null) {
                                                    AppSingle.getInstance().setEmail(task.getResult().getUser().getEmail());
                                                    Bundle b = new Bundle();
                                                    b.putString(Constant.CATEGORY, "profile");
                                                    b.putString(Constant.ACTION, "signin_social");
                                                    SplashScreenActivity.sendEvent("profile_signin_social", b);
                                                    UiUtil.addLoginToSharedPref(SignInActivity.this, true);
                                                    UiUtil.addUserDetails(SignInActivity.this, userDetails);
                                                    AppSingle.getInstance().setComp_name(new Gson().fromJson(userDetails.getData().getUserDetails(), UserDetails.class).getActiveBusiness().getName());
                                                    Intent mainIntent = new Intent(SignInActivity.this, DashboardActivity.class);
                                                    SignInActivity.this.startActivity(mainIntent);
                                                } else {
                                                    Toast.makeText(mContext, "Login data error", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(SignInActivity.this, userDetails.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignInActivity.this, "Fail to get response", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignInActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (authStateListener != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
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

    private void showUpdateDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.layout_dilog_update, viewGroup, false);
        Button no = dialogView.findViewById(R.id.btn_no);
        Button update = dialogView.findViewById(R.id.btn_update);
        TextView msg = dialogView.findViewById(R.id.update_message);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }
        });
        alertDialog.show();
    }

    private void getUpdateDilog() {
        RestClient.getInstance(this).getUpadte().enqueue(new CustomCallBack<UpdateResponse>(this, null) {
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        int versionNumber = pinfo.versionCode;
                        if (response.body().getData() > versionNumber) {
                            showUpdateDilog();
                        }
                    } else {
                        Toast.makeText(mContext, "else", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error :: ", e.toString());
                    LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign in");
                }
            }

            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.e("Error :: ", t.toString());
                LoginRepo.prinLogs("" + Log.getStackTraceString(t), 5, "Sign in");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    private boolean isValid() {
        if (UiUtil.isValidEmail(this.emailET.getText().toString().trim())) {
            //UiUtil.showToast(this, "Please enter valid email");
            emailET.requestFocus();
            tv_error.setText("Please enter valid email");
            tv_error.setVisibility(View.VISIBLE);
            emailET.setBackgroundResource(R.drawable.error);
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
            password_error.setText("Password must be more the 6 characters.");
            password_error.setVisibility(View.VISIBLE);
            password_ll.setBackgroundResource(R.drawable.error);
            return false;
        } else {
            return true;
        }
    }

    private void reset() {
        emailET.setBackgroundResource(R.drawable.sign_in_input);
        password_ll.setBackgroundResource(R.drawable.sign_in_input);

        tv_error.setVisibility(View.GONE);
        password_error.setVisibility(View.GONE);
    }
}
