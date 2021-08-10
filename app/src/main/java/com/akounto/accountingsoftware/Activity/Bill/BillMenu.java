package com.akounto.accountingsoftware.Activity.Bill;

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
import com.akounto.accountingsoftware.Activity.Dashboard.MainMenu;
import com.akounto.accountingsoftware.Activity.fragment.BillsFragment;
import com.akounto.accountingsoftware.Activity.fragment.ProductsAndServicesPurchaseFragment;
import com.akounto.accountingsoftware.Activity.fragment.VendorsFragment;
import com.akounto.accountingsoftware.databinding.LayoutPurchaseFragmentBinding;
import com.akounto.accountingsoftware.util.AddFragments;

public class BillMenu extends Fragment {

    private LayoutPurchaseFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_purchase_fragment, container, false);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, MainMenu.class);
            }
        });
        binding.btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BillList.class));
                //AddFragments.addFragmentToDrawerActivity(getActivity(), null, BillsFragment.class, true);
            }
        });
        binding.btnVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, VendorsFragment.class, true);
            }
        });
        binding.btnProductsAndServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, ProductsAndServicesPurchaseFragment.class, true);
            }
        });
        return binding.getRoot();
    }
}
