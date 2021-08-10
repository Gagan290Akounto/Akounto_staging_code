package com.akounto.accountingsoftware.Activity.Invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.CustomerAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CustomerList extends AppCompatActivity {

    Context mContext;
    List<Customer> customerList = new ArrayList();
    RecyclerView rc;
    ConstraintLayout noDataLayout;
    LinearLayout add_customer;
    public static Customer result = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layount_add_customer);
        mContext = this;
        rc = findViewById(R.id.rc_customer);
        add_customer = findViewById(R.id.add_customer);
        noDataLayout = findViewById(R.id.noDataLayout);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CustomerList.this, AddCustomers.class), 2);
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCustomerList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 2) {
                if (AddCustomers.result_add != null) {
                    result = new Customer();
                    result.setName(AddCustomers.result_add.getData().getName());
                    result.setEmail(AddCustomers.result_add.getData().getEmail());
                    result.setPhone(AddCustomers.result_add.getData().getPhone());
                    result.setHeadTransactionId(AddCustomers.result_add.getData().getHeadTransactionId());
                } else {
                    result = null;
                }
                lunch();
            }
        }
    }

    private void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", AddCustomers.result_add.getData().getName());
        setResult(2, intent);
        finish();//finishing activity
    }

    private void getCustomerList() {
        RestClient.getInstance((Activity) this).getCustomerList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<CustomerResponse>(this, (String) null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body() != null) {
                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            CustomerList.this.customerList = response.body().getData();
                            setAdapter();
                        } else {
                            rc.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rc.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    rc.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
                rc.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setAdapter() {
        try {
            if (customerList.size() != 0) {
                rc.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);
                rc.removeAllViews();
                rc.setAdapter(new CustomerAdapter(customerList, new CustomerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Customer cust, int position) {
                        result = cust;
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", result.getName());
                        setResult(2, intent);
                        finish();//finishing activity
                    }
                }));
                rc.invalidate();
            } else {
                rc.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }
}
