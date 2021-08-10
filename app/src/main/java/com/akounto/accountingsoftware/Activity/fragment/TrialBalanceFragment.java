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
import com.akounto.accountingsoftware.request.TrialBalanceRequest;
import com.akounto.accountingsoftware.response.getusercompany.GetUserCompany;
import com.akounto.accountingsoftware.response.trialbalance.TrialBalanceDetails;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Response;

public class TrialBalanceFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_balancesheet, container, false);
        inItUi();
        return this.view;
    }

    private void inItUi() {
        getCOmpanyList();
        getTrailBalance();
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

    private void getTrailBalance() {
        TrialBalanceRequest req = new TrialBalanceRequest();
        req.setCompanyId(0);
        req.setTillDate("Feb 06, 2021");
        req.setPDF(true);
        RestClient.getInstance(getActivity()).getTrailBalance(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),req).enqueue(new CustomCallBack<TrialBalanceDetails>(getActivity(), null) {
            public void onResponse(Call<TrialBalanceDetails> call, Response<TrialBalanceDetails> response) {
                super.onResponse(call, response);
                Log.d("getTrailBalance----", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<TrialBalanceDetails> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
