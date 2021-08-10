package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.FragmentReportTest;
import com.akounto.accountingsoftware.util.AddFragments;

public class ReportFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        view.findViewById(R.id.profitAndLossRel).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                //ReportFragment.this.lambda$onCreateView$0$ReportFragment(view);
                Intent intent = new Intent(getActivity(), FragmentReportTest.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.balanceSheetRL).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportFragment.this.lambda$onCreateView$1$ReportFragment(view);
            }
        });
        view.findViewById(R.id.cashFlowRL).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportFragment.this.lambda$onCreateView$2$ReportFragment(view);
            }
        });
        view.findViewById(R.id.saleTaxRL).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportFragment.this.lambda$onCreateView$3$ReportFragment(view);
            }
        });
        view.findViewById(R.id.trailBalanceRL).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportFragment.this.lambda$onCreateView$4$ReportFragment(view);
            }
        });
        return view;
    }

    public /* synthetic */ void lambda$onCreateView$0$ReportFragment(View click) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, ReportsDetailedFragment.class);
    }

    public /* synthetic */ void lambda$onCreateView$1$ReportFragment(View click) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, BalanceShetFragment.class);
    }

    public /* synthetic */ void lambda$onCreateView$2$ReportFragment(View click) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, CashFlowFragment.class);
    }

    public /* synthetic */ void lambda$onCreateView$3$ReportFragment(View click) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, SaleTaxFragment.class);
    }

    public /* synthetic */ void lambda$onCreateView$4$ReportFragment(View click) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, TrialBalanceFragment.class);
    }
}
