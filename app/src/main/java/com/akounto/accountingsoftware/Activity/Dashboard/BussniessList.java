package com.akounto.accountingsoftware.Activity.Dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akounto.accountingsoftware.util.AppSingle;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Business;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.BussniessAdapter;
import com.akounto.accountingsoftware.databinding.LayoutBussniessListBinding;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.BussinessData;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BussniessList extends Fragment {

    LayoutBussniessListBinding binding;
    Context mContext;
    List<Business> businesses = new ArrayList<>();
    UserDetails userDetails;
    SignInResponse signInResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_bussniess_list, container, false);
        mContext = this.getContext();
        userDetails = UiUtil.getUserDetail(mContext);
        signInResponse = UiUtil.getUserDetails(mContext);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(mContext, null, MoreFragment.class);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBussinessList();
    }

    private void getBussinessList() {
        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        RestClient.getInstance(getContext()).getBusinessList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext())).enqueue(new Callback<BussinessData>() {
            public void onResponse(Call<BussinessData> call, Response<BussinessData> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            setAdapter(response.body().getData().getBusiness());
                        }else{
                            Toast.makeText(mContext,"Business not fetched",Toast.LENGTH_LONG).show();
                            setAdapter(userDetails.getBusiness());
                        }
                    }else{
                        Toast.makeText(mContext,"Business not fetched",Toast.LENGTH_LONG).show();
                        setAdapter(userDetails.getBusiness());
                    }
                } catch (Exception e) {
                    UiUtil.showToast(getContext(), e.getMessage());
                }
            }

            public void onFailure(Call<BussinessData> call, Throwable t) {
                Log.d("error", t.toString());
                UiUtil.cancelProgressDialogue();
            }
        });
    }

    public void setAdapter(List<Business> list){
        binding.rcCustomer.setHasFixedSize(true);
        binding.rcCustomer.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.rcCustomer.setAdapter(new BussniessAdapter(list, userDetails.getActiveBusiness().getId(), new BussniessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Business cust, int position) {
                userDetails.setActiveBusiness(cust);
                signInResponse.setUserDetails(new Gson().toJson(userDetails));
                AppSingle.getInstance().setComp_name(cust.getName());
                AddFragments.changgeName(getContext(),cust.getName());
                LoginData loginData = new Gson().fromJson(new Gson().toJson(signInResponse), LoginData.class);
                loginData.setExpires(signInResponse.getExpires());
                UiUtil.addUserDetails(mContext, signInResponse);
                AddFragments.addFragmentToDrawerActivity(mContext, null, MoreFragment.class);
            }

        }));
    }
}
