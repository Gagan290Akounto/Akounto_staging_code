package com.akounto.accountingsoftware.Activity.Bill;

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
import com.akounto.accountingsoftware.Activity.CreateProductsAndServicesPurchase;
import com.akounto.accountingsoftware.adapter.BillItem;
import com.akounto.accountingsoftware.adapter.BillItemAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.PurchaseItemResponse;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ListPurchaseItem extends AppCompatActivity {
    Context mContext;
    List<PurchaseItem> itmeList = new ArrayList();
    RecyclerView rc;
    LinearLayout add_item;
    public static PurchaseItem result = null;
    public static PurchaseItem adds_item = null;
    ConstraintLayout noData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);
        noData = findViewById(R.id.noDataLayout);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rc = findViewById(R.id.rc_customer);
        add_item = findViewById(R.id.add_customer);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CreateProductsAndServicesPurchase.buildIntentNo(mContext,"Create");
                startActivityForResult(new Intent(ListPurchaseItem.this, CreateProductsAndServicesPurchase.class), 8);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getItemList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 3) {
                lunch();
            } else if (requestCode == 4) {
                result = adds_item;
                try {
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", result.getDescription());
                    setResult(4, intent);
                } catch (Exception e) {
                }
                finish();//finishing activit
            } else if (requestCode == 8) {
                getItemList();
            }
        }
    }

    private void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        setResult(13, intent);
        finish();//finishing activity
    }

    private void getItemList() {
        RestClient.getInstance(mContext).getPurchaseItem(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<PurchaseItemResponse>(this, null) {
            public void onResponse(Call<PurchaseItemResponse> call, Response<PurchaseItemResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body()!=null) {
                        try {
                            Log.d("CustomerResponse---", response.toString());
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                ListPurchaseItem.this.itmeList = response.body().getData();
                                setAdapter();
                            }else{
                                rc.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            rc.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }else{
                        rc.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    rc.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<PurchaseItemResponse> call, Throwable t) {
                super.onFailure(call, t);
                rc.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getDataList() {

    }

    public void setAdapter() {
        if (itmeList.size() != 0) {
            rc.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            rc.removeAllViews();
            rc.setAdapter(new BillItemAdapter(itmeList, "$", new BillItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PurchaseItem item, int position) {
                    result = item;
                    adds_item = item;
                    startActivityForResult(new Intent(ListPurchaseItem.this, AddBillItem.class), 4);
                }
            }));
            rc.invalidate();
        } else {
            rc.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }
}
