package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.SignUpResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.xwray.passwordview.PasswordView;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class PasswordFragment extends Fragment {
    EditText confirmNewPwdEt;
    TextView confirmNwPwdTv;
    boolean isPasswordMatched = false;
    PasswordView newPwdEt;
    TextView newPwdNoteTv;
    TextView oldPasswordNote;
    EditText oldPwdEt;
    Map<String, String> request = new HashMap();
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.password_fragment, container, false);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, MoreFragment.class);
            }
        });
        initUI();
        return this.view;
    }

    private void initUI() {
        this.oldPwdEt = this.view.findViewById(R.id.et_oldPwd);
        this.newPwdEt = this.view.findViewById(R.id.et_newPwd);
        this.confirmNewPwdEt = this.view.findViewById(R.id.et_confirmNewPwd);
        this.newPwdNoteTv = this.view.findViewById(R.id.newPasswordNote);
        this.confirmNwPwdTv = this.view.findViewById(R.id.passwordsDoNotMatch);
        this.oldPasswordNote = this.view.findViewById(R.id.incorrectPassword);
        this.confirmNewPwdEt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", s.toString());
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPwd = PasswordFragment.this.newPwdEt.getText().toString().trim();
                PasswordFragment.this.confirmNwPwdTv.setVisibility(View.VISIBLE);
                PasswordFragment.this.isPasswordMatched = false;
                if (s.toString().equals(newPwd)) {
                    PasswordFragment.this.confirmNwPwdTv.setVisibility(View.GONE);
                    PasswordFragment.this.isPasswordMatched = true;
                }
            }

            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", s.toString());
                if (s.toString().isEmpty()) {
                    PasswordFragment.this.confirmNwPwdTv.setVisibility(View.GONE);
                }
            }
        });
        this.view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                PasswordFragment.this.lambda$initUI$0$PasswordFragment(view);
            }
        });
    }

    public void lambda$initUI$0$PasswordFragment(View v) {
        updatePassword();
    }

    private void updatePassword() {
        String oldPassword = this.oldPwdEt.getText().toString();
        if (oldPassword.isEmpty()) {
            this.oldPasswordNote.setText("Please enter current password");
            this.oldPasswordNote.setVisibility(View.VISIBLE);
            return;
        }
        this.oldPasswordNote.setVisibility(View.INVISIBLE);
        String newPassword = this.newPwdEt.getText().toString();
        if (newPassword.isEmpty()) {
            this.newPwdNoteTv.setText("Please enter new password");
            this.newPwdNoteTv.setVisibility(View.VISIBLE);
        } else if (newPassword.length() < 6) {
            this.newPwdNoteTv.setText("Min 6 chars required");
            this.newPwdNoteTv.setVisibility(View.VISIBLE);
        } else {
            this.newPwdNoteTv.setVisibility(View.GONE);
            String confirmNewPwd = this.confirmNewPwdEt.getText().toString();
            if (!confirmNewPwd.equals(newPassword)) {
                this.confirmNwPwdTv.setVisibility(View.VISIBLE);
                this.isPasswordMatched = false;
                return;
            }
            this.confirmNwPwdTv.setVisibility(View.GONE);
            this.isPasswordMatched = true;
            this.request.put("OldPassword", oldPassword);
            this.request.put("NewPassword", newPassword);
            this.request.put("ConfirmPassword", confirmNewPwd);
            submitRequest(this.request);
        }
    }

    private void submitRequest(Map<String, String> map) {
        RestClient.getInstance(getContext()).changeUserPassword(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),map).enqueue(new CustomCallBack<SignUpResponse>(getContext(), null) {
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                super.onResponse(call, response);
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"profile");
                b.putString(Constant.ACTION,"change_password");
                SplashScreenActivity.sendEvent("profile_change_password",b);
                if (!response.isSuccessful()) {
                    UiUtil.showToast(PasswordFragment.this.getContext(), "Error while updating");
                } else if (response.body().getTransactionStatus().isIsSuccess()) {
                    UiUtil.showToast(PasswordFragment.this.getContext(), "Password Updated");
                    PasswordFragment.this.clearFields();
                } else {
                    PasswordFragment.this.oldPasswordNote.setVisibility(View.VISIBLE);
                    PasswordFragment.this.oldPasswordNote.setText(response.body().getTransactionStatus().getError().getDescription());
                }
            }

            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void clearFields() {
        this.oldPwdEt.setText("");
        this.newPwdEt.setText("");
        this.confirmNewPwdEt.setText("");
        this.newPwdNoteTv.setVisibility(View.VISIBLE);
        this.newPwdNoteTv.setText("At least 6 characters, but longer is better");
        this.oldPwdEt.requestFocus();
    }
}
