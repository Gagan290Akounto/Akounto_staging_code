package com.akounto.accountingsoftware.Activity.Bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.Activity.fragment.BillClick;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.BillsAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.Bills;
import com.akounto.accountingsoftware.request.SendBill;
import com.akounto.accountingsoftware.response.GetBillsResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BillList extends AppCompatActivity implements View.OnClickListener, BillClick {

    TextView billHderTv;
    ImageView billImage;
    List<Bills> billsList;
    RecyclerView billsRecycler;
    BillClick click;
    TextView createBill;
    TextView title;
    ConstraintLayout noData;
    Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills_fragment_layout);
        mContext = this;
        //this.view = inflater.inflate(R.layout.bills_fragment_layout, container, false);
        this.click = this;
        noData = findViewById(R.id.noDataLayout);
        createBill = findViewById(R.id.createBill);
        createBill.setOnClickListener(this);
        this.billHderTv = findViewById(R.id.billHderTv);
        this.title = findViewById(R.id.title);
        this.billsRecycler = findViewById(R.id.billsRecycler);
        this.billImage = findViewById(R.id.billImage);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        findViewById(R.id.btn_create_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CreateBill.class));
            }
        });
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
        RestClient.getInstance(mContext).getBillsList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), billsReq).enqueue(new CustomCallBack<GetBillsResponse>(mContext, null) {
            public void onResponse(Call<GetBillsResponse> call, Response<GetBillsResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getData() != null) {
                    BillList.this.billsList = response.body().getData().getBills();
                    Log.d("CustomerResponse---", response.toString());
                    noData.setVisibility(View.GONE);
                    BillsAdapter customersAdapter = new BillsAdapter(BillList.this.mContext, response.body().getData().getBills(), BillList.this.click);
                    BillList.this.billsRecycler.setLayoutManager(new LinearLayoutManager(BillList.this.mContext));
                    BillList.this.billsRecycler.setAdapter(customersAdapter);
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
            startActivity(new Intent(mContext, CreateBill.class));
        }
    }

    public void billClickItem(Bills bill, int type) {
        startActivity(new Intent(mContext, CreateBillActivity.class));
    }

    public void deleteBill(Bills customer, int id) {
        if (id == 3) {
            Intent intent = new Intent(mContext, ViewBill.class);//
            intent.putExtra("GUID", customer.getGuid());
            startActivity(intent);
        } else if (id == 2) {
            Intent intent2 = new Intent(mContext, EditBill.class);
            intent2.putExtra("GUID", customer.getGuid());
            intent2.putExtra("IS_EDIT", true);
            startActivity(intent2);
        }
    }
}
