package com.akounto.accountingsoftware.Activity.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.Activity.SignInActivity;
import com.akounto.accountingsoftware.Activity.fragment.AddBusinessFragment;
import com.akounto.accountingsoftware.Activity.fragment.HomeDashboardFragment;
import com.akounto.accountingsoftware.Activity.fragment.PasswordFragment;
import com.akounto.accountingsoftware.Activity.fragment.PersonalInformationFragment;
import com.akounto.accountingsoftware.databinding.LayoutMoreBinding;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.AppSingle;
import com.akounto.accountingsoftware.util.UiUtil;

public class MoreFragment extends Fragment {

    private LayoutMoreBinding binding;
    private Context mContext;
    private UserDetails userDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_more, container, false);
        mContext = this.getContext();
        userDetails=UiUtil.getUserDetail(mContext);
        binding.password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, PasswordFragment.class);
            }
        });
        binding.createBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, AddBusinessFragment.class);
            }
        });
        binding.perosonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, PersonalInformationFragment.class);
            }
        });
        binding.currentBussinessEmail.setText(userDetails.getActiveBusiness().getName());
        binding.currentBussinessName.setText(userDetails.getActiveBusiness().getUName());
        binding.tvCharName.setText(userDetails.getActiveBusiness().getName().charAt(0)+"");
        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddFragments.addFragmentToDrawerActivity(mContext, null, HomeDashboardFragment.class);
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                clearBackStack();
                UiUtil.addLoginToSharedPref(getActivity(), false);
                UiUtil.addUserDetails(mContext, (SignInResponse)null);
                //update
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, HomeDashboardFragment.class);
            }
        });
        binding.btnChangeBussniess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, BussniessList.class);
            }
        });
        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, SettingMenu.class);
            }
        });
        return binding.getRoot();
    }
    public void clearBackStack() {
        try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }
}
