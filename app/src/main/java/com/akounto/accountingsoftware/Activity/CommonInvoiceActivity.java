package com.akounto.accountingsoftware.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.AddEstimationWithTaxExpandable;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.adapter.RIAddItemsAdapter;
import com.akounto.accountingsoftware.adapter.RITaxSummaryItemsAdapter;
import com.akounto.accountingsoftware.adapter.TaxListAdapter;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.model.TaxSummaryList;
import com.akounto.accountingsoftware.model.UserBusiness;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.request.CreateRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceTransactionItem;
import com.akounto.accountingsoftware.request.RecurringInvoiceSalesTaxItem;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerOnlyResponse;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.EffectiveTax;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.InvoiceNumberResponse;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.ReportSetting.Data;
import com.akounto.accountingsoftware.response.ReportSetting.ReportSettings;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.io.File;
import java.io.IOException;
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

public class CommonInvoiceActivity extends AppCompatActivity implements CommonInvoiceItemUpdate {

    private static final int REQUEST_IMAGE = 100;
    private ImageView addCustomerButton;
    AddEstimationWithTaxExpandable addEstimationWithTaxExpandable;
    private PowerSpinnerView addItemSpinner;
    TextView billedCOmpanyNmTv;
    TextView cancelButton;
    List<Currency> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    private PowerSpinnerView currencySpinner;
    String customerCurrency = "USD";
    TextView customerEmailTv;
    int customerHeadTransactionId = 0;
    List<Customer> customerList = new ArrayList();
    TextView customerNameTv;
    TextView customerPhoneTv;
    private PowerSpinnerView customerSpinner;
    /* access modifiers changed from: private */
    public Dialog dialogTax;
    Dialog dialogueEdit;
    String displayName;
    Date dueDate;
    String dueDateFormatted;
    Product editproduct;
    Group estimateGroup;
    String estimateName = null;
    TextView et_estimateNo;
    EditText et_invoiceNo;
    int groupPosition;
    TextView invoiceAmount;
    Date invoiceDate;
    String invoiceDateFormatted;
    TextView invoiceDateTv;
    TextView invoiceDueTv;
    TextView invoiceNoTv;
    boolean isItemClickDisabled = false;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    List<Product> itemsList = new ArrayList();
    RecyclerView itemsRecyclerView;
    ImageView logoUpload;
    private int mDay;
    private int mMonth;
    private int mYear;
    EditText notesEt;
    Group notesGroup;
    TextView pageTitle;
    TextView paymentDueDateTextView;
    int paymentDueDays = 0;
    private PowerSpinnerView paymentDueSpinner;
    String poNumber = null;
    ExpandableListView productExpandable;
    List<Product> productList = new ArrayList();
    Map<Integer, Integer> quantityMap = new HashMap();
    RITaxSummaryItemsAdapter rITaxSummaryItemsAdapter;
    InvoiceDetails receivedData;
    TextView saveButton;
    String selectdCurrency = "$";
    Type selectedActivityType;
    Customer selectedCustomer;
    com.akounto.accountingsoftware.response.currency.Currency selectedExchangeCurrency;
    com.akounto.accountingsoftware.response.currency.Currency selectedBussinessCurrency;
    String selectdCurrencyCode;
    Product selectedProduct;
    PurchaseItem selectedPurchaseItem;
    SimpleDateFormat simpleDateFormat;
    double subTotal = Utils.DOUBLE_EPSILON;
    TextView subTotalTv;
    Map<Integer, List<TaxResponse>> taxListMap = new HashMap();
    /* access modifiers changed from: private */
    public List<TaxResponse> taxReceivedList = new ArrayList();
    List<TaxSummaryList> taxSummaryList = new ArrayList();
    RecyclerView taxSummaryRecyclerView;
    TextView totalAmountTv;
    TextView tv_customeraddres;
    TextView tv_customeraddres2;
    TextView tv_invoiceDate;
    Uri uri;
    private ReportSettings settings;

    public static Intent buildIntent(Context context, String type) {
        Intent intent = new Intent(context, CommonInvoiceActivity.class);
        intent.putExtra("typeEnum", type);
        return intent;
    }

