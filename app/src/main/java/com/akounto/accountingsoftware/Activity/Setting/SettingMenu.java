package com.akounto.accountingsoftware.Activity.Setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment;
import com.akounto.accountingsoftware.Activity.Invoice.CustomizeInvoice;
import com.akounto.accountingsoftware.Activity.fragment.DateAndCurrencyFragment;
import com.akounto.accountingsoftware.Activity.fragment.SaleTaxesFragment;
import com.akounto.accountingsoftware.Activity.fragment.UserManagementFragment;
import com.akounto.accountingsoftware.databinding.LayoutSettingFragmentBinding;
import com.akounto.accountingsoftware.util.AddFragments;

public class SettingMenu extends Fragment {

    private LayoutSettingFragmentBinding binding;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_setting_fragment, container, false);
        mContext=this.getContext();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, MoreFragment.class);
            }
        });
        binding.btnCustomizeInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, CustomizeInvoice.class);
            }
        });
        binding.btnInvoiceCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, SaleTaxesFragment.class);

            }
        });
        binding.btnUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, UserManagementFragment.class);

            }
        });
        binding.btnAccountBasis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, AccountBasisFragment.class);
            }
        });
        binding.btnDateAndCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, DateAndCurrencyFragment.class);
            }
        });
        return binding.getRoot();
    }
}
