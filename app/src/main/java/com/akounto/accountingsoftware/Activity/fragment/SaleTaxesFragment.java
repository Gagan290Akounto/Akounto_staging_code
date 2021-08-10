package com.akounto.accountingsoftware.Activity.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.adapter.SettingSaleTaxItemClick;
import com.akounto.accountingsoftware.adapter.SettingSaleTaxesAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.response.saletax.EditSaleTaxResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SaleTaxesFragment extends Fragment implements SettingSaleTaxItemClick {
    Dialog dialog;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.sale_taxes_fragment, container, false);
        getSalesTaxList();
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, SettingMenu.class);
            }
        });
        this.view.findViewById(R.id.addSaleTax).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                showPopUpWindow();
            }
        });
        return this.view;
    }


    /* access modifiers changed from: private */
    public void getSalesTaxList() {
        RestClient.getInstance(getContext()).getSalesTaxList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),"").enqueue(new CustomCallBack<SalesTaxResponse>(getContext(), null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    SaleTaxesFragment.this.setUpSaleTaxList(response.body().getData());
                }
            }

            public void onFailure(Call<SalesTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpSaleTaxList(List<SaleTax> data) {
        RecyclerView recyclerView = this.view.findViewById(R.id.saleTaxesRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (data != null) {
            recyclerView.setAdapter(new SettingSaleTaxesAdapter(getContext(), data, this));
        }
    }

    public void showPopUpWindow() {
        Dialog dialog2=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.add_salestax_popup_layout);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        this.dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        this.dialog.findViewById(R.id.isCompoundTax).setVisibility(View.GONE);
        ((RadioButton) this.dialog.findViewById(R.id.isRecoverableTax)).setChecked(true);
        this.dialog.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                String taxName = ((EditText) dialog.findViewById(R.id.et_taxName)).getText().toString().trim();
                String taxDesc = ((EditText) dialog.findViewById(R.id.et_taxDesc)).getText().toString().trim();
                String taxNumber = ((EditText)dialog.findViewById(R.id.et_taxNumber)).getText().toString().trim();
                String taxRate = ((EditText) dialog.findViewById(R.id.et_taxRate)).getText().toString().trim();
                ArrayList<EffectiveTaxesItem> effectiveTaxesItems = new ArrayList<>();
                effectiveTaxesItems.add(new EffectiveTaxesItem(Double.parseDouble(taxRate)));
                ArrayList<EffectiveTaxesItem> arrayList = effectiveTaxesItems;
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"setting");
                b.putString(Constant.ACTION,"add_tax");
                SplashScreenActivity.sendEvent("setting_add_tax",b);
                RestClient.getInstance(getContext()).addSaleTax(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),new AddSaleTaxRequest(taxName, taxDesc, false, true, taxNumber, effectiveTaxesItems)).enqueue(new CustomCallBack<ResponseBody>(getContext(), "Adding Sale tax...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(SaleTaxesFragment.this.getContext(), "Sale Tax added!");
                            SaleTaxesFragment.this.dialog.dismiss();
                            SaleTaxesFragment.this.getSalesTaxList();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }
        });
        this.dialog.show();
    }

    public void editTax(SaleTax saleTax) {
        getSaleTaxById(saleTax.getId());
    }

    private void getSaleTaxById(int id) {
        Bundle b=new Bundle();
        b.putString(Constant.CATEGORY,"setting");
        b.putString(Constant.ACTION,"edit_tax");
        SplashScreenActivity.sendEvent("setting_edit_tax",b);
        RestClient.getInstance(getContext()).getSaleTaxById(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),id).enqueue(new CustomCallBack<EditSaleTaxResponse>(getContext(), null) {
            public void onResponse(Call<EditSaleTaxResponse> call, Response<EditSaleTaxResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    AddFragments.addFragmentToDrawerActivity(getActivity(), null, EditSaleTaxeFragment.class);
                    EditSaleTaxeFragment.saleTax=response.body().getData();
                }
            }

            public void onFailure(Call<EditSaleTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}
