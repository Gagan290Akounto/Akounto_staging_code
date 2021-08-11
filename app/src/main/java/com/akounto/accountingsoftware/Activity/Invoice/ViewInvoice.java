package com.akounto.accountingsoftware.Activity.Invoice;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Activity.fragment.InvoicesFragment;
import com.akounto.accountingsoftware.util.AddFragments;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.SendEstimateActivity;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.ItemAdapter;
import com.akounto.accountingsoftware.databinding.LayoutTest3Binding;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ApproveRecurringInvoice;
import com.akounto.accountingsoftware.request.ConvertToInvoice;
import com.akounto.accountingsoftware.request.UpdateStatus;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.UpdateStatusResponse;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.PriceCal;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class ViewInvoice extends AppCompatActivity {

    public static InvoiceDetails receivedData;
    private List<Product> items = new ArrayList<>();
    private RecyclerView rc;
    private Context mContext;
    private LayoutTest3Binding binding;
    private String selectedCurrencyId = "", bussinessCurrency = "$";
    private PriceCal priceCal;
    public static String cust_email = "";
    private List<Currency> currencyList = new ArrayList<>();
    private Currency corency = new Currency();
    public static String guid;
    private int discount_index = 0;
    private TextView tv_dicount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_test3);
        mContext = this;
        guid = "";
        bussinessCurrency = UiUtil.getBussinessCurrenSymbul(mContext);
        rc = binding.rcItem;
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddFragments.addFragmentToDrawerActivity(getApplicationContext(), null, InvoicesFragment.class,true);
                finish();
            }
        });
        binding.convertToInvoice.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ConvertToInvoice invoice = new ConvertToInvoice();
                invoice.setEstimateId(receivedData.getId());
                convertToInvoice(invoice);
            }
        });
        binding.cacncelInvoice.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                deleteCard();
            }
        });
        binding.approveTv.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                sendAproval();
            }
        });
        binding.sendEstimate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                startActivity(new Intent(ViewInvoice.this, SendEstimateActivity.class));
            }
        });
        binding.editDraft.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                startActivity(EditInvoice.buildIntentWithData(ViewInvoice.this, Type.ESTIMATES.name(), receivedData));
            }
        });
        if (getIntent().getSerializableExtra("DATA") != null) {
            try {
                Gson gson = new Gson();
                String stringExtra = getIntent().getStringExtra("DATA");
                receivedData = gson.fromJson(stringExtra, InvoiceDetails.class);
                items = new ArrayList<>();
                for (int k = 0; k < receivedData.getInvoiceTransaction().size(); k++) {
                    Product temp = gson.fromJson(gson.toJson(receivedData.getInvoiceTransaction().get(k)), Product.class);
                    if (temp.getTaxes() != null) {
                        temp.setProductServiceTaxes(UiUtil.trasforme(temp.getTaxes()));
                    }
                    items.add(temp);
                }
            } catch (Exception e) {
                Log.e("Error :: ", e.getMessage());
            }
            items = UiUtil.reset_list(items);

            if (receivedData.getDiscountType() != 0) {
                items = UiUtil.priceAfterDiscount(items, UiUtil.getDisType(receivedData.getDiscountType()), receivedData.getDiscount());
            } else {
                items = items;
            }
            discount_index = UiUtil.getDisType(receivedData.getDiscountType());
            if (receivedData.getDiscount() != 0.0) {
                binding.discountLlView.setVisibility(View.VISIBLE);
                if (discount_index == 2) {
                    binding.tvDicount.setText(UiUtil.getBussinessCurrenSymbul(mContext) + " " + receivedData.getDiscount() + "");
                } else {
                    binding.tvDicount.setText(receivedData.getDiscount() + "" + " %");
                }
            } else {
                binding.discountLlView.setVisibility(View.GONE);
            }
        }
        try {
            selectedCurrencyId = receivedData.getCustCurrencySymbol();
            binding.customerCompany.setText(receivedData.getCustomer().getName());
            binding.cusmoterName.setText(receivedData.getCustomer().getEmail());
            cust_email = receivedData.getCustomer().getEmail();
            binding.customerEmail.setText(receivedData.getCustomer().getPhone());
            binding.billNumber.setText(receivedData.getInvoiceNoPS());
            try {
                binding.noInvoice.setText("#" + String.valueOf(receivedData.getInvoiceNo()));
            } catch (Exception e) {
                Log.e("Error :: ", e.toString());
            }
            binding.dueAmount.setText(bussinessCurrency + " " + String.format("%.2f", receivedData.getDueAmount()));
            binding.dueOnPTV.setText(getFormattedDate(receivedData.getPaymentDue()));
            fetchCurrencies();
            corency = UiUtil.getcurancy(receivedData.getCustCurrency(), currencyList);
            binding.currency.setText(corency.getName() + " (" + corency.getId() + " )");
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }

        if (receivedData.getStatus() == 0) {
            binding.approveTv.setVisibility(View.VISIBLE);
            binding.approveTv.setText("Approve draft");
            binding.editDraft.setVisibility(View.VISIBLE);
        } else if (receivedData.getStatus() == 100) {
            binding.approveTv.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
            binding.cacncelInvoice.setVisibility(View.VISIBLE);
            binding.convertToInvoice.setVisibility(View.GONE);
            binding.sendEstimate.setVisibility(View.VISIBLE);
        } else if (receivedData.getStatus() == 400) {
            binding.cacncelInvoice.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
            binding.sendEstimate.setVisibility(View.GONE);
            binding.convertToInvoice.setVisibility(View.GONE);
            binding.approveTv.setVisibility(View.GONE);
        } else {
            binding.editDraft.setVisibility(View.GONE);
            binding.approveTv.setVisibility(View.GONE);
            binding.sendEstimate.setText("Resend Invoice");
            binding.sendEstimate.setVisibility(View.VISIBLE);
        }
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!guid.equalsIgnoreCase("")) {
            fetchInvoiceDetails(guid, 2);
        }
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
        }

    }

    private void setUI() {

        try {
            Gson gson = new Gson();
            items = new ArrayList<>();
            for (int k = 0; k < receivedData.getInvoiceTransaction().size(); k++) {
                Product temp = gson.fromJson(gson.toJson(receivedData.getInvoiceTransaction().get(k)), Product.class);
                if (temp.getTaxes() != null) {
                    temp.setProductServiceTaxes(UiUtil.trasforme(temp.getTaxes()));
                }
                items.add(temp);
            }
            items = UiUtil.reset_list(items);

            if (receivedData.getDiscountType() != 0) {
                items = UiUtil.priceAfterDiscount(items, UiUtil.getDisType(receivedData.getDiscountType()), receivedData.getDiscount());
            } else {
                items = items;
            }

            discount_index = UiUtil.getDisType(receivedData.getDiscountType());
            if (receivedData.getDiscount() != 0.0) {
                binding.discountLlView.setVisibility(View.VISIBLE);
                if (discount_index == 2) {
                    binding.tvDicount.setText(UiUtil.getBussinessCurrenSymbul(mContext) + " " + receivedData.getDiscount() + "");
                } else {
                    binding.tvDicount.setText(receivedData.getDiscount() + "" + " %");
                }
            } else {
                binding.discountLlView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Error :: ", e.getMessage());
        }

        try {
            selectedCurrencyId = receivedData.getCustCurrencySymbol();
            binding.customerCompany.setText(receivedData.getCustomer().getName());
            binding.cusmoterName.setText(receivedData.getCustomer().getEmail());
            cust_email = receivedData.getCustomer().getEmail();
            binding.customerEmail.setText(receivedData.getCustomer().getPhone());
            binding.billNumber.setText(receivedData.getInvoiceNoPS());
            try {
                binding.noInvoice.setText("#" + String.valueOf(receivedData.getInvoiceNo()));
            } catch (Exception e) {
                Log.e("Error :: ", e.toString());
            }
            binding.dueAmount.setText(bussinessCurrency + " " + String.format("%.2f", receivedData.getDueAmount()));
            binding.dueOnPTV.setText(getFormattedDate(receivedData.getPaymentDue()));
            fetchCurrencies();
            corency = UiUtil.getcurancy(receivedData.getCustCurrency(), currencyList);
            binding.currency.setText(corency.getName() + " (" + corency.getId() + " )");
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }

        if (receivedData.getStatus() == 0) {
            binding.approveTv.setVisibility(View.VISIBLE);
            binding.approveTv.setText("Approve draft");
            binding.editDraft.setVisibility(View.VISIBLE);
        } else if (receivedData.getStatus() == 100) {
            binding.approveTv.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
            binding.cacncelInvoice.setVisibility(View.VISIBLE);
            binding.convertToInvoice.setVisibility(View.GONE);
            binding.sendEstimate.setVisibility(View.VISIBLE);
        } else if (receivedData.getStatus() == 400) {
            binding.cacncelInvoice.setVisibility(View.GONE);
            binding.editDraft.setVisibility(View.GONE);
            binding.sendEstimate.setVisibility(View.GONE);
            binding.convertToInvoice.setVisibility(View.GONE);
            binding.approveTv.setVisibility(View.GONE);
        } else {
            binding.editDraft.setVisibility(View.GONE);
            binding.approveTv.setVisibility(View.GONE);
            binding.sendEstimate.setText("Resend Invoice");
            binding.sendEstimate.setVisibility(View.VISIBLE);
        }
        setAdapter();
    }

    private void sendAproval() {
        UpdateStatus status = new UpdateStatus();
        status.setInvoiceId(receivedData.getId());
        if (receivedData.getStatus() == 0) {
            status.setStatus(100);
        } else if (receivedData.getStatus() == 100) {
            status.setStatus(0);
        }
        RestClient.getInstance(mContext).updateEstimateStatus(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "invoice", status).enqueue(new CustomCallBack<UpdateStatusResponse>(mContext, null) {
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                super.onResponse(call, response);
                if (response != null) {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        binding.approveTv.setVisibility(View.GONE);
                        binding.editDraft.setVisibility(View.GONE);
                        binding.cacncelInvoice.setVisibility(View.VISIBLE);
                        binding.convertToInvoice.setVisibility(View.GONE);
                        binding.sendEstimate.setVisibility(View.VISIBLE);
                        Bundle b = new Bundle();
                        b.putString(Constant.CATEGORY, "invoicing");
                        b.putString(Constant.ACTION, "change_status");
                        SplashScreenActivity.sendEvent("invoice_change_status", b);
                    } else {
                        UiUtil.showToast(ViewInvoice.this, ((response.body().getTransactionStatus().getError())).getDescription());
                    }
                } else {
                    UiUtil.showToast(ViewInvoice.this, "Invoice update fail");
                }
            }

            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    public static String getFormattedDate(String invoiceDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.US);
        try {
            Date date = simpleDateFormat.parse(invoiceDate);
            if (date != null) {
                return newDateFormat.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAdapter() {
        if (items != null) {
            displayPrice();
            rc.removeAllViews();
            rc.setAdapter(new ItemAdapter(items, selectedCurrencyId, new ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product item, int p) {

                }
            }));
            rc.invalidate();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    private void displayPrice() {
        try {
            priceCal = new PriceCal(items);
            binding.subtotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getSub_totel()));
            binding.taxTotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            binding.grantTotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            display_taxs();
        } catch (Exception e) {
            Log.e("Exc :: ", e.toString());
        }
    }

    private void display_taxs() {
        try {
            binding.taxsNameList.removeAllViews();
            binding.taxsAmountList.removeAllViews();
            TextView lable = new TextView(mContext);
            TextView discount = new TextView(mContext);
            lable.setText("Total discount:");
            discount.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getDiscount()));
            discount.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            discount.setGravity(Gravity.RIGHT);
            if (priceCal.getDiscount() != 0) {
                binding.taxsNameList.addView(lable);
                binding.taxsAmountList.addView(discount);
            }
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

    public void deleteCard() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.delete_invoice);
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
                ViewInvoice.this.cancelInvoice();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void fetchInvoiceDetails(String guid, final int type) {
        RestClient.getInstance(mContext).getInvoiceById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), "invoice", guid).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(mContext, null) {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                ViewInvoice.guid = "";
                try {
                    if (response.body().getTransactionStatus().isIsSuccess() && response.body() != null) {
                        receivedData = response.body().getInvoiceDetails();
                        setUI();
                    } else {
                        Toast.makeText(mContext, "Unable to fetch", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<GetInvoiceByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void cancelInvoice() {
        ApproveRecurringInvoice req = new ApproveRecurringInvoice();
        req.setInvoiceId(this.receivedData.getId());
        req.setStatus(400);
        RestClient.getInstance(this).invoiceCacel(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), req).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(this, null) {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    UiUtil.showToast(ViewInvoice.this, "cancelled");
                    binding.approveTv.setVisibility(View.GONE);
                    binding.editDraft.setVisibility(View.GONE);
                    binding.cacncelInvoice.setVisibility(View.GONE);
                    binding.convertToInvoice.setVisibility(View.GONE);
                    binding.sendEstimate.setVisibility(View.GONE);
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "invoicing");
                    b.putString(Constant.ACTION, "cancel_invoice");
                    SplashScreenActivity.sendEvent("invoice_cancel_invoice", b);
                    return;
                }
                UiUtil.showToast(ViewInvoice.this, "Update scheduler, before approving");
            }

            public void onFailure(Call<GetInvoiceByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    private void convertToInvoice(ConvertToInvoice convertToInvoice2) {
        RestClient.getInstance(this).estimateToInvoice(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), convertToInvoice2).enqueue(new CustomCallBack<UpdateStatusResponse>(this, "Adding Journal Transaction...") {
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(ViewInvoice.this, "Succefully Converted");
                    ViewInvoice.this.finish();
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "invoicing");
                    b.putString(Constant.ACTION, "cancel_invoice");
                    SplashScreenActivity.sendEvent("invoice_cancel_invoice", b);
                    return;
                }
                UiUtil.showToast(ViewInvoice.this, "Error while adding");
            }

            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}
