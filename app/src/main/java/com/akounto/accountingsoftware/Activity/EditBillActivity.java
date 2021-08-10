package com.akounto.accountingsoftware.Activity;

import android.annotation.SuppressLint;
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
import com.akounto.accountingsoftware.adapter.TaxListAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddBill;
import com.akounto.accountingsoftware.request.AddBillTax;
import com.akounto.accountingsoftware.request.AddBillTransaction;
import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.PurchaseItemResponse;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.VendorDetailsResponse;
import com.akounto.accountingsoftware.response.VendorResponse;
import com.akounto.accountingsoftware.response.currency.Currency;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class EditBillActivity extends AppCompatActivity {
    AddBillWithTaxExpandable addBillWithTaxExpandable;
    TextView addItem;
    EditText content;
    TextView customerEmailTv;
    /* access modifiers changed from: private */
    public List<Vendor> customerList = new ArrayList();
    TextView customerNameTv;
    TextView customerPhoneTv;
    AddBIllItemAdapter customersAdapter;
    TextView dateEV;
    Dialog dialog;
    Dialog dialogTax;
    TextView duedateET;
    TextView et_estimateNo;
    TextView invoiceAmount;
    TextView invoiceDateTv;
    TextView invoiceDueTv;
    TextView invoiceNoTv;
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
    Currency selectedExchangeCurrency;
    PurchaseItem selectedPurchaseItem;
    SimpleDateFormat simpleDateFormat;
    TextView subtotalText;
    List<TaxResponse> taxAddedList = new ArrayList();
    Map<Integer, List<TaxResponse>> taxList = new HashMap();
    List<TaxResponse> taxReceivedList = new ArrayList();
    TextView tv_customeraddres;
    TextView tv_customeraddres2;
    TextView tv_invoiceDate;
    List<String> vendorList = new ArrayList();
    PowerSpinnerView vendorSpinner;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbill);
        ((TextView) findViewById(R.id.pageTitle)).setText("Create Bill");
        inItUi();
    }

    private void inItUi() {
        this.subtotalText = findViewById(R.id.subtotalText);
        this.priceTotal = findViewById(R.id.priceTotal);
        this.productRecycler = findViewById(R.id.productRecycler);
        this.customersAdapter = new AddBIllItemAdapter(this, this.purchaseItemList);
        this.productRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.productRecycler.setAdapter(this.customersAdapter);
        TextView textView = findViewById(R.id.addItem);
        this.addItem = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditBillActivity.this.lambda$inItUi$0$EditBillActivity(view);
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
        this.dateEV.setOnClickListener(view -> EditBillActivity.this.lambda$inItUi$2$EditBillActivity(view));
        this.duedateET.setOnClickListener(view -> EditBillActivity.this.lambda$inItUi$4$EditBillActivity(view));
        getTaxList();
        this.productExpandable = findViewById(R.id.productExpandable);
        AddBillWithTaxExpandable addBillWithTaxExpandable2 = new AddBillWithTaxExpandable(this, this.purchaseItemListSelected, this.taxList, this.quantityMap);
        this.addBillWithTaxExpandable = addBillWithTaxExpandable2;
        this.productExpandable.setAdapter(addBillWithTaxExpandable2);
        this.productExpandable.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            EditBillActivity.this.setListViewHeight(parent, groupPosition);
            return false;
        });
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditBillActivity.this.lambda$inItUi$5$EditBillActivity(view);
            }
        });
        findViewById(R.id.addVendorButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditBillActivity.this.lambda$inItUi$6$EditBillActivity(view);
            }
        });
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditBillActivity.this.lambda$inItUi$7$EditBillActivity(view);
            }
        });
    }

    public void lambda$inItUi$0$EditBillActivity(View v) {
        openDialogue();
    }

    public void lambda$inItUi$2$EditBillActivity(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditBillActivity.this.lambda$null$1$EditBillActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$1$EditBillActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView editText = this.dateEV;
        editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$4$EditBillActivity(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear1 = c.get(1);
        this.mMonth1 = c.get(2);
        this.mDay1 = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditBillActivity.this.lambda$null$3$EditBillActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$3$EditBillActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView editText = this.duedateET;
        editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    public void lambda$inItUi$5$EditBillActivity(View v) {
        addBill();
    }

    public void lambda$inItUi$6$EditBillActivity(View v) {
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    public void lambda$inItUi$7$EditBillActivity(View v) {
        finish();
    }

    /* access modifiers changed from: private */
    public void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        @SuppressLint("WrongConstant") int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), 1073741824);
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
    }

    private void getDataList() {
        RestClient.getInstance(this).getPurchaseItem(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<PurchaseItemResponse>(this, null) {
            public void onResponse(Call<PurchaseItemResponse> call, Response<PurchaseItemResponse> response) {
                super.onResponse(call, response);
                Log.d("responseresponse", new Gson().toJson(response.body().getData()));
                if (response.isSuccessful()) {
                    EditBillActivity.this.purchaseItemList = response.body().getData();
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
        this.vendorSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener) (i, obj, i2, obj2) -> EditBillActivity.this.lambda$setVendorSpinner$8$EditBillActivity(customerList2, i, (String) obj, i2, (String) obj2));
    }

    public void lambda$setVendorSpinner$8$EditBillActivity(List customerList2, int i, String s, int selectedIndex, String selectedItem) {
        Log.d("selectedItemse--", selectedItem);
        if (selectedItem == "ADD NEW VENDOR") {
            startActivity(new Intent(this, AddVendorActivity.class));
            return;
        }
        int pos = this.vendorList.indexOf(selectedItem);
        Log.d("pospospos", pos + "");
        getVendorById(((Vendor) customerList2.get(pos + -1)).getHeadTransactionId());
    }

    private void getVendorList() {
        this.vendorList.clear();
        this.vendorList.add("ADD NEW VENDOR");
        RestClient.getInstance(this).getVendorList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<VendorResponse>(this, null) {
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                super.onResponse(call, response);
                List unused = EditBillActivity.this.customerList = response.body().getData();
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    EditBillActivity editBillActivity = EditBillActivity.this;
                    editBillActivity.setVendorSpinner(editBillActivity.customerList);
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
                if (response.isSuccessful()) {
                    EditBillActivity.this.updateVendor(response.body().getData());
                }
            }

            public void onFailure(Call<VendorDetailsResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(EditBillActivity.this, "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateVendor(AddVendorRequest vendor) {
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
    }

    private void addBill() {
        AddBill billsReq = new AddBill();
        billsReq.setBillAt(this.dateEV.getText().toString());
        List<AddBillTransaction> abbBillList = new ArrayList<>();
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            AddBillTransaction addBillTransaction = new AddBillTransaction();
            addBillTransaction.setDescription(purchaseItem.getDescription());
            addBillTransaction.setPrice(Double.valueOf(purchaseItem.getPrice()));
            addBillTransaction.setProductId(purchaseItem.getId());
            addBillTransaction.setQuantity(1);
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
        billsReq.setCurrency("USD");
        billsReq.setDueAt(this.duedateET.getText().toString() + "T00:00:00.000Z");
        billsReq.setExchangeRate(Double.valueOf(1.0d));
        billsReq.setHeadTransactionVendorId(7625);
        billsReq.setInvoiceNo(this.content.getText().toString());
        billsReq.setPoNumber(this.po_soET.getText().toString());
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(this).createBill(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), billsReq).enqueue(new CustomCallBack<CustomeResponse>(this, null) {
            public void onResponse(Call<CustomeResponse> call, Response<CustomeResponse> response) {
                super.onResponse(call, response);
                try{
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    EditBillActivity.this.finish();
                } else {
                    UiUtil.showToast(getApplicationContext(), "Fail to update.");
                }}catch(Exception e){

                }
            }

            public void onFailure(Call<CustomeResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    private void openDialogue() {
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
        this.dialog.findViewById(R.id.addNewItem).setOnClickListener($$Lambda$EditBillActivity$d9d408ikXKBXiFRNb8VntW6JVQA.INSTANCE);
        this.dialog.findViewById(R.id.addNewItem).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditBillActivity.this.openAddNewItemDialogue();
                EditBillActivity.this.dialog.dismiss();
            }
        });
        this.dialog.show();
    }

    static void lambda$openDialogue$9(View v) {
    }

    /* access modifiers changed from: private */
    public void openAddNewItemDialogue() {
        Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(1);
        dialog2.setContentView(R.layout.dialogue_add_new_iem);
        dialog2.setCancelable(true);
        dialog2.setCanceledOnTouchOutside(true);
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    private void getTaxList() {
        RestClient.getInstance(this).getTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                Log.d("getTaxList----", new Gson().toJson(response.body().getData()));
                if (response.isSuccessful()) {
                    EditBillActivity.this.taxReceivedList = response.body().getData();
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void addBill(PurchaseItem purchaseItem) {
        Log.d("purchaseItempurc", this.purchaseItemList.size() + "");
        this.dialog.dismiss();
        this.purchaseItemListSelected.add(purchaseItem);
        this.customersAdapter.notifyData(this.purchaseItemListSelected);
        double price = Utils.DOUBLE_EPSILON;
        for (PurchaseItem item : this.purchaseItemListSelected) {
            price += item.getPrice();
        }
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        setListViewHeight(this.productExpandable, 0);
        TextView textView = this.subtotalText;
        textView.setText("$" + price);
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + price);
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
                EditBillActivity.this.openAddNewItemDialogue();
                EditBillActivity.this.dialogTax.dismiss();
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
        double price = Utils.DOUBLE_EPSILON;
        for (PurchaseItem purchaseItem : this.purchaseItemListSelected) {
            if (this.taxList.get(Integer.valueOf(purchaseItem.getId())) != null) {
                float tax = 0.0f;
                for (TaxResponse taxResp : this.taxList.get(Integer.valueOf(purchaseItem.getId()))) {
                    tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                }
                double d = tax;
                double price2 = purchaseItem.getPrice();
                Double.isNaN(d);
                price = ((d * price2) / 100.0d) + price + purchaseItem.getPrice();
            } else {
                price += purchaseItem.getPrice();
            }
        }
        TextView textView = this.subtotalText;
        textView.setText("$" + price);
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + price);
    }

    public void deleteTax(PurchaseItem purchaseItem, int childPosition) {
        List<TaxResponse> exitingTax = this.taxList.get(Integer.valueOf(purchaseItem.getId()));
        exitingTax.remove(childPosition);
        this.taxList.put(Integer.valueOf(purchaseItem.getId()), exitingTax);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        updateTax();
    }

    public void deleteProduct(int groupPosition) {
        this.purchaseItemListSelected.remove(groupPosition);
        double price = Utils.DOUBLE_EPSILON;
        for (PurchaseItem item : this.purchaseItemListSelected) {
            price += item.getPrice();
        }
        this.addBillWithTaxExpandable.notifyData(this.purchaseItemListSelected, this.selectedExchangeCurrency);
        this.addBillWithTaxExpandable.notifyDataMAp(this.taxList, this.quantityMap);
        setListViewHeight(this.productExpandable, 0);
        TextView textView = this.subtotalText;
        textView.setText("$" + price);
        TextView textView2 = this.priceTotal;
        textView2.setText("$" + price);
    }
}
