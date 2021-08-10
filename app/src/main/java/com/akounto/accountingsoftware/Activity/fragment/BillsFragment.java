package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Bill.BillMenu;
import com.akounto.accountingsoftware.Activity.Bill.CreateBill;
import com.akounto.accountingsoftware.Activity.Bill.EditBill;
import com.akounto.accountingsoftware.Activity.Bill.ViewBill;
import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.adapter.BillsAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.Bills;
import com.akounto.accountingsoftware.request.SendBill;
import com.akounto.accountingsoftware.response.GetBillsResponse;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BillsFragment extends Fragment implements View.OnClickListener, BillClick {
    TextView billHderTv;
    ImageView billImage;
    List<Bills> billsList;
    RecyclerView billsRecycler;
    BillClick click;
    TextView createBill;
    TextView title;
    View view;
    ConstraintLayout noData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.bills_fragment_layout, container, false);
        this.click = this;
        noData = view.findViewById(R.id.noDataLayout);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, BillMenu.class);
            }
        });
        view.findViewById(R.id.btn_create_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateBill.class));
            }
        });
        inItUi();
        return this.view;
    }

    private void inItUi() {
        TextView textView = this.view.findViewById(R.id.createBill);
        this.createBill = textView;
        textView.setOnClickListener(this);
        this.billHderTv = this.view.findViewById(R.id.billHderTv);
        this.title = this.view.findViewById(R.id.title);
        this.billsRecycler = this.view.findViewById(R.id.billsRecycler);
        this.billImage = this.view.findViewById(R.id.billImage);
    }

    public void onResume() {
        super.onResume();
        getBillsList();
    }

    private void getBillsList() {
        SendBill billsReq = new SendBill();
        billsReq.setPageNumber(1);
        billsReq.setRecordsPerPage(100);
        billsReq.setPageRequestType(1);
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(getContext()).getBillsList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), billsReq).enqueue(new CustomCallBack<GetBillsResponse>(getContext(), null) {
            public void onResponse(Call<GetBillsResponse> call, Response<GetBillsResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getData() != null) {
                    BillsFragment.this.billsList = response.body().getData().getBills();
                    Log.d("CustomerResponse---", response.toString());
                    noData.setVisibility(View.GONE);
                    BillsAdapter customersAdapter = new BillsAdapter(BillsFragment.this.getActivity(), response.body().getData().getBills(), BillsFragment.this.click);
                    BillsFragment.this.billsRecycler.setLayoutManager(new LinearLayoutManager(BillsFragment.this.getActivity()));
                    BillsFragment.this.billsRecycler.setAdapter(customersAdapter);
                }
            }

            public void onFailure(Call<GetBillsResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.createBill) {
            startActivity(new Intent(getActivity(), CreateBill.class));
        }
    }

    public void billClickItem(Bills bill, int type) {
        startActivity(new Intent(getActivity(), CreateBillActivity.class));
    }

    public void deleteBill(Bills customer, int id) {
        if (id == 3) {
            Intent intent = new Intent(getActivity(), ViewBill.class);//
            intent.putExtra("GUID", customer.getGuid());
            startActivity(intent);
        } else if (id == 2) {
            Intent intent2 = new Intent(getActivity(), EditBill.class);
            intent2.putExtra("GUID", customer.getGuid());
            intent2.putExtra("IS_EDIT", true);
            startActivity(intent2);
        }
    }
}
