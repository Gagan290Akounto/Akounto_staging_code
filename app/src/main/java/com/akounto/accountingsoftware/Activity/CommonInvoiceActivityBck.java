package com.akounto.accountingsoftware.Activity;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.RIAddItemsAdapter;
import com.akounto.accountingsoftware.adapter.RITaxSummaryItemsAdapter;
import com.akounto.accountingsoftware.adapter.TaxListAdapter;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.model.TaxSummaryList;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.CreateRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceTransactionItem;
import com.akounto.accountingsoftware.request.RecurringInvoiceSalesTaxItem;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerOnlyResponse;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.TaxResponseList;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

public class CommonInvoiceActivityBck extends AppCompatActivity implements CommonInvoiceItemUpdate {
    private static final int REQUEST_IMAGE = 100;
    private PowerSpinnerView addItemSpinner;
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
    String displayName;
    Date dueDate;
    String dueDateFormatted;
    Group estimateGroup;
    String estimateName = null;
    TextView et_estimateNo;
    EditText et_invoiceNo;
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
    List<Product> productList = new ArrayList();
    InvoiceDetails receivedData;
    TextView saveButton;
    Type selectedActivityType;
    PurchaseItem selectedPurchaseItem;
    SimpleDateFormat simpleDateFormat;
    double subTotal = Utils.DOUBLE_EPSILON;
    TextView subTotalTv;
    /* access modifiers changed from: private */
    public List<TaxResponse> taxReceivedList = new ArrayList();
    List<TaxSummaryList> taxSummaryList = new ArrayList();
    RecyclerView taxSummaryRecyclerView;
    TextView totalAmountTv;
    TextView tv_customeraddres;
    TextView tv_customeraddres2;
    TextView tv_invoiceDate;
    Uri uri;

    public static Intent buildIntent(Context context, String type) {
        Intent intent = new Intent(context, CommonInvoiceActivityBck.class);
        intent.putExtra("typeEnum", type);
        return intent;
    }

