package com.akounto.accountingsoftware.Activity.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.Accounting.TransactionsActivity;
import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.RegisterBank.Bank;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.adapter.UserManagementAdminAdapter;
import com.akounto.accountingsoftware.adapter.UserManagementAdminItemClick;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddUserRequest;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.request.UserDelete;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.UserManagementResponse;
import com.akounto.accountingsoftware.response.Users;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UserManagementFragment extends Fragment implements UserManagementAdminItemClick {

    Dialog dialog;
    String f90id = "0";
    String mode = "create-user";
    RadioButton roleButton;
    View view;
    public static List<Users> data;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.user_management_fragment, container, false);
        disableAutofill();
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, SettingMenu.class);
            }
        });
        init();
        return this.view;
    }

    private void init() {
        getUsers();
        this.view.findViewById(R.id.inviteUserButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                InviteUser.isEdit = false;
                InviteUser.users = null;
                UserManagementFragment.this.lambda$init$0$UserManagementFragment(view);
            }
        });
    }

    public void lambda$init$0$UserManagementFragment(View v) {
        try {
            AddFragments.addFragmentToDrawerActivity(getContext(), null, InviteUser.class);
        } catch (Exception e) {
        }
    }

    public void onResume() {
        super.onResume();
        getUsers();
    }

    public void getUsers() {
        RestClient.getInstance(getActivity()).getUsers(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext())).enqueue(new CustomCallBack<UserManagementResponse>(getContext(), null) {
            public void onResponse(Call<UserManagementResponse> call, Response<UserManagementResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Log.d("response", response.body().getData().toString());
                    data = response.body().getData();
                    UserManagementFragment.this.setUpAdminList(data);
                }
            }

            public void onFailure(Call<UserManagementResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void setUpAdminList(List<Users> data) {
        RecyclerView recyclerView = this.view.findViewById(R.id.adminsRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new UserManagementAdminAdapter(getContext(), data, this));
    }

    public void editAdmin(Users users) {
        InviteUser f = new InviteUser();
        f.setUser(users);
        InviteUser.users = users;
        InviteUser.isEdit = true;
        AddFragments.addFragmentToDrawerActivity(getContext(), null, f.getClass());
    }

    private boolean isUser(String email) {
        boolean result = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getEmail().equalsIgnoreCase(email))
                result = true;
        }
        return result;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }

    public void showDialog(Context activity, Users users) {

        dialog = new Dialog(activity);
        if (!dialog.isShowing()) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dilog_bank_import);
            TextView descp = dialog.findViewById(R.id.descp);
            descp.setText("You want to disassociate user.");
            Button btn_import = dialog.findViewById(R.id.btn_import);
            btn_import.setText("Yes");
            Button btn_cancle = dialog.findViewById(R.id.btn_cancel);
            btn_cancle.setText("No");
            btn_import.setOnClickListener(v -> {
                dialog.dismiss();
                RestClient.getInstance(getContext()).disassociateUser(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), new UserDelete(users.getId())).enqueue(new CustomCallBack<CustomeResponse>(getContext(), "Deleting...") {
                    @Override
                    public void onResponse(Call<CustomeResponse> call, Response<CustomeResponse> response) {
                        super.onResponse(call, response);

                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            Toast.makeText(getContext(), "User disassociate successfully.", Toast.LENGTH_LONG).show();
                            getUsers();
                        } else {
                            Toast.makeText(getContext(), "User disassociate fail.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomeResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Toast.makeText(getContext(), "User disassociate fail.", Toast.LENGTH_LONG).show();
                    }
                });

            });
            btn_cancle.setOnClickListener(v -> {
                dialog.dismiss();

            });
            dialog.show();
        }
    }

    @Override
    public void deleteAdmin(Users users) {
        showDialog(getActivity(), users);
    }

    private void editAdminDialog(Users users) {
        Dialog dialog2 = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog2.requestWindowFeature(1);
        try {
            this.dialog = dialog2;
            this.dialog.setContentView(R.layout.user_mgmt_edit_admin_layout);
            this.dialog.setCancelable(true);
            this.dialog.setCanceledOnTouchOutside(true);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            LinearLayout back = this.dialog.findViewById(R.id.back);
            TextView title = this.dialog.findViewById(R.id.title);
            if (users != null) {
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
            //Toast.makeText(getContext(), selectedRadioButton.getText().toString(), Toast.LENGTH_LONG).show();
            TextView fnameErrorTv = this.dialog.findViewById(R.id.fNameErrorTv);
            TextView lnameErrorTv = this.dialog.findViewById(R.id.lNameErrorTv);
            TextView emailErrorTv = this.dialog.findViewById(R.id.emailErrorTv);
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
            this.dialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    dialog.dismiss();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    dialog.dismiss();
                }
            });
            this.dialog.show();

        } catch (Exception e) {
        }
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

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        getActivity().getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }

    public void reset(TextView fnameErrorTv, TextView lnameErrorTv, TextView emailErrorTv) {
        fnameErrorTv.setVisibility(View.GONE);
        lnameErrorTv.setVisibility(View.GONE);
        emailErrorTv.setVisibility(View.GONE);

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
                    UiUtil.showToast(UserManagementFragment.this.getContext(), "Added");
                    if (UserManagementFragment.this.dialog != null) {
                        UserManagementFragment.this.dialog.dismiss();
                    }
                    UserManagementFragment.this.getUsers();
                    return;
                }
                Bundle b = new Bundle();
                b.putString(Constant.CATEGORY, "setting");
                b.putString(Constant.ACTION, "add_user_fail");
                SplashScreenActivity.sendEvent("setting_add_user", b);
                UiUtil.showToast(UserManagementFragment.this.getContext(), "Error while adding");
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }
}
