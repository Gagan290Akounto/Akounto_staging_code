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
import com.akounto.accountingsoftware.Activity.CreatingProductsAndServices;
import com.akounto.accountingsoftware.adapter.InvoiceItemAdapter;
import com.akounto.accountingsoftware.adapter.ItemAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ItemList extends AppCompatActivity {

    Context mContext;
    List<Product> itmeList = new ArrayList();
    RecyclerView rc;
    LinearLayout add_item;
    public static Product result = null;
    public static Product adds_item = null;
    ConstraintLayout noData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);

        mContext = this;
        rc = findViewById(R.id.rc_customer);
        add_item = findViewById(R.id.add_customer);
        noData = findViewById(R.id.noDataLayout);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ItemList.this, CreatingProductsAndServices.class), 8);
            }
        });
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = null;
                adds_item = null;
                finish();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getItemList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        result = null;
        adds_item = null;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 3) {
/*            if (AddCustomers.result_add != null) {
                result=new Product();
                result.setName(result_add.getData().getName());
                result.setDescription(result_add.getData().getDe);
                //result.setPhone(AddCustomers.result_add.getData().getPhone());
            } else {
                result=null;
            }*/
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
        intent.putExtra("MESSAGE", AddCustomers.result_add.getData().getName());
        setResult(3, intent);
        finish();//finishing activity
    }

    private void getItemList() {

        RestClient.getInstance((Activity) this).getSalesProductList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<SalesProductResponse>(this, (String) null) {
            public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body()!=null) {
                        try {
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                ItemList.this.itmeList = response.body().getData();
                                setAdapter();
                            }else{
                                UiUtil.showToast(getApplicationContext(),"Fail to fetch items");
                                rc.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.e("Error :: ",e.getMessage());
                            rc.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }else{
                        rc.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("Error :: ",e.getMessage());
                    rc.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<SalesProductResponse> call, Throwable t) {
                super.onFailure(call, t);
                rc.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setAdapter() {
        if (itmeList.size() != 0) {
            rc.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            rc.removeAllViews();
            rc.setAdapter(new InvoiceItemAdapter(itmeList, "$", new InvoiceItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product item, int position) {
                    result = item;
                    adds_item = item;
                    startActivityForResult(new Intent(ItemList.this, AddItem.class), 4);
                }
            }));
            rc.invalidate();
        } else {
            rc.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

}
