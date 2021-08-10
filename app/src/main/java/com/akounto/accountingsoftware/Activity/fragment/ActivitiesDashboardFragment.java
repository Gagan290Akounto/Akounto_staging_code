package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Dashboard.BankBalance;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.Dashboard.LastActivity;
import com.akounto.accountingsoftware.Data.Dashboard.LastBankTransaction;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Bank.BankListActivity;
import com.akounto.accountingsoftware.adapter.ActivitiesDashboardAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetDashboardRequest;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ActivitiesDashboardFragment extends Fragment {
    RecyclerView activitiesRecyclerView;
    public static DashboardData dashboardResponse = null;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    RecyclerView transactionsRecyclerView;
    View view;
    private int c_index = 0;
    TextView bankName, bankAmount, bankAccountNo;
    TextView btnPrev, btnNxt;
    LinearLayout no_tranc_data_ll, tranc_data_ll, no_activities_ll;
    private Context mContext;
    private List<BankBalance> bank_data = null;
    private DashboardData dbdata = null;
    private List<LastBankTransaction> bank_trans_list;
    private List<LastActivity> activity_list;
    private List<String> filter;
    private Button alltrans;
    private LinearLayout circle_bill;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activities_dashboard_fragment, container, false);
        no_tranc_data_ll = this.view.findViewById(R.id.no_tranc_data_ll);
        tranc_data_ll = this.view.findViewById(R.id.tranc_data_ll);
        no_activities_ll = this.view.findViewById(R.id.no_activities_ll);
        view.findViewById(R.id.goHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, HomeDashboardFragment.class);
            }
        });
        init();
        return this.view;
    }

    private void init() {
        RecyclerView recyclerView = this.view.findViewById(R.id.activitiesRV);
        this.activitiesRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView recyclerView2 = this.view.findViewById(R.id.transactionRv);
        this.transactionsRecyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        alltrans = this.view.findViewById(R.id.viewAllTransactions);
        circle_bill = this.view.findViewById(R.id.id_circle);
        alltrans.setOnClickListener(v -> AddFragments.addFragmentToDrawerActivity(getContext(), null, TransactionsFragment.class));

        bankName = this.view.findViewById(R.id.bank_name);
        bankAmount = this.view.findViewById(R.id.bank_amount);
        bankAccountNo = this.view.findViewById(R.id.bank_account_no);
        btnPrev = this.view.findViewById(R.id.sfdfsdfsf);
        btnNxt = this.view.findViewById(R.id.btn_nxt_activity);

        btnPrev.setOnClickListener(v -> {
            c_index--;
            bankSectionUI(bank_data);
        });

        btnNxt.setOnClickListener(v -> {
            c_index++;
            bankSectionUI(bank_data);
        });


        circle_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BankListActivity.class));
            }
        });

        if (ActivitiesDashboardFragment.this.dashboardResponse != null) {
            ActivitiesDashboardFragment.this.loadData();
            bank_data = dashboardResponse.getData().getBankBalance();
            bankSectionUI(bank_data);
        }
    }

    public void onResume() {
        super.onResume();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Calendar c = Calendar.getInstance();
        String toDate = simpleDateFormat.format(c.getTime());
        c.add(1, -1);
        String fromDate = simpleDateFormat.format(c.getTime());
        //getHomeDashBoardData(new GetDashboardRequest(fromDate, toDate, fromDate,UiUtil.getAccountingType(getContext()),0), false, 0);
    }

    private void getHomeDashBoardData(GetDashboardRequest getDashboardRequest, boolean refreshWithDates, int graphType) {
        RestClient.getInstance(getActivity()).getDashboard(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), getDashboardRequest).enqueue(new CustomCallBack<DashboardData>(getContext(), null) {
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ActivitiesDashboardFragment.this.dashboardResponse = response.body();
                    if (ActivitiesDashboardFragment.this.dashboardResponse != null) {
                        ActivitiesDashboardFragment.this.loadData();
                        bank_data = response.body().getData().getBankBalance();
                        bankSectionUI(bank_data);
                    }
                }
            }

            public void onFailure(Call<DashboardData> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void loadData() {
        if (this.dashboardResponse.getData().getLastBankTransactions() != null) {
            no_tranc_data_ll.setVisibility(View.GONE);
            tranc_data_ll.setVisibility(View.VISIBLE);
            this.transactionsRecyclerView.setAdapter(new ActivitiesDashboardAdapter(getContext(), null, this.dashboardResponse.getData().getLastBankTransactions(), 0));
        } else {
            no_tranc_data_ll.setVisibility(View.VISIBLE);
            tranc_data_ll.setVisibility(View.GONE);
        }
        if (this.dashboardResponse.getData().getLastActivities() != null) {
            no_activities_ll.setVisibility(View.GONE);
            this.activitiesRecyclerView.setVisibility(View.VISIBLE);
            this.activitiesRecyclerView.setAdapter(new ActivitiesDashboardAdapter(getContext(), this.dashboardResponse.getData().getLastActivities(), null, 1));
        } else {
            no_activities_ll.setVisibility(View.VISIBLE);
            this.activitiesRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setBankData(List<BankBalance> data) {
        try {
            this.bankName.setText(data.get(c_index).getBankName());
            this.bankAmount.setText("$ " + data.get(c_index).getAmount());
            if (!data.get(c_index).getAccountNumber().isEmpty()) {
                this.bankAccountNo.setText("********" + data.get(c_index).getAccountNumber());
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

/*    private void setSpiner() {
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spFilter.setAdapter(dataAdapter);
    }*/

    private void setPreUI() {
        try {
            if (c_index != 0) {
                this.btnPrev.setVisibility(View.VISIBLE);
            } else {
                this.btnPrev.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    private void setNxtUI() {
        try {
            if (c_index != (bank_data.size() - 1)) {
                this.btnNxt.setVisibility(View.VISIBLE);
            } else {
                this.btnNxt.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    private void bankSectionUI(List<BankBalance> data) {
        setPreUI();
        setNxtUI();
        setBankData(data);
    }
}
