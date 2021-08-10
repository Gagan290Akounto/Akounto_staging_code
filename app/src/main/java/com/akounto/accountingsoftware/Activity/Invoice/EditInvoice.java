package com.akounto.accountingsoftware.Activity.Invoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.Activity.DashboardActivity;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.adapter.ItemAdapter;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.InvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.ItemWithId;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.ItemWithoutId;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.TaxWithoutId;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.InvoiceNumberResponse;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ReportSetting.Data;
import com.akounto.accountingsoftware.response.ReportSetting.ReportSettings;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.response.currency.CurrencyResponse;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.PriceCal;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInvoice extends AppCompatActivity {
    List<Customer> customerList = new ArrayList();
    private Customer cust;
    private ReportSettings settings;
    private String isoDatePattern = "yyyy-MM-dd";
    private TextView in_date, tv_name_char, comp_name, tv_name;
    private SimpleDateFormat simpleDateFormat;
    private ImageView iv_due_date;
    private TextView et_invoiceNo, compDesc, due_date, subtotal, tax_total, grant_total, tv_note;
    private LinearLayout add_customer, btn_cust, id_cust, add_items;
    private int mDay;
    private int mMonth;
    private int mYear;
    double exgRate;
    InvoiceDetails receivedData;
    boolean isItemClickDisabled = false;
    int customerHeadTransactionId = 0;
    int paymentDueDays = 0;
    String dueDateFormatted;
    String estimateName = null;
    com.akounto.accountingsoftware.response.currency.Currency selectedExchangeCurrency;
    com.akounto.accountingsoftware.response.currency.Currency selectedBussinessCurrency;
    String selectdCurrencyCode;
    String poNumber = null;
    Type selectedActivityType;
    private Context mContext;
    private List<Product> items = new ArrayList<>();
    private RecyclerView rc;
    int position;
    public static Product edit_item = null;
    private List<Currency> currencyList = new ArrayList();
    private List<String> currencyListForSpinner = new ArrayList();
    private List<String> currencyListForDisplay = new ArrayList();
    private String selectedCurrencyId = "$";
    private LinearLayout taxs_name_list, taxs_amount_list;
    private PriceCal priceCal;
    Spinner currencySpinner;

    public static Intent buildIntent(Context context, String type) {
        Intent intent = new Intent(context, EditInvoice.class);
        intent.putExtra("typeEnum", type);
        return intent;
    }

    public static Intent buildIntentWithData(Context context, String type, InvoiceDetails invoiceDetails) {
        Intent intent = new Intent(context, EditInvoice.class);
        intent.putExtra("typeEnum", type);
        intent.putExtra("invoiceDetails", invoiceDetails);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_invoice);
        if (getIntent().getSerializableExtra("invoiceDetails") != null) {
            this.isItemClickDisabled = true;
            this.receivedData = (InvoiceDetails) getIntent().getSerializableExtra("invoiceDetails");
            Gson gson = new Gson();
            items = new ArrayList<>();
            try {
                for (int k = 0; k < receivedData.getInvoiceTransaction().size(); k++) {
                    Product temp = gson.fromJson(gson.toJson(receivedData.getInvoiceTransaction().get(k)), Product.class);
                    try {
                        temp.setProductServiceTaxes(UiUtil.trasforme(temp.getTaxes()));
                    } catch (Exception e) {
                    }
                    items.add(temp);
                }
            } catch (Exception e) {
                Log.e("Error :: ", e.toString());
            }
        }
        //UiUtil.getcurancy(receivedData.getCustCurrency(),currencyList);
        EditInvoice.this.selectedExchangeCurrency = null;
        EditInvoice.this.selectedBussinessCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        EditInvoice.this.selectdCurrencyCode = UiUtil.getBussinessCurren(getApplicationContext());
        et_invoiceNo = findViewById(R.id.et_estimateNo);
        compDesc = findViewById(R.id.comp_desc);
        add_customer = findViewById(R.id.add_customer);
        btn_cust = findViewById(R.id.btn_cust);
        id_cust = findViewById(R.id.id_customer);
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
        iv_due_date = findViewById(R.id.due_date);
        rc = findViewById(R.id.rc_customer);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        compDesc.setText(UiUtil.getUserName(getApplicationContext()));
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Calendar c = Calendar.getInstance();
        in_date.setText(simpleDateFormat.format(c.getTime()));
        mContext = this;
        iv_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDate(v);
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditInvoice.this, ItemList.class), 3);
            }
        });
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditInvoice.this, CustomerList.class), 2);
            }
        });
        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareAddData();
            }
        });
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        selectedCurrencyId = UiUtil.getBussinessCurrenSymbul(getApplicationContext());
        try {
            this.selectedActivityType = Type.valueOf("invoice");
            int i = EditInvoice.C067113.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
        } catch (Exception e) {
        }
        setdata();
    }

    public void setdata() {
        try {
            et_invoiceNo.setText(receivedData.getInvoiceNoPS());
            cust = receivedData.getCustomer();
            tv_note.setText(receivedData.getNotes());
            due_date.setText(receivedData.getPaymentDue().split("T")[0]);
            in_date.setText(receivedData.getInvoiceDate().split("T")[0]);
        } catch (Exception e) {
        }
        setAdapter();
        setCustomer();
    }

    public void setCustomer() {
        try {
            if (cust != null) {
                id_cust.setVisibility(View.VISIBLE);
                btn_cust.setVisibility(View.GONE);
                tv_name_char.setText("" + cust.getName().charAt(0));
                comp_name.setText(cust.getName());
                tv_name.setText(cust.getEmail());
            } else {
                id_cust.setVisibility(View.GONE);
                btn_cust.setVisibility(View.VISIBLE);
            }
            this.customerHeadTransactionId = cust.getHeadTransactionId();
        } catch (Exception e) {
        }
    }

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

    public void initUiDate(View v) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                EditInvoice.this.DatePopup(datePicker, i, i2, i3);
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

    public void openInvoiceSetting() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                EditInvoice.this.finish();
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
                            getInvoiceNo();
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

    private void getCustomerList() {
        RestClient.getInstance((Activity) this).getCustomerList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<CustomerResponse>(this, (String) null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                try {
                    Log.d("CustomerResponse---", response.toString());
                    if (response.isSuccessful()) {
                        EditInvoice.this.customerList = response.body().getData();
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

    private void getInvoiceNo() {
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        Api api = ApiUtils.getAPIService();
        RestClient.getInstance(getApplicationContext()).getInvoiceNo(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new Callback<InvoiceNumberResponse>() {
            public void onResponse(Call<InvoiceNumberResponse> call, Response<InvoiceNumberResponse> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    if (response.isSuccessful()) {
                        et_invoiceNo.setText(response.body().getData());
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<InvoiceNumberResponse> call, Throwable t) {
                Log.d("error", t.toString());
                UiUtil.cancelProgressDialogue();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAdapter() {
        if (items != null) {
            displayPrice();
            rc.removeAllViews();
            rc.setAdapter(new ItemAdapter(items, selectedCurrencyId, new ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product item, int p) {
                    position = p;
                    CreateInvoice.edit_item = item;
                    startActivityForResult(new Intent(EditInvoice.this, EditItem.class), 5);
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
            subtotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getSub_totel()));
            tax_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            grant_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            display_taxs();
        } catch (Exception e) {
            Log.e("Exc :: ", e.toString());
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

    private List<String> isValidData() {
        List<String> dataList = new ArrayList<>();
        try {
            try {
                if (this.due_date.getText().toString().length() == 0) {
                    dataList.add(" -- Enter valid payment due date");
                }
            } catch (Exception e) {
            }
            if (cust == null) {
                dataList.add(" -- Please select a customer");
            }
            if (items.size() == 0) {
                dataList.add(" -- Please Add at least one item.");
            }
        } catch (Exception e) {
        }
        return dataList;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                if (this.due_date.getText().toString().length() == 0) {
                    UiUtil.showToast(this, "Please enter proper invoice expiry date");
                    return;
                }
            }
            ArrayList<Object> arrayList = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Object rist = null;
                List<Object> recurringInvoiceSalesTaxItems = null;
                recurringInvoiceSalesTaxItems = new ArrayList<>();
                try {
                    for (int j = 0; j < items.get(i).getProductServiceTaxes().size(); j++) {
                  /*  if (items.get(i).getProductServiceTaxes().get(j).getId() != 0) {
                        rist = new TaxWithId(items.get(i).getProductServiceTaxes().get(j).getId(), (int) items.get(i).getProductServiceTaxes().get(j).getRate(), items.get(i).getProductServiceTaxes().get(j).getTaxId(), items.get(i).getProductServiceTaxes().get(j).getTaxName());
                        recurringInvoiceSalesTaxItems.add(rist);
                    } else {*/
                        rist = new TaxWithoutId((int) items.get(i).getProductServiceTaxes().get(j).getRate(), items.get(i).getProductServiceTaxes().get(j).getTaxId(), items.get(i).getProductServiceTaxes().get(j).getTaxName());
                        recurringInvoiceSalesTaxItems.add(rist);
                        // }
                    }
                } catch (Exception e) {
                }
                if (items.get(i).getId() == 0 || items.get(i).getId() == items.get(i).getProductId()) {
                    arrayList.add(new ItemWithoutId(items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), items.get(i).getIncomeAccountId(), recurringInvoiceSalesTaxItems));
                } else {
                    arrayList.add(new ItemWithId(items.get(i).getId(), items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), Integer.parseInt(items.get(i).getProductTransactionHeadId()), recurringInvoiceSalesTaxItems));
                }
            }
            InvoiceRequest createRecurringInvoiceRequest = new InvoiceRequest(receivedData.getId(), receivedData.getInvoiceNo(), exgRate, (int) Math.round(EditInvoice.this.selectedBussinessCurrency.getExchangeRate()), this.customerHeadTransactionId, calculatedays(in_date.getText().toString(), this.due_date.getText().toString()), this.due_date.getText().toString(), this.in_date.getText().toString(), arrayList, this.selectedExchangeCurrency.getCode(), this.tv_note.getText().toString().trim(), receivedData.getPoNumber(), this.estimateName, receivedData.getInvoiceNoPS());
            addInvoice("invoice", createRecurringInvoiceRequest);
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
            Toast.makeText(mContext, "Something wrong in data", Toast.LENGTH_SHORT).show();
        }
    }

    public int calculatedays(String cureent, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long days = 0;
        try {
            Date start = sdf.parse(cureent);
            Date enddate = sdf.parse(end);
            long diff = enddate.getTime() - start.getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
        }
        return (int) days;
    }

    private void getExchangedCurrency(String id) {
       /* UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        RestClient.getInstance(getApplicationContext()).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(getApplicationContext(),"") {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(response);
                try {
                    if (response.isSuccessful()) {
                        CreateInvoice.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        CreateInvoice commonInvoiceActivity = CreateInvoice.this;
                        commonInvoiceActivity.selectedCurrencyId = commonInvoiceActivity.selectedExchangeCurrency.getSymbol();
                    }
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
            }
        });*/
        RestClient.getInstance((Activity) this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, (String) null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson((Object) response.body().getData()));
                        selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        selectedBussinessCurrency = response.body().getData().getBusinessCurrency();
                        EditInvoice commonInvoiceActivity = EditInvoice.this;
                        exgRate = response.body().getData().getBusinessCurrency().getExchangeRate() / response.body().getData().getExchangeCurrency().getExchangeRate();
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

    private void addInvoice(final String invoice_type, InvoiceRequest createRecurringInvoiceRequest) {
        RestClient.getInstance((Activity) this).UpdateInvoice(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), invoice_type, createRecurringInvoiceRequest).enqueue(new CustomCallBack<GetChartResponse>(this, "Creating invoice...") {
            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        Log.d("invoice_type", new Gson().toJson((Object) response.body()));
                        EditInvoice EditInvoice = EditInvoice.this;
                        UiUtil.showToast(EditInvoice, invoice_type + " Updated");
                        ViewInvoice.guid=receivedData.getGuid();
                        finish();
                        return;
                    } else {
                        UiUtil.showToast(EditInvoice.this, ((response.body().getTransactionStatus().getError())).getDescription());
                    }
                    EditInvoice commonInvoiceActivity2 = EditInvoice.this;
                    UiUtil.showToast(commonInvoiceActivity2, invoice_type + " Error while adding");
                } catch (Exception e) {
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            currencyListForDisplay.add(jsonObject.getString("Name") + " ( " + jsonObject.getString("Id") + " ) ");
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(currencyListForSpinner, this.currencyListForDisplay);
    }

    private void setCurrencySpinner(List<String> currencies, List<String> display) {
        Spinner currencySpinner = findViewById(R.id.currencySpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, display);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(dataAdapter);
        currencySpinner.setSelection(UiUtil.getcurancyindex(receivedData.getCustCurrency(), currencyList));
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

    private void getReportSetting() {
        RestClient.getInstance(this).getReportSetting(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<ReportSettings>(this, null) {
            public void onResponse(Call<ReportSettings> call, Response<ReportSettings> response) {
                super.onResponse(call, response);
                try {
                    settings = response.body();
                    if (settings.getTransactionStatus().getIsSuccess()) {
                        if (settings.getData().getInvoiceInitialNo() != 0) {
                            getInvoiceNo();

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == 2) {
            try {
                if (CustomerList.result != null) {
                    id_cust.setVisibility(View.VISIBLE);
                    btn_cust.setVisibility(View.GONE);
                    tv_name_char.setText("" + CustomerList.result.getName().charAt(0));
                    comp_name.setText(CustomerList.result.getName());
                    tv_name.setText(CustomerList.result.getEmail());
                } else {
                    id_cust.setVisibility(View.GONE);
                    btn_cust.setVisibility(View.VISIBLE);
                }
                this.customerHeadTransactionId = CustomerList.result.getHeadTransactionId();
            } catch (Exception e) {
            }
        } else
        if (resultCode != 0) {
            if (requestCode == 3) {
                try {
                    if (ItemList.result != null) {
                        items.add(ItemList.result);
                        setAdapter();
                    }
                } catch (Exception e) {
                }
            } else if (requestCode == 4) {
                try {
                    items.remove(position);
                    items.add(ItemList.adds_item);
                    setAdapter();
                } catch (Exception e) {
                }
            } else if (requestCode == 5) {
                if (resultCode == 6) {
                    try {
                        items.remove(position);
                        setAdapter();
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        items.remove(position);
                        items.add(CreateInvoice.edit_item);
                        setAdapter();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}