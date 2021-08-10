package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.akounto.accountingsoftware.R;

public class SettingsFragment extends Fragment {
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.settings_fragment, container, false);
        init();
        return this.view;
    }

    private void init() {
        this.view.findViewById(R.id.usersTv).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SettingsFragment.this.lambda$init$0$SettingsFragment(view);
            }
        });
        this.view.findViewById(R.id.datesAndCurrencyTv).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SettingsFragment.this.lambda$init$1$SettingsFragment(view);
            }
        });
        this.view.findViewById(R.id.salesTaxesTv).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SettingsFragment.this.lambda$init$2$SettingsFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$init$0$SettingsFragment(View v) {
        navigate(new UserManagementFragment());
    }

    public /* synthetic */ void lambda$init$1$SettingsFragment(View v) {
        navigate(new DateAndCurrencyFragment());
    }

    public /* synthetic */ void lambda$init$2$SettingsFragment(View v) {
        navigate(new SaleTaxesFragment());
    }

    private void navigate(Fragment newFragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.activity_open_translate, R.anim.activity_close_scale, R.anim.activity_open_translate, R.anim.activity_close_scale);
        ft.replace(R.id.fragment_container, newFragment, newFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }
}
