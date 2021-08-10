package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.EffectiveSaleTaxesAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class EditSaleTaxeFragment extends Fragment {

    TextView currentRateTv;
    EditText etEffectiveDate;
    EditText etNewTaxRate;
    EditText etTaxDesc;
    EditText etTaxName;
    EditText etTaxNumber;
    private int mDay;
    private int mMonth;
    private int mYear;
    public static SaleTax saleTax;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.edit_sale_tax_fragment, container, false);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getActivity().getSupportFragmentManager().popBackStack();
                } catch (Exception e) {
                }
            }
        });
        initUI();
        return view;
    }

    private void initUI() {
        this.etTaxName = this.view.findViewById(R.id.et_taxName);
        this.etTaxDesc = this.view.findViewById(R.id.et_taxDesc);
        this.etTaxNumber = this.view.findViewById(R.id.et_taxNumber);
        this.etNewTaxRate = this.view.findViewById(R.id.et_newTaxRate);
        this.etEffectiveDate = this.view.findViewById(R.id.et_effectiveDate);
        this.currentRateTv = this.view.findViewById(R.id.taxRateTv);
        Locale.setDefault(Locale.US);
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        EditText editText = this.etEffectiveDate;
        editText.setText(this.mYear + "-" + (this.mMonth + 1) + "-" + this.mDay);
        this.etEffectiveDate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditSaleTaxeFragment.this.lambda$initUI$1$EditSaleTaxeFragment(view);
            }
        });
        SaleTax saleTax2 = this.saleTax;
        if (saleTax2 != null) {
            this.etTaxName.setText(saleTax2.getName());
            this.etTaxDesc.setText(this.saleTax.getDescription().toString());
            this.etTaxNumber.setText(this.saleTax.getAccountTaxId().toString());
            TextView textView = this.currentRateTv;
            textView.setText(this.saleTax.getCurrentRate() + "");
            setUpSaleTaxList(this.saleTax.getEffectiveTaxes());
            this.view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    EditSaleTaxeFragment.this.lambda$initUI$2$EditSaleTaxeFragment(view);
                }
            });
        }
    }

    public /* synthetic */ void lambda$initUI$1$EditSaleTaxeFragment(View v) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditSaleTaxeFragment.this.lambda$null$0$EditSaleTaxeFragment(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    public /* synthetic */ void lambda$null$0$EditSaleTaxeFragment(DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        EditText editText = this.etEffectiveDate;
        editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public /* synthetic */ void lambda$initUI$2$EditSaleTaxeFragment(View v) {
        save(this.saleTax);
    }

    private void setUpSaleTaxList(List<EffectiveTaxesItem> data) {
        RecyclerView recyclerView = this.view.findViewById(R.id.saleTaxesRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EffectiveSaleTaxesAdapter(getContext(), data));
    }

    public void save(SaleTax saleTax2) {
        ArrayList<EffectiveTaxesItem> effectiveTaxesItems;
        String taxName = this.etTaxName.getText().toString().trim();
        if (taxName.isEmpty()) {
            UiUtil.showToast(getContext(), "Tax name can't be empty.");
            return;
        }
        String taxDesc = this.etTaxDesc.getText().toString().trim();
        String taxNumber = this.etTaxNumber.getText().toString().trim();
        String taxRate = this.etNewTaxRate.getText().toString().trim();
        if (!taxRate.isEmpty()) {
            ArrayList<EffectiveTaxesItem> effectiveTaxesItems2 = new ArrayList<>();
            effectiveTaxesItems2.add(new EffectiveTaxesItem(Double.parseDouble(taxRate), this.etEffectiveDate.getText().toString()));
            effectiveTaxesItems = effectiveTaxesItems2;
        } else {
            effectiveTaxesItems = null;
        }
        RestClient.getInstance(getContext()).editSaleTax(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), new SaleTax(taxDesc, saleTax2.getId(), effectiveTaxesItems, taxNumber, false, true, saleTax2.getSubHeadId(), taxName)).enqueue(new CustomCallBack<ResponseBody>(getContext(), "Modifying Sale tax...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(EditSaleTaxeFragment.this.getContext(), "Sale Tax updated!");
                    EditSaleTaxeFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(EditSaleTaxeFragment.this.getContext(), "Error occurred while updating.");
            }
        });
    }
}