    public static Intent buildIntentWithData(Context context, String type, InvoiceDetails invoiceDetails) {
        Intent intent = new Intent(context, CommonInvoiceActivity.class);
        intent.putExtra("typeEnum", type);
        intent.putExtra("invoiceDetails", invoiceDetails);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_invoice);
        try {
            this.selectedActivityType = Type.valueOf(getIntent().getStringExtra("typeEnum"));
            this.pageTitle = (TextView) findViewById(R.id.pageTitle);
            int i = C067113.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
            if (i == 1) {
                this.pageTitle.setText("Create Estimate");
            } else if (i != 2) {
                this.pageTitle.setText("New recurring invoice");
            } else {
                this.pageTitle.setText("Create invoice");
            }
            initUi();
        } catch (Exception e) {
        }
    }

    /* renamed from: com.iakounto.i2a.activity.CommonInvoiceActivity$13 */
    static class C067113 {
        static final int[] $SwitchMap$com$akounto$android$activity$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$com$akounto$android$activity$Type = iArr;
            try {
                iArr[Type.ESTIMATES.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$akounto$android$activity$Type[Type.INVOICES.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private void initUi() {
        try {
            Log.d("business_name", ((UserBusiness) new Gson().fromJson(UiUtil.getUserDetails(this).getUserDetails(), UserBusiness.class)).getActiveBusiness().getCountry() + "");
            this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
            Date time = Calendar.getInstance().getTime();
            this.invoiceDate = time;
            this.invoiceDateFormatted = this.simpleDateFormat.format(time);
            this.customerSpinner = (PowerSpinnerView) findViewById(R.id.selectCustomerSpinner);
            this.currencySpinner = (PowerSpinnerView) findViewById(R.id.currencySpinner);
            this.addItemSpinner = (PowerSpinnerView) findViewById(R.id.addItemSpinner);
            PowerSpinnerView powerSpinnerView = (PowerSpinnerView) findViewById(R.id.paymentDueSpinner);
            this.paymentDueSpinner = powerSpinnerView;
            powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                    CommonInvoiceActivity.this.lambda$initUi$0$CommonInvoiceActivity(i, (String) obj, i2, (String) obj2);
                }
            });
            TextView textView = (TextView) findViewById(R.id.paymentDueDatePicker);
            this.paymentDueDateTextView = textView;
            textView.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$2$CommonInvoiceActivity(view);
                }
            });
            this.customerNameTv = (TextView) findViewById(R.id.tv_customerName);
            this.customerEmailTv = (TextView) findViewById(R.id.tv_customerEmail);
            this.customerPhoneTv = (TextView) findViewById(R.id.tv_customerPhone);
            this.tv_customeraddres = (TextView) findViewById(R.id.tv_customeraddres);
            this.tv_customeraddres2 = (TextView) findViewById(R.id.tv_customeraddres2);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_rv);
            this.itemsRecyclerView = recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.itemsRecyclerView.setAdapter(new RIAddItemsAdapter(this, this.itemsList, this));
            this.taxSummaryRecyclerView = (RecyclerView) findViewById(R.id.tax_list_rv);
            this.rITaxSummaryItemsAdapter = new RITaxSummaryItemsAdapter(this, this.taxSummaryList);
            this.taxSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.taxSummaryRecyclerView.setAdapter(this.rITaxSummaryItemsAdapter);
            this.subTotalTv = (TextView) findViewById(R.id.tv_subTotal);
            this.totalAmountTv = (TextView) findViewById(R.id.tv_totalincoiceamount);
            this.billedCOmpanyNmTv = (TextView) findViewById(R.id.billedCOmpanyNmTv);
            if (((UserBusiness) new Gson().fromJson(UiUtil.getUserDetails(this).getUserDetails(), UserBusiness.class)).getActiveBusiness().getName() != null) {
                this.billedCOmpanyNmTv.setText(((UserBusiness) new Gson().fromJson(UiUtil.getUserDetails(this).getUserDetails(), UserBusiness.class)).getActiveBusiness().getName());
            }
            this.invoiceAmount = (TextView) findViewById(R.id.tv_invoiceAmount);
            this.invoiceNoTv = (TextView) findViewById(R.id.invoiceNo);
            this.invoiceDateTv = (TextView) findViewById(R.id.invoiceDate);
            this.tv_invoiceDate = (TextView) findViewById(R.id.tv_invoiceDate);
            this.invoiceDueTv = (TextView) findViewById(R.id.paymentDue);
            Calendar calendar = Calendar.getInstance();
            this.tv_invoiceDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            this.estimateGroup = (Group) findViewById(R.id.estimateGroup);
            this.notesGroup = (Group) findViewById(R.id.notesGroup);
            this.et_invoiceNo = (EditText) findViewById(R.id.et_invoiceNo);
            this.et_estimateNo = (TextView) findViewById(R.id.et_estimateNo);
            this.notesEt = (EditText) findViewById(R.id.et_notes);
            int i = C067113.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
            if (i == 1) {
                this.paymentDueDateTextView.setVisibility(View.VISIBLE);
                this.paymentDueSpinner.setVisibility(View.GONE);
                this.estimateGroup.setVisibility(View.VISIBLE);
                this.invoiceNoTv.setText("P.O. Number");
                this.invoiceDateTv.setText("Estimate Date");
                this.invoiceDueTv.setText("Estimate expiry on");
                this.et_invoiceNo.setText("");
                this.et_invoiceNo.setHint("Enter P.O. Number here");
                this.et_invoiceNo.setTextColor(getResources().getColor(R.color.dark_grey));
                this.notesGroup.setVisibility(View.GONE);
            } else if (i != 2) {
                this.et_invoiceNo.setEnabled(false);
                this.paymentDueSpinner.setVisibility(View.VISIBLE);
                this.paymentDueDateTextView.setVisibility(View.GONE);
            } else {
                this.et_invoiceNo.setEnabled(false);
                this.paymentDueDateTextView.setVisibility(View.VISIBLE);
                this.paymentDueSpinner.setVisibility(View.GONE);
            }
            TextView textView2 = (TextView) findViewById(R.id.saveButton);
            this.saveButton = textView2;
            textView2.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$3$CommonInvoiceActivity(view);
                }
            });
            TextView textView3 = (TextView) findViewById(R.id.cancel_button);
            this.cancelButton = textView3;
            textView3.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$4$CommonInvoiceActivity(view);
                }
            });
            if (getIntent().getSerializableExtra("invoiceDetails") != null) {
                this.isItemClickDisabled = true;
                this.receivedData = (InvoiceDetails) getIntent().getSerializableExtra("invoiceDetails");
               /* Gson gson = new Gson();
                List<Product> item=new ArrayList<>();
                for (int k=0;k<receivedData.getInvoiceTransaction().size();k++){
                    Product temp=gson.fromJson(gson.toJson(receivedData.getInvoiceTransaction().get(k)), Product.class);
                    temp.setProductServiceTaxes(temp.getTaxes());
                    item.add(temp);
                }*/
                isEditingEnabled(false);
            } else {
                this.isItemClickDisabled = false;
            }
            getReportSetting();
            getCustomerList();
            try {
                fetchCurrencies();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageView imageView = (ImageView) findViewById(R.id.logoUpload);
            this.logoUpload = imageView;
            imageView.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$5$CommonInvoiceActivity(view);
                }
            });
            this.productExpandable = (ExpandableListView) findViewById(R.id.productExpandable);
            this.addEstimationWithTaxExpandable = new AddEstimationWithTaxExpandable(this, this.itemsList, this.taxListMap, this.quantityMap);
            this.productExpandable.setAdapter(addEstimationWithTaxExpandable);
            this.productExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition1, long id) {
                    CommonInvoiceActivity.this.groupPosition = groupPosition1;
                    Log.d("11111111", parent.isGroupExpanded(CommonInvoiceActivity.this.groupPosition) + "---" + groupPosition1);
                    if (parent.isGroupExpanded(CommonInvoiceActivity.this.groupPosition)) {
                        CommonInvoiceActivity createBillActivity = CommonInvoiceActivity.this;
                        createBillActivity.setListViewHeight(parent, createBillActivity.groupPosition, false);
                    } else {
                        CommonInvoiceActivity createBillActivity2 = CommonInvoiceActivity.this;
                        createBillActivity2.setListViewHeight(parent, createBillActivity2.groupPosition, true);
                    }
                    return false;
                }
