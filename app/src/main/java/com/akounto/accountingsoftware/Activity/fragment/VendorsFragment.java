package com.akounto.accountingsoftware.Activity.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddVendorActivity;
import com.akounto.accountingsoftware.Activity.Bill.BillMenu;
import com.akounto.accountingsoftware.adapter.VendorAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.DeleteCustomerResponse;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.VendorResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VendorsFragment extends Fragment implements CustomerClick {

    private TextView addCustomerButton, title;
    public List<Vendor> customerList = new ArrayList();
    private RecyclerView customer_rv;
    private ConstraintLayout noDataLayout;
    private View view;
    Button create_new;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.customers_fragment, container, false);
        this.view = inflate;
        try {
            TextView tv = view.findViewById(R.id.tv_title);
            create_new = view.findViewById(R.id.btn_create_new);
            create_new.setText("Add a Vendor");
            create_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VendorsFragment.this.lambda$inItUi$0$VendorsFragment(view);
                }
            });
            title = view.findViewById(R.id.title);//Add your Customers &amp; start invoicing
            title.setText("Add your vendors & start invoicing");
            tv.setText("Vendors");
            view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFragments.addFragmentToDrawerActivity(getActivity(), null, BillMenu.class);
                }
            });
            this.customer_rv = inflate.findViewById(R.id.customersRecyclerView);
            this.noDataLayout = this.view.findViewById(R.id.noDataLayout);
            inItUi();
        } catch (Exception e) {
        }
        return this.view;
    }

    private void inItUi() {
        TextView textView = this.view.findViewById(R.id.addCustomerButton);
        this.addCustomerButton = textView;
        //textView.setText("Add Vendor");
        this.addCustomerButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                VendorsFragment.this.lambda$inItUi$0$VendorsFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$inItUi$0$VendorsFragment(View click) {
        startActivity(new Intent(getActivity(), AddVendorActivity.class));
    }

    public void onResume() {
        super.onResume();
        getCustomerList();
    }

    /* access modifiers changed from: private */
    public void getCustomerList() {
        RestClient.getInstance(getContext()).getVendorList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "").enqueue(new CustomCallBack<VendorResponse>(getContext(), null) {
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    List unused = VendorsFragment.this.customerList = response.body().getData();
                    VendorsFragment vendorsFragment = VendorsFragment.this;
                    vendorsFragment.populateData(vendorsFragment.customerList);
                }
            }

            public void onFailure(Call<VendorResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void populateData(List<Vendor> customerList2) {
        if (customerList2 == null) {
            this.noDataLayout.setVisibility(View.VISIBLE);
        } else {
            this.noDataLayout.setVisibility(View.GONE);
        }
        VendorAdapter customersAdapter = new VendorAdapter(getContext(), customerList2, this);
        this.customer_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        this.customer_rv.setAdapter(customersAdapter);
    }

    private void openDialogue(final int id) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogue_delete);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView = dialog.findViewById(R.id.head);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VendorsFragment.this.deleteVendor(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void deleteVendor(final int id) {
        RestClient.getInstance(getActivity()).deleteVendor(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), String.valueOf(id)).enqueue(new CustomCallBack<DeleteCustomerResponse>(getContext(), "Deleting Customer...") {
            public void onResponse(Call<DeleteCustomerResponse> call, Response<DeleteCustomerResponse> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    Context context = VendorsFragment.this.getContext();
                    UiUtil.showToast(context, "Deleted " + id);
                    VendorsFragment.this.getCustomerList();
                    return;
                }
                UiUtil.showToast(VendorsFragment.this.getContext(), response.body().getTransactionStatus().getError().getDescription());
            }

            public void onFailure(Call<DeleteCustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void deleteCustomer(Vendor customer, int id) {
        if (id == 1) {
            Intent intent = new Intent(getActivity(), AddVendorActivity.class);
            intent.putExtra(UiConstants.IS_EDIT, 1);
            intent.putExtra(UiConstants.CUSTOMER_DATE, new Gson().toJson(customer));
            startActivity(intent);
        } else if (id == 2) {
            Intent intent2 = new Intent(getActivity(), AddVendorActivity.class);
            intent2.putExtra(UiConstants.IS_EDIT, 2);
            intent2.putExtra(UiConstants.CUSTOMER_DATE, new Gson().toJson(customer));
            startActivity(intent2);
        } else if (id == 3) {
            openDialogue(customer.getHeadTransactionId());
        }
    }
}
