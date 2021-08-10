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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.BillItem;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddBill;
import com.akounto.accountingsoftware.request.AddBillTax;
import com.akounto.accountingsoftware.request.AddBillTransaction;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.response.viewbill.ViewBillByid;
import com.akounto.accountingsoftware.util.BillPriceCalculator;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class CreateBill extends AppCompatActivity {

    private ViewBillByid ceratedBill;
    private String isoDatePattern = "yyyy-MM-dd";
    private TextView in_date, tv_name_char, comp_name, tv_name;
    private SimpleDateFormat simpleDateFormat;
    private ImageView iv_due_date, iv_invoice_date;
    private TextView due_date, subtotal, tax_total, grant_total, tv_note,title;
    private EditText billNo, po_soET;
    private LinearLayout add_vendor, btn_vendor, id_vendor, add_items;
    private int mDay;
    private int mMonth;
    private int mYear;
    private int customerHeadTransactionId = 0;
    private int paymentDueDays = 0;
    private String dueDateFormatted;
    private String estimateName = null;
    private com.akounto.accountingsoftware.response.currency.Currency selectedExchangeCurrency;
    private com.akounto.accountingsoftware.response.currency.Currency selectedBussinessCurrency;
    private String selectdCurrencyCode;
    String poNumber = null;
    Type selectedActivityType;
    private Context mContext;
    private List<PurchaseItem> items = null;
    private RecyclerView rc;
    int position;
    public static PurchaseItem edit_item = null;
    private List<Currency> currencyList = new ArrayList();
    private List<String> currencyListForSpinner = new ArrayList();
    private List<String> currencyListForDisplay = new ArrayList();
    private String selectedCurrencyId = "$";
    private LinearLayout taxs_name_list, taxs_amount_list;
    private BillPriceCalculator priceCal;
    Spinner currencySpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_bill);
        mContext = this;
        items = new ArrayList<>();
        billNo = findViewById(R.id.bill_no);
        po_soET = findViewById(R.id.po_soET);
        add_vendor = findViewById(R.id.add_vendor);
        btn_vendor = findViewById(R.id.btn_vendor);
        id_vendor = findViewById(R.id.id_vendor);
        title = findViewById(R.id.title);
        title.setText("New Bill");
        currencySpinner = findViewById(R.id.currencySpinner);
        taxs_name_list = findViewById(R.id.taxs_name_list);
        taxs_amount_list = findViewById(R.id.taxs_amount_list);
        in_date = findViewById(R.id.invoice_date);
        tv_note = findViewById(R.id.tv_note);
        tv_name_char = findViewById(R.id.tv_name_char);
        comp_name = findViewById(R.id.comp_name);
        tv_name = findViewById(R.id.tv_name);
        add_items = findViewById(R.id.add_items);
        due_date = findViewById(R.id.tv_due_date);
        subtotal = findViewById(R.id.subtotal);
        tax_total = findViewById(R.id.tax_total);
        grant_total = findViewById(R.id.grant_total);
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        iv_due_date = findViewById(R.id.due_date);
        iv_invoice_date = findViewById(R.id.iv_invoice_date);

        rc = findViewById(R.id.rc_customer);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBill();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        iv_invoice_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDateBill(v);
            }
        });

        iv_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDate(v);
            }
        });

        add_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateBill.this, ListVendor.class), 11);
            }
        });
        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateBill.this, ListPurchaseItem.class), 13);
            }
        });

    }

    private void addBill() {
        List<String> invalidList = isValidData();
        if (invalidList.size() > 0) {
            showErrorDialogue(invalidList);
            return;
        }
        AddBill billsReq = new AddBill();
        billsReq.setBillAt(this.in_date.getText().toString());
        List<AddBillTransaction> abbBillList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            PurchaseItem purchaseItem = items.get(i);
            AddBillTransaction addBillTransaction = new AddBillTransaction();
            addBillTransaction.setDescription(purchaseItem.getDescription());
            addBillTransaction.setPrice(Double.valueOf(purchaseItem.getPrice()));
            addBillTransaction.setProductId(purchaseItem.getId());
            addBillTransaction.setQuantity(purchaseItem.getQuantity());
            addBillTransaction.setPrice(Double.valueOf(purchaseItem.getPrice()));
            addBillTransaction.setProductTransactionHeadId(purchaseItem.getExpenseAccountId());
            addBillTransaction.setProductName(purchaseItem.getName());
            List<ProductServiceTaxesItem> taxAddedList2 = items.get(i).getProductServiceTaxes();
            List<AddBillTax> taxes = new ArrayList<>();
            if (taxAddedList2 != null) {
                for (ProductServiceTaxesItem res : taxAddedList2) {
                    AddBillTax addBillTax = new AddBillTax();
                    addBillTax.setName(res.getTaxName());
                    addBillTax.setRate((int) res.getRate());
                    addBillTax.setHeadTransactionTexId(res.getTaxId());
                    taxes.add(addBillTax);
                }
                addBillTransaction.setTaxes(taxes);
            }
            abbBillList.add(addBillTransaction);
        }
        billsReq.setBillTransaction(abbBillList);
        com.akounto.accountingsoftware.response.currency.Currency currency = this.selectedExchangeCurrency;
        if (currency != null) {
            billsReq.setCurrency(currency.getCode());
        } else {
            billsReq.setCurrency(selectdCurrencyCode);
        }
        billsReq.setDueAt(this.due_date.getText().toString() + "T00:00:00.000Z");
        com.akounto.accountingsoftware.response.currency.Currency currency2 = this.selectedExchangeCurrency;
        if (currency2 != null) {
            billsReq.setExchangeRate(Double.valueOf(1.0d / currency2.getExchangeRate().doubleValue()));
        } else {
            billsReq.setExchangeRate(Double.valueOf(1.0d));
        }
        billsReq.setHeadTransactionVendorId(ListVendor.result.getHeadTransactionId());
        billsReq.setInvoiceNo(this.billNo.getText().toString());
        billsReq.setPoNumber(this.po_soET.getText().toString());
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(this).createBill(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), billsReq).enqueue(new CustomCallBack<CustomeResponse>(this, null) {
            public void onResponse(Call<CustomeResponse> call, Response<CustomeResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"billing");
                        b.putString(Constant.ACTION,"adding_success");
                        SplashScreenActivity.sendEvent("bill_create",b);
                        finish();
                    } else {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"billing");
                        b.putString(Constant.ACTION,"adding_fail");
                        SplashScreenActivity.sendEvent("bill_create",b);
                        UiUtil.showToast(getApplicationContext(), response.body().getTransactionStatus().getError().getDescription());
                    }
                } catch (Exception e) {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"billing");
                    b.putString(Constant.ACTION,"adding_fail");
                    b.putString(Constant.CAUSES,e.getMessage());
                    SplashScreenActivity.sendEvent("bill_create",b);
                    UiUtil.showToast(getApplicationContext(), "Failed");
                }
            }

            public void onFailure(Call<CustomeResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"billing");
                b.putString(Constant.ACTION,"adding_fail");
                b.putString(Constant.CAUSES,t.toString());
                SplashScreenActivity.sendEvent("bill_create",b);
                UiUtil.showToast(getApplicationContext(), "Something went wrong");
            }
        });
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
        if (this.billNo.getText().toString().length() == 0) {
            dataList.add("Enter valid bill no");
        }
        if (this.in_date.getText().toString().equalsIgnoreCase("Bill date")) {
            dataList.add("Enter valid Date");
        }
        if (this.due_date.getText().toString().equalsIgnoreCase("Payment due")) {
            dataList.add("Enter valid Due Date");
        }
        if (ListVendor.result == null) {
            dataList.add("Select vendor");
        }
        if (items.size() == 0) {
            dataList.add("Select Items");
        }
        return dataList;
    }

    public void initUiDateBill(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBill.this.DatePopupBill(datePicker, i, i2, i3);
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
        in_date.setText(this.simpleDateFormat.format(calendar.getTime()));
    }

    public void initUiDate(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBill.this.DatePopup(datePicker, i, i2, i3);
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
        due_date.setText(this.simpleDateFormat.format(calendar.getTime()));
    }

    public void setAdapter() {
        try {
            if (items != null) {
                displayPrice();
                rc.removeAllViews();
                rc.setAdapter(new BillItem(items, selectedCurrencyId, new BillItem.OnItemClickListener() {
                    @Override
                    public void onItemClick(PurchaseItem item, int p) {
                        position = p;
                        edit_item = item;
                        startActivityForResult(new Intent(CreateBill.this, EditBillItem.class), 15);
                    }
                }));
                rc.invalidate();
            }
        } catch (Exception e) {
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    private void displayPrice() {
        try {
            priceCal = new BillPriceCalculator(items);
            subtotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getSub_totel()));
            tax_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            grant_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            display_taxs();
        } catch (Exception e) {
        }
    }

    private void display_taxs() {
        try {
            taxs_name_list.removeAllViews();
            taxs_amount_list.removeAllViews();
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
                taxs_name_list.addView(name);
                taxs_amount_list.addView(amount);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateBill.this.selectedExchangeCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        CreateBill.this.selectedBussinessCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        CreateBill.this.selectdCurrencyCode = UiUtil.getBussinessCurren(getApplicationContext());
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
//        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            currencyListForDisplay.add(  jsonObject.getString("Id") + "-"+jsonObject.getString("Name"));
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(currencyListForSpinner, this.currencyListForDisplay);
    }

    private void getExchangedCurrency(String id) {
        RestClient.getInstance((Activity) this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, (String) null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson((Object) response.body().getData()));
                        CreateBill.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        CreateBill commonInvoiceActivity = CreateBill.this;
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

    private void setCurrencySpinner(List<String> currencies, List<String> display) {
        Spinner currencySpinner = findViewById(R.id.currencySpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, display);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(dataAdapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 11) {
                try {
                    if (ListVendor.result != null) {
                        id_vendor.setVisibility(View.VISIBLE);
                        btn_vendor.setVisibility(View.GONE);
                        tv_name_char.setText("" + ListVendor.result.getVendorName().charAt(0));
                        comp_name.setText(ListVendor.result.getVendorName());
                        tv_name.setText(ListVendor.result.getEmail());
                    } else {
                        id_vendor.setVisibility(View.GONE);
                        btn_vendor.setVisibility(View.VISIBLE);
                    }
                    this.customerHeadTransactionId = ListVendor.result.getHeadTransactionId();
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
}
