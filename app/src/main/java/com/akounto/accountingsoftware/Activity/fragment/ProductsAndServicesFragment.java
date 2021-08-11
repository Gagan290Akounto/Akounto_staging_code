package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CreatingProductsAndServices;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceMenu;
import com.akounto.accountingsoftware.adapter.ProductAndServicesAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class ProductsAndServicesFragment extends Fragment implements View.OnClickListener, UpdateProduct {

    TextView goButton;
    ConstraintLayout noDataLayout;
    RelativeLayout withDataLayout;
    public List<Product> productList = new ArrayList();
    View view;
    Button btn_create_new;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_p_n_s, container, false);
        this.view = inflate;
        btn_create_new=view.findViewById(R.id.btn_create_new);
        goButton=view.findViewById(R.id.goButton);
        withDataLayout=view.findViewById(R.id.withDataLayout);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, InvoiceMenu.class,true);
            }
        });
        this.noDataLayout = view.findViewById(R.id.noDataLayout);
        inItUi();
        return this.view;
    }

    private void inItUi() {
        try{
        goButton.setOnClickListener(this);
        btn_create_new.setOnClickListener(this);}catch(Exception e){}
    }

    private void getSalesProductList() {
        RestClient.getInstance(getContext()).getSalesProductList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),"").enqueue(new CustomCallBack<SalesProductResponse>(getContext(), null) {
            public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List unused = ProductsAndServicesFragment.this.productList = response.body().getData();
                    ProductsAndServicesFragment productsAndServicesFragment = ProductsAndServicesFragment.this;
                    productsAndServicesFragment.populateData(productsAndServicesFragment.productList);
                }
            }

            public void onFailure(Call<SalesProductResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void populateData(List<Product> productList2) {
        if (productList2 == null || productList2.size() == 0) {
            this.noDataLayout.setVisibility(View.VISIBLE);
            withDataLayout.setVisibility(View.GONE);
        } else {
            this.noDataLayout.setVisibility(View.GONE);
            withDataLayout.setVisibility(View.VISIBLE);
        }
        RecyclerView product_rv = this.view.findViewById(R.id.prodAndServicesRecyclerView);
        ProductAndServicesAdapter productAndServicesAdapter = new ProductAndServicesAdapter(getContext(), productList2, this);
        product_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        product_rv.setAdapter(productAndServicesAdapter);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.goButton) {
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, CreatingProductsAndServicesFragment.class);
        }else if(v.getId() == R.id.btn_create_new){
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, CreatingProductsAndServicesFragment.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSalesProductList();
    }
    public void editProduct(Product product) {
        startActivity(CreatingProductsAndServices.buildIntent(getContext(), product));
    }
}
