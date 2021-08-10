package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddChatOfAccountActivity;
import com.akounto.accountingsoftware.adapter.HeadSubAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.accounting.AccountDetailByIdResponse;
import com.akounto.accountingsoftware.response.chartaccount.HeadTransactions;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ChartSubDetailsFragment extends Fragment implements ChartsOfAccountItemClick {
    LinearLayout addaNewacLL;
    RecyclerView headListRecycler;

    /* renamed from: id */
    int f89id = -1;
    List<HeadTransactions> tansList;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_chatsubdetails, container, false);
        this.tansList = LocalManager.getInstance().getHeadTransactionsList();
        try{
        this.f89id = getArguments().getInt("id");}catch(Exception e){}
        inItUi();
        return this.view;
    }

    private void inItUi() {
        LinearLayout linearLayout = this.view.findViewById(R.id.addaNewacLL);
        this.addaNewacLL = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ChartSubDetailsFragment.this.lambda$inItUi$0$ChartSubDetailsFragment(view);
            }
        });
        if (this.tansList.size() > 0) {
            this.addaNewacLL.setVisibility(View.GONE);
        } else {
            this.addaNewacLL.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = this.view.findViewById(R.id.headListRecycler);
        this.headListRecycler = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.headListRecycler.setAdapter(new HeadSubAdapter(getActivity(), this.tansList, this));
    }

    public /* synthetic */ void lambda$inItUi$0$ChartSubDetailsFragment(View v) {
        if (this.f89id != 1) {
            startActivity(AddChatOfAccountActivity.buildIntent(getContext(), this.f89id));
        }
    }

    public void editAccountAction(int id) {
        RestClient.getInstance(getContext()).getAccountDetailsById(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),id).enqueue(new CustomCallBack<AccountDetailByIdResponse>(getContext(), null) {
            public void onResponse(Call<AccountDetailByIdResponse> call, Response<AccountDetailByIdResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ChartSubDetailsFragment chartSubDetailsFragment = ChartSubDetailsFragment.this;
                    chartSubDetailsFragment.startActivity(AddChatOfAccountActivity.buildIntentForEdit(chartSubDetailsFragment.getContext(), response.body().getAccountDetailById()));
                }
            }

            public void onFailure(Call<AccountDetailByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }
}
