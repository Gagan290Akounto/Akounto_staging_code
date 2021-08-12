package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.ViewModel.LoginViewModel;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

public class SignupOptions extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient = null;
    private static final int RC_SIGN_IN = 1;
    private LinearLayout googlesign_dummy;
    private Context mContext;
    private LoginViewModel model;
    private LifecycleOwner owner;
    private String name, email;
    private String idToken;
    private Button signin,signup;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option_signup);
        mContext=this;
        googlesign_dummy = findViewById(R.id.sign_in_button1);
        signin = findViewById(R.id.sign_in);
        signup = findViewById(R.id.sign_up);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupOptions.this.startActivity(new Intent(SignupOptions.this, SignInActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupOptions.this.startActivity(new Intent(SignupOptions.this, SIgnUpStep0.class));
            }
        });
        try{
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
            googlesign_dummy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //UiUtil.showToast(mContext,"signInButton");
                    UiUtil.showProgressDialogue(mContext, "", "Loading..");
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    SignupOptions.this.startActivityForResult(intent, RC_SIGN_IN);
                }
            });
            googlesign_dummy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiUtil.showProgressDialogue(mContext, "", "Loading..");
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    SignupOptions.this.startActivityForResult(intent, RC_SIGN_IN);
                }
            });
        }catch (Exception e){

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
                model.checkEmail(mContext, task.getResult().getUser().getEmail()).observe(SignupOptions.this, new Observer<CheckEmailData>() {
                    @Override
                    public void onChanged(CheckEmailData checkEmailData) {
                       /* if (external_sign_up) {
                            external_sign_up = false;*/
                        try {
                            if (checkEmailData.getStatus() == 0) {
                                if (!checkEmailData.isData()) {
                                    Intent mainIntent = new Intent(SignupOptions.this, ExternalSignUp.class);
                                    mainIntent.putExtra(Constant.FIRST_NAME, task.getResult().getUser().getDisplayName());
                                    mainIntent.putExtra(Constant.EMAIL, task.getResult().getUser().getEmail());
                                    mainIntent.putExtra(Constant.ID_TOKEN, account.getIdToken());
                                    SignupOptions.this.startActivity(mainIntent);
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
                                                    UiUtil.addLoginToSharedPref(SignupOptions.this, true);
                                                    UiUtil.addUserDetails(SignupOptions.this, userDetails);
                                                    AppSingle.getInstance().setComp_name(new Gson().fromJson(userDetails.getData().getUserDetails(), UserDetails.class).getActiveBusiness().getName());
                                                    Intent mainIntent = new Intent(SignupOptions.this, DashboardActivity.class);
                                                    SignupOptions.this.startActivity(mainIntent);
                                                } else {
                                                    Toast.makeText(mContext, "Login data error", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(SignupOptions.this, userDetails.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignupOptions.this, "Fail to get response", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignupOptions.this, "Authentication failed.",
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
}