/*            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition1, long id) {
                CommonInvoiceActivity.this.groupPosition = groupPosition1;
                if (parent.isGroupExpanded(groupPosition1)) {
                    CommonInvoiceActivity commonInvoiceActivity = CommonInvoiceActivity.this;
                    commonInvoiceActivity.setListViewHeight(parent, groupPosition1, true);
                } else {
                    CommonInvoiceActivity commonInvoiceActivity2 = CommonInvoiceActivity.this;
                    this.setListViewHeight(parent, groupPosition1, false);
                }
                return false;
            }*/
            });
            findViewById(R.id.addCustomerButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$6$CommonInvoiceActivity(view);
                }
            });
            findViewById(R.id.addProductButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    CommonInvoiceActivity.this.lambda$initUi$7$CommonInvoiceActivity(view);
                }
            });
            getTaxList();
        } catch (Exception e) {
        }
    }

    public void lambda$initUi$0$CommonInvoiceActivity(int i, String s, int selectedIndex, String selectedItem) {
        updateDatesForRequest(selectedIndex);
    }

    public void lambda$initUi$2$CommonInvoiceActivity(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CommonInvoiceActivity.this.lambda$null$1$CommonInvoiceActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$1$CommonInvoiceActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        this.dueDateFormatted = this.simpleDateFormat.format(calendar.getTime());
        TextView textView = this.paymentDueDateTextView;
        textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }

    public void lambda$initUi$3$CommonInvoiceActivity(View v) {
        prepareAddData();
    }

    public void lambda$initUi$4$CommonInvoiceActivity(View v) {
        onBackPressed();
    }

    public void lambda$initUi$5$CommonInvoiceActivity(View v) {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    public void lambda$initUi$6$CommonInvoiceActivity(View v) {
        startActivity(new Intent(this, AddCustomersActivity.class));
    }

    public void lambda$initUi$7$CommonInvoiceActivity(View v) {
        startActivity(new Intent(this, CreatingProductsAndServices.class));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CommonInvoiceActivity.this.selectedExchangeCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        CommonInvoiceActivity.this.selectedBussinessCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        CommonInvoiceActivity.this.selectdCurrencyCode = UiUtil.getBussinessCurren(getApplicationContext());
        fetchAvailableItems();
    }

    private void getEstNo() {
        RestClient.getInstance(this).getEstimateNo(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<InvoiceNumberResponse>(this, (String) null) {
            public void onResponse(Call<InvoiceNumberResponse> call, Response<InvoiceNumberResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        int i = C067113.$SwitchMap$com$akounto$android$activity$Type[selectedActivityType.ordinal()];
                        if (i == 1) {
                            TextView textView = CommonInvoiceActivity.this.et_estimateNo;
                            textView.setText(response.body().getData());
                        } else {
                            /* et_invoiceNo.setText(response.body().getData());*/
                        }
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<InvoiceNumberResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getInvoiceNo() {
        RestClient.getInstance(this).getInvoiceNo(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<InvoiceNumberResponse>(this, (String) null) {
            public void onResponse(Call<InvoiceNumberResponse> call, Response<InvoiceNumberResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        int i = C067113.$SwitchMap$com$akounto$android$activity$Type[selectedActivityType.ordinal()];
                        if (i == 1) {
                      /*  TextView textView = CommonInvoiceActivity.this.et_estimateNo;
                        textView.setText(response.body().getData());*/
                        } else {
                            et_invoiceNo.setText(response.body().getData());
                        }
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<InvoiceNumberResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getReportSetting() {
        RestClient.getInstance(this).getReportSetting(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<ReportSettings>(this, null) {
            public void onResponse(Call<ReportSettings> call, Response<ReportSettings> response) {
                super.onResponse(call, response);
                try {
                    settings = response.body();
                    if (settings.getTransactionStatus().getIsSuccess()) {
                        if (settings.getData().getInvoiceInitialNo() != 0) {
                            int i = C067113.$SwitchMap$com$akounto$android$activity$Type[selectedActivityType.ordinal()];
                            if (i == 1) {
                                getEstNo();
                            } else {
                                getInvoiceNo();
                            }
                        } else {
                            openInvoiceSetting();
                        }
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<ReportSettings> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void updateReportSetting(String prefix, int no, String saafix) {
        Data d = settings.getData();
        d.setInvoiceInitialNo(no);
        d.setInvoiceNoPrefix(prefix);
        d.setInvoiceNoSuffix(saafix);
        settings.setData(d);
        RestClient.getInstance(this).updateReportSetting(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), settings.getData()).enqueue(new CustomCallBack<ReportSettings>(this, null) {
            public void onResponse(Call<ReportSettings> call, Response<ReportSettings> response) {
                super.onResponse(call, response);
                try {
                    settings = response.body();
                    String e = new Gson().toJson(settings.getData());
                    if (settings.getTransactionStatus().getIsSuccess()) {
                        if (settings.getData().getInvoiceInitialNo() != 0) {
                            int i = C067113.$SwitchMap$com$akounto$android$activity$Type[selectedActivityType.ordinal()];
                            if (i == 1) {
                                getEstNo();
                            } else {
                                getInvoiceNo();
                            }
                        } else {
                            openInvoiceSetting();
                        }
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<ReportSettings> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    public void setListViewHeight(ExpandableListView listView, int group, boolean isExpandable) {
        try {
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
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1) {
                Uri uri2 = data.getData();
                String absolutePath = new File(uri2.toString()).getAbsolutePath();
                Uri uri3 = (Uri) data.getParcelableExtra("path");
                Uri selectedImage = data.getData();
                ImageView uploadedImage = (ImageView) findViewById(R.id.uploadedLogoDisplay);
                TextView textView = (TextView) findViewById(R.id.textLogoUpload);
                try {
                    uploadedImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage));
                    uploadedImage.setVisibility(View.VISIBLE);
                    this.logoUpload.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
                this.logoUpload.setImageURI(uri2);
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        try {
            if (Build.VERSION.SDK_INT > 27) {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), photoUri));
            }
            return MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void initUiWithReceivedData(InvoiceDetails receivedData2) {
        try {
            this.paymentDueSpinner.setText(String.valueOf(receivedData2.getPaymentDueDays()));
            updateCustomerDetailsById(receivedData2.getCustomer());
            this.currencySpinner.setText(receivedData2.getCustCurrency());
            for (GetInvoiceTransactionItem getInvoiceTransactionItem : receivedData2.getInvoiceTransaction()) {
                updateProductsListRecyclerView(getItemIndex(getInvoiceTransactionItem.getProductId()));
            }
            this.paymentDueDateTextView.setText(getFormattedDate(receivedData2.getPaymentDue()));
            TextView textView = this.et_estimateNo;
            textView.setText(receivedData2.getName() + "-" + receivedData2.getInvoiceNo());
            this.et_invoiceNo.setText(TextUtils.isEmpty(receivedData2.getPoNumber()) ? "auto-generated" : receivedData2.getPoNumber());
            this.tv_invoiceDate.setText(getFormattedDate(receivedData2.getInvoiceDate()));
        } catch (Exception e) {
        }
    }

    private int getItemIndex(int productId) {
        int indexOfProduct = 0;
        try {
            for (int i = 0; i < this.productList.size(); i++) {
                if (productId == this.productList.get(i).getId()) {
                    indexOfProduct = i;
                }
            }
        } catch (Exception e) {
        }
        return indexOfProduct;
    }

    private void isEditingEnabled(boolean enabled) {
        if (!enabled) {
            this.paymentDueSpinner.setClickable(false);
            this.addItemSpinner.setClickable(false);
            this.customerSpinner.setClickable(false);
            this.currencySpinner.setClickable(false);
            this.saveButton.setVisibility(View.GONE);
            this.cancelButton.setText("Go back");
            this.paymentDueDateTextView.setClickable(false);
            this.et_invoiceNo.setEnabled(false);
        }
    }

    private void updateDatesForRequest(int selectedIndex) {
        Calendar c = Calendar.getInstance();
        c.setTime(this.invoiceDate);
        int i = getResources().getIntArray(R.array.paymentDueDays)[selectedIndex];
        this.paymentDueDays = i;
        c.add(5, i);
        Date time = c.getTime();
        this.dueDate = time;
        this.dueDateFormatted = this.simpleDateFormat.format(time);
    }

    private void getCustomerList() {
        RestClient.getInstance((Activity) this).getCustomerList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<CustomerResponse>(this, (String) null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                try {
                    Log.d("CustomerResponse---", response.toString());
                    if (response.isSuccessful()) {
                        CommonInvoiceActivity.this.customerList = response.body().getData();
                        CommonInvoiceActivity commonInvoiceActivity = CommonInvoiceActivity.this;
                        commonInvoiceActivity.setUpCustomerSpinner(commonInvoiceActivity.customerList);
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getCustomerOnly(int id) {
        RestClient.getInstance((Activity) this).getCustomerListById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CustomerOnlyResponse>(this, (String) null) {
            public void onResponse(Call<CustomerOnlyResponse> call, Response<CustomerOnlyResponse> response) {
                super.onResponse(call, response);
                try {
                    Log.d("CustomerResponse---", response.toString());
                    if (response.isSuccessful()) {
                        CommonInvoiceActivity.this.updateCustomerData(response.body().getData());
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CustomerOnlyResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpCustomerSpinner(List<Customer> data) {
        try {
            ArrayList<String> customers = new ArrayList<>();
            if (data != null) {
                for (Customer customer : data) {
                    customers.add(customer.getName());
                }
                this.customerSpinner.setItems(customers);
                this.customerSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                    public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                        CommonInvoiceActivity.this.lambda$setUpCustomerSpinner$8$CommonInvoiceActivity(i, (String) obj, i2, (String) obj2);
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    public void lambda$setUpCustomerSpinner$8$CommonInvoiceActivity(int i, String s, int selectedIndex, String selectedItem) {
        updateCustomerDetailsById(this.customerList.get(selectedIndex));
    }

    private void updateCustomerDetailsById(Customer selectedCustomer2) {
        getCustomerOnly(selectedCustomer2.getHeadTransactionId());
    }

    public void updateCustomerData(Customer selectedCustomer2) {
        try {
            this.selectedCustomer = selectedCustomer2;
            this.customerNameTv.setText(selectedCustomer2.getName());
            String address = "";
            if (!TextUtils.isEmpty(selectedCustomer2.getBillAddress1())) {
                address = address + selectedCustomer2.getBillAddress1();
            }
            if (!TextUtils.isEmpty(selectedCustomer2.getBillAddress2())) {
                if (address.length() > 0) {
                    address = address + "," + selectedCustomer2.getBillAddress2();
                } else {
                    address = address + selectedCustomer2.getBillAddress2();
                }
            }
            if (address.length() > 0) {
                this.tv_customeraddres.setText(address);
                this.tv_customeraddres.setVisibility(View.VISIBLE);
            } else {
                this.tv_customeraddres.setVisibility(View.GONE);
            }
            String cityState = "";
            if (!TextUtils.isEmpty(selectedCustomer2.getBillCity())) {
                cityState = cityState + selectedCustomer2.getBillCity();
            }
            if (!TextUtils.isEmpty(selectedCustomer2.getBillState())) {
                if (address.length() > 0) {
                    cityState = cityState + "," + selectedCustomer2.getBillState();
                } else {
                    cityState = cityState + selectedCustomer2.getBillState();
                }
            }
            if (!TextUtils.isEmpty(selectedCustomer2.getBillPostCode())) {
                if (address.length() > 0) {
                    cityState = cityState + "," + selectedCustomer2.getBillPostCode();
                } else {
                    cityState = cityState + selectedCustomer2.getBillPostCode();
                }
            }
            if (cityState.length() > 0) {
                this.tv_customeraddres2.setText(cityState);
                this.tv_customeraddres2.setVisibility(View.VISIBLE);
            } else {
                this.tv_customeraddres2.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(selectedCustomer2.getEmail())) {
                this.customerEmailTv.setText("Email: " + selectedCustomer2.getEmail());
            } else {
                this.customerEmailTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(selectedCustomer2.getPhone())) {
                this.customerPhoneTv.setText("Phone: " + selectedCustomer2.getPhone());
            } else {
                this.customerPhoneTv.setVisibility(View.GONE);
            }
            this.customerCurrency = selectedCustomer2.getCurrency() != null ? selectedCustomer2.getCurrency() : this.customerCurrency;
            this.customerHeadTransactionId = selectedCustomer2.getHeadTransactionId();
        } catch (Exception e) {
        }
    }

    private void fetchCurrencies() throws JSONException {
        try {
            String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", this);
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
                CommonInvoiceActivity.this.lambda$setUpCurrencySpinner$9$CommonInvoiceActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpCurrencySpinner$9$CommonInvoiceActivity(int i, String s, int selectedIndex, String selectedItem) {
        updateCurrency(selectedItem);
    }

    private void updateCurrency(String selectedItem) {
        getExchangedCurrency(selectedItem.substring(0, Math.min(selectedItem.length(), 3)));
    }

    private void getExchangedCurrency(String id) {
        RestClient.getInstance((Activity) this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, (String) null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson((Object) response.body().getData()));
                        CommonInvoiceActivity.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        CommonInvoiceActivity commonInvoiceActivity = CommonInvoiceActivity.this;
                        commonInvoiceActivity.selectdCurrency = commonInvoiceActivity.selectedExchangeCurrency.getSymbol();
                        CommonInvoiceActivity.this.calculateTaxAfterDelete();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void fetchAvailableItems() {
        RestClient.getInstance((Activity) this).getSalesProductList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<SalesProductResponse>(this, (String) null) {
            public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        CommonInvoiceActivity.this.productList = response.body().getData();
                        CommonInvoiceActivity commonInvoiceActivity = CommonInvoiceActivity.this;
                        commonInvoiceActivity.setUpProductItems(commonInvoiceActivity.productList);
                        if (CommonInvoiceActivity.this.isItemClickDisabled) {
                            CommonInvoiceActivity commonInvoiceActivity2 = CommonInvoiceActivity.this;
                            commonInvoiceActivity2.initUiWithReceivedData(commonInvoiceActivity2.receivedData);
                        }
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<SalesProductResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpProductItems(List<Product> productList2) {
        ArrayList<String> products = new ArrayList<>();
        for (Product product : productList2) {
            products.add(product.getName() + "  " + product.getPrice());
        }
        this.addItemSpinner.setItems(products);
        this.addItemSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CommonInvoiceActivity.this.lambda$setUpProductItems$10$CommonInvoiceActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpProductItems$10$CommonInvoiceActivity(int i, String s, int selectedIndex, String selectedItem) {
        updateProductsListRecyclerView(selectedIndex);
    }

    private void updateProductsListRecyclerView(int selectedIndex) {
        try {
            this.addItemSpinner.setText("Add an item");
            this.itemsList.add(this.productList.get(selectedIndex));
            this.quantityMap.put(Integer.valueOf(this.productList.get(selectedIndex).getId()), 1);
            this.subTotal += this.productList.get(selectedIndex).getPrice();
            prepareTaxList(this.productList.get(selectedIndex).getPrice(), this.productList.get(selectedIndex).getProductServiceTaxes());
            this.itemsRecyclerView.getAdapter().notifyDataSetChanged();
            this.addEstimationWithTaxExpandable.notifyData(this.itemsList);
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            updateIntialTax(this.productList.get(selectedIndex));
            setListViewHeight(this.productExpandable, 0, true);
        } catch (Exception e) {
        }
    }

    private void updateIntialTax(Product product) {
        try {
            Map<String, Double> taxListTotal = new HashMap<>();
            double price = Utils.DOUBLE_EPSILON;
            double priceWithoutTax = Utils.DOUBLE_EPSILON;
            for (Product item : this.itemsList) {
                if (item.getProductServiceTaxes() == null || item.getProductServiceTaxes().size() <= 0) {
                    int qunntityTmp = 1;
                    if (this.quantityMap.get(Integer.valueOf(product.getId())) != null) {
                        qunntityTmp = this.quantityMap.get(Integer.valueOf(product.getId())).intValue();
                    }
                    double price2 = item.getPrice();
                    double d = (double) qunntityTmp;
                    Double.isNaN(d);
                    price += price2 * d;
                } else {
                    for (ProductServiceTaxesItem productServiceTaxesItem : item.getProductServiceTaxes()) {
                        int qunntityTmp2 = 1;
                        if (this.quantityMap.get(Integer.valueOf(product.getId())) != null) {
                            qunntityTmp2 = this.quantityMap.get(Integer.valueOf(product.getId())).intValue();
                        }
                        Double taxprice = taxListTotal.get(productServiceTaxesItem.getTaxName());
                        if (taxprice != null) {
                            double doubleValue = taxprice.doubleValue();
                            double d2 = (double) qunntityTmp2;
                            Double.isNaN(d2);
                            taxListTotal.put(productServiceTaxesItem.getTaxName(), Double.valueOf(doubleValue + (((productServiceTaxesItem.getRate() * product.getPrice()) * d2) / 100.0d)));
                        } else {
                            double rate = productServiceTaxesItem.getRate() * product.getPrice();
                            double d3 = (double) qunntityTmp2;
                            Double.isNaN(d3);
                            taxListTotal.put(productServiceTaxesItem.getTaxName(), Double.valueOf((rate * d3) / 100.0d));
                        }
                        double rate2 = productServiceTaxesItem.getRate() * product.getPrice();
                        double d4 = (double) qunntityTmp2;
                        Double.isNaN(d4);
                        price = ((rate2 * d4) / 100.0d) + price + product.getPrice();
                    }
                    this.taxListMap.put(Integer.valueOf(product.getId()), getItemBasedOnTaxresponse(item.getProductServiceTaxes()));
                }
                priceWithoutTax += item.getPrice();
            }
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
        } catch (Exception e) {
        }
    }

    private void prepareTaxList(double productPrice, List<ProductServiceTaxesItem> productServiceTaxes) {
        try {
            float totalCost = 0.0f;
            for (ProductServiceTaxesItem psti : productServiceTaxes) {
                double d = 100.0d;
                if (this.taxListMap.get(Integer.valueOf(psti.getId())) != null) {
                    float tax = 0.0f;
                    for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti.getId()))) {
                        tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                        List<TaxSummaryList> list = this.taxSummaryList;
                        String name = taxResp.getName();
                        double rate = (double) taxResp.getEffectiveTaxes().get(0).getRate();
                        Double.isNaN(rate);
                        list.add(new TaxSummaryList(name, (rate * productPrice) / d, taxResp.getId()));
                        double d2 = (double) totalCost;
                        double rate2 = (double) taxResp.getEffectiveTaxes().get(0).getRate();
                        Double.isNaN(rate2);
                        Double.isNaN(d2);
                        totalCost = (float) (d2 + ((rate2 * productPrice) / 100.0d));
                        d = 100.0d;
                    }
                } else {
                    double d3 = (double) totalCost;
                    Double.isNaN(d3);
                    totalCost = (float) (d3 + ((psti.getRate() * productPrice) / 100.0d));
                    this.taxSummaryList.add(new TaxSummaryList(psti.getTaxName(), (psti.getRate() * productPrice) / 100.0d, psti.getId()));
                }
            }
            updateTotals(this.taxSummaryList, totalCost);
        } catch (Exception e) {
        }
    }

    private void prepareTaxListChild() {
        this.taxSummaryList.clear();
        float totalCost = 0.0f;
        for (Product psti : this.itemsList) {
            double d = 100.0d;
            if (this.taxListMap.get(Integer.valueOf(psti.getId())) != null) {
                float tax = 0.0f;
                for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti.getId()))) {
                    double intValue = (double) (((float) this.quantityMap.get(Integer.valueOf(this.selectedProduct.getId())).intValue()) * totalCost);
                    double rate = (double) taxResp.getEffectiveTaxes().get(0).getRate();
                    double price = psti.getPrice();
                    Double.isNaN(rate);
                    Double.isNaN(intValue);
                    totalCost = (float) (intValue + ((rate * price) / d));
                    tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                    List<TaxSummaryList> list = this.taxSummaryList;
                    String name = taxResp.getName();
                    double intValue2 = (double) (this.quantityMap.get(Integer.valueOf(this.selectedProduct.getId())).intValue() * taxResp.getEffectiveTaxes().get(0).getRate());
                    double price2 = psti.getPrice();
                    Double.isNaN(intValue2);
                    list.add(new TaxSummaryList(name, (intValue2 * price2) / d, taxResp.getId()));
                    d = 100.0d;
                }
            } else {
                double d2 = (double) totalCost;
                double intValue3 = (double) this.quantityMap.get(Integer.valueOf(this.selectedProduct.getId())).intValue();
                double rate2 = psti.getProductServiceTaxes().get(0).getRate();
                Double.isNaN(intValue3);
                Double.isNaN(d2);
                totalCost = (float) (d2 + (((intValue3 * rate2) * psti.getPrice()) / 100.0d));
                this.taxSummaryList.add(new TaxSummaryList(psti.getName(), (psti.getPrice() * psti.getPrice()) / 100.0d, psti.getId()));
            }
        }
        updateTotals(this.taxSummaryList, totalCost);
    }

    private void updateTotals(List<TaxSummaryList> lists, float totalCost) {
        try {
            this.taxSummaryRecyclerView.getAdapter().notifyDataSetChanged();
            double totalTaxAmount = Utils.DOUBLE_EPSILON;
            for (TaxSummaryList taxSummaryList2 : lists) {
                totalTaxAmount += taxSummaryList2.getTaxValue();
            }
            if (totalCost != 0.0f) {
                this.subTotalTv.setText(String.valueOf(this.subTotal));
            } else {
                this.subTotalTv.setText(String.valueOf(0));
            }
            DecimalFormat precision = new DecimalFormat("0.00");
            double d = this.subTotal;
            double d2 = (double) totalCost;
            Double.isNaN(d2);
            String finalAmount = precision.format(d + d2).replace(",", ".");
            TextView textView = this.invoiceAmount;
            textView.setText(this.selectdCurrency + finalAmount);
            TextView textView2 = this.totalAmountTv;
            textView2.setText(this.selectdCurrency + finalAmount);
        } catch (Exception e) {
        }
    }

    public void deleteItem(Product product) {
        try {
            if (!this.isItemClickDisabled) {
                this.subTotal -= product.getPrice();
                removeItemsFromTaxList(product);
                this.itemsList.remove(product);
                this.itemsRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    private void removeItemsFromTaxList(Product listToRemove) {
        try {
            for (ProductServiceTaxesItem psti : listToRemove.getProductServiceTaxes()) {
                Iterator<TaxSummaryList> iterator = this.taxSummaryList.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId() == psti.getId()) {
                        iterator.remove();
                    }
                }
            }
            updateTotals(this.taxSummaryList, 0.0f);
        } catch (Exception e) {
        }
    }

    private void prepareAddData() {
        try {
            List<String> invalidList = isValidData();
            if (invalidList.size() > 0) {
                showErrorDialogue(invalidList);
                return;
            }
            if (this.selectedActivityType == Type.ESTIMATES) {
                this.poNumber = this.et_invoiceNo.getText().toString().trim();
                this.estimateName = "Estimate";
                if (this.paymentDueDateTextView.getText().toString().length() == 0) {
                    UiUtil.showToast(this, "Please enter proper estimate expiry date");
                    return;
                }
            }
            List<RecurringInvoiceSalesTaxItem> recurringInvoiceSalesTaxItems = new ArrayList<>();
            ArrayList arrayList = new ArrayList();
            for (Product product : this.itemsList) {
                for (ProductServiceTaxesItem productServiceTaxesItem : product.getProductServiceTaxes()) {
                    recurringInvoiceSalesTaxItems.add(new RecurringInvoiceSalesTaxItem((int) productServiceTaxesItem.getRate(), productServiceTaxesItem.getTaxId(), productServiceTaxesItem.getTaxName()));
                }
                arrayList.add(new InvoiceTransactionItem(product.getDescription(), product.getName(), recurringInvoiceSalesTaxItems, (int) product.getPrice(), 1, product.getId(), product.getIncomeAccountId()));
            }
            ArrayList arrayList2 = arrayList;
            CreateRecurringInvoiceRequest createRecurringInvoiceRequest = new CreateRecurringInvoiceRequest((int) Math.round(CommonInvoiceActivity.this.selectedExchangeCurrency.getExchangeRate()), (int) Math.round(CommonInvoiceActivity.this.selectedBussinessCurrency.getExchangeRate()), this.customerHeadTransactionId, this.paymentDueDays, this.dueDateFormatted, this.invoiceDateFormatted, arrayList, this.selectdCurrencyCode, this.notesEt.getText().toString().trim(), this.poNumber, this.estimateName);
            int i = C067113.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
            if (i == 1) {
                addInvoice("estimate", createRecurringInvoiceRequest);
            } else if (i != 2) {
                addInvoice("recurring", createRecurringInvoiceRequest);
            } else {
                addInvoice("invoice", createRecurringInvoiceRequest);
            }
        } catch (Exception e) {
        }
    }

    private boolean isValidDataEntered() {
        if (this.et_invoiceNo.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter valid invoice no");
            this.et_invoiceNo.setError("Please enter valid invoice no");
            return false;
        } else if (this.selectedCustomer == null) {
            UiUtil.showToast(this, "Please select a customer");
            return false;
        } else if (this.itemsList.size() != 0) {
            return true;
        } else {
            UiUtil.showToast(this, "Please Add at least one item.");
            return false;
        }
    }

    private void addInvoice(final String invoice_type, CreateRecurringInvoiceRequest createRecurringInvoiceRequest) {
       /* RestClient.getInstance((Activity) this).createInvoice(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), invoice_type, createRecurringInvoiceRequest).enqueue(new CustomCallBack<CustomerResponse>(this, "Creating invoice...") {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("invoice_type", new Gson().toJson((Object) response.body()));
                        CommonInvoiceActivity commonInvoiceActivity = CommonInvoiceActivity.this;
                        UiUtil.showToast(commonInvoiceActivity, invoice_type + " Added");
                        CommonInvoiceActivity.this.finish();
                        return;
                    }
                    CommonInvoiceActivity commonInvoiceActivity2 = CommonInvoiceActivity.this;
                    UiUtil.showToast(commonInvoiceActivity2, invoice_type + " Error while adding");
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });*/
    }

    public static String getFormattedDate(String invoiceDate2) {
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.US);
        try {
            Date date = simpleDateFormat2.parse(invoiceDate2);
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
    public void getTaxList() {
        RestClient.getInstance((Activity) this).getTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, (String) null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                try {
                    Log.d("getTaxList----", new Gson().toJson((Object) response.body().getData()));
                    if (response.isSuccessful()) {
                        List unused = CommonInvoiceActivity.this.taxReceivedList = response.body().getData();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void addTax() {
        if (this.taxReceivedList.size() != 0) {
            try {
                Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                this.dialogTax = dialog;
                dialog.requestWindowFeature(1);
                this.dialogTax.setContentView(R.layout.dialogue_add_bill_item);
                this.dialogTax.setCancelable(true);
                this.dialogTax.setCanceledOnTouchOutside(true);
                this.dialogTax.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                TextView addNewItem = (TextView) this.dialogTax.findViewById(R.id.addNewItem);
                RecyclerView listItemRecycler = (RecyclerView) this.dialogTax.findViewById(R.id.listItemRecycler);
                TaxListAdapter adapter2 = new TaxListAdapter(this, this.taxReceivedList, convert(this.selectedProduct.getProductServiceTaxes()));
                listItemRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                listItemRecycler.setAdapter(adapter2);
                addNewItem.setText("Add new tax(+)");
                addNewItem.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CommonInvoiceActivity.this.dialogTax.dismiss();
                    }
                });
                this.dialogTax.show();
            } catch (Exception e) {
            }
        }
    }

    public static List<TaxResponse> convert(List<ProductServiceTaxesItem> list) {
        List<TaxResponse> taxResponses = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                TaxResponse taxResponse = new TaxResponse();
                taxResponse.setName(list.get(i).getName());
                taxResponse.setId(list.get(i).getTaxId());
                taxResponses.add(taxResponse);
            }
        } catch (Exception e) {
        }
        return taxResponses;
    }

    /* access modifiers changed from: private */
    public void openAddTaxItemDialogue() {
        try {
            View mView = LayoutInflater.from(this).inflate(R.layout.add_salestax_popup_layout, null, false);
            PopupWindow popUp = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
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
                    CommonInvoiceActivity.this.lambda$openAddTaxItemDialogue$12$CommonInvoiceActivity(mView, popUp, view);
                }
            });
        } catch (Exception e) {
        }
    }

    public void openInvoiceSetting() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.invoice_no_setting);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView finaltext = dialog.findViewById(R.id.final_invoice_no);
        EditText prefix = dialog.findViewById(R.id.set_invoice_prefix);
        EditText no = dialog.findViewById(R.id.set_invoice_number);
        EditText suffix = dialog.findViewById(R.id.set_invoice_suffix);
        prefix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    finaltext.setText("Final Invoice No:-" + prefix.getText().toString() + no.getText().toString() + suffix.getText().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    finaltext.setText("Final Invoice No:-" + prefix.getText().toString() + no.getText().toString() + suffix.getText().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        suffix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    finaltext.setText("Final Invoice No:-" + prefix.getText().toString() + no.getText().toString() + suffix.getText().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                CommonInvoiceActivity.this.finish();
            }
        });
        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int number = 0;
                String pre = prefix.getText().toString();
                try {
                    number = Integer.parseInt(no.getText().toString());
                } catch (Exception e) {
                }
                String suf = suffix.getText().toString();
                if (number != 0) {
                    dialog.dismiss();
                    updateReportSetting(pre, number, suf);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid Invoice Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void lambda$openAddTaxItemDialogue$12$CommonInvoiceActivity(View mView, PopupWindow popUp, View v) {
        try {
            boolean isRecoverableTax;
            boolean isCompoundTaxSelected;
            View view = mView;
            EditText etTaxName = (EditText) view.findViewById(R.id.et_taxName);
            if (etTaxName.getText().toString().length() == 0) {
                UiUtil.showToast(this, "Please enter Tax Name");
                return;
            }
            String taxName = etTaxName.getText().toString().trim();
            String taxDesc = ((EditText) view.findViewById(R.id.et_taxDesc)).getText().toString().trim();
            String trim = ((EditText) view.findViewById(R.id.et_taxNumber)).getText().toString().trim();
            String taxRate = ((EditText) view.findViewById(R.id.et_taxRate)).getText().toString().trim();
            if (taxRate.length() == 0) {
                UiUtil.showToast(this, "Please enter Tax Rate");
                return;
            }
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
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
            ArrayList<EffectiveTaxesItem> effectiveTaxesItems2 = effectiveTaxesItems;
            int i = selectedId;
            RadioGroup radioGroup2 = radioGroup;
            final PopupWindow popupWindow = popUp;
            RestClient.getInstance((Activity) this).addSaleTax(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems2)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Sale tax...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(CommonInvoiceActivity.this, "Sale Tax added!");
                        popupWindow.dismiss();
                        CommonInvoiceActivity.this.getTaxList();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
        } catch (Exception e) {
        }
    }

    public void deleteTax(Product product, int childPosition) {
        try {
            List<TaxResponse> exitingTax = this.taxListMap.get(Integer.valueOf(product.getId()));
            exitingTax.remove(childPosition);
            this.taxListMap.put(Integer.valueOf(product.getId()), exitingTax);
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            updateTax();
            calculateTaxAfterDelete();
        } catch (Exception e) {
        }
    }

    public void updateTax() {
        try {
            float price = 0.0f;
            double priceWithoutTax = Utils.DOUBLE_EPSILON;
            Map<String, Double> finalTaxMap = new HashMap<>();
            for (Product psti : this.itemsList) {
                if (this.taxListMap.get(Integer.valueOf(psti.getId())) != null) {
                    for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti.getId()))) {
                        float tax = (float) taxResp.getEffectiveTaxes().get(0).getRate();
                        if (finalTaxMap.get(taxResp.getName()) != null) {
                            double intValue = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                            double doubleValue = finalTaxMap.get(taxResp.getName()).doubleValue();
                            double d = (double) tax;
                            double price2 = psti.getPrice();
                            Double.isNaN(d);
                            Double.isNaN(intValue);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf(intValue * (doubleValue + ((d * price2) / 100.0d))));
                            double d2 = (double) price;
                            double d3 = (double) tax;
                            double price3 = psti.getPrice();
                            Double.isNaN(d3);
                            Double.isNaN(d2);
                            price = (float) (d2 + ((d3 * price3) / 100.0d));
                        } else {
                            double intValue2 = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                            double d4 = (double) tax;
                            double price4 = psti.getPrice();
                            Double.isNaN(d4);
                            Double.isNaN(intValue2);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf((double) ((float) (intValue2 * ((d4 * price4) / 100.0d)))));
                            double d5 = (double) price;
                            double d6 = (double) tax;
                            double price5 = psti.getPrice();
                            Double.isNaN(d6);
                            Double.isNaN(d5);
                            price = (float) (d5 + ((d6 * price5) / 100.0d));
                        }
                    }
                } else {
                    double d7 = (double) price;
                    double intValue3 = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                    double price6 = psti.getPrice();
                    Double.isNaN(intValue3);
                    Double.isNaN(d7);
                    price = (float) (d7 + (intValue3 * price6));
                }
                double price7 = psti.getPrice();
                double intValue4 = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                Double.isNaN(intValue4);
                priceWithoutTax += price7 * intValue4;
            }
            showAddedTax(finalTaxMap, priceWithoutTax);
        } catch (Exception e) {
        }
    }

    public void addTax(Product product) {
        try {
            this.selectedProduct = product;
            if (this.taxReceivedList.size() != 0) {
                Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                this.dialogTax = dialog;
                dialog.requestWindowFeature(1);
                this.dialogTax.setContentView(R.layout.dialogue_add_bill_item);
                this.dialogTax.setCancelable(true);
                this.dialogTax.setCanceledOnTouchOutside(true);
                this.dialogTax.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                TextView addNewItem = (TextView) this.dialogTax.findViewById(R.id.addNewItem);
                RecyclerView listItemRecycler = (RecyclerView) this.dialogTax.findViewById(R.id.listItemRecycler);
                TaxListAdapter adapter2 = new TaxListAdapter(this, this.taxReceivedList, convert(product.getProductServiceTaxes()));
                listItemRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                listItemRecycler.setAdapter(adapter2);
                addNewItem.setText("Add new tax(+)");
                addNewItem.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CommonInvoiceActivity.this.openAddTaxItemDialogue();
                        CommonInvoiceActivity.this.dialogTax.dismiss();
                    }
                });
                this.dialogTax.show();
            }
        } catch (Exception e) {
        }
    }

    public void addTaxTopup(TaxResponse taxResponse) {
        try {
            this.dialogTax.dismiss();
            List<TaxResponse> taxListTmp = new ArrayList<>();
            if (this.taxListMap.get(Integer.valueOf(this.selectedProduct.getId())) != null) {
                taxListTmp = this.taxListMap.get(Integer.valueOf(this.selectedProduct.getId()));
            }
            taxListTmp.add(taxResponse);
            this.taxListMap.put(Integer.valueOf(this.selectedProduct.getId()), taxListTmp);
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            setListViewHeight(this.productExpandable, 0, true);
            updateTax();
        } catch (Exception e) {
        }
    }

    public void deleteProduct(int groupPosition2) {
        try {
            int i = groupPosition2;
            Product pduct = this.itemsList.get(i);
            this.itemsList.remove(i);
            this.addEstimationWithTaxExpandable.notifyData(this.itemsList);
            this.taxListMap.remove(Integer.valueOf(pduct.getId()));
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            float totalCost = 0.0f;
            for (Product psti : this.itemsList) {
                int i2 = 0;
                if (this.taxListMap.get(Integer.valueOf(psti.getId())) != null) {
                    float tax = 0.0f;
                    for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti.getId()))) {
                        double d = (double) totalCost;
                        double intValue = (double) (this.quantityMap.get(Integer.valueOf(pduct.getId())).intValue() * taxResp.getEffectiveTaxes().get(i2).getRate());
                        double price = psti.getPrice();
                        Double.isNaN(intValue);
                        Double.isNaN(d);
                        totalCost = (float) (d + ((intValue * price) / 100.0d));
                        tax += (float) taxResp.getEffectiveTaxes().get(i2).getRate();
                        List<TaxSummaryList> list = this.taxSummaryList;
                        String name = taxResp.getName();
                        double intValue2 = (double) (this.quantityMap.get(Integer.valueOf(pduct.getId())).intValue() * taxResp.getEffectiveTaxes().get(i2).getRate());
                        double price2 = psti.getPrice();
                        Double.isNaN(intValue2);
                        list.add(new TaxSummaryList(name, (intValue2 * price2) / 100.0d, taxResp.getId()));
                        i2 = 0;
                    }
                } else {
                    double d2 = (double) totalCost;
                    Double.isNaN(d2);
                    totalCost = (float) (d2 + ((psti.getProductServiceTaxes().get(0).getRate() * psti.getPrice()) / 100.0d));
                    this.taxSummaryList.add(new TaxSummaryList(psti.getName(), (psti.getPrice() * psti.getPrice()) / 100.0d, psti.getId()));
                }
            }
            updateTotals(this.taxSummaryList, totalCost);
        } catch (Exception e) {
        }
    }

    public void edit(int index, final List<Product> product) {
        try {
            this.editproduct = product.get(index);
            Dialog dialog = new Dialog(this);
            this.dialogueEdit = dialog;
            dialog.requestWindowFeature(1);
            //this.dialogueEdit.setContentView(R.layout.dialogue_edititem);
            this.dialogueEdit.setContentView(R.layout.edit_product_dilog);
            this.dialogueEdit.setCancelable(true);
            this.dialogueEdit.setCanceledOnTouchOutside(true);
            this.dialogueEdit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            final EditText editQuantity = (EditText) this.dialogueEdit.findViewById(R.id.et_quantity);
            final EditText editDesc = (EditText) this.dialogueEdit.findViewById(R.id.et_desc);
            final EditText editPrice = (EditText) this.dialogueEdit.findViewById(R.id.et_price);
            //editQuantity.setText(product.);
            if (!editDesc.getText().toString().equalsIgnoreCase("")) {
                product.get(index).setDescription(editDesc.getText().toString());
            }
            if (!editPrice.getText().toString().equalsIgnoreCase("")) {
                product.get(index).setPrice(Double.parseDouble(editDesc.getText().toString()));
            }
            ((TextView) this.dialogueEdit.findViewById(R.id.btn_apply)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!editQuantity.getText().toString().equalsIgnoreCase("")) {
                        CommonInvoiceActivity.this.editQuantity(editQuantity.getText().toString(), product.get(index));
                        //addEstimationWithTaxExpandable.notifyData(product);
                    }
                    CommonInvoiceActivity.this.dialogueEdit.dismiss();
                }
            });
            this.dialogueEdit.show();
        } catch (Exception e) {
        }
    }

    public void editQuantity(String quant, Product product) {
        try {
            this.quantityMap.put(Integer.valueOf(product.getId()), Integer.valueOf(Integer.parseInt(quant)));
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            updatePrice();
        } catch (Exception e) {
        }
    }

    private void updatePrice() {
        try {
            Double price = 0.0;
            Double price2 = 0.0;
            Double priceWithoutTax = 0.0;
            for (Product psti : this.itemsList) {
                double doubleValue = price.doubleValue();
                double price3 = psti.getPrice();
                double intValue = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                Double.isNaN(intValue);
                price2 = Double.valueOf(doubleValue + (price3 * intValue));
                double doubleValue2 = priceWithoutTax.doubleValue();
                double price4 = psti.getPrice();
                double intValue2 = (double) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
                Double.isNaN(intValue2);
                priceWithoutTax = Double.valueOf(doubleValue2 + (price4 * intValue2));
            }
            this.addEstimationWithTaxExpandable.notifyDataMAp(this.taxListMap, this.quantityMap);
            setListViewHeight(this.productExpandable, 0, true);
            Map<String, Double> finalTaxMap = new HashMap<>();
            for (Product psti2 : this.itemsList) {
                if (!(this.taxListMap.get(Integer.valueOf(psti2.getId())) == null || this.taxListMap.get(Integer.valueOf(psti2.getId())) == null)) {
                    float tax = 0.0f;
                    for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti2.getId()))) {
                        tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                        if (finalTaxMap.get(taxResp.getName()) != null) {
                            double intValue3 = (double) this.quantityMap.get(Integer.valueOf(psti2.getId())).intValue();
                            double doubleValue3 = finalTaxMap.get(taxResp.getName()).doubleValue();
                            Double.isNaN(intValue3);
                            double d = (double) tax;
                            double price5 = psti2.getPrice();
                            Double.isNaN(d);
                            Double totalTax = Double.valueOf((intValue3 * doubleValue3) + ((d * price5) / 100.0d));
                            price = Double.valueOf(price.doubleValue() + totalTax.doubleValue());
                            finalTaxMap.put(taxResp.getName(), totalTax);
                        } else {
                            double intValue4 = (double) this.quantityMap.get(Integer.valueOf(psti2.getId())).intValue();
                            double d2 = (double) tax;
                            double price6 = psti2.getPrice();
                            Double.isNaN(d2);
                            Double.isNaN(intValue4);
                            float taxTmp = (float) ((intValue4 * (d2 * price6)) / 100.0d);
                            double doubleValue4 = price.doubleValue();
                            double d3 = (double) taxTmp;
                            Double.isNaN(d3);
                            price = Double.valueOf(doubleValue4 + d3);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf((double) taxTmp));
                        }
                    }
                }
            }
            showAddedTax(finalTaxMap, priceWithoutTax.doubleValue());
        } catch (Exception e) {
        }
    }

    private void updateWithQuantity() {
        double price = Utils.DOUBLE_EPSILON;
        double priceWithoutTax = Utils.DOUBLE_EPSILON;
        for (Product psti : this.itemsList) {
            int qunntityTmp = 1;
            if (this.quantityMap.get(Integer.valueOf(psti.getId())) != null) {
                qunntityTmp = this.quantityMap.get(Integer.valueOf(psti.getId())).intValue();
            }
            if (psti.getProductServiceTaxes() == null || psti.getProductServiceTaxes().size() <= 0) {
                double price2 = psti.getPrice();
                double d = (double) qunntityTmp;
                Double.isNaN(d);
                price += price2 * d;
            } else {
                for (ProductServiceTaxesItem productServiceTaxesItem : psti.getProductServiceTaxes()) {
                    double rate = ((productServiceTaxesItem.getRate() * psti.getPrice()) / 100.0d) + psti.getPrice();
                    double d2 = (double) qunntityTmp;
                    Double.isNaN(d2);
                    price += rate * d2;
                }
                this.taxListMap.put(Integer.valueOf(psti.getId()), getItemBasedOnTaxresponse(psti.getProductServiceTaxes()));
            }
            double price3 = psti.getPrice();
            double d3 = (double) qunntityTmp;
            Double.isNaN(d3);
            priceWithoutTax += price3 * d3;
        }
        TextView textView = this.subTotalTv;
        textView.setText(this.selectdCurrency + priceWithoutTax);
        TextView textView2 = this.totalAmountTv;
        textView2.setText(this.selectdCurrency + price);
        calculateTaxAfterDelete();
    }

    public void calculateTaxAfterDelete() {
        try {
            Double priceWithoutTax = Double.valueOf(Utils.DOUBLE_EPSILON);
            Map<String, Double> finalTaxMap = new HashMap<>();
            for (Product psti : this.itemsList) {
                if (this.taxListMap.get(Integer.valueOf(psti.getId())) != null) {
                    float tax = 0.0f;
                    for (TaxResponse taxResp : this.taxListMap.get(Integer.valueOf(psti.getId()))) {
                        tax += (float) taxResp.getEffectiveTaxes().get(0).getRate();
                        if (finalTaxMap.get(taxResp.getName()) != null) {
                            double doubleValue = finalTaxMap.get(taxResp.getName()).doubleValue();
                            double intValue = (double) (((float) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue()) * tax);
                            double price = psti.getPrice();
                            Double.isNaN(intValue);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf(doubleValue + ((intValue * price) / 100.0d)));
                        } else {
                            double intValue2 = (double) (((float) this.quantityMap.get(Integer.valueOf(psti.getId())).intValue()) * tax);
                            double price2 = psti.getPrice();
                            Double.isNaN(intValue2);
                            finalTaxMap.put(taxResp.getName(), Double.valueOf((double) ((float) ((intValue2 * price2) / 100.0d))));
                        }
                    }
                }
                priceWithoutTax = Double.valueOf(priceWithoutTax.doubleValue() + psti.getPrice());
            }
            showAddedTax(finalTaxMap, priceWithoutTax.doubleValue());
        } catch (Exception e) {
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

    public void showAddedTax(Map<String, Double> finalTaxMap, double priceWithoutTax) {
        try {
            String taxBreakDown = "";
            double totalTax = Utils.DOUBLE_EPSILON;
            new DecimalFormat("#.##");
            this.taxSummaryList.clear();
            for (Map.Entry<String, Double> entry : finalTaxMap.entrySet()) {
                String finalAmount = String.format("%.2f", new Object[]{entry.getValue()}).replace(",", ".");
                taxBreakDown = taxBreakDown + " <br> " + entry.getKey() + " &nbsp; : &nbsp; " + finalAmount;
                this.taxSummaryList.add(new TaxSummaryList(entry.getKey(), Double.parseDouble(finalAmount)));
                totalTax += Double.parseDouble(finalAmount);
            }
            this.totalAmountTv.setText(this.selectdCurrency + (priceWithoutTax + totalTax));
            this.subTotalTv.setText(this.selectdCurrency + priceWithoutTax);
            this.invoiceAmount.setText(this.selectdCurrency + (priceWithoutTax + totalTax));
            this.rITaxSummaryItemsAdapter.notifyDataChange(this.taxSummaryList, this.selectedExchangeCurrency);
        } catch (Exception e) {
        }
    }

    private List<String> isValidData() {
        List<String> dataList = new ArrayList<>();
        try {
            if (!this.selectedActivityType.equals(Type.RECURRING_INVOICES) && this.paymentDueDateTextView.getText().toString().length() == 0) {
                if (this.selectedActivityType.equals(Type.ESTIMATES)) {
                    dataList.add(" -- Enter valid expiry date.");
                } else if (this.selectedActivityType.equals(Type.INVOICES)) {
                    dataList.add(" -- Enter valid payment due date");
                } else if (this.selectedActivityType.equals(Type.RECURRING_INVOICES)) {
                    dataList.add(" -- Enter valid recurring invoices expiry date.");
                }
            }
            if (this.selectedCustomer == null) {
                dataList.add(" -- Please select a customer");
            }
            if (this.itemsList.size() == 0) {
                dataList.add(" -- Please Add at least one item.");
            }
        } catch (Exception e) {
        }
        return dataList;
    }

    private void showErrorDialogue(List<String> invalidList) {
        Dialog dialoginvalid = new Dialog(this);
        dialoginvalid.requestWindowFeature(1);
        dialoginvalid.setContentView(R.layout.dialogue_validerror);
        dialoginvalid.setCancelable(true);
        dialoginvalid.setCanceledOnTouchOutside(true);
        TextView textView = (TextView) dialoginvalid.findViewById(R.id.addNewItem);
        RecyclerView listItemRecycler = (RecyclerView) dialoginvalid.findViewById(R.id.listItemRecycler);
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
}
