package com.akounto.accountingsoftware.Activity.Accounting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MainMenu;
import com.akounto.accountingsoftware.Activity.fragment.ChartAccountsFragment;
import com.akounto.accountingsoftware.Activity.fragment.TransactionsFragment;
import com.akounto.accountingsoftware.databinding.LayoutAccountingFragmentBinding;
import com.akounto.accountingsoftware.util.AddFragments;

public class AccountingMenu extends Fragment {

    private LayoutAccountingFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_accounting_fragment, container, false);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, MainMenu.class);
            }
        });
        binding.btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, TransactionsFragment.class, true);
            }
        });
        binding.btnChartOfAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, ChartAccountsFragment.class, true);
            }
        });
        return binding.getRoot();
    }
}
