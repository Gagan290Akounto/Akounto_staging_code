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
import com.akounto.accountingsoftware.request.SaleTaxRequest;
import com.akounto.accountingsoftware.response.saletax.SaleTaxDetails;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Response;

public class SaleTaxFragment extends Fragment {
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_saletax, container, false);
        getSaleTax();
        return this.view;
    }

    private void getSaleTax() {
        SaleTaxRequest req = new SaleTaxRequest();
        req.setCompanyId(0);
        req.setEndDate("Feb 06, 2021");
        req.setStartDate("Feb 06, 2021");
        req.setPDF(true);
        RestClient.getInstance(getActivity()).getSaleTax(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),req).enqueue(new CustomCallBack<SaleTaxDetails>(getActivity(), null) {
            public void onResponse(Call<SaleTaxDetails> call, Response<SaleTaxDetails> response) {
                super.onResponse(call, response);
                Log.d("SaleTaxDetails----", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<SaleTaxDetails> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
