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

import com.akounto.accountingsoftware.Activity.Invoice.InvoiceList;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Accounting.AccountingMenu;
import com.akounto.accountingsoftware.Activity.Bank.BankListActivity;
import com.akounto.accountingsoftware.Activity.Bill.BillMenu;
import com.akounto.accountingsoftware.Activity.FragmentReportTest;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceMenu;
import com.akounto.accountingsoftware.Activity.fragment.HomeDashboardFragment;
import com.akounto.accountingsoftware.databinding.LayoutMenuFragmentBinding;
import com.akounto.accountingsoftware.util.AddFragments;

public class MainMenu extends Fragment {

    private LayoutMenuFragmentBinding binding;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_menu_fragment, container, false);
        mContext = this.getContext();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, HomeDashboardFragment.class);
            }
        });
        binding.salse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, InvoiceMenu.class);

            }
        });
        binding.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, BillMenu.class);
            }
        });
        binding.btnAccounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, AccountingMenu.class);
            }
        });
        binding.btnBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BankListActivity.class));
            }
        });

        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, FragmentReportTest.class);
            }
        });
        return binding.getRoot();
    }
}