    public static Intent buildIntentWithData(Context context, String type, InvoiceDetails invoiceDetails) {
        Intent intent = new Intent(context, CommonInvoiceActivityBck.class);
        intent.putExtra("typeEnum", type);
        intent.putExtra("invoiceDetails", invoiceDetails);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_invoice);
        this.selectedActivityType = Type.valueOf(getIntent().getStringExtra("typeEnum"));
        this.pageTitle = findViewById(R.id.pageTitle);
        int i = C06867.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
        if (i == 1) {
            this.pageTitle.setText("Create Estimate");
        } else if (i != 2) {
            this.pageTitle.setText("New recurring invoice");
        } else {
            this.pageTitle.setText("Create invoice");
        }
        initUi();
    }

    /* renamed from: com.akounto.android.activity.CommonInvoiceActivityBck$7 */
    static class C06867 {
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
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Date time = Calendar.getInstance().getTime();
        this.invoiceDate = time;
        this.invoiceDateFormatted = this.simpleDateFormat.format(time);
        this.customerSpinner = findViewById(R.id.selectCustomerSpinner);
        this.currencySpinner = findViewById(R.id.currencySpinner);
        this.addItemSpinner = findViewById(R.id.addItemSpinner);
        PowerSpinnerView powerSpinnerView = findViewById(R.id.paymentDueSpinner);
        this.paymentDueSpinner = powerSpinnerView;
        powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CommonInvoiceActivityBck.this.lambda$initUi$0$CommonInvoiceActivityBck(i, (String) obj, i2, (String) obj2);
            }
        });
        TextView textView = findViewById(R.id.paymentDueDatePicker);
        this.paymentDueDateTextView = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CommonInvoiceActivityBck.this.lambda$initUi$2$CommonInvoiceActivityBck(view);
            }
        });
        this.customerNameTv = findViewById(R.id.tv_customerName);
        this.customerEmailTv = findViewById(R.id.tv_customerEmail);
        this.customerPhoneTv = findViewById(R.id.tv_customerPhone);
        this.tv_customeraddres = findViewById(R.id.tv_customeraddres);
        this.tv_customeraddres2 = findViewById(R.id.tv_customeraddres2);
        RecyclerView recyclerView = findViewById(R.id.item_rv);
        this.itemsRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.itemsRecyclerView.setAdapter(new RIAddItemsAdapter(this, this.itemsList, this));
        RecyclerView recyclerView2 = findViewById(R.id.tax_list_rv);
        this.taxSummaryRecyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        this.taxSummaryRecyclerView.setAdapter(new RITaxSummaryItemsAdapter(this, this.taxSummaryList));
        this.subTotalTv = findViewById(R.id.tv_subTotal);
        this.totalAmountTv = findViewById(R.id.tv_totalincoiceamount);
        this.invoiceAmount = findViewById(R.id.tv_invoiceAmount);
        this.invoiceNoTv = findViewById(R.id.invoiceNo);
        this.invoiceDateTv = findViewById(R.id.invoiceDate);
        this.tv_invoiceDate = findViewById(R.id.tv_invoiceDate);
        this.invoiceDueTv = findViewById(R.id.paymentDue);
        this.estimateGroup = findViewById(R.id.estimateGroup);
        this.notesGroup = findViewById(R.id.notesGroup);
        this.et_invoiceNo = findViewById(R.id.et_invoiceNo);
        this.et_estimateNo = findViewById(R.id.et_estimateNo);
        this.notesEt = findViewById(R.id.et_notes);
        int i = C06867.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
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
        TextView textView2 = findViewById(R.id.saveButton);
        this.saveButton = textView2;
        textView2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CommonInvoiceActivityBck.this.lambda$initUi$3$CommonInvoiceActivityBck(view);
            }
        });
        TextView textView3 = findViewById(R.id.cancel_button);
        this.cancelButton = textView3;
        textView3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CommonInvoiceActivityBck.this.lambda$initUi$4$CommonInvoiceActivityBck(view);
            }
        });
        if (getIntent().getSerializableExtra("invoiceDetails") != null) {
            this.isItemClickDisabled = true;
            this.receivedData = (InvoiceDetails) getIntent().getSerializableExtra("invoiceDetails");
            isEditingEnabled(false);
        } else {
            this.isItemClickDisabled = false;
        }
        getCustomerList();
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetchAvailableItems();
        ImageView imageView = findViewById(R.id.logoUpload);
        this.logoUpload = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CommonInvoiceActivityBck.this.lambda$initUi$5$CommonInvoiceActivityBck(view);
            }
        });
        getTaxList();
    }

    public void lambda$initUi$0$CommonInvoiceActivityBck(int i, String s, int selectedIndex, String selectedItem) {
        updateDatesForRequest(selectedIndex);
    }

    public void lambda$initUi$2$CommonInvoiceActivityBck(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                CommonInvoiceActivityBck.this.lambda$null$1$CommonInvoiceActivityBck(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$1$CommonInvoiceActivityBck(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        this.dueDateFormatted = this.simpleDateFormat.format(calendar.getTime());
        TextView textView = this.paymentDueDateTextView;
        textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }

    public void lambda$initUi$3$CommonInvoiceActivityBck(View v) {
        prepareAddData();
    }

    public void lambda$initUi$4$CommonInvoiceActivityBck(View v) {
        onBackPressed();
    }

    public void lambda$initUi$5$CommonInvoiceActivityBck(View v) {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Uri uri2 = data.getData();
            String absolutePath = new File(uri2.toString()).getAbsolutePath();
            Uri uri3 = data.getParcelableExtra("path");
            try {
                MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.logoUpload.setImageURI(uri2);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    }

    private int getItemIndex(int productId) {
        int indexOfProduct = 0;
        for (int i = 0; i < this.productList.size(); i++) {
            if (productId == this.productList.get(i).getId()) {
                indexOfProduct = i;
            }
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
        RestClient.getInstance(this).getCustomerList(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<CustomerResponse>(this, null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    CommonInvoiceActivityBck.this.customerList = response.body().getData();
                    CommonInvoiceActivityBck commonInvoiceActivityBck = CommonInvoiceActivityBck.this;
                    commonInvoiceActivityBck.setUpCustomerSpinner(commonInvoiceActivityBck.customerList);
                }
            }

            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    private void getCustomerOnly(int id) {
        RestClient.getInstance(this).getCustomerListById(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),id).enqueue(new CustomCallBack<CustomerOnlyResponse>(this, null) {
            public void onResponse(Call<CustomerOnlyResponse> call, Response<CustomerOnlyResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    CommonInvoiceActivityBck.this.updateCustomerData(response.body().getData());
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
        ArrayList<String> customers = new ArrayList<>();
        for (Customer customer : data) {
            customers.add(customer.getName());
        }
        this.customerSpinner.setItems(customers);
        this.customerSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CommonInvoiceActivityBck.this.lambda$setUpCustomerSpinner$6$CommonInvoiceActivityBck(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpCustomerSpinner$6$CommonInvoiceActivityBck(int i, String s, int selectedIndex, String selectedItem) {
        updateCustomerDetailsById(this.customerList.get(selectedIndex));
    }

    private void updateCustomerDetailsById(Customer selectedCustomer) {
        getCustomerOnly(selectedCustomer.getHeadTransactionId());
    }

    public void updateCustomerData(Customer selectedCustomer) {
        this.customerNameTv.setText(selectedCustomer.getName());
        TextView textView = this.tv_customeraddres;
        textView.setText(selectedCustomer.getBillAddress1() + "," + selectedCustomer.getBillAddress2());
        TextView textView2 = this.tv_customeraddres2;
        textView2.setText(selectedCustomer.getBillCity() + "," + selectedCustomer.getBillState() + "," + selectedCustomer.getBillPostCode());
        TextView textView3 = this.customerEmailTv;
        StringBuilder sb = new StringBuilder();
        sb.append("Email: ");
        sb.append(selectedCustomer.getEmail());
        textView3.setText(sb.toString());
        TextView textView4 = this.customerPhoneTv;
        textView4.setText("Phone: " + selectedCustomer.getPhone());
        this.customerCurrency = selectedCustomer.getCurrency() != null ? selectedCustomer.getCurrency() : this.customerCurrency;
        this.customerHeadTransactionId = selectedCustomer.getHeadTransactionId();
    }

    private void fetchCurrencies() throws JSONException {
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
    }

    private void setUpCurrencySpinner(List<String> currencyListForSpinner2) {
        this.currencySpinner.setItems(currencyListForSpinner2);
        this.currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                CommonInvoiceActivityBck.this.lambda$setUpCurrencySpinner$7$CommonInvoiceActivityBck(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpCurrencySpinner$7$CommonInvoiceActivityBck(int i, String s, int selectedIndex, String selectedItem) {
        updateCurrency(selectedItem);
    }

    private void updateCurrency(String selectedItem) {
    }

    private void fetchAvailableItems() {
        RestClient.getInstance(this).getSalesProductList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<SalesProductResponse>(this, null) {
            public void onResponse(Call<SalesProductResponse> call, Response<SalesProductResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CommonInvoiceActivityBck.this.productList = response.body().getData();
                    CommonInvoiceActivityBck commonInvoiceActivityBck = CommonInvoiceActivityBck.this;
                    commonInvoiceActivityBck.setUpProductItems(commonInvoiceActivityBck.productList);
                    if (CommonInvoiceActivityBck.this.isItemClickDisabled) {
                        CommonInvoiceActivityBck commonInvoiceActivityBck2 = CommonInvoiceActivityBck.this;
                        commonInvoiceActivityBck2.initUiWithReceivedData(commonInvoiceActivityBck2.receivedData);
                    }
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
                CommonInvoiceActivityBck.this.lambda$setUpProductItems$8$CommonInvoiceActivityBck(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setUpProductItems$8$CommonInvoiceActivityBck(int i, String s, int selectedIndex, String selectedItem) {
        updateProductsListRecyclerView(selectedIndex);
    }

    private void updateProductsListRecyclerView(int selectedIndex) {
        this.addItemSpinner.setText("Add an item");
        this.itemsList.add(this.productList.get(selectedIndex));
        this.subTotal += this.productList.get(selectedIndex).getPrice();
        prepareTaxList(this.productList.get(selectedIndex).getPrice(), this.productList.get(selectedIndex).getProductServiceTaxes());
        this.itemsRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void prepareTaxList(double productPrice, List<ProductServiceTaxesItem> productServiceTaxes) {
        for (ProductServiceTaxesItem psti : productServiceTaxes) {
            this.taxSummaryList.add(new TaxSummaryList(psti.getTaxName(), (psti.getRate() * productPrice) / 100.0d, psti.getId()));
        }
        updateTotals(this.taxSummaryList);
    }

    private void updateTotals(List<TaxSummaryList> lists) {
        this.taxSummaryRecyclerView.getAdapter().notifyDataSetChanged();
        double totalTaxAmount = Utils.DOUBLE_EPSILON;
        for (TaxSummaryList taxSummaryList2 : lists) {
            totalTaxAmount += taxSummaryList2.getTaxValue();
        }
        this.subTotalTv.setText(String.valueOf(this.subTotal));
        String finalAmount = new DecimalFormat("0.00").format(this.subTotal + totalTaxAmount).replace(",", ".");
        this.totalAmountTv.setText(finalAmount);
        this.invoiceAmount.setText(finalAmount);
    }

    public void deleteItem(Product product) {
        if (!this.isItemClickDisabled) {
            this.subTotal -= product.getPrice();
            removeItemsFromTaxList(product);
            this.itemsList.remove(product);
            this.itemsRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void removeItemsFromTaxList(Product listToRemove) {
        for (ProductServiceTaxesItem psti : listToRemove.getProductServiceTaxes()) {
            Iterator<TaxSummaryList> iterator = this.taxSummaryList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() == psti.getId()) {
                    iterator.remove();
                }
            }
        }
        updateTotals(this.taxSummaryList);
    }

    private void prepareAddData() {
        if (this.selectedActivityType == Type.ESTIMATES) {
            this.poNumber = this.et_invoiceNo.getText().toString().trim();
            this.estimateName = "Estimate";
        }
        List<RecurringInvoiceSalesTaxItem> recurringInvoiceSalesTaxItems = new ArrayList<>();
        List<InvoiceTransactionItem> invoiceTransactionItems = new ArrayList<>();
        for (Product product : this.itemsList) {
            for (ProductServiceTaxesItem productServiceTaxesItem : product.getProductServiceTaxes()) {
                recurringInvoiceSalesTaxItems.add(new RecurringInvoiceSalesTaxItem((int) productServiceTaxesItem.getRate(), productServiceTaxesItem.getTaxId(), productServiceTaxesItem.getTaxName()));
            }
            invoiceTransactionItems.add(new InvoiceTransactionItem(product.getDescription(), product.getName(), recurringInvoiceSalesTaxItems, (int) product.getPrice(), 1, product.getId(), product.getIncomeAccountId()));
        }
        CreateRecurringInvoiceRequest createRecurringInvoiceRequest = new CreateRecurringInvoiceRequest(1, 1, this.customerHeadTransactionId, this.paymentDueDays, this.dueDateFormatted, this.invoiceDateFormatted, invoiceTransactionItems, this.customerCurrency, this.notesEt.getText().toString().trim(), this.poNumber, this.estimateName);
        int i = C06867.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
        if (i == 1) {
            addInvoice("estimate", createRecurringInvoiceRequest);
        } else if (i != 2) {
            addInvoice("recurring", createRecurringInvoiceRequest);
        } else {
            addInvoice("invoice", createRecurringInvoiceRequest);
        }
    }

    private void addInvoice(final String invoice_type, CreateRecurringInvoiceRequest createRecurringInvoiceRequest) {
       /* RestClient.getInstance(this).createInvoice(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),invoice_type, createRecurringInvoiceRequest).enqueue(new CustomCallBack<ResponseBody>(this, "Creating invoice...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CommonInvoiceActivityBck commonInvoiceActivityBck = CommonInvoiceActivityBck.this;
                    UiUtil.showToast(commonInvoiceActivityBck, invoice_type + " Added");
                    CommonInvoiceActivityBck.this.finish();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    private void getTaxList() {
        RestClient.getInstance(this).getTaxList(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<TaxResponseList>(this, null) {
            public void onResponse(Call<TaxResponseList> call, Response<TaxResponseList> response) {
                super.onResponse(call, response);
                Log.d("getTaxList----", new Gson().toJson(response.body().getData()));
                if (response.isSuccessful()) {
                    List unused = CommonInvoiceActivityBck.this.taxReceivedList = response.body().getData();
                }
            }

            public void onFailure(Call<TaxResponseList> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void addTax() {
        if (this.taxReceivedList.size() != 0) {
            Dialog dialog = new Dialog(this);
            this.dialogTax = dialog;
            dialog.requestWindowFeature(1);
            this.dialogTax.setContentView(R.layout.dialogue_add_bill_item);
            this.dialogTax.setCancelable(true);
            this.dialogTax.setCanceledOnTouchOutside(true);
            this.dialogTax.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RecyclerView listItemRecycler = this.dialogTax.findViewById(R.id.listItemRecycler);
            TaxListAdapter adapter2 = new TaxListAdapter(this, this.taxReceivedList, null);
            listItemRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
            listItemRecycler.setAdapter(adapter2);
            this.dialogTax.findViewById(R.id.addNewItem).setVisibility(View.GONE);
            this.dialogTax.findViewById(R.id.addNewItem).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CommonInvoiceActivityBck.this.dialogTax.dismiss();
                }
            });
            this.dialogTax.show();
        }
    }
}
