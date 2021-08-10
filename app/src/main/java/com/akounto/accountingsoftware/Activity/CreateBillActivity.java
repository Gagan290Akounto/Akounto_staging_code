package com.akounto.accountingsoftware.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.AddBIllItemAdapter;
import com.akounto.accountingsoftware.adapter.AddBillWithTaxExpandable;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.adapter.ListItemAdapter;
import com.akounto.accountingsoftware.adapter.RITaxSummaryItemsAdapter;
import com.akounto.accountingsoftware.adapter.TaxListAdapter;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.model.TaxSummaryList;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddBill;
import com.akounto.accountingsoftware.request.AddBillTax;
import com.akounto.accountingsoftware.request.AddBillTransaction;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.akounto.accountingsoftware.response.EffectiveTax;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.PurchaseItemResponse;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.VendorDetailsResponse;
import com.akounto.accountingsoftware.response.VendorResponse;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.response.viewbill.ViewBillByid;
import com.akounto.accountingsoftware.response.viewbill.ViewBillResponse;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

public class CreateBillActivity extends AppCompatActivity {
    AddBillWithTaxExpandable addBillWithTaxExpandable;
    TextView addItem;
    ViewBillByid ceratedBill;
    EditText content;
    List<Currency> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    PowerSpinnerView currencySpinner;
    TextView customerEmailTv;
    /* access modifiers changed from: private */
    public List<Vendor> customerList = new ArrayList();
    TextView customerNameTv;
    TextView customerPhoneTv;
    AddBIllItemAdapter customersAdapter;
    TextView dateEV;
    Dialog dialog;
    Dialog dialogTax;
    Dialog dialogueEdit;
    TextView duedateET;
    PurchaseItem editedpurchaseItem;
    TextView et_estimateNo;
    int groupPosition;
    String guid;
    TextView invoiceAmount;
    TextView invoiceDateTv;
    TextView invoiceDueTv;
    TextView invoiceNoTv;
    Boolean isEdit = false;
    int mDay;
    int mDay1;
    int mMonth;
    int mMonth1;
    int mYear;
    int mYear1;
    EditText po_soET;
    TextView priceTotal;
    ExpandableListView productExpandable;
    RecyclerView productRecycler;
    List<PurchaseItem> purchaseItemList = new ArrayList();
    List<PurchaseItem> purchaseItemListSelected = new ArrayList();
    Map<Integer, Integer> quantityMap = new HashMap();
    RITaxSummaryItemsAdapter rITaxSummaryItemsAdapter;
    String selectdCurrency = "$";
    com.akounto.accountingsoftware.response.currency.Currency selectedExchangeCurrency;
    PurchaseItem selectedPurchaseItem;
    AddVendorRequest selectedVendor;
    SimpleDateFormat simpleDateFormat;
    RelativeLayout subTotalInDollar;
    TextView subTotalPaidInDolar;
    TextView subtotalAmountInDolar;
    TextView subtotalText;
    List<TaxResponse> taxAddedList = new ArrayList();
    Map<Integer, List<TaxResponse>> taxList = new HashMap();
    List<TaxResponse> taxReceivedList = new ArrayList();
    List<TaxSummaryList> taxSummaryList = new ArrayList();
    RecyclerView taxSummaryRecyclerView;
    TextView taxTotal;
    TextView tv_customeraddres;
    TextView tv_customeraddres2;
    TextView tv_invoiceAmount;
    TextView tv_invoiceDate;
    List<String> vendorList = new ArrayList();
    PowerSpinnerView vendorSpinner;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbill);
        ((TextView) findViewById(R.id.pageTitle)).setText("Create Bill");
        inItUi();
        this.isEdit = Boolean.valueOf(getIntent().getBooleanExtra("IS_EDIT", false));
        Log.d("isEditisEdit", this.isEdit + "");
        this.guid = getIntent().getStringExtra("GUID");
        if (this.isEdit.booleanValue()) {
            getBillsDetailsById(this.guid);
        }
    }

    private void inItUi() {
        this.subTotalInDollar = findViewById(R.id.subTotalInDollar);
        this.subtotalText = findViewById(R.id.subtotalText);
        this.priceTotal = findViewById(R.id.priceTotal);
        this.tv_invoiceAmount = findViewById(R.id.tv_invoiceAmount);
        this.subTotalPaidInDolar = findViewById(R.id.subTotalPaidInDolar);
        this.subtotalAmountInDolar = findViewById(R.id.subtotalAmountInDolar);
        this.productRecycler = findViewById(R.id.productRecycler);
        this.customersAdapter = new AddBIllItemAdapter(this, this.purchaseItemList);
        this.productRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.productRecycler.setAdapter(this.customersAdapter);
        TextView textView = findViewById(R.id.addItem);
        this.addItem = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$0$CreateBillActivity(view);
            }
        });
        getSalesTaxList();
        this.customerNameTv = findViewById(R.id.tv_customerName);
        this.customerEmailTv = findViewById(R.id.tv_customerEmail);
        this.tv_customeraddres = findViewById(R.id.tv_customeraddres);
        this.tv_customeraddres2 = findViewById(R.id.tv_customeraddres2);
        this.dateEV = findViewById(R.id.dateEV);
        this.duedateET = findViewById(R.id.duedateET);
        this.po_soET = findViewById(R.id.po_soET);
        this.content = findViewById(R.id.content);
        this.taxTotal = findViewById(R.id.taxTotal);
        this.dateEV.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$2$CreateBillActivity(view);
            }
        });
        this.duedateET.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$4$CreateBillActivity(view);
            }
        });
        this.productExpandable = findViewById(R.id.productExpandable);
        AddBillWithTaxExpandable addBillWithTaxExpandable2 = new AddBillWithTaxExpandable(this, this.purchaseItemListSelected, this.taxList, this.quantityMap);
        this.addBillWithTaxExpandable = addBillWithTaxExpandable2;
        this.productExpandable.setAdapter(addBillWithTaxExpandable2);
        this.productExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition1, long id) {
                CreateBillActivity.this.groupPosition = groupPosition1;
                Log.d("11111111", parent.isGroupExpanded(CreateBillActivity.this.groupPosition) + "---" + groupPosition1);
                if (parent.isGroupExpanded(CreateBillActivity.this.groupPosition)) {
                    CreateBillActivity createBillActivity = CreateBillActivity.this;
                    createBillActivity.setListViewHeight(parent, createBillActivity.groupPosition, false);
                } else {
                    CreateBillActivity createBillActivity2 = CreateBillActivity.this;
                    createBillActivity2.setListViewHeight(parent, createBillActivity2.groupPosition, true);
                }
                return false;
            }
        });
        RecyclerView recyclerView = findViewById(R.id.tax_list_rv);
        this.taxSummaryRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RITaxSummaryItemsAdapter rITaxSummaryItemsAdapter2 = new RITaxSummaryItemsAdapter(this, this.taxSummaryList);
        this.rITaxSummaryItemsAdapter = rITaxSummaryItemsAdapter2;
        this.taxSummaryRecyclerView.setAdapter(rITaxSummaryItemsAdapter2);
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$5$CreateBillActivity(view);
            }
        });
        findViewById(R.id.addVendorButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$6$CreateBillActivity(view);
            }
        });
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$7$CreateBillActivity(view);
            }
        });
        findViewById(R.id.addProductButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$8$CreateBillActivity(view);
            }
        });
        this.currencySpinner = findViewById(R.id.currencySpinner);
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateBillActivity.this.lambda$inItUi$9$CreateBillActivity(view);
            }
        });
    }

    public void lambda$inItUi$0$CreateBillActivity(View v) {
        openDialogue();
    }

    public void lambda$inItUi$2$CreateBillActivity(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBillActivity.this.lambda$null$1$CreateBillActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void lambda$null$1$CreateBillActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView textView = this.dateEV;
        textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$4$CreateBillActivity(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear1 = c.get(1);
        this.mMonth1 = c.get(2);
        this.mDay1 = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBillActivity.this.lambda$null$3$CreateBillActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$3$CreateBillActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView textView = this.duedateET;
        textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$5$CreateBillActivity(View v) {
        addBill();
    }

    public void lambda$inItUi$6$CreateBillActivity(View v) {
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    public void lambda$inItUi$7$CreateBillActivity(View v) {
        finish();
    }

    public void lambda$inItUi$8$CreateBillActivity(View v) {
        openAddNewItemDialogue();
    }

    public void lambda$inItUi$9$CreateBillActivity(View v) {
        finish();
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json",this);
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            List<String> list = this.currencyListForSpinner;
            list.add(jsonObject.getString("Id") + " - " + jsonObject.getString("Name"));
        }
        setUpCurrencySpinner(this.currencyListForSpinner);
    }

    private void setUpCurrencySpinner(List<String> currencyListForSpinner2) {
        this.currencySpinner.setItems(currencyListForSpinner2);
        this.currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CreateBillActivity.this.lambda$setUpCurrencySpinner$10$CreateBillActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpCurrencySpinner$10$CreateBillActivity(int i, String s, int selectedIndex, String selectedItem) {
        updateCurrency(selectedItem);
    }

    private void updateCurrency(String selectedItem) {
        Log.d("selectedItem", selectedItem);
        getExchangedCurrency(selectedItem.substring(0, Math.min(selectedItem.length(), 3)));
    }

    private void getExchangedCurrency(String id) {
        RestClient.getInstance(this).getCurrencyConverter(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),id).enqueue(new CustomCallBack<CurrencyResponse>(this, null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Log.d("currencyrate", new Gson().toJson(response.body().getData()));
                    CreateBillActivity.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                    CreateBillActivity createBillActivity = CreateBillActivity.this;
                    createBillActivity.selectdCurrency = createBillActivity.selectedExchangeCurrency.getSymbol();
                    if (CreateBillActivity.this.selectedExchangeCurrency.getExchangeRate().doubleValue() == 1.0d) {
                        CreateBillActivity.this.subTotalInDollar.setVisibility(View.GONE);
                    } else {
                        CreateBillActivity.this.subTotalInDollar.setVisibility(View.VISIBLE);
                    }
                    CreateBillActivity.this.updatePrice();
                }
            }

            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void getBillsDetailsById(String id) {
        RestClient.getInstance(this).getBillForEdit(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),id).enqueue(new CustomCallBack<ViewBillResponse>(this, null) {
            public void onResponse(Call<ViewBillResponse> call, Response<ViewBillResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CreateBillActivity.this.updateBillData(response.body().getData());
                }
            }

            public void onFailure(Call<ViewBillResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                UiUtil.showToast(CreateBillActivity.this, t.toString());
            }
        });
    }

    public void updateBillData(ViewBillByid data) {
        this.ceratedBill = data;
        getVendorById(data.getHeadTransactionVendorId());
        Log.d("datadatadata", new Gson().toJson(data));
        this.dateEV.setText(getFormattedDate(data.getBillAt()));
        this.duedateET.setText(getFormattedDate(data.getDueAt()));
        updateVendorForEdit(data.getVendor());
        TextView textView = this.subtotalText;
        textView.setText("$" + data.getPrice());
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + data.getPrice());
        this.content.setText(data.getInvoiceNo());
        this.po_soET.setText(data.getPoNumber());
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        for (AddBillTransaction item : data.getBillTransaction()) {
            List<TaxResponse> taxAddedList2 = new ArrayList<>();
            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setDescription(item.getDescription());
            purchaseItem.setPrice(item.getPrice().doubleValue());
            purchaseItem.setId(item.getProductId());
            purchaseItem.setQuantity(item.getQuantity());
            purchaseItem.setPrice(item.getPrice().doubleValue());
            purchaseItem.setExpenseAccountId(item.getProductTransactionHeadId());
            purchaseItem.setName(item.getProductName());
            this.purchaseItemListSelected.add(purchaseItem);
            if (item.getTaxes() != null) {
                for (AddBillTax tax : item.getTaxes()) {
                    TaxResponse taxResponse = new TaxResponse();
                    taxResponse.setId(tax.getHeadTransactionTexId());
                    taxResponse.setName(tax.getName());
                    List<EffectiveTax> eTaxlist = new ArrayList<>();
                    eTaxlist.add(new EffectiveTax((int)tax.getRate()));
                    taxResponse.setEffectiveTaxes(eTaxlist);
                    taxAddedList2.add(taxResponse);
                }
            }
            this.taxList.put(Integer.valueOf(purchaseItem.getId()), taxAddedList2);
            this.quantityMap.put(Integer.valueOf(item.getProductId()), Integer.valueOf(item.getQuantity()));
        }
        this.currencySpinner.setText(data.getCurrency());
        getExchangedCurrency(data.getCurrency());
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        setListViewHeight(this.productExpandable, this.groupPosition, true);
    }

    private void updateVendorForEdit(Vendor vendor) {
        if (vendor != null) {
            if (vendor.getVendorName() != null) {
                this.customerNameTv.setText(vendor.getVendorName());
            }
            TextView textView = this.tv_customeraddres;
            textView.setText(vendor.getBillAddress1() + "," + vendor.getBillAddress2());
            TextView textView2 = this.tv_customeraddres2;
            textView2.setText(vendor.getBillCity() + "," + vendor.getBillState() + "," + vendor.getBillPostCode());
            if (vendor.getEmail() != null) {
                TextView textView3 = this.customerEmailTv;
                textView3.setText("Email: " + vendor.getEmail());
            }
        }
    }

    public static String getFormattedDate(String invoiceDate) {
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
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

    /* access modifiers changed from: private */
    public void setListViewHeight(ExpandableListView listView, int group, boolean isExpandable) {
        ExpandableListView expandableListView = listView;
        boolean z = isExpandable;
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, expandableListView);
            groupItem.measure(desiredWidth, 0);
            totalHeight += groupItem.getMeasuredHeight();
            Log.d("expandableexpandable", z + "-----" + this.groupPosition);
            if (z && this.groupPosition == i) {
                int totalHeight2 = totalHeight;
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(desiredWidth, 0);
                    totalHeight2 += listItem.getMeasuredHeight();
                }
                totalHeight = totalHeight2;
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1)) + totalHeight;
        if (height < 10) {
            height = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        }
        params.height = height;
        expandableListView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void getDataList() {
        RestClient.getInstance(this).getPurchaseItem(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<PurchaseItemResponse>(this, null) {
            public void onResponse(Call<PurchaseItemResponse> call, Response<PurchaseItemResponse> response) {
                super.onResponse(call, response);
                Log.d("responseresponse", new Gson().toJson(response.body().getData()));
                if (response.isSuccessful()) {
                    CreateBillActivity.this.purchaseItemList = response.body().getData();
                }
            }

            public void onFailure(Call<PurchaseItemResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void getSalesTaxList() {
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                response.isSuccessful();
            }

            public void onFailure(Call<SalesTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void addVendor() {
        new AddVendorRequest();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getDataList();
        getVendorList();
        getTaxList();
    }

    /* access modifiers changed from: private */
    public void setVendorSpinner(List<Vendor> customerList2) {
        if (customerList2 != null) {
            for (Vendor vendor : customerList2) {
                this.vendorList.add(vendor.getVendorName());
            }
        }
        PowerSpinnerView powerSpinnerView = findViewById(R.id.vendorSpinner);
        this.vendorSpinner = powerSpinnerView;
        powerSpinnerView.setItems(this.vendorList);
        this.vendorSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {

            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CreateBillActivity.this.lambda$setVendorSpinner$11$CreateBillActivity(customerList2, i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setVendorSpinner$11$CreateBillActivity(List customerList2, int i, String s, int selectedIndex, String selectedItem) {
        Log.d("selectedItemse--", selectedItem);
        if (selectedItem == "ADD NEW VENDOR") {
            startActivity(new Intent(this, AddVendorActivity.class));
            return;
        }
        int pos = this.vendorList.indexOf(selectedItem);
        Log.d("pospospos", pos + "");
        getVendorById(((Vendor) customerList2.get(pos)).getHeadTransactionId());
    }

    private void getVendorList() {
        this.vendorList.clear();
        RestClient.getInstance(this).getVendorList(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<VendorResponse>(this, null) {
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                super.onResponse(call, response);
                List unused = CreateBillActivity.this.customerList = response.body().getData();
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    CreateBillActivity createBillActivity = CreateBillActivity.this;
                    createBillActivity.setVendorSpinner(createBillActivity.customerList);
                }
            }

            public void onFailure(Call<VendorResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getVendorById(int id) {
        RestClient.getInstance(this).getVendorDetails(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),id).enqueue(new CustomCallBack<VendorDetailsResponse>(this, "Fetching States...") {
            public void onResponse(Call<VendorDetailsResponse> call, Response<VendorDetailsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CreateBillActivity.this.updateVendor(response.body().getData());
                }
            }

            public void onFailure(Call<VendorDetailsResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(CreateBillActivity.this, "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateVendor(AddVendorRequest vendor) {
        this.selectedVendor = vendor;
        this.customerNameTv.setText(vendor.getVendorName());
        if (!TextUtils.isEmpty(vendor.getBillAddress1())) {
            TextView textView = this.tv_customeraddres;
            textView.setText(vendor.getBillAddress1() + "," + vendor.getBillAddress2());
            this.tv_customeraddres.setVisibility(View.VISIBLE);
        } else {
            this.tv_customeraddres.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(vendor.getBillCity())) {
            TextView textView2 = this.tv_customeraddres2;
            textView2.setText(vendor.getBillCity() + "," + vendor.getBillState() + "," + vendor.getBillPostCode());
            this.tv_customeraddres2.setVisibility(View.VISIBLE);
        } else {
            this.tv_customeraddres2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(vendor.getEmail())) {
            TextView textView3 = this.customerEmailTv;
            textView3.setText("Email: " + vendor.getEmail());
            this.customerEmailTv.setVisibility(View.VISIBLE);
            return;
        }
        this.customerEmailTv.setVisibility(View.GONE);
    }

    private void addBill() {
        List<String> invalidList = isValidData();
        if (invalidList.size() > 0) {
            showErrorDialogue(invalidList);
            return;
        }
        AddBill billsReq = new AddBill();
        billsReq.setBillAt(this.dateEV.getText().toString());
        List<AddBillTransaction> abbBillList = new ArrayList<>();
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            AddBillTransaction addBillTransaction = new AddBillTransaction();
            addBillTransaction.setDescription(purchaseItem.getDescription());
            addBillTransaction.setPrice(Double.valueOf(purchaseItem.getPrice()));
            addBillTransaction.setProductId(purchaseItem.getId());
            addBillTransaction.setQuantity(this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue());
            addBillTransaction.setPrice(Double.valueOf(purchaseItem.getPrice()));
            addBillTransaction.setProductTransactionHeadId(purchaseItem.getExpenseAccountId());
            addBillTransaction.setProductName(purchaseItem.getName());
            List<TaxResponse> taxAddedList2 = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
            List<AddBillTax> taxes = new ArrayList<>();
            if (taxAddedList2 != null) {
                for (TaxResponse res : taxAddedList2) {
                    AddBillTax addBillTax = new AddBillTax();
                    addBillTax.setName(res.getName());
                    addBillTax.setRate(res.getEffectiveTaxes().get(0).getRate());
                    addBillTax.setHeadTransactionTexId(res.getId());
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
            billsReq.setCurrency("USD");
        }
        billsReq.setDueAt(this.duedateET.getText().toString() + "T00:00:00.000Z");
        com.akounto.accountingsoftware.response.currency.Currency currency2 = this.selectedExchangeCurrency;
        if (currency2 != null) {
            billsReq.setExchangeRate(Double.valueOf(1.0d / currency2.getExchangeRate().doubleValue()));
        } else {
            billsReq.setExchangeRate(Double.valueOf(1.0d));
        }
        billsReq.setHeadTransactionVendorId(this.selectedVendor.getHeadTransactionId());
        billsReq.setInvoiceNo(this.content.getText().toString());
        billsReq.setPoNumber(this.po_soET.getText().toString());
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        if (this.isEdit.booleanValue()) {
            billsReq.setId(this.ceratedBill.getId());
          /*  RestClient.getInstance(this).updateBill(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),billsReq).enqueue(new CustomCallBack<SendMailResponse>(this, null) {
                public void onResponse(Call<SendMailResponse> call, Response<SendMailResponse> response) {
                    super.onResponse(call, response);
                    CreateBillActivity.this.finish();
                }

                public void onFailure(Call<SendMailResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.d("errorerror-bill", t.toString());
                }
            });*/
            return;
        }
       /* RestClient.getInstance(this).createBill(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),billsReq).enqueue(new CustomCallBack<AddBill>(this, null) {
            public void onResponse(Call<AddBill> call, Response<AddBill> response) {
                super.onResponse(call, response);
                CreateBillActivity.this.finish();
            }

            public void onFailure(Call<AddBill> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });*/
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
        if (this.content.getText().toString().length() == 0) {
            dataList.add(" -- Enter valid bill no");
        }
        if (this.dateEV.getText().toString().length() == 0) {
            dataList.add(" -- Enter valid Date");
        }
        if (this.duedateET.getText().toString().length() == 0) {
            dataList.add(" -- Enter valid Due Date");
        }
        if (this.selectedVendor == null) {
            dataList.add(" -- Select vendor");
        }
        return dataList;
    }

    private void openDialogue() {
        Dialog dialog2=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialogue_add_bill_item);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView title=this.dialog.findViewById(R.id.title);
        title.setText("Add Item");
        RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
        ListItemAdapter adapter = new ListItemAdapter(this, this.purchaseItemList);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        listItemRecycler.setAdapter(adapter);
        this.dialog.findViewById(R.id.addNewItem).setOnClickListener($$Lambda$CreateBillActivity$XphJTW5kWKkKwCGDw2T0veKnRiU.INSTANCE);
        this.dialog.findViewById(R.id.addNewItem).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateBillActivity.this.openAddNewItemDialogue();
                CreateBillActivity.this.dialog.dismiss();
            }
        });
        this.dialog.show();
    }

    static void lambda$openDialogue$13(View v) {
    }

    /* access modifiers changed from: private */
    public void openAddNewItemDialogue() {
        Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(1);
        dialog2.setContentView(R.layout.dialogue_add_new_iem);
        dialog2.setCancelable(true);
        dialog2.setCanceledOnTouchOutside(true);
        startActivity(new Intent(this, CreateProductsAndServicesPurchase.class));
    }

    /* access modifiers changed from: private */
    public void getTaxList() {
        RestClient.getInstance(this).getTaxList(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                Log.d("getTaxList----", new Gson().toJson(response.body().getData()));
                if (response.isSuccessful()) {
                    CreateBillActivity.this.taxReceivedList = response.body().getData();
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void addBill(PurchaseItem purchaseItem) {
        double price;
        Log.d("purchaseItempurc", this.purchaseItemList.size() + "");
        this.dialog.dismiss();
        this.purchaseItemListSelected.add(purchaseItem);
        this.groupPosition = this.purchaseItemListSelected.size() - 1;
        this.quantityMap.put(Integer.valueOf(purchaseItem.getId()), 1);
        this.customersAdapter.notifyData(this.purchaseItemListSelected);
        if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
            List<TaxResponse> taxInProd = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
            taxInProd.addAll(getItemBasedOnTaxresponse(purchaseItem.getProductServiceTaxes()));
            this.taxList.put(Integer.valueOf(purchaseItem.getId()), taxInProd);
        } else {
            this.taxList.put(Integer.valueOf(purchaseItem.getId()), getItemBasedOnTaxresponse(purchaseItem.getProductServiceTaxes()));
        }
        Map<String, Double> taxListMap = new HashMap<>();
        double price2 = Utils.DOUBLE_EPSILON;
        double priceWithoutTax = Utils.DOUBLE_EPSILON;
        for (PurchaseItem item : this.purchaseItemListSelected) {
            if (item.getProductServiceTaxes() == null || this.taxList.get(Integer.valueOf(item.getId())).size() <= 0) {
                int qunntityTmp = 1;
                if (this.quantityMap.get(Integer.valueOf(item.getId())) != null) {
                    qunntityTmp = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                }
                double price3 = item.getPrice();
                double d = qunntityTmp;
                Double.isNaN(d);
                price2 += price3 * d;
            } else {
                for (TaxResponse productServiceTaxesItem : this.taxList.get(Integer.valueOf(item.getId()))) {
                    int qunntityTmp2 = 1;
                    if (this.quantityMap.get(Integer.valueOf(item.getId())) != null) {
                        qunntityTmp2 = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                    }
                    Double taxprice = taxListMap.get(productServiceTaxesItem.getName());
                    if (taxprice != null) {
                        double doubleValue = taxprice.doubleValue();
                        double rate = productServiceTaxesItem.getEffectiveTaxes().get(0).getRate();
                        double price4 = item.getPrice();
                        Double.isNaN(rate);
                        double d2 = rate * price4;
                        price = price2;
                        double d3 = qunntityTmp2;
                        Double.isNaN(d3);
                        taxListMap.put(productServiceTaxesItem.getName(), Double.valueOf(doubleValue + ((d2 * d3) / 100.0d)));
                    } else {
                        price = price2;
                        double rate2 = productServiceTaxesItem.getEffectiveTaxes().get(0).getRate();
                        double price5 = item.getPrice();
                        Double.isNaN(rate2);
                        double d4 = rate2 * price5;
                        double d5 = qunntityTmp2;
                        Double.isNaN(d5);
                        taxListMap.put(productServiceTaxesItem.getName(), Double.valueOf((d4 * d5) / 100.0d));
                    }
                    double rate3 = productServiceTaxesItem.getEffectiveTaxes().get(0).getRate();
                    double price6 = item.getPrice();
                    Double.isNaN(rate3);
                    double d6 = rate3 * price6;
                    double d7 = qunntityTmp2;
                    Double.isNaN(d7);
                    price2 = price + ((d6 * d7) / 100.0d) + item.getPrice();
                }
                double d8 = price2;
            }
            double price7 = item.getPrice();
            double intValue = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
            Double.isNaN(intValue);
            priceWithoutTax += price7 * intValue;
        }
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        setListViewHeight(this.productExpandable, this.groupPosition, true);
        TextView textView = this.subtotalText;
        textView.setText("$" + price2);
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + priceWithoutTax);
        updateTax();
        showAddedTax(taxListMap, Double.valueOf(priceWithoutTax));
    }

    private List<TaxResponse> getItemBasedOnTaxresponse(List<ProductServiceTaxesItem> productServiceTaxes) {
        List<TaxResponse> taxResponseList = new ArrayList<>();
        for (ProductServiceTaxesItem item : productServiceTaxes) {
            TaxResponse taxResponse = new TaxResponse();
            taxResponse.setName(item.getTaxName());
            taxResponse.setId(item.getId());
            List<EffectiveTax> tax = new ArrayList<>();
            tax.add(new EffectiveTax((int) item.getRate()));
            taxResponse.setEffectiveTaxes(tax);
            taxResponseList.add(taxResponse);
        }
        return taxResponseList;
    }

    public void addTax(PurchaseItem taxResponses, int groupPosition2) {
        this.selectedPurchaseItem = taxResponses;
        this.groupPosition = groupPosition2;
        Dialog dialog2=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.dialogTax = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialogTax.setContentView(R.layout.dialogue_add_bill_item);
        this.dialogTax.setCancelable(true);
        this.dialogTax.setCanceledOnTouchOutside(true);
        this.dialogTax.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView addNewItem = this.dialogTax.findViewById(R.id.addNewItem);
        addNewItem.setText("Add new tax(+)");
        RecyclerView listItemRecycler = this.dialogTax.findViewById(R.id.listItemRecycler);
        TaxListAdapter adapter2 = new TaxListAdapter(this, this.taxReceivedList,CommonInvoiceActivity.convert(taxResponses.getProductServiceTaxes()));
        listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        listItemRecycler.setAdapter(adapter2);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateBillActivity.this.openAddTaxItemDialogue();
                CreateBillActivity.this.dialogTax.dismiss();
            }
        });
        this.dialogTax.show();
    }
    public void openAddTaxItemDialogue() {
        View mView = LayoutInflater.from(this).inflate(R.layout.add_salestax_popup_layout, null, false);
        PopupWindow popUp = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAtLocation(getWindow().getDecorView(), 17, 50, 50);
        View container = (View) popUp.getContentView().getParent();
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= 2;
        p.dimAmount = 0.3f;
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(container, p);
        mView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                popUp.dismiss();
            }
        });
        mView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                CreateBillActivity.this.lambda$openAddTaxItemDialogue$15$CreateBillActivity(mView, popUp, view);
            }
        });
    }

    public void lambda$openAddTaxItemDialogue$15$CreateBillActivity(View mView, PopupWindow popUp, View v) {
        boolean isRecoverableTax;
        boolean isCompoundTaxSelected;
        View view = mView;
        EditText etTaxName = view.findViewById(R.id.et_taxName);
        String taxName = etTaxName.getText().toString().trim();
        String taxDesc = ((EditText) view.findViewById(R.id.et_taxDesc)).getText().toString().trim();
        String trim = ((EditText) view.findViewById(R.id.et_taxNumber)).getText().toString().trim();
        EditText etTaxRate = view.findViewById(R.id.et_taxRate);
        String taxRate = etTaxRate.getText().toString().trim();
        if (etTaxName.getText().toString().length() == 0) {
            PopupWindow popupWindow = popUp;
        } else if (taxRate.length() == 0) {
            PopupWindow popupWindow2 = popUp;
        } else {
            RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (((RadioButton) radioGroup.findViewById(selectedId)).getText().equals("Is Recoverable Tax")) {
                isCompoundTaxSelected = false;
                isRecoverableTax = true;
            } else {
                isCompoundTaxSelected = true;
                isRecoverableTax = false;
            }
            ArrayList<EffectiveTaxesItem> effectiveTaxesItems = new ArrayList<>();
            if (!(taxName == null || taxName.length() == 0)) {
                effectiveTaxesItems.add(new EffectiveTaxesItem(Double.parseDouble(taxRate)));
            }
            int i = selectedId;
            final PopupWindow popupWindow3 = popUp;
            RestClient.getInstance(this).addSaleTax(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Sale tax...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(CreateBillActivity.this, "Sale Tax added!");
                        popupWindow3.dismiss();
                        CreateBillActivity.this.getTaxList();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
            return;
        }
        if (etTaxName.getText().toString().length() == 0) {
            etTaxName.setError("Please enter Tax Name");
        }
        if (taxRate.length() == 0) {
            etTaxRate.setError("Please enter Tax Rate");
        }
    }

    public void addTaxTopup(TaxResponse taxResponse) {
        this.dialogTax.dismiss();
        List<TaxResponse> taxListTmp = new ArrayList<>();
        if (this.taxList.get(Integer.valueOf(this.selectedPurchaseItem.getId())) != null) {
            taxListTmp = this.taxList.get(Integer.valueOf(this.selectedPurchaseItem.getId()));
        }
        taxListTmp.add(taxResponse);
        this.taxList.put(Integer.valueOf(this.selectedPurchaseItem.getId()), taxListTmp);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        updateTax();
    }

    public void updateTax() {
        Iterator<TaxResponse> it;
        List<TaxResponse> taxResList;
        double price = Utils.DOUBLE_EPSILON;
        double priceWithoutTax = Utils.DOUBLE_EPSILON;
        Map<String, Double> finalTaxMap = new HashMap<>();
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
                List<TaxResponse> taxResList2 = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
                Iterator<TaxResponse> it2 = taxResList2.iterator();
                while (it2.hasNext()) {
                    TaxResponse taxResp = it2.next();
                    float tax = (float) taxResp.getEffectiveTaxes().get(0).getRate();
                    if (finalTaxMap.get(taxResp.getName()) != null) {
                        double intValue = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                        double doubleValue = finalTaxMap.get(taxResp.getName()).doubleValue();
                        taxResList = taxResList2;
                        it = it2;
                        double d = tax;
                        double price2 = purchaseItem.getPrice();
                        Double.isNaN(d);
                        Double.isNaN(intValue);
                        finalTaxMap.put(taxResp.getName(), Double.valueOf(intValue * (doubleValue + ((d * price2) / 100.0d))));
                        double d2 = tax;
                        double price3 = purchaseItem.getPrice();
                        Double.isNaN(d2);
                        price = (float) (((d2 * price3) / 100.0d) + price);
                    } else {
                        taxResList = taxResList2;
                        it = it2;
                        double intValue2 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                        double d3 = tax;
                        double price4 = purchaseItem.getPrice();
                        Double.isNaN(d3);
                        Double.isNaN(intValue2);
                        finalTaxMap.put(taxResp.getName(), Double.valueOf(intValue2 * ((d3 * price4) / 100.0d)));
                        double d4 = tax;
                        double price5 = purchaseItem.getPrice();
                        Double.isNaN(d4);
                        price = (float) (((d4 * price5) / 100.0d) + price);
                    }
                    taxResList2 = taxResList;
                    it2 = it;
                }
            } else {
                double intValue3 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                double price6 = purchaseItem.getPrice();
                Double.isNaN(intValue3);
                price += intValue3 * price6;
            }
            double price7 = purchaseItem.getPrice();
            double intValue4 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
            Double.isNaN(intValue4);
            priceWithoutTax += price7 * intValue4;
        }
        showAddedTax(finalTaxMap, Double.valueOf(priceWithoutTax));
    }

    public void deleteTax(PurchaseItem purchaseItem, int childPosition) {
        List<TaxResponse> exitingTax = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
        exitingTax.remove(childPosition);
        this.taxList.put(Integer.valueOf(purchaseItem.getId()), exitingTax);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        updateTax();
    }

    public void calculateTaxAfterDelete() {
        Double priceWithoutTax = Double.valueOf(Utils.DOUBLE_EPSILON);
        Map<String, Double> finalTaxMap = new HashMap<>();
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
                float tax = 0.0f;
                for (TaxResponse taxResp : this.taxList.get(Integer.valueOf(purchaseItem.getId()))) {
                    tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                    if (finalTaxMap.get(taxResp.getName()) != null) {
                        double doubleValue = finalTaxMap.get(taxResp.getName()).doubleValue();
                        double intValue = ((float) this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue()) * tax;
                        double price = purchaseItem.getPrice();
                        Double.isNaN(intValue);
                        finalTaxMap.put(taxResp.getName(), Double.valueOf(doubleValue + ((intValue * price) / 100.0d)));
                    } else {
                        double intValue2 = ((float) this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue()) * tax;
                        double price2 = purchaseItem.getPrice();
                        Double.isNaN(intValue2);
                        finalTaxMap.put(taxResp.getName(), Double.valueOf((intValue2 * price2) / 100.0d));
                    }
                }
            }
            priceWithoutTax = Double.valueOf(priceWithoutTax.doubleValue() + purchaseItem.getPrice());
        }
        showAddedTax(finalTaxMap, priceWithoutTax);
    }

    public void showAddedTax(Map<String, Double> finalTaxMap, Double total) {
        String taxBreakDown = "";
        double totalTax = Utils.DOUBLE_EPSILON;
        new DecimalFormat("#.##");
        this.taxSummaryList.clear();
        for (Map.Entry<String, Double> entry : finalTaxMap.entrySet()) {
            String finalAmount = String.format("%.2f", entry.getValue()).replace(",", ".");
            taxBreakDown = taxBreakDown + " <br> " + entry.getKey() + " &nbsp; : &nbsp; " + finalAmount;
            this.taxSummaryList.add(new TaxSummaryList(entry.getKey(), Double.parseDouble(finalAmount)));
            totalTax += Double.parseDouble(finalAmount);
        }
        this.rITaxSummaryItemsAdapter.notifyDataChange(this.taxSummaryList, this.selectedExchangeCurrency);
        this.subtotalText.setText(this.selectdCurrency + (total.doubleValue() + totalTax));
        this.tv_invoiceAmount.setText(this.selectdCurrency + (total.doubleValue() + totalTax));
        this.priceTotal.setText(this.selectdCurrency + total);
        com.akounto.accountingsoftware.response.currency.Currency currency = this.selectedExchangeCurrency;
        if (currency == null || currency.getExchangeRate().doubleValue() == 1.0d) {
            this.subTotalInDollar.setVisibility(View.GONE);
            this.subtotalAmountInDolar.setText(this.selectdCurrency + (total.doubleValue() + totalTax));
        } else {
            DecimalFormat f = new DecimalFormat("##.00");
            Double totalAFterConvert = Double.valueOf((1.0d / this.selectedExchangeCurrency.getExchangeRate().doubleValue()) * (total.doubleValue() + totalTax));
            this.subTotalInDollar.setVisibility(View.VISIBLE);
            this.subTotalPaidInDolar.setText("Total (USD at " + f.format(1.0d / this.selectedExchangeCurrency.getExchangeRate().doubleValue()).replace(',', '.') + ")");
            this.subtotalAmountInDolar.setText("$" + f.format(totalAFterConvert).replace(',', '.'));
        }
        setListViewHeight(this.productExpandable, this.groupPosition, true);
        if (this.purchaseItemListSelected.size() > 0) {
            int i = 0;
            while (true) {
                int i2 = this.groupPosition;
                if (i <= i2) {
                    if (i == i2) {
                        this.productExpandable.expandGroup(i);
                    } else {
                        this.productExpandable.collapseGroup(i);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void deleteProduct(int groupPosition2) {
        this.purchaseItemListSelected.remove(groupPosition2);
        updatePrice();
    }

    /* access modifiers changed from: private */
    public void updatePrice() {
        Double price = Double.valueOf(Utils.DOUBLE_EPSILON);
        Double priceWithoutTax = Double.valueOf(Utils.DOUBLE_EPSILON);
        for (PurchaseItem item : this.purchaseItemListSelected) {
            double doubleValue = price.doubleValue();
            double price3 = item.getPrice();
            double intValue = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
            Double.isNaN(intValue);
            price = Double.valueOf(doubleValue + (price3 * intValue));
            double doubleValue2 = priceWithoutTax.doubleValue();
            double price4 = item.getPrice();
            double intValue2 = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
            Double.isNaN(intValue2);
            priceWithoutTax = Double.valueOf(doubleValue2 + (price4 * intValue2));
        }
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        setListViewHeight(this.productExpandable, this.groupPosition, true);
        Map<String, Double> finalTaxMap = new HashMap<>();
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
                float tax = 0.0f;
                for (TaxResponse taxResp : this.taxList.get(Integer.valueOf(purchaseItem.getId()))) {
                    tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                    if (finalTaxMap.get(taxResp.getName()) != null) {
                        double intValue3 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                        double doubleValue3 = finalTaxMap.get(taxResp.getName()).doubleValue();
                        Double.isNaN(intValue3);
                        double d = tax;
                        double price5 = purchaseItem.getPrice();
                        Double.isNaN(d);
                        Double totalTax = Double.valueOf((intValue3 * doubleValue3) + ((d * price5) / 100.0d));
                        price = Double.valueOf(price.doubleValue() + totalTax.doubleValue());
                        finalTaxMap.put(taxResp.getName(), totalTax);
                    } else {
                        double intValue4 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                        double d2 = tax;
                        double price6 = purchaseItem.getPrice();
                        Double.isNaN(d2);
                        Double.isNaN(intValue4);
                        double taxTmp = (intValue4 * (d2 * price6)) / 100.0d;
                        price = Double.valueOf(price.doubleValue() + taxTmp);
                        finalTaxMap.put(taxResp.getName(), Double.valueOf(taxTmp));
                    }
                }
            }
        }
        TextView textView = this.subtotalText;
        textView.setText("$" + price);
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + priceWithoutTax);
        showAddedTax(finalTaxMap, priceWithoutTax);
    }

    public void edit(final PurchaseItem purchaseItem) {
        this.editedpurchaseItem = purchaseItem;
        Dialog dialog2 = new Dialog(this);
        this.dialogueEdit = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialogueEdit.setContentView(R.layout.dialogue_edititem);
        this.dialogueEdit.setCancelable(true);
        this.dialogueEdit.setCanceledOnTouchOutside(true);
        this.dialogueEdit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText editQuantity = this.dialogueEdit.findViewById(R.id.editQuantity);
        this.dialogueEdit.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateBillActivity.this.editQuantity(editQuantity.getText().toString(), purchaseItem);
                CreateBillActivity.this.dialogueEdit.dismiss();
            }
        });
        this.dialogueEdit.show();
    }

    /* access modifiers changed from: private */
    public void editQuantity(String quant, PurchaseItem purchaseItem) {
        this.quantityMap.put(Integer.valueOf(purchaseItem.getId()), Integer.valueOf(Integer.parseInt(quant)));
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        updatePrice();
    }

    private void updateWithQuantity() {
        double price = Utils.DOUBLE_EPSILON;
        double priceWithoutTax = Utils.DOUBLE_EPSILON;
        Double taxTotal2 = Double.valueOf(Utils.DOUBLE_EPSILON);
        Iterator<PurchaseItem> it = this.purchaseItemListSelected.iterator();
        while (true) {
            int i = 0;
            if (it.hasNext()) {
                PurchaseItem item = it.next();
                if (item.getProductServiceTaxes() == null || this.taxList.get(Integer.valueOf(item.getId())).size() <= 0) {
                    int qunntityTmp = 1;
                    if (this.quantityMap.get(Integer.valueOf(item.getId())) != null) {
                        qunntityTmp = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                    }
                    double price2 = item.getPrice();
                    double d = qunntityTmp;
                    Double.isNaN(d);
                    price += price2 * d;
                } else {
                    Iterator it2 = this.taxList.get(Integer.valueOf(item.getId())).iterator();
                    while (it2.hasNext()) {
                        TaxResponse productServiceTaxesItem = (TaxResponse) it2.next();
                        int qunntityTmp2 = 1;
                        if (this.quantityMap.get(Integer.valueOf(item.getId())) != null) {
                            qunntityTmp2 = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                        }
                        double doubleValue = taxTotal2.doubleValue();
                        double rate = productServiceTaxesItem.getEffectiveTaxes().get(i).getRate();
                        double price3 = item.getPrice();
                        Double.isNaN(rate);
                        double d2 = qunntityTmp2;
                        Double.isNaN(d2);
                        taxTotal2 = Double.valueOf(doubleValue + ((((rate * price3) / 100.0d) + item.getPrice()) * d2));
                        it2 = it2;
                        i = 0;
                    }
                    this.taxList.put(Integer.valueOf(item.getId()), getItemBasedOnTaxresponse(item.getProductServiceTaxes()));
                }
                priceWithoutTax += item.getPrice();
            } else {
                this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
                this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
                setListViewHeight(this.productExpandable, 0, true);
                TextView textView = this.subtotalText;
                textView.setText("$" + price);
                TextView textView2 = this.priceTotal;
                textView2.setText("$" + priceWithoutTax);
                calculateTaxAfterDelete();
                return;
            }
        }
    }
}
