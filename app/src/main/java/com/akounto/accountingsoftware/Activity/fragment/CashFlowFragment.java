package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.CashFlowRequest;
import com.akounto.accountingsoftware.response.cashflow.CashFLowData;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Response;

public class CashFlowFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_cashflow, container, false);
        getBCashFLowData();
        return this.view;
    }

    private void getBCashFLowData() {
        CashFlowRequest billsReq = new CashFlowRequest();
        billsReq.setStartDate1("Feb 01, 2021");
        billsReq.setEndDate1("Feb 06, 2021");
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(getActivity()).getCashFlow(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),billsReq).enqueue(new CustomCallBack<CashFLowData>(getActivity(), null) {
            public void onResponse(Call<CashFLowData> call, Response<CashFLowData> response) {
                super.onResponse(call, response);
                Log.d("CashFLowData----", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<CashFLowData> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
