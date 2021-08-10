package com.akounto.accountingsoftware.Activity.Setting;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Activity.fragment.HomeDashboardFragment;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.databinding.LayoutSettingAccountingBinding;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.Setting;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AccountBasisFragment extends Fragment {

    private LayoutSettingAccountingBinding binding;
    private Context mContext;
    private RadioButton cash, accrual;
    private int type = 0;
    private UserDetails user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_setting_accounting, container, false);
        mContext = this.getContext();
        type = UiUtil.getUserDetail(getContext()).getActiveBusiness().getAccountingBasisType();
        user = new Gson().fromJson(UiUtil.getUserDetails(mContext).getUserDetails(), UserDetails.class);
        if (type == 1) {
            binding.radioButtonCash.setChecked(true);
        } else {
            binding.radioButtonAccrual.setChecked(true);
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, SettingMenu.class);
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.radioButtonCash.isChecked()) {
                    type=1;
                } else {
                    type=2;
                }
                submitRequest();
            }
        });
        return binding.getRoot();
    }
    private void submitRequest() {
        RestClient.getInstance(getContext()).updateAccountingBasisType(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), new Setting(String.valueOf(type))).enqueue(new CustomCallBack<ResponseBody>(getContext(), null) {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"setting");
                    b.putString(Constant.ACTION,"account_basis");
                    SplashScreenActivity.sendEvent("setting_account_basis",b);
                    UiUtil.showToast(AccountBasisFragment.this.getContext(), "Saved");
                    user.getActiveBusiness().setAccountingBasisType(type);
                    SignInResponse signInResponse=UiUtil.getUserDetails(mContext);
                    signInResponse.setUserDetails(new Gson().toJson(user));
                    UiUtil.addUserDetails(mContext, signInResponse);
                    AddFragments.addFragmentToDrawerActivity(mContext, null, HomeDashboardFragment.class);
                } else {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"setting");
                    b.putString(Constant.ACTION,"account_basis_fail");
                    SplashScreenActivity.sendEvent("setting_account_basis",b);
                    UiUtil.showToast(AccountBasisFragment.this.getContext(), "Error while Saving");
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"setting");
                b.putString(Constant.ACTION,"account_basis_fail");
                SplashScreenActivity.sendEvent("setting_account_basis",b);
            }
        });
    }
}
