package com.akounto.accountingsoftware.Activity.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddUserRequest;
import com.akounto.accountingsoftware.response.Users;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class InviteUser extends Fragment {

    String f90id = "0";
    String mode = "create-user";
    RadioButton roleButton;
    View dialog;
    List<Users> data;
    static Users users;
    static boolean isEdit = false;
TextView cancelButton;
    public void setUser(Users users) {
        this.users = users;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dialog = inflater.inflate(R.layout.user_mgmt_edit_admin_layout, container, false);
        try {
            TextView title = this.dialog.findViewById(R.id.title);
            cancelButton = this.dialog.findViewById(R.id.cancelButton);
            if (isEdit) {
                title.setText(" Edit User");
                this.mode = "edit-user";
            } else {
                title.setText(" Invite User");
            }
            EditText fnameEt = this.dialog.findViewById(R.id.et_fname);
            EditText lnameEt = this.dialog.findViewById(R.id.et_lname);
            EditText emailEt = this.dialog.findViewById(R.id.et_email);
            RadioGroup roleGroup = this.dialog.findViewById(R.id.radioGroup);
            RadioButton selectedRadioButton = this.dialog.findViewById(roleGroup.getCheckedRadioButtonId());
            TextView fnameErrorTv = this.dialog.findViewById(R.id.fNameErrorTv);
            TextView lnameErrorTv = this.dialog.findViewById(R.id.lNameErrorTv);
            TextView emailErrorTv = this.dialog.findViewById(R.id.emailErrorTv);
            dialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFragments.addFragmentToDrawerActivity(getContext(), null, UserManagementFragment.class);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFragments.addFragmentToDrawerActivity(getContext(), null, UserManagementFragment.class);
                }
            });
            if (users != null) {
                fnameEt.setText(users.getFirstName());
                lnameEt.setText(users.getLastName());
                emailEt.setText(users.getEmail());
                if (users.getRole().equals("Admin")) {
                    roleGroup.check(R.id.adminRB);
                } else {
                    roleGroup.check(R.id.userRB);
                }
                this.f90id = users.getId();
            }
            TextView save = this.dialog.findViewById(R.id.saveButton);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reset(fnameErrorTv, lnameErrorTv, emailErrorTv);
                    if (isValidAbout(roleGroup, fnameEt, lnameEt, emailEt, fnameErrorTv, lnameErrorTv, emailErrorTv)) {
                        addUser(new AddUserRequest(roleButton.getText().toString(), emailEt.getText().toString(), fnameEt.getText().toString(), f90id, lnameEt.getText().toString()), mode);
                    }
                }
            });
        } catch (Exception e) {
        }
        return this.dialog;
    }

    private void addUser(AddUserRequest addUserRequest, String mode2) {
        RestClient.getInstance(getContext()).createUser(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), addUserRequest, mode2).enqueue(new CustomCallBack<ResponseBody>(getContext(), null) {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "setting");
                    b.putString(Constant.ACTION, "add_user");
                    SplashScreenActivity.sendEvent("setting_add_user", b);
                    UiUtil.showToast(InviteUser.this.getContext(), "Added");
                    AddFragments.addFragmentToDrawerActivity(getContext(), null, UserManagementFragment.class);
                    return;
                }
                Bundle b = new Bundle();
                b.putString(Constant.CATEGORY, "setting");
                b.putString(Constant.ACTION, "add_user_fail");
                SplashScreenActivity.sendEvent("setting_add_user", b);
                UiUtil.showToast(InviteUser.this.getContext(), "Error while adding");
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private boolean isUser(String email) {
        boolean result = false;
        for (int i = 0; i < UserManagementFragment.data.size(); i++) {
            if (UserManagementFragment.data.get(i).getEmail().equalsIgnoreCase(email))
                result = true;
        }
        return result;
    }

    private boolean isValidAbout(RadioGroup roleGroup, EditText fnameEt, EditText lnameEt, EditText emailEt, TextView fnameErrorTv, TextView lnameErrorTv, TextView emailErrorTv) {
        boolean response = true;
        boolean focusfield = true;

        this.roleButton = this.dialog.findViewById(roleGroup.getCheckedRadioButtonId());
        String firstName = fnameEt.getText().toString();
        if (firstName.isEmpty()) {
            fnameErrorTv.setText("Please enter first name");
            fnameErrorTv.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                fnameEt.requestFocus();
                focusfield = false;
            }
        } else if (firstName.length() < 3) {
            fnameErrorTv.setText("First name is too small. (min 3 chars)");
            fnameErrorTv.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                fnameEt.requestFocus();
                focusfield = false;
            }
        }

        String lastName = lnameEt.getText().toString();
        if (lastName.isEmpty()) {
            lnameErrorTv.setText("Please enter last name");
            lnameErrorTv.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                lnameEt.requestFocus();
                focusfield = false;
            }
        } else if (lastName.length() < 3) {
            lnameErrorTv.setText("Last name is too small. (min 3 chars)");
            lnameErrorTv.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                lnameEt.requestFocus();
                focusfield = false;
            }
        }
        String email = emailEt.getText().toString();
        if (UiUtil.isValidEmail(email)) {
            emailErrorTv.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                emailEt.requestFocus();
                focusfield = false;
            }
        } else if (isUser(email)) {
            emailErrorTv.setVisibility(View.VISIBLE);
            emailErrorTv.setText("This user already associated with the business!");
            response = false;
            if (focusfield) {
                emailEt.requestFocus();
                focusfield = false;
            }
        }

        return response;
    }

    public void reset(TextView fnameErrorTv, TextView lnameErrorTv, TextView emailErrorTv) {
        fnameErrorTv.setVisibility(View.GONE);
        lnameErrorTv.setVisibility(View.GONE);
        emailErrorTv.setVisibility(View.GONE);

    }
}
