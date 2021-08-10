package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddCustomersActivity;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceMenu;
import com.akounto.accountingsoftware.adapter.CustomersAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.DeleteCustomerResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class CustomersFragment extends Fragment implements UpdateCustomer {

    public List<Customer> customerList = new ArrayList();
    RecyclerView customer_rv;
    ConstraintLayout noDataLayout;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.customers_fragment, container, false);
        this.view = inflate;
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, InvoiceMenu.class);
            }
        });
        this.noDataLayout = inflate.findViewById(R.id.noDataLayout);
        this.customer_rv = this.view.findViewById(R.id.customersRecyclerView);
        this.view.findViewById(R.id.addCustomerButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CustomersFragment.this.lambda$onCreateView$0$CustomersFragment(view);
            }
        });
        this.view.findViewById(R.id.btn_create_new).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CustomersFragment.this.lambda$onCreateView$0$CustomersFragment(view);
            }
        });
        return this.view;
    }

    public /* synthetic */ void lambda$onCreateView$0$CustomersFragment(View click) {
       // startActivity(new Intent(getActivity(), AddCustomersActivity.class));
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, AddCustomerFragmet.class);
    }

    public void onResume() {
        super.onResume();
        getCustomerList();
    }

    /* access modifiers changed from: private */
    public void getCustomerList() {
        RestClient.getInstance(getContext()).getCustomerList(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),"").enqueue(new CustomCallBack<CustomerResponse>(getContext(), null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    List unused = CustomersFragment.this.customerList = response.body().getData();
                    CustomersFragment customersFragment = CustomersFragment.this;
                    customersFragment.populateData(customersFragment.customerList);
                }
            }

            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void populateData(List<Customer> customerList2) {
        if (customerList2 == null) {
            this.noDataLayout.setVisibility(View.VISIBLE);
        } else {
            this.noDataLayout.setVisibility(View.GONE);
        }
        CustomersAdapter customersAdapter = new CustomersAdapter(getContext(), customerList2, this);
        this.customer_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        this.customer_rv.setAdapter(customersAdapter);
    }

    public void deleteCustomer(final int id) {
        RestClient.getInstance(getActivity()).deleteCustomer(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),String.valueOf(id)).enqueue(new CustomCallBack<DeleteCustomerResponse>(getContext(), "Deleting Customer...") {
            public void onResponse(Call<DeleteCustomerResponse> call, Response<DeleteCustomerResponse> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    Context context = CustomersFragment.this.getContext();
                    UiUtil.showToast(context, "Deleted " + id);
                    CustomersFragment.this.getCustomerList();
                    return;
                }
                UiUtil.showToast(CustomersFragment.this.getContext(), response.body().getTransactionStatus().getError().getDescription());
            }

            public void onFailure(Call<DeleteCustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void deleteCustomer(Customer customer, int id) {
        if (id == 3) {
            deleteCustomer(customer.getHeadTransactionId());
        } else if (id == 2) {
           /* Intent intent = new Intent(getActivity(), AddCustomersActivity.class);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("EXTRA_DATA", new Gson().toJson(customer));
            intent.putExtra(UiConstants.IS_EDIT, 2);
            intent.putExtra(UiConstants.CUSTOMER_DATE, new Gson().toJson(customer));
            startActivity(intent);*/
            AddCustomerFragmet.is_edit=true;
            AddCustomerFragmet.editCont=2;
            AddCustomerFragmet.customer=customer;
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, AddCustomerFragmet.class);
        }
    }
}
