package com.akounto.accountingsoftware.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.akounto.accountingsoftware.request.BillsById;
import com.akounto.accountingsoftware.response.BillsByIdResponse;
import com.akounto.accountingsoftware.response.EffectiveTax;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class CreateBillActivityBcg extends AppCompatActivity {
    AddBillWithTaxExpandable addBillWithTaxExpandable;
    TextView addItem;
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
    TextView tv_invoiceDate;
    List<String> vendorList = new ArrayList();
    PowerSpinnerView vendorSpinner;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbill);
        try {
            ((TextView) findViewById(R.id.pageTitle)).setText("Create Bill");
            inItUi();
            this.isEdit = Boolean.valueOf(getIntent().getBooleanExtra("IS_EDIT", false));
            Log.d("isEditisEdit", this.isEdit + "");
            this.guid = getIntent().getStringExtra("GUID");
            if (this.isEdit.booleanValue()) {
                getBillsDetailsById(this.guid);
            }
        } catch (Exception e) {
        }
    }

    private void inItUi() {
        try {
            this.subTotalInDollar = findViewById(R.id.subTotalInDollar);
            this.subtotalText = findViewById(R.id.subtotalText);
            this.priceTotal = findViewById(R.id.priceTotal);
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
                    CreateBillActivityBcg.this.lambda$inItUi$0$CreateBillActivityBcg(view);
                }
            });
            getDataList();
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
                    CreateBillActivityBcg.this.lambda$inItUi$2$CreateBillActivityBcg(view);
                }
            });
            this.duedateET.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CreateBillActivityBcg.this.lambda$inItUi$4$CreateBillActivityBcg(view);
                }
            });
            getTaxList();
            this.productExpandable = findViewById(R.id.productExpandable);
            AddBillWithTaxExpandable addBillWithTaxExpandable2 = new AddBillWithTaxExpandable(this, this.purchaseItemListSelected, this.taxList, this.quantityMap);
            this.addBillWithTaxExpandable = addBillWithTaxExpandable2;
            this.productExpandable.setAdapter(addBillWithTaxExpandable2);
            this.productExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    CreateBillActivityBcg.this.setListViewHeight(parent, groupPosition);
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
                    CreateBillActivityBcg.this.lambda$inItUi$5$CreateBillActivityBcg(view);
                }
            });
            findViewById(R.id.addVendorButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CreateBillActivityBcg.this.lambda$inItUi$6$CreateBillActivityBcg(view);
                }
            });
            findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CreateBillActivityBcg.this.lambda$inItUi$7$CreateBillActivityBcg(view);
                }
            });
            this.currencySpinner = findViewById(R.id.currencySpinner);
            try {
                fetchCurrencies();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    public void lambda$inItUi$0$CreateBillActivityBcg(View v) {
        openDialogue();
    }

    public void lambda$inItUi$2$CreateBillActivityBcg(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBillActivityBcg.this.lambda$null$1$CreateBillActivityBcg(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    public void lambda$null$1$CreateBillActivityBcg(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView textView = this.dateEV;
        textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$4$CreateBillActivityBcg(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear1 = c.get(1);
        this.mMonth1 = c.get(2);
        this.mDay1 = c.get(5);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CreateBillActivityBcg.this.lambda$null$3$CreateBillActivityBcg(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay).show();
    }

    public void lambda$null$3$CreateBillActivityBcg(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView textView = this.duedateET;
        textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$5$CreateBillActivityBcg(View v) {
        addBill();
    }

    public void lambda$inItUi$6$CreateBillActivityBcg(View v) {
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    public void lambda$inItUi$7$CreateBillActivityBcg(View v) {
        finish();
    }

    private void fetchCurrencies() throws JSONException {
        try {
            String loadJSONFromAsset = UiUtil.loadJSONFromAsset(this, "currency.json");
            Objects.requireNonNull(loadJSONFromAsset);
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                this.currencyList.add(new Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
                List<String> list = this.currencyListForSpinner;
                list.add(jsonObject.getString("Id") + " - " + jsonObject.getString("Name"));
            }
            setUpCurrencySpinner(this.currencyListForSpinner);
        } catch (Exception e) {
        }
    }

    private void setUpCurrencySpinner(List<String> currencyListForSpinner2) {
        this.currencySpinner.setItems(currencyListForSpinner2);
        this.currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CreateBillActivityBcg.this.lambda$setUpCurrencySpinner$8$CreateBillActivityBcg(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpCurrencySpinner$8$CreateBillActivityBcg(int i, String s, int selectedIndex, String selectedItem) {
        updateCurrency(selectedItem);
    }

    private void updateCurrency(String selectedItem) {
        Log.d("selectedItem", selectedItem);
        getExchangedCurrency(selectedItem.substring(0, Math.min(selectedItem.length(), 3)));
    }

    private void getExchangedCurrency(String id) {
        RestClient.getInstance(this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson(response.body().getData()));
                        CreateBillActivityBcg.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        CreateBillActivityBcg createBillActivityBcg = CreateBillActivityBcg.this;
                        createBillActivityBcg.selectdCurrency = createBillActivityBcg.selectedExchangeCurrency.getSymbol();
                        if (CreateBillActivityBcg.this.selectedExchangeCurrency.getExchangeRate().doubleValue() == 1.0d) {
                            CreateBillActivityBcg.this.subTotalInDollar.setVisibility(View.GONE);
                        } else {
                            CreateBillActivityBcg.this.subTotalInDollar.setVisibility(View.VISIBLE);
                        }
                        CreateBillActivityBcg.this.calculateTaxAfterDelete();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void getBillsDetailsById(String id) {
        RestClient.getInstance(this).getBillById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<BillsByIdResponse>(this, null) {
            public void onResponse(Call<BillsByIdResponse> call, Response<BillsByIdResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("response----1-1", new Gson().toJson(response.body().getData()));
                        CreateBillActivityBcg.this.updateBillData(response.body().getData());
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<BillsByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                UiUtil.showToast(CreateBillActivityBcg.this, t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateBillData(BillsById data) {
        try {
            this.dateEV.setText(getFormattedDate(data.getBillAt()));
            this.duedateET.setText(getFormattedDate(data.getDueAt()));
            updateVendorForEdit(data.getVendor());
            TextView textView = this.subtotalText;
            textView.setText("$" + data.getPrice());
            TextView textView2 = this.priceTotal;
            textView2.setText("$" + data.getPrice());
            this.content.setText(data.getInvoiceNo());
            this.po_soET.setText(data.getPoNumber());
            convertToProductData(data.getBillTransaction());
        } catch (Exception e) {
        }
    }

    private void convertToProductData(List<GetInvoiceTransactionItem> billTransaction) {
        new ArrayList();
        for (GetInvoiceTransactionItem next : billTransaction) {
        }
    }

    private void updateVendorForEdit(Vendor vendor) {
        try {
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
        } catch (Exception e) {
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
    public void setListViewHeight(ExpandableListView listView, int group) {
        try {
            ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View groupItem = listAdapter.getGroupView(i, false, null, listView);
                groupItem.measure(desiredWidth, 0);
                totalHeight += groupItem.getMeasuredHeight();
                if ((listView.isGroupExpanded(i) && i != group) || (!listView.isGroupExpanded(i) && i == group)) {
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
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
        }
    }

    private void getDataList() {
        RestClient.getInstance(this).getPurchaseItem(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<PurchaseItemResponse>(this, null) {
            public void onResponse(Call<PurchaseItemResponse> call, Response<PurchaseItemResponse> response) {
                super.onResponse(call, response);
                try {
                    Log.d("responseresponse", new Gson().toJson(response.body().getData()));
                    if (response.isSuccessful()) {
                        CreateBillActivityBcg.this.purchaseItemList = response.body().getData();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<PurchaseItemResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void getSalesTaxList() {
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                try {
                    response.isSuccessful();
                } catch (Exception e) {
                }
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
        getVendorList();
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
                CreateBillActivityBcg.this.lambda$setVendorSpinner$9$CreateBillActivityBcg(customerList2, i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setVendorSpinner$9$CreateBillActivityBcg(List customerList2, int i, String s, int selectedIndex, String selectedItem) {
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
        RestClient.getInstance(this).getVendorList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<VendorResponse>(this, null) {
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                super.onResponse(call, response);
                try {
                    List unused = CreateBillActivityBcg.this.customerList = response.body().getData();
                    Log.d("CustomerResponse---", response.toString());
                    if (response.isSuccessful()) {
                        CreateBillActivityBcg createBillActivityBcg = CreateBillActivityBcg.this;
                        createBillActivityBcg.setVendorSpinner(createBillActivityBcg.customerList);
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<VendorResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getVendorById(int id) {
        RestClient.getInstance(this).getVendorDetails(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<VendorDetailsResponse>(this, "Fetching States...") {
            public void onResponse(Call<VendorDetailsResponse> call, Response<VendorDetailsResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        CreateBillActivityBcg.this.updateVendor(response.body().getData());
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<VendorDetailsResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(CreateBillActivityBcg.this, "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateVendor(AddVendorRequest vendor) {
        try {
            this.selectedVendor = vendor;
            this.customerNameTv.setText(vendor.getVendorName());
            TextView textView = this.tv_customeraddres;
            textView.setText(vendor.getBillAddress1() + "," + vendor.getBillAddress2());
            TextView textView2 = this.tv_customeraddres2;
            textView2.setText(vendor.getBillCity() + "," + vendor.getBillState() + "," + vendor.getBillPostCode());
            TextView textView3 = this.customerEmailTv;
            StringBuilder sb = new StringBuilder();
            sb.append("Email: ");
            sb.append(vendor.getEmail());
            textView3.setText(sb.toString());
        } catch (Exception e) {
        }
    }

    private void addBill() {
        if (isValidData()) {
            try {
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
                            addBillTax.setId(res.getId());
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
                    billsReq.setExchangeRate(currency2.getExchangeRate());
                } else {
                    billsReq.setExchangeRate(Double.valueOf(1.0d));
                }
                billsReq.setHeadTransactionVendorId(this.selectedVendor.getHeadTransactionId());
                billsReq.setInvoiceNo(this.content.getText().toString());
                billsReq.setPoNumber(this.po_soET.getText().toString());
                Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
               /* RestClient.getInstance(this).createBill(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), billsReq).enqueue(new CustomCallBack<AddBill>(this, null) {
                    public void onResponse(Call<AddBill> call, Response<AddBill> response) {
                        super.onResponse(call, response);
                        CreateBillActivityBcg.this.finish();
                    }

                    public void onFailure(Call<AddBill> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("errorerror-bill", t.toString());
                    }
                });*/
            } catch (Exception e) {
            }
        }
    }

    private boolean isValidData() {
        try {
            if (this.content.getText().toString().length() == 0) {
                UiUtil.showToast(this, "Please enter valid bill no");
                return false;
            } else if (this.po_soET.getText().toString().length() == 0) {
                UiUtil.showToast(this, "Please enter valid P.O./S.O");
                return false;
            } else if (this.dateEV.getText().toString().length() == 0) {
                UiUtil.showToast(this, "Please enter valid Date");
                return false;
            } else if (this.duedateET.getText().toString().length() == 0) {
                UiUtil.showToast(this, "Please enter valid Due Date");
                return false;
            } else if (this.selectedVendor != null) {
                return true;
            } else {
                UiUtil.showToast(this, "Please select a vendor");
                return false;
            }
        } catch (Exception e) {
            UiUtil.showToast(this, "Something went wrong");
            return false;
        }
    }

    private void openDialogue() {
        try {
            Dialog dialog2 = new Dialog(this);
            this.dialog = dialog2;
            dialog2.requestWindowFeature(1);
            this.dialog.setContentView(R.layout.dialogue_add_bill_item);
            this.dialog.setCancelable(true);
            this.dialog.setCanceledOnTouchOutside(true);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
            ListItemAdapter adapter = new ListItemAdapter(this, this.purchaseItemList);
            listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
            listItemRecycler.setAdapter(adapter);
            this.dialog.findViewById(R.id.addNewItem).setOnClickListener($$Lambda$CreateBillActivityBcg$FKPcGS9VddEes1zx73zy8ES9M2Y.INSTANCE);
            this.dialog.findViewById(R.id.addNewItem).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CreateBillActivityBcg.this.openAddNewItemDialogue();
                    CreateBillActivityBcg.this.dialog.dismiss();
                }
            });
            this.dialog.show();
        } catch (Exception e) {
        }
    }

    static void lambda$openDialogue$10(View v) {
    }

    /* access modifiers changed from: private */
    public void openAddNewItemDialogue() {
        try {
            Dialog dialog2 = new Dialog(this);
            dialog2.requestWindowFeature(1);
            dialog2.setContentView(R.layout.dialogue_add_new_iem);
            dialog2.setCancelable(true);
            dialog2.setCanceledOnTouchOutside(true);
            startActivity(new Intent(this, AddVendorActivity.class));
        } catch (Exception e) {
        }
    }

    private void getTaxList() {
        RestClient.getInstance(this).getTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                try {
                    Log.d("getTaxList----", new Gson().toJson(response.body().getData()));
                    if (response.isSuccessful()) {
                        CreateBillActivityBcg.this.taxReceivedList = response.body().getData();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void addBill(PurchaseItem purchaseItem) {
        Iterator it;
        Double taxprice;
        Log.d("purchaseItempurc", this.purchaseItemList.size() + "");
        this.dialog.dismiss();
        this.purchaseItemListSelected.add(purchaseItem);
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
        double price = Utils.DOUBLE_EPSILON;
        double priceWithoutTax = Utils.DOUBLE_EPSILON;
        Iterator<PurchaseItem> it2 = this.purchaseItemListSelected.iterator();
        while (true) {
            int i = 0;
            if (it2.hasNext()) {
                PurchaseItem item = it2.next();
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
                    Iterator it3 = this.taxList.get(Integer.valueOf(item.getId())).iterator();
                    while (it3.hasNext()) {
                        TaxResponse productServiceTaxesItem = (TaxResponse) it3.next();
                        int qunntityTmp2 = 1;
                        if (this.quantityMap.get(Integer.valueOf(item.getId())) != null) {
                            qunntityTmp2 = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                        }
                        Double taxprice2 = taxListMap.get(productServiceTaxesItem.getName());
                        if (taxprice2 != null) {
                            double doubleValue = taxprice2.doubleValue();
                            double rate = productServiceTaxesItem.getEffectiveTaxes().get(i).getRate();
                            double price3 = item.getPrice();
                            Double.isNaN(rate);
                            it = it3;
                            double d2 = qunntityTmp2;
                            Double.isNaN(d2);
                            taxprice = Double.valueOf(doubleValue + (((rate * price3) * d2) / 100.0d));
                            taxListMap.put(productServiceTaxesItem.getName(), taxprice);
                        } else {
                            it = it3;
                            double rate2 = productServiceTaxesItem.getEffectiveTaxes().get(0).getRate();
                            double price4 = item.getPrice();
                            Double.isNaN(rate2);
                            double d3 = rate2 * price4;
                            double d4 = qunntityTmp2;
                            Double.isNaN(d4);
                            taxprice = Double.valueOf((d3 * d4) / 100.0d);
                            taxListMap.put(productServiceTaxesItem.getName(), taxprice);
                        }
                        double rate3 = productServiceTaxesItem.getEffectiveTaxes().get(0).getRate();
                        double price5 = item.getPrice();
                        Double.isNaN(rate3);
                        double d5 = rate3 * price5;
                        Double d6 = taxprice;
                        double d7 = qunntityTmp2;
                        Double.isNaN(d7);
                        price = ((d5 * d7) / 100.0d) + price + item.getPrice();
                        it3 = it;
                        i = 0;
                    }
                }
                double price6 = item.getPrice();
                double intValue = this.quantityMap.get(Integer.valueOf(item.getId())).intValue();
                Double.isNaN(intValue);
                priceWithoutTax += price6 * intValue;
            } else {
                this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
                this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
                setListViewHeight(this.productExpandable, 0);
                TextView textView = this.subtotalText;
                textView.setText("$" + price);
                TextView textView2 = this.priceTotal;
                textView2.setText("$" + priceWithoutTax);
                updateTax();
                showAddedTax(taxListMap, Double.valueOf(priceWithoutTax));
                return;
            }
        }
    }

    private List<TaxResponse> getItemBasedOnTaxresponse(List<ProductServiceTaxesItem> productServiceTaxes) {

        List<TaxResponse> taxResponseList = new ArrayList<>();
        try {
            for (ProductServiceTaxesItem item : productServiceTaxes) {
                TaxResponse taxResponse = new TaxResponse();
                taxResponse.setName(item.getTaxName());
                taxResponse.setId(item.getId());
                List<EffectiveTax> tax = new ArrayList<>();
                tax.add(new EffectiveTax((int) item.getRate()));
                taxResponse.setEffectiveTaxes(tax);
                taxResponseList.add(taxResponse);
            }
        } catch (Exception e) {
        }
        return taxResponseList;
    }

    public void addTax(PurchaseItem taxResponses) {
        this.selectedPurchaseItem = taxResponses;
        Dialog dialog2 = new Dialog(this);
        this.dialogTax = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialogTax.setContentView(R.layout.dialogue_add_bill_item);
        this.dialogTax.setCancelable(true);
        this.dialogTax.setCanceledOnTouchOutside(true);
        this.dialogTax.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView listItemRecycler = this.dialogTax.findViewById(R.id.listItemRecycler);
        TaxListAdapter adapter2 = new TaxListAdapter(this, this.taxReceivedList, CommonInvoiceActivity.convert(taxResponses.getProductServiceTaxes()));
        listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        listItemRecycler.setAdapter(adapter2);
        this.dialogTax.findViewById(R.id.addNewItem).setVisibility(View.GONE);
        this.dialogTax.findViewById(R.id.addNewItem).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateBillActivityBcg.this.openAddNewItemDialogue();
                CreateBillActivityBcg.this.dialogTax.dismiss();
            }
        });
        this.dialogTax.show();
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
        setListViewHeight(this.productExpandable, 0);
        updateTax();
    }

    public void updateTax() {
        try {
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
        } catch (Exception e) {
        }
    }

    public void deleteTax(PurchaseItem purchaseItem, int childPosition) {
        try {
            List<TaxResponse> exitingTax = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
            exitingTax.remove(childPosition);
            this.taxList.put(Integer.valueOf(purchaseItem.getId()), exitingTax);
            this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
            updateTax();
            calculateTaxAfterDelete();
        } catch (Exception e) {
        }
    }

    public void calculateTaxAfterDelete() {
        try {
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
        } catch (Exception e) {
        }
    }

    public void showAddedTax(Map<String, Double> finalTaxMap, Double total) {
        try {
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
            this.priceTotal.setText(this.selectdCurrency + total);
            com.akounto.accountingsoftware.response.currency.Currency currency = this.selectedExchangeCurrency;
            if (currency == null || currency.getExchangeRate().doubleValue() == 1.0d) {
                this.subTotalInDollar.setVisibility(View.GONE);
                this.subtotalAmountInDolar.setText(this.selectdCurrency + (total.doubleValue() + totalTax));
                return;
            }
            DecimalFormat f = new DecimalFormat("##.00");
            Double totalAFterConvert = Double.valueOf((1.0d / this.selectedExchangeCurrency.getExchangeRate().doubleValue()) * (total.doubleValue() + totalTax));
            this.subTotalInDollar.setVisibility(View.VISIBLE);
            this.subTotalPaidInDolar.setText("Total (USD at " + f.format(1.0d / this.selectedExchangeCurrency.getExchangeRate().doubleValue()).replace(',', '.') + ")");
            this.subtotalAmountInDolar.setText("$" + f.format(totalAFterConvert).replace(',', '.'));
        } catch (Exception e) {
        }
    }

    public void deleteProduct(int groupPosition) {
        this.purchaseItemListSelected.remove(groupPosition);
        updatePrice();
    }

    private void updatePrice() {
        try {
            Iterator<PurchaseItem> it;
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
            int i = 0;
            setListViewHeight(this.productExpandable, 0);
            Map<String, Double> finalTaxMap = new HashMap<>();
            Iterator<PurchaseItem> it2 = this.purchaseItemListSelected.iterator();
            while (it2.hasNext()) {
                PurchaseItem purchaseItem = it2.next();
                if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
                    float tax = 0.0f;
                    for (TaxResponse taxResp : this.taxList.get(Integer.valueOf(purchaseItem.getId()))) {
                        tax += (float) taxResp.getEffectiveTaxes().get(i).getRate();
                        if (finalTaxMap.get(taxResp.getName()) != null) {
                            double intValue3 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                            double doubleValue3 = finalTaxMap.get(taxResp.getName()).doubleValue();
                            Double.isNaN(intValue3);
                            double d = intValue3 * doubleValue3;
                            it = it2;
                            double d2 = tax;
                            double price5 = purchaseItem.getPrice();
                            Double.isNaN(d2);
                            Double totalTax = Double.valueOf(d + ((d2 * price5) / 100.0d));
                            price = Double.valueOf(price.doubleValue() + totalTax.doubleValue());
                            finalTaxMap.put(taxResp.getName(), totalTax);
                        } else {
                            it = it2;
                            double intValue4 = this.quantityMap.get(Integer.valueOf(purchaseItem.getId())).intValue();
                            double d3 = tax;
                            double price6 = purchaseItem.getPrice();
                            Double.isNaN(d3);
                            Double.isNaN(intValue4);
                            double taxTmp = (intValue4 * (d3 * price6)) / 100.0d;
                            price = Double.valueOf(price.doubleValue() + taxTmp);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf(taxTmp));
                        }
                        it2 = it;
                        i = 0;
                    }
                }
                it2 = it2;
                i = 0;
            }
            TextView textView = this.subtotalText;
            textView.setText("$" + price);
            TextView textView2 = this.priceTotal;
            textView2.setText("$" + priceWithoutTax);
            showAddedTax(finalTaxMap, priceWithoutTax);
        } catch (Exception e) {
        }
    }

    public void edit(final PurchaseItem purchaseItem) {
        try {
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
                    CreateBillActivityBcg.this.editQuantity(editQuantity.getText().toString(), purchaseItem);
                    CreateBillActivityBcg.this.dialogueEdit.dismiss();
                }
            });
            this.dialogueEdit.show();
        } catch (Exception e) {
        }
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
                setListViewHeight(this.productExpandable, 0);
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
