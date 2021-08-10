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
import com.akounto.accountingsoftware.request.GetBalanceSheetRequest;
import com.akounto.accountingsoftware.response.getBalanceSheet.GetBalanceSheet;
import com.akounto.accountingsoftware.response.getusercompany.GetUserCompany;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Response;

public class BalanceShetFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_balancesheet, container, false);
        inItUi();
        return this.view;
    }

    private void inItUi() {
        getCOmpanyList();
        getBalanceSHeet();
    }

    private void getCOmpanyList() {
        RestClient.getInstance(getActivity()).getUserCompany(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext())).enqueue(new CustomCallBack<GetUserCompany>(getActivity(), null) {
            public void onResponse(Call<GetUserCompany> call, Response<GetUserCompany> response) {
                super.onResponse(call, response);
                Log.d("getTaxList----", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<GetUserCompany> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void getBalanceSHeet() {
        GetBalanceSheetRequest billsReq = new GetBalanceSheetRequest();
        billsReq.setBalanceStartDate("Feb 01, 2021");
        billsReq.setBalanceTillDate("Feb 06, 2021");
        billsReq.setCompanyId(0);
        billsReq.setPDF(true);
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(getActivity()).getBalanceSHeet(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),billsReq).enqueue(new CustomCallBack<GetBalanceSheet>(getActivity(), null) {
            public void onResponse(Call<GetBalanceSheet> call, Response<GetBalanceSheet> response) {
                super.onResponse(call, response);
                Log.d("getBalanceSHeet----", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<GetBalanceSheet> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
