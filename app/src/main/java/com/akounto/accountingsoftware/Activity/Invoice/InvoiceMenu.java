package com.akounto.accountingsoftware.Activity.Invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MainMenu;
import com.akounto.accountingsoftware.Activity.fragment.CustomersFragment;
import com.akounto.accountingsoftware.Activity.fragment.EstimatesFragment;
import com.akounto.accountingsoftware.Activity.fragment.InvoicesFragment;
import com.akounto.accountingsoftware.Activity.fragment.ProductsAndServicesFragment;
import com.akounto.accountingsoftware.Activity.fragment.RecurringInvoicesFragment;
import com.akounto.accountingsoftware.databinding.LayoutSalesFargmentBinding;
import com.akounto.accountingsoftware.util.AddFragments;

public class InvoiceMenu extends Fragment {

    private LayoutSalesFargmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_sales_fargment, container, false);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, MainMenu.class);
            }
        });
        binding.btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InvoiceList.class));
               // AddFragments.addFragmentToDrawerActivity(getActivity(), null, InvoicesFragment.class,true);
            }
        });
        binding.btnEstimates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, EstimatesFragment.class,true);
            }
        });
        binding.btnEstimates.setEnabled(false);
        binding.btnEstimates.setBackgroundResource(R.drawable.disable);
        binding.btnRecurringInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFragments.addFragmentToDrawerActivity(getActivity(), null, RecurringInvoicesFragment.class,true);
            }
        });
        binding.btnRecurringInvoices.setEnabled(false);
        binding.btnRecurringInvoices.setBackgroundResource(R.drawable.disable);
        binding.btnCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFragments.addFragmentToDrawerActivity(getActivity(), null, CustomersFragment.class,true);
            }
        });
        binding.btnProductAndServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFragments.addFragmentToDrawerActivity(getActivity(), null, ProductsAndServicesFragment.class,true);
            }
        });
        return binding.getRoot();
    }
}
