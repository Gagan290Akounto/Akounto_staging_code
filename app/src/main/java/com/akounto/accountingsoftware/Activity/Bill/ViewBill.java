package com.akounto.accountingsoftware.Activity.Bill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.BillItem;
import com.akounto.accountingsoftware.databinding.LayoutBviewBillBinding;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ApproveBillReq;
import com.akounto.accountingsoftware.response.BillsByIdResponse;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.viewbill.ViewBillByid;
import com.akounto.accountingsoftware.response.viewbill.ViewBillResponse;
import com.akounto.accountingsoftware.util.BillPriceCalculator;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class ViewBill extends AppCompatActivity {

    private LayoutBviewBillBinding binding;
    private List<PurchaseItem> items = new ArrayList<>();
    private String guid;
    private int mDay;
    private int mMonth;
    private int mYear;
    private SimpleDateFormat simpleDateFormat;
    private BillPriceCalculator priceCal;
    private int position;
    public static ViewBillResponse receivedData;
    public static PurchaseItem edit_item = null;
    private String selectedCurrencyId = "$";
    private Context mContext;
    private List<Currency> currencyList = new ArrayList();
    private List<String> currencyListForSpinner = new ArrayList();
    private com.akounto.accountingsoftware.response.currency.Currency selectedExchangeCurrency;
    private com.akounto.accountingsoftware.response.currency.Currency selectedBussinessCurrency;
    private String selectdCurrencyCode;
    private List<String> currencyListForDisplay = new ArrayList<>();
    private String isoDatePattern = "yyyy-MM-dd";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_bview_bill);

        mContext = this;
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        binding.rcItem.setHasFixedSize(true);
        binding.rcItem.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        this.guid = getIntent().getStringExtra("GUID");
        if (this.guid != null) {
            getBillsDetailsById(this.guid);
        }

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.editDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receivedData != null) {
                    Intent intent = new Intent(ViewBill.this, EditBill.class);
                    intent.putExtra("GUID", receivedData.getData().getGuid());
                    intent.putExtra("IS_EDIT", true);
                    startActivity(intent);
                }
            }
        });

        binding.approveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveBill();
            }
        });
    }

    public void updateBillData(ViewBillByid receivedData) {
        //..selectedCurrencyId=data.getCustCurrencySymbol();
        fetchCurrencies();
        Gson gson = new Gson();
        try {
            binding.billNumber.setText("Bill Number #" + receivedData.getInvoiceNo());
            binding.noBill.setText("#" + receivedData.getPoNumber());
            //binding..setText(getFormattedDate(receivedData.getBillAt()));
            binding.dueAmount.setText(UiUtil.getBussinessCurrenSymbul(mContext) + " " + receivedData.getDueAmount());
            binding.dueOnPTV.setText(getFormattedDate(receivedData.getDueAt()));
            items = new ArrayList<>();
            for (int k = 0; k < receivedData.getBillTransaction().size(); k++) {
                try {
                    PurchaseItem temp = gson.fromJson(gson.toJson(receivedData.getBillTransaction().get(k)), PurchaseItem.class);
                    temp.setProductServiceTaxes(UiUtil.trasformeBill(receivedData.getBillTransaction().get(k).getTaxes()));
                    items.add(temp);
                } catch (Exception e) {
                }
            }
            items.get(0).getCompanyId();
            selectedCurrencyId = receivedData.getCustCurrencySymbol();
            binding.currency.setText(currencyListForDisplay.get(UiUtil.getcurancyindex(receivedData.getCurrency(), currencyList)));
            selectedCurrencyId = receivedData.getCustCurrencySymbol();
            binding.customerCompany.setText(receivedData.getVendor().getVendorName());
            binding.cusmoterName.setText(receivedData.getVendor().getEmail());
            binding.customerEmail.setText(receivedData.getVendor().getPhone());
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
        if (receivedData.getStatus() == 0) {
            //this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_draft));
            binding.approveTv.setVisibility(View.VISIBLE);
            binding.editDraft.setVisibility(View.VISIBLE);
        } else if (receivedData.getStatus() == 100) {
            setAprove();
            //this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_approved));
            binding.approveTv.setText("Approved");
            binding.approveTv.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
        } else if (receivedData.getStatus() == 150) {
            //this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_partialsettled));
            binding.approveTv.setText("Partialsettled");
            binding.approveTv.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
        } else {
            binding.approveTv.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
        }
        setAdapter();
    }

    public void setAprove() {
        binding.billDetails.setBackgroundColor(Color.parseColor("#28a745"));
        binding.billDetailsExr.setBackgroundColor(Color.parseColor("#28a745"));
        binding.billNumber.setTextColor(Color.parseColor("#ffffff"));
        binding.noBillTitle.setTextColor(Color.parseColor("#ffffff"));
        binding.noBill.setTextColor(Color.parseColor("#ffffff"));
        binding.dueAmountTitle.setTextColor(Color.parseColor("#ffffff"));
        binding.dueAmount.setTextColor(Color.parseColor("#ffffff"));
        binding.dueOnPTVTitle.setTextColor(Color.parseColor("#ffffff"));
        binding.dueOnPTV.setTextColor(Color.parseColor("#ffffff"));
    }

    private void fetchCurrencies() {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
                this.currencyListForDisplay.add(jsonObject.getString("Name") + " ( " + jsonObject.getString("Id") + " ) ");
                this.currencyListForSpinner.add(jsonObject.getString("Name"));
            }
        } catch (Exception e) {
        }
    }

    public static String getFormattedDate(String invoiceDate) {
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = simpleDateFormat2.parse(invoiceDate);
            if (date != null) {
                return newDateFormat.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void getBillsDetailsById(String id) {
        RestClient.getInstance(this).getBillForEdit(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<ViewBillResponse>(this, null) {
            public void onResponse(Call<ViewBillResponse> call, Response<ViewBillResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    receivedData = response.body();
                    ViewBill.this.updateBillData(response.body().getData());
                }
            }

            public void onFailure(Call<ViewBillResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                UiUtil.showToast(ViewBill.this, t.toString());
            }
        });
    }

    public void setAdapter() {
        try {
            if (items != null) {
                binding.rcItem.removeAllViews();
                binding.rcItem.setAdapter(new BillItem(items, selectedCurrencyId, new BillItem.OnItemClickListener() {
                    @Override
                    public void onItemClick(PurchaseItem item, int p) {

                    }
                }));
                displayPrice();
                binding.rcItem.invalidate();
            }
        } catch (Exception e) {
        }
    }

    private void displayPrice() {
        try {
            priceCal = new BillPriceCalculator(items);
            binding.subtotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getSub_totel()));
            binding.taxTotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            binding.grantTotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            display_taxs();
        } catch (Exception e) {
        }
    }

    private void display_taxs() {
        try {
            binding.taxsNameList.removeAllViews();
            binding.taxsAmountList.removeAllViews();
            for (int i = 0; i < priceCal.getTaxes().size(); i++) {
                TextView name = new TextView(mContext);
                TextView amount = new TextView(mContext);
                if (priceCal.getTaxes().get(i).getName() != null) {
                    name.setText(priceCal.getTaxes().get(i).getName() + " (" + priceCal.getTaxes().get(i).getRate() + " %)");
                } else {
                    name.setText(priceCal.getTaxes().get(i).getTaxName() + " (" + priceCal.getTaxes().get(i).getRate() + " %)");
                }
                amount.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTaxes().get(i).getAmount()));
                amount.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                amount.setGravity(Gravity.RIGHT);
                binding.taxsNameList.addView(name);
                binding.taxsAmountList.addView(amount);
            }
        } catch (Exception e) {
        }
    }

    private void approveBill() {
        ApproveBillReq req = new ApproveBillReq();
        req.setBillId(this.receivedData.getData().getId());
        req.setStatus(100);
        RestClient.getInstance(this).updateBillStatus(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), req).enqueue(new CustomCallBack<BillsByIdResponse>(this, null) {
            public void onResponse(Call<BillsByIdResponse> call, Response<BillsByIdResponse> response) {
                super.onResponse(call, response);
                ViewBill.this.finish();
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"billing");
                b.putString(Constant.ACTION,"approve_bill");
                SplashScreenActivity.sendEvent("bill_approve_bill",b);

            }

            public void onFailure(Call<BillsByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
