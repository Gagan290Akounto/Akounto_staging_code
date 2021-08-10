package com.akounto.accountingsoftware.Activity.Bill;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.BillItem;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.databinding.LayoutCreateBillBinding;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddBill;
import com.akounto.accountingsoftware.request.BillUpdate.BillTaxs;
import com.akounto.accountingsoftware.request.BillUpdate.BillWithId;
import com.akounto.accountingsoftware.request.BillUpdate.BillWithoutId;
import com.akounto.accountingsoftware.request.BillUpdate.RequestBill;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.response.viewbill.ViewBillByid;
import com.akounto.accountingsoftware.response.viewbill.ViewBillResponse;
import com.akounto.accountingsoftware.util.BillPriceCalculator;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class EditBill extends AppCompatActivity {

    LayoutCreateBillBinding binding;
    private Vendor vend;
    private List<PurchaseItem> items = new ArrayList<>();
    String guid;
    private int mDay;
    private int mMonth;
    private int mYear;
    private SimpleDateFormat simpleDateFormat;
    private BillPriceCalculator priceCal;
    int position;
    ViewBillResponse receivedData;
    public static PurchaseItem edit_item = null;
    private String selectedCurrencyId = "$";
    Context mContext;
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
        binding = DataBindingUtil.setContentView(this, R.layout.layout_create_bill);
        mContext = this;
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        binding.rcCustomer.setHasFixedSize(true);
        binding.rcCustomer.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        this.guid = getIntent().getStringExtra("GUID");
        if (this.guid != null) {
            getBillsDetailsById(this.guid);
        }
        binding.ivInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDateBill(v);
            }
        });

        binding.dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDate(v);
            }
        });
        binding.addVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditBill.this, ListVendor.class), 11);
            }
        });
        binding.addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditBill.this, ListPurchaseItem.class), 13);
            }
        });
        binding.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBill();
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setVendor() {
        if (vend != null) {
            binding.idVendor.setVisibility(View.VISIBLE);
            binding.btnVendor.setVisibility(View.GONE);
            binding.tvNameChar.setText("" + vend.getVendorName().charAt(0));
            binding.compName.setText(vend.getVendorName());
            binding.tvName.setText(vend.getEmail());
            try {
                selectedCurrencyId = UiUtil.getcurancy(vend.getCurrency(), currencyList).getSymbol();
            } catch (Exception e) {
            }
            binding.currencySpinner.setSelection(UiUtil.getcurancyindex(vend.getCurrency(), currencyList));
        } else {
            binding.idVendor.setVisibility(View.GONE);
            binding.btnVendor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditBill.this.selectedExchangeCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        EditBill.this.selectedBussinessCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        EditBill.this.selectdCurrencyCode = UiUtil.getBussinessCurren(getApplicationContext());

    }

    private void addBill() {
        List<String> invalidList = isValidData();
        if (invalidList.size() > 0) {
            showErrorDialogue(invalidList);
            return;
        }
        RequestBill billsReq = null;
        List<Object> addBillList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            List<ProductServiceTaxesItem> taxAddedList2 = items.get(i).getProductServiceTaxes();
            List<Object> taxes = new ArrayList<>();
            if (taxAddedList2 != null) {
                BillTaxs addBillTax = null;
                for (ProductServiceTaxesItem res : taxAddedList2) {
                    addBillTax = new BillTaxs();
                    addBillTax.setName(res.getTaxName());
                    addBillTax.setRate((int) res.getRate());
                    addBillTax.setTransactionHeadTaxId(res.getTaxId());
                    taxes.add(addBillTax);
                }
            }
            if (items.get(i).getId() == 0 || items.get(i).getId() == items.get(i).getProductId()) {
                addBillList.add(new BillWithoutId(items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), items.get(i).getQuantity(), items.get(i).getPrice(), items.get(i).getExpenseAccountId(), taxes));
            } else {
                addBillList.add(new BillWithId(items.get(i).getId(), items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), items.get(i).getQuantity(), items.get(i).getPrice(), Integer.parseInt(items.get(i).getProductTransactionHeadId()), taxes));
            }
        }
        com.akounto.accountingsoftware.response.currency.Currency currency = this.selectedExchangeCurrency;
        if (currency != null) {
            //billsReq.setCurrency(currency.getCode());
        } else {
            //billsReq.setCurrency(selectdCurrencyCode);
        }
        com.akounto.accountingsoftware.response.currency.Currency currency2 = this.selectedExchangeCurrency;
        double ext = 0.0;
        if (currency2 != null) {
            ext = Double.valueOf(1.0d / currency2.getExchangeRate().doubleValue());
        } else {
            ext = Double.valueOf(1.0d);
        }
        //billsReq.setBillAt(binding.invoiceDate.getText().toString());
        billsReq = new RequestBill(receivedData.getData().getId(), receivedData.getData().getVendor().getHeadTransactionId(), binding.billNo.getText().toString(), binding.invoiceDate.getText().toString(), binding.tvDueDate.getText().toString(), selectdCurrencyCode, ext, binding.tvNote.getText().toString(), binding.poSoET.getText().toString(), addBillList);
        //billsReq.setId(this.receivedData.getData().getId());
        RestClient.getInstance(this).updateBill(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), billsReq).enqueue(new CustomCallBack<AddBill>(this, null) {
            public void onResponse(Call<AddBill> call, Response<AddBill> response) {
                super.onResponse(call, response);
                EditBill.this.finish();
                UiUtil.showToast(mContext, "Updated");
            }

            public void onFailure(Call<AddBill> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 11) {
                try {
                    if (ListVendor.result != null) {
                        vend = ListVendor.result;
                        binding.idVendor.setVisibility(View.VISIBLE);
                        binding.btnVendor.setVisibility(View.GONE);
                        binding.tvNameChar.setText("" + ListVendor.result.getVendorName().charAt(0));
                        binding.compName.setText(ListVendor.result.getVendorName());
                        binding.tvName.setText(ListVendor.result.getEmail());
                    } else {
                        binding.idVendor.setVisibility(View.GONE);
                        binding.btnVendor.setVisibility(View.VISIBLE);
                    }
                    //this.customerHeadTransactionId = ListVendor.result.getHeadTransactionId();
                } catch (Exception e) {
                }
            } else if (requestCode == 13) {
                try {
                    items.add(ListPurchaseItem.result);
                    setAdapter();
                } catch (Exception e) {
                }
            } else if (requestCode == 14) {
                try {
                    items.remove(position);
                    items.add(ListPurchaseItem.adds_item);
                    setAdapter();
                } catch (Exception e) {
                }
            } else if (requestCode == 15) {
                if (resultCode == 16) {
                    try {
                        items.remove(position);
                        setAdapter();
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        items.remove(position);
                        items.add(CreateBill.edit_item);
                        setAdapter();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void updateBillData(ViewBillByid receivedData) {
        //..selectedCurrencyId=data.getCustCurrencySymbol();
        Gson gson = new Gson();
        binding.billNo.setText(receivedData.getInvoiceNo());
        binding.poSoET.setText(receivedData.getPoNumber());
        binding.invoiceDate.setText(getFormattedDate(receivedData.getBillAt()));
        binding.tvDueDate.setText(getFormattedDate(receivedData.getDueAt()));
        vend = receivedData.getVendor();
        items = new ArrayList<>();
        for (int k = 0; k < receivedData.getBillTransaction().size(); k++) {
            try{
            PurchaseItem temp = gson.fromJson(gson.toJson(receivedData.getBillTransaction().get(k)), PurchaseItem.class);
            temp.setProductServiceTaxes(UiUtil.trasformeBill(receivedData.getBillTransaction().get(k).getTaxes()));
            items.add(temp);} catch (Exception e) {
            }
        }
        items.get(0).getCompanyId();
        selectedCurrencyId = receivedData.getCustCurrencySymbol();
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVendor();
        setAdapter();
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
                    EditBill.this.updateBillData(response.body().getData());
                }
            }

            public void onFailure(Call<ViewBillResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                UiUtil.showToast(EditBill.this, t.toString());
            }
        });
    }

    public void setAdapter() {
        try {
            if (items != null) {
                binding.rcCustomer.removeAllViews();
                binding.rcCustomer.setAdapter(new BillItem(items, selectedCurrencyId, new BillItem.OnItemClickListener() {
                    @Override
                    public void onItemClick(PurchaseItem item, int p) {
                        position = p;
                        edit_item = item;
                        CreateBill.edit_item = item;
                        startActivityForResult(new Intent(EditBill.this, EditBillItem.class), 15);
                    }
                }));
                displayPrice();
                binding.rcCustomer.invalidate();
            }
        } catch (Exception e) {
        }

    }

    public void initUiDateBill(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditBill.this.DatePopupBill(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void DatePopupBill(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        binding.invoiceDate.setText(getFormattedDate(this.simpleDateFormat.format(calendar.getTime())));
    }

    public void initUiDate(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditBill.this.DatePopup(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void DatePopup(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        binding.tvDueDate.setText(getFormattedDate(this.simpleDateFormat.format(calendar.getTime())));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
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

    /*private void display_taxs() {
        try {
            binding.taxsNameList.removeAllViews();
            binding.taxsAmountList.removeAllViews();
            for (int i = 0; i < priceCal.getTaxes().size(); i++) {
                TextView name = new TextView(mContext);
                TextView amount = new TextView(mContext);
                if (priceCal.getTaxes().get(i).getName() != null) {
                    name.setText(priceCal.getTaxes().get(i).getName() + " " + priceCal.getTaxes().get(i).getRate());
                } else {
                    name.setText(priceCal.getTaxes().get(i).getTaxName() + " " + priceCal.getTaxes().get(i).getRate());
                }
                amount.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTaxes().get(i).getAmount()));
                amount.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                amount.setGravity(Gravity.RIGHT);
                binding.taxsNameList.addView(name);
                binding.taxsAmountList.addView(amount);
            }
        } catch (Exception e) {
        }
    }*/
    private void display_taxs() {
        try {
            binding.taxsNameList.removeAllViews();
            binding.taxsAmountList.removeAllViews();
            for (int i = 0; i < priceCal.getTaxes().size(); i++) {
                TextView name = new TextView(mContext);
                TextView amount = new TextView(mContext);
                if (priceCal.getTaxes().get(i).getName() != null) {
                    name.setText(priceCal.getTaxes().get(i).getName() + " " + priceCal.getTaxes().get(i).getRate());
                } else {
                    name.setText(priceCal.getTaxes().get(i).getTaxName() + " " + priceCal.getTaxes().get(i).getRate());
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

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            currencyListForDisplay.add(  jsonObject.getString("Id") + "-"+jsonObject.getString("Name"));
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void getExchangedCurrency(String id) {
        RestClient.getInstance((Activity) this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, (String) null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson((Object) response.body().getData()));
                        EditBill.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        EditBill commonInvoiceActivity = EditBill.this;
                        commonInvoiceActivity.selectedCurrencyId = commonInvoiceActivity.selectedExchangeCurrency.getSymbol();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void setCurrencySpinner(List<String> currencies) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, currencyListForDisplay);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currencySpinner.setAdapter(dataAdapter);
        binding.currencySpinner.setSelection(UiUtil.getcurancyindex(receivedData.getData().getCurrency(), currencyList));
        binding.currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrencyId = getCurrencyName(currencies.get(position));
                getExchangedCurrency(getCurrencyId(currencies.get(position)));
                setAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getCurrencyName(String selectedItem) {
        for (Currency currency : this.currencyList) {
            if (currency.getName().equals(selectedItem)) {
                return currency.getSymbol();
            }
        }
        return "ALL";
    }

    private String getCurrencyId(String selectedItem) {
        for (Currency currency : this.currencyList) {
            if (currency.getName().equals(selectedItem)) {
                return currency.getId();
            }
        }
        return "ALL";
    }

    private void showErrorDialogue(List<String> invalidList) {
        Dialog dialoginvalid = new Dialog(this);
        dialoginvalid.requestWindowFeature(1);
        dialoginvalid.setContentView(R.layout.dialogue_validerror);
        dialoginvalid.setCancelable(true);
        dialoginvalid.setCanceledOnTouchOutside(true);
        TextView textView = dialoginvalid.findViewById(R.id.addNewItem);
        RecyclerView listItemRecycler = dialoginvalid.findViewById(R.id.listItemRecycler);
        InvalidAdapter adapter = new InvalidAdapter(this, invalidList);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        listItemRecycler.setAdapter(adapter);
        dialoginvalid.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                dialoginvalid.dismiss();
            }
        });
        dialoginvalid.show();
    }

    private List<String> isValidData() {
        List<String> dataList = new ArrayList<>();
        if (binding.billNo.getText().toString().length() == 0) {
            dataList.add("Enter valid bill no");
        }
        if (binding.invoiceDate.getText().toString().equalsIgnoreCase("Bill date")) {
            dataList.add("Enter valid Date");
        }
        if (binding.tvDueDate.getText().toString().equalsIgnoreCase("Payment due")) {
            dataList.add("Enter valid Due Date");
        }
        if (vend == null) {
            dataList.add("Select vendor");
        }
        if (items.size() == 0) {
            dataList.add("Select Items");
        }
        return dataList;
    }
}
