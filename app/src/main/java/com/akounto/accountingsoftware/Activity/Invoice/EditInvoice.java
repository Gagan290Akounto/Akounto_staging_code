package com.akounto.accountingsoftware.Activity.Invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.InvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.ItemWithId;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.ItemWithoutId;
import com.akounto.accountingsoftware.request.InvoiceUpdateRequest.TaxWithoutId;
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.util.DecimalDigitsInputFilter;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.Activity.DashboardActivity;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.InvalidAdapter;
import com.akounto.accountingsoftware.adapter.ItemAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.CreateRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.InvoiceTransactionItem;
import com.akounto.accountingsoftware.request.RecurringInvoiceSalesTaxItem;
import com.akounto.accountingsoftware.response.InvoiceNumberResponse;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ReportSetting.Data;
import com.akounto.accountingsoftware.response.ReportSetting.ReportSettings;
import com.akounto.accountingsoftware.model.Currency;
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
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInvoice extends AppCompatActivity {

    private TextView discount_lb, title;
    private ReportSettings settings;
    private String isoDatePattern = "yyyy-MM-dd";
    private TextView in_date, tv_name_char, comp_name, tv_name;
    private SimpleDateFormat simpleDateFormat;
    private ImageView iv_due_date;
    private Customer cust;
    private EditText discount;
    private Spinner discount_spi;
    Button apply_discount;
    double d_value = 0.0, total = 0.0;
    ImageView delete_discount, edit_discount;
    int discount_index = 0;
    String[] discount_type_data = {"Please select type", "Percent discount", "Flat discount"};
    private TextView et_invoiceNo, compDesc, due_date, subtotal, tax_total, grant_total, tv_note, tv_discount;
    private LinearLayout add_customer, btn_cust, id_cust, add_items, discount_ll, discount_edit_ll, add_discount;
    private int mDay;
    private int mMonth;
    private int mYear;
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
    TextView discount_error;
    private List<Product> items = new ArrayList<>();
    private List<Product> cur_items = new ArrayList<>();
    private RecyclerView rc;
    int position;
    double exgRate = 0.0;
    String cust_curancy = "USD";
    public static Product edit_item = null;
    private List<Currency> currencyList = new ArrayList();
    private List<String> currencyListForSpinner = new ArrayList();
    private List<String> currencyListForDisplay = new ArrayList();
    private String selectedCurrencyId = "$";
    private LinearLayout taxs_name_list, taxs_amount_list;
    InvoiceDetails receivedData;
    boolean isItemClickDisabled = false, cur_start = false;
    public static boolean start = false;

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
        setContentView(R.layout.layount_create_invoice);
        mContext = this;
        CustomerList.result = null;
        discount = findViewById(R.id.discount);
        title = findViewById(R.id.title);
        title.setText("Edit Invoice");
        discount_error = findViewById(R.id.discount_error);
        discount_spi = findViewById(R.id.discount_type);
        discount_ll = findViewById(R.id.discount_ll);
        discount_edit_ll = findViewById(R.id.discount_edit_ll);
        tv_discount = findViewById(R.id.tv_discount);
        delete_discount = findViewById(R.id.delete_discount);
        edit_discount = findViewById(R.id.edit_discount);
        et_invoiceNo = findViewById(R.id.et_estimateNo);
        compDesc = findViewById(R.id.comp_desc);
        add_customer = findViewById(R.id.add_customer);
        add_discount = findViewById(R.id.add_discount);
        apply_discount = findViewById(R.id.apply_discount);
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
        setDiscountAdp(discount_type_data);
        delete_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount_ll.setVisibility(View.GONE);
                discount_edit_ll.setVisibility(View.GONE);
                add_discount.setVisibility(View.VISIBLE);
                discount.setText("");
                discount_index = 0;
                displayPrice(UiUtil.reset_list(items));
            }
        });
        add_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() != 0) {
                    if (discount_ll.getVisibility() == View.VISIBLE) {
                        discount_ll.setVisibility(View.GONE);
                    } else {
                        discount_ll.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(mContext, "No discount applicable.", Toast.LENGTH_LONG).show();
                }
            }
        });
        discount_spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discount_error.setVisibility(View.GONE);
                if (position == 1) {
                    discount.setVisibility(View.VISIBLE);
                    apply_discount.setVisibility(View.VISIBLE);
                    discount.setText("");
                    discount.setHint("Percent discount");
                    discount_index = 1;
                } else if (position == 2) {
                    discount.setVisibility(View.VISIBLE);
                    apply_discount.setVisibility(View.VISIBLE);
                    discount.setText("");
                    discount.setHint("Flat discount");
                    discount_index = 2;
                } else {
                    discount.setVisibility(View.GONE);
                    apply_discount.setVisibility(View.GONE);
                    discount.setText("");
                    discount.setHint("discount");
                    discount_index = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        apply_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDiscount()) {
                    if (!discount.getText().toString().isEmpty()) {
                        if (discount_spi.getSelectedItem().toString().equalsIgnoreCase("Flat discount")) {
                            discount_index = 1;
                        } else {
                            discount_index = 2;
                        }
                        displayPrice(UiUtil.reset_list(items));
                        PriceCal cal = new PriceCal(items);
                        if (discount.getHint().toString().equalsIgnoreCase("Flat discount")) {
                            double final_val = 0;
                            try {
                                final_val = Double.parseDouble(subtotal.getText().toString().split(" ")[1]) - cal.getDiscount();
                            } catch (Exception e) {
                            }
                            if (final_val >= Double.parseDouble(discount.getText().toString())) {
                                if (items.size() == 1) {
                                    try {
                                        d_value = Double.parseDouble(discount.getText().toString());
                                    } catch (Exception e) {
                                    }
                                    discount_edit_ll.setVisibility(View.VISIBLE);
                                    discount_ll.setVisibility(View.GONE);
                                    add_discount.setVisibility(View.GONE);
                                    discount_error.setVisibility(View.GONE);
                                    tv_discount.setText("Applied discount at invoice: " + UiUtil.getBussinessCurrenSymbul(mContext) + " " + discount.getText().toString());
                                    displayPrice(UiUtil.priceAfterDiscount(items, 2, Double.parseDouble(discount.getText().toString())));
                                } else {
                                    discount_error.setText("While applying discount at invoice level in (amount), then invoice can have only one item!");
                                    discount_error.setVisibility(View.VISIBLE);
                                }
                            } else {
                                discount_error.setText("Item discount cannot be applied more than amount!");
                                discount_error.setVisibility(View.VISIBLE);
                            }
                        } else {
                            discount_edit_ll.setVisibility(View.VISIBLE);
                            discount_ll.setVisibility(View.GONE);
                            add_discount.setVisibility(View.GONE);
                            try {
                                d_value = Double.parseDouble(discount.getText().toString());
                            } catch (Exception e) {
                            }
                            tv_discount.setText("Applied discount at invoice: " + discount.getText().toString() + " %");
                            displayPrice(UiUtil.priceAfterDiscount(items, 1, Double.parseDouble(discount.getText().toString())));
                        }

                    } else {
                        discount_error.setText("Item discount cannot be applied more than amount!");
                        discount_error.setVisibility(View.VISIBLE);
                        //Toast.makeText(mContext, "Item discount cannot be applied more than amount!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    discount_error.setVisibility(View.VISIBLE);
                }
            }
        });
        edit_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount_ll.setVisibility(View.VISIBLE);
                discount_edit_ll.setVisibility(View.GONE);
                if (discount_spi.getSelectedItem().toString().equalsIgnoreCase("Flat discount")) {
                    discount_index = 2;
                } else {
                    discount_index = 1;
                }
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUiDate(v);
            }
        });
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditInvoice.this, CustomerList.class), 2);
            }
        });
        discount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(25, 2)});
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                        s.clear();
                    }
                    if (discount.getHint().toString().equalsIgnoreCase("Percent discount")) {
                        discount_error.setText("Item discount cannot be applied more than amount!");
                        if (Double.parseDouble(discount.getText().toString()) > 100) {
                            discount.setText("");
                            discount_error.setVisibility(View.VISIBLE);
                            //Toast.makeText(EditInvoices.this, "Invalid value given as input.", Toast.LENGTH_SHORT).show();
                        } else {
                            discount_error.setVisibility(View.GONE);
                        }
                    } else if (discount.getHint().toString().equalsIgnoreCase("Flat discount")) {
                        discount_error.setText("Item discount cannot be applied more than amount!");
                        double st = 0.0;
                        try {
                            st = Double.parseDouble(subtotal.getText().toString().split(" ")[1]);
                        } catch (Exception e) {
                        }
                        if (Double.parseDouble(discount.getText().toString()) > st) {
                            discount.setText("");
                            discount_error.setVisibility(View.VISIBLE);
                            //Toast.makeText(EditInvoices.this, "Invalid value given as input.", Toast.LENGTH_SHORT).show();
                        } else {
                            discount_error.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditInvoice.this, ItemList.class), 3);
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
            discount_index = receivedData.getDiscountType();
            if (discount_index != 0) {
                d_value = receivedData.getDiscount();
                discount.setText(String.valueOf(d_value));
            } else {
                discount.setText(String.valueOf(0.0));
            }
        }
        getReportSetting();
        selectedCurrencyId = UiUtil.getBussinessCurrenSymbul(getApplicationContext());
        try {
            this.selectedActivityType = Type.valueOf("invoice");
            int i = EditInvoice.C067113.$SwitchMap$com$akounto$android$activity$Type[this.selectedActivityType.ordinal()];
        } catch (Exception e) {
        }
        setdata();
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
                startActivity(new Intent(EditInvoice.this, DashboardActivity.class));
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
                           /* int i = CommonInvoiceActivity.C067113.$SwitchMap$com$akounto$android$activity$Type[selectedActivityType.ordinal()];
                            if (i == 1) {
                                getEstNo();
                            } else {*/
                            et_invoiceNo.setText(receivedData.getInvoiceNo());
                            /* }*/
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

 /*   private void getInvoiceNo() {
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
    }*/

    public void setAdapterCur(List<Product> list) {
        if (list != null) {
            displayPrice(list);
            rc.removeAllViews();
            rc.setAdapter(new ItemAdapter(list, selectedCurrencyId, new ItemAdapter.OnItemClickListener() {
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
    public void setAdapter(List<Product> list) {
        if (list != null) {
        /*    list=UiUtil.reset_list(list);
            if (discount_index != 0) {
                if (discount.getHint().toString().equalsIgnoreCase("Flat discount")) {
                    list = UiUtil.priceAfterDiscount(items, 2, d_value);
                    cur_items = UiUtil.priceAfterDiscount(items, 2, d_value);
                } else {
                    list = UiUtil.priceAfterDiscount(items, 1, d_value);
                    cur_items = UiUtil.priceAfterDiscount(items, 1, d_value);
                }
            } else {
                list = items;
                cur_items=items;
            }
            displayPrice(list);*/
            apply_discount.performClick();
            cur_items = list;
            rc.removeAllViews();
            rc.setAdapter(new ItemAdapter(list, selectedCurrencyId, new ItemAdapter.OnItemClickListener() {
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

    private boolean isDiscount(List<Product> list) {
        boolean result = false;
        if (receivedData.getDiscountType() != 0) {
            if (list.size() > 1) {
                result = false;
            } else {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }

    private int getDiscoutType() {
        int i = 0;
        if (discount_spi.getSelectedItem().toString().equalsIgnoreCase("Percent discount")) {
            i = 2;
        } else if (discount_spi.getSelectedItem().toString().equalsIgnoreCase("Flat discount")) {
            i = 1;
        } else {
            i = 0;
        }
        return i;
    }

    /*private boolean isDiscontAbilable() {
        boolean result = false;
        try {
            int dis = 0;
            try {
                dis = getTax(items.get(0).getProductServiceTaxes());
            } catch (Exception e) {

            }
            if (items.size() > 1) {
                for (int i = 1; i < items.size(); i++) {
                    int camp = 0;
                    try {
                        camp = getTax(items.get(i).getProductServiceTaxes());
                    } catch (Exception e) {
                    }
                    if (dis == camp) {
                        result = true;
                    } else {
                        result = false;
                        break;
                    }
                }
            } else {
                result = true;
            }
        } catch (Exception e) {

        }
        return result;
    }*/

    private int getTax(List<ProductServiceTaxesItem> productServiceTaxes) {
        double result = 0;
        for (int i = 0; i < productServiceTaxes.size(); i++) {
            result = result + productServiceTaxes.get(i).getRate();
        }
        return (int) result;
    }


    private void displayPrice(List<Product> itm) {
        try {
            PriceCal priceCal = new PriceCal(itm);
            subtotal.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getSub_totel()));
            tax_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            grant_total.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getTotel()));
            total = priceCal.getTotel();
            display_taxs(priceCal);
        } catch (Exception e) {
        }
    }

    private boolean checkDiscount() {
        boolean result = false;
        PriceCal cal = new PriceCal(items);
        double d = 0.0;
        try {
            d = Double.parseDouble(discount.getText().toString());
        } catch (Exception e) {
        }

        if (!discount.getText().toString().isEmpty() && d != 0) {
            if (discount.getHint().toString().equalsIgnoreCase("Percent discount")) {
                discount_error.setText("While applying discount at invoice level in (%) then taxes should be same in all items!");
                if (Double.parseDouble(discount.getText().toString()) < 101.0) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                discount_error.setText("While applying discount at invoice level in (amount), then invoice can have only one item!");
                if (Double.parseDouble(discount.getText().toString()) > cal.getSub_totel()) {
                    result = false;
                } else {
                    result = true;
                }
            }
        } else {
            result = true;
        }
        return result;
    }

    private void display_taxs(PriceCal priceCal) {
        try {
            taxs_name_list.removeAllViews();
            taxs_amount_list.removeAllViews();
            TextView lable = new TextView(mContext);
            discount_lb = new TextView(mContext);
            lable.setText("Total discount:");
            discount_lb.setText(selectedCurrencyId + " " + String.format("%.2f", priceCal.getDiscount()));
            discount_lb.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            discount_lb.setGravity(Gravity.RIGHT);
            if (priceCal.getDiscount() != 0) {
                taxs_name_list.addView(lable);
                taxs_amount_list.addView(discount_lb);
            }
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

    private List<String> isValidData(List<Product> list) {
        List<String> dataList = new ArrayList<>();
        try {
            try {
                if (this.due_date.getText().toString().length() == 0) {
                    dataList.add("Enter valid payment due date");
                }
            } catch (Exception e) {
            }
            if (CustomerList.result == null) {
                dataList.add("Please select a customer");
            }
            if (items.size() == 0) {
                dataList.add("Please Add at least one item.");
            }
            if (total < 0) {
                dataList.add("Discount cannot be applied more than amount!");
            }
            if (discount_index != 0) {
                if (discount.getText().toString().isEmpty()) {
                    dataList.add("Please enter discount value.");
                }
            }
            if (discount_index != 0) {
                if (list.size() > 1) {
                    if (getDiscoutType() == 1) {
                        dataList.add("While applying discount at invoice level in (amount), then invoice can have only one item!");
                    }
                }
            }
        } catch (Exception e) {
        }
        return dataList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditInvoice.this.selectedExchangeCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        EditInvoice.this.selectedBussinessCurrency = new com.akounto.accountingsoftware.response.currency.Currency("USD", 1.0, "Dollar", "$");
        EditInvoice.this.selectdCurrencyCode = UiUtil.getBussinessCurren(getApplicationContext());
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
            List<String> invalidList = isValidData(items);
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
                    if (items.get(i).getDiscountType() == 0) {
                        arrayList.add(new ItemWithoutId(items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), items.get(i).getIncomeAccountId(), recurringInvoiceSalesTaxItems));
                    } else {
                        arrayList.add(new ItemWithoutId(items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), items.get(i).getIncomeAccountId(), recurringInvoiceSalesTaxItems, items.get(i).getDiscountType(), items.get(i).getDiscount()));
                    }
                } else {
                    if (items.get(i).getDiscountType() == 0) {
                        arrayList.add(new ItemWithId(items.get(i).getId(), items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), Integer.parseInt(items.get(i).getProductTransactionHeadId()), recurringInvoiceSalesTaxItems));
                    } else {
                        arrayList.add(new ItemWithId(items.get(i).getId(), items.get(i).getProductId(), items.get(i).getName(), items.get(i).getDescription(), Integer.parseInt(items.get(i).getQty()), items.get(i).getPrice(), Integer.parseInt(items.get(i).getProductTransactionHeadId()), recurringInvoiceSalesTaxItems, items.get(i).getDiscountType(), items.get(i).getDiscount()));
                    }
                }
            }
            //InvoiceRequest createRecurringInvoiceRequest = new InvoiceRequest(receivedData.getId(), receivedData.getInvoiceNo(), exgRate, (int) Math.round(EditInvoice.this.selectedBussinessCurrency.getExchangeRate()), this.customerHeadTransactionId, calculatedays(in_date.getText().toString(), this.due_date.getText().toString()), this.due_date.getText().toString(), this.in_date.getText().toString(), arrayList, this.selectedExchangeCurrency.getCode(), this.tv_note.getText().toString().trim(), receivedData.getPoNumber(), this.estimateName, receivedData.getInvoiceNoPS());
            InvoiceRequest createRecurringInvoiceRequest;
            //discount.getText().toString()
            if (discount_index != 0 && d_value != 0) {
                createRecurringInvoiceRequest = new InvoiceRequest(receivedData.getId(), receivedData.getInvoiceNo(), exgRate, (int) Math.round(EditInvoice.this.selectedBussinessCurrency.getExchangeRate()), this.customerHeadTransactionId, calculatedays(in_date.getText().toString(), this.due_date.getText().toString()), this.due_date.getText().toString(), this.in_date.getText().toString(), arrayList, this.selectedExchangeCurrency.getCode(), this.tv_note.getText().toString().trim(), receivedData.getPoNumber(), this.estimateName, receivedData.getInvoiceNoPS(), discount_index, Double.parseDouble(discount.getText().toString()));
            } else {
                createRecurringInvoiceRequest = new InvoiceRequest(receivedData.getId(), receivedData.getInvoiceNo(), exgRate, (int) Math.round(EditInvoice.this.selectedBussinessCurrency.getExchangeRate()), this.customerHeadTransactionId, calculatedays(in_date.getText().toString(), this.due_date.getText().toString()), this.due_date.getText().toString(), this.in_date.getText().toString(), arrayList, this.selectedExchangeCurrency.getCode(), this.tv_note.getText().toString().trim(), receivedData.getPoNumber(), this.estimateName, receivedData.getInvoiceNoPS());
            }
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
        RestClient.getInstance((Activity) this).getCurrencyConverter(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CurrencyResponse>(this, (String) null) {
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful()) {
                        Log.d("currencyrate", new Gson().toJson((Object) response.body().getData()));
                        EditInvoice.this.selectedExchangeCurrency = response.body().getData().getExchangeCurrency();
                        cust_curancy = response.body().getData().getExchangeCurrency().getCode();
                        EditInvoice.this.selectedBussinessCurrency = response.body().getData().getBusinessCurrency();
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
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            Log.d("invoice_type", new Gson().toJson((Object) response.body()));
                            EditInvoice EditInvoice = EditInvoice.this;
                            UiUtil.showToast(EditInvoice, invoice_type + " Updated");
                            ViewInvoice.guid = receivedData.getGuid();
                            finish();
                            return;
                        } else {
                            UiUtil.showToast(EditInvoice.this, ((response.body().getTransactionStatus().getError())).getDescription());
                        }
                    }else{
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                       Toast.makeText(mContext,"The request is invalid.",Toast.LENGTH_LONG).show();
                    }
                    EditInvoice commonInvoiceActivity2 = EditInvoice.this;
                    UiUtil.showToast(commonInvoiceActivity2, invoice_type + " Error while adding");
                } catch (Exception e) {
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
//        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            currencyListForDisplay.add(jsonObject.getString("Name") + "(" + jsonObject.getString("Id") + ")");
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(currencyListForSpinner, this.currencyListForDisplay);
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
                if (cur_start) {
                    setAdapterCur(cur_items);
                } else {
                    cur_start = true;
                }
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
                            //getInvoiceNo();

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

    private void setDiscountAdp(String[] value) {
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, value);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        discount_spi.setAdapter(aa);
    }

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
        } else if (resultCode != 0) {
            if (requestCode == 3) {
                try {
                    if (ItemList.result != null) {
                        items.add(ItemList.result);
                        setAdapter(UiUtil.reset_list(items));
                    }
                } catch (Exception e) {
                }
            } else if (requestCode == 4) {
                try {
                    items.remove(position);
                    items.add(ItemList.adds_item);
                    setAdapter(UiUtil.reset_list(items));
                } catch (Exception e) {
                }
            } else if (requestCode == 5) {
                if (resultCode == 6) {
                    try {
                        items.remove(position);
                        setAdapter(UiUtil.reset_list(items));
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        items.remove(position);
                        items.add(CreateInvoice.edit_item);
                        setAdapter(UiUtil.reset_list(items));
                    } catch (Exception e) {
                    }
                }
            }
        }
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
        discount_spi.setSelection(UiUtil.getDisType(discount_index));
        if (discount_index == 2) {
            discount.setHint("Percent discount");
        } else {
            discount.setHint("Flat discount");
        }
        setAdapter(items);
        apply_discount.performClick();
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
                CustomerList.result = receivedData.getCustomer();
            } else {
                id_cust.setVisibility(View.GONE);
                btn_cust.setVisibility(View.VISIBLE);
            }
            this.customerHeadTransactionId = cust.getHeadTransactionId();
        } catch (Exception e) {
        }
    }
}
