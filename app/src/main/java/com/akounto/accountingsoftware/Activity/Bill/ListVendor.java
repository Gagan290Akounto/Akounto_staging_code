package com.akounto.accountingsoftware.Activity.Bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddVendorActivity;
import com.akounto.accountingsoftware.Activity.Invoice.AddCustomers;
import com.akounto.accountingsoftware.adapter.BillVendorAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.VendorResponse;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ListVendor extends AppCompatActivity {

    Context mContext;
    List<Vendor> vendorList = new ArrayList();
    RecyclerView rc;
    TextView title;
    LinearLayout add_vendor;
    public static Vendor result = null;
    private ConstraintLayout noDataLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_vendor);

        mContext = this;
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rc = findViewById(R.id.rc_vendor);
        add_vendor = findViewById(R.id.add_vendor);
        noDataLayout = findViewById(R.id.noDataLayout);
        title=findViewById(R.id.title);//Add your Customers &amp; start invoicing
        title.setText("Add your vendors & start invoicing");
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        add_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ListVendor.this, AddVendorActivity.class), 12);
            }
        });
        getVendorList();
    }

    private void getVendorList() {
        this.vendorList.clear();
        RestClient.getInstance(this).getVendorList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<VendorResponse>(this, null) {
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        vendorList = response.body().getData();
                        setAdapter();
                    }else{
                        rc.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    rc.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<VendorResponse> call, Throwable t) {
                super.onFailure(call, t);
                rc.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 12) {
                if (AddCustomers.result_add != null) {
                    result = new Vendor();
                    result.setVendorName(AddCustomers.result_add.getData().getName());
                    result.setEmail(AddCustomers.result_add.getData().getEmail());
                    result.setPhone(AddCustomers.result_add.getData().getPhone());
                } else {
                    result = null;
                }
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "");
                setResult(11, intent);
                finish();//finishing activity
            }
        }
    }

    public void setAdapter() {
        if (vendorList.size() != 0) {
            rc.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            rc.removeAllViews();
            rc.setAdapter(new BillVendorAdapter(vendorList, (cust, position) -> {
                result = cust;
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", result.getVendorName());
                setResult(11, intent);
                finish();//finishing activity
            }));
            rc.invalidate();
        } else {
            rc.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }
}
