package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Business;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.SettingSaleTaxItemClick;
import com.akounto.accountingsoftware.adapter.SettingSaleTaxesAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.GetCompanyResponse;
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class BusinessFragment extends Fragment implements SettingSaleTaxItemClick {
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.business_fragment, container, false);
        initUI();
        return this.view;
    }

    private void initUI() {
        this.view.findViewById(R.id.createBusiness).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                BusinessFragment.this.lambda$initUI$0$BusinessFragment(view);
            }
        });
    }

    public void lambda$initUI$0$BusinessFragment(View v) {
        createABusiness();
    }

    public void onResume() {
        super.onResume();
        setUpSaleTaxList();
    }

    private void setUpSaleTaxList() {
        List<SaleTax> adapterList = convertToSaleTaxForAdapter(new Gson().fromJson(UiUtil.getUserDetails(getContext()).getUserDetails(), UserDetails.class).getBusiness());
        RecyclerView recyclerView = this.view.findViewById(R.id.saleTaxesRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SettingSaleTaxesAdapter(getContext(), adapterList, this));
    }

    private List<SaleTax> convertToSaleTaxForAdapter(List<Business> businessList) {
        List<SaleTax> list = new ArrayList<>();
        for (Business business : businessList) {
            list.add(new SaleTax(business.getId(), business.getName()));
        }
        return list;
    }

    public void createABusiness() {
        AddFragments.addFragmentToDrawerActivity(getContext(), null, AddBusinessFragment.class);
    }

    public void editTax(SaleTax saleTax) {
        getBusinessById(saleTax.getId());
    }

    private void getBusinessById(int id) {
        RestClient.getInstance(getContext()).getBusinessById(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),id).enqueue(new CustomCallBack<GetCompanyResponse>(getContext(), null) {
            public void onResponse(Call<GetCompanyResponse> call, Response<GetCompanyResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("businessDetails", response.body().getBusinessDetails());
                    AddFragments.addFragmentToDrawerActivity(BusinessFragment.this.getContext(), bundle, EditBusinessFragment.class);
                }
            }

            public void onFailure(Call<GetCompanyResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}
