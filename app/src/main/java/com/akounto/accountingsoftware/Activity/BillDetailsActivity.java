package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.ProductRecyclerAdapter;
import com.akounto.accountingsoftware.adapter.RITaxSummaryItemsAdapter;
import com.akounto.accountingsoftware.model.TaxSummaryList;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ApproveBillReq;
import com.akounto.accountingsoftware.request.BillsById;
import com.akounto.accountingsoftware.response.BillsByIdResponse;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.currency.Currency;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class BillDetailsActivity extends AppCompatActivity {
    ProductRecyclerAdapter adapter;
    TextView amountPTV;
    TextView approve;
    LinearLayout approvedLL;
    TextView billNumber;
    TextView billnoEDTV;
    TextView createAnotherBillButton;
    TextView createdOnTv;
    BillsById data;
    TextView dateEDTV;
    TextView draftBillTV;
    TextView draftStatusTV;
    TextView dueOnPTV;
    TextView duedateEDTV;
    TextView editDraft;
    Map<String, Double> finalTaxMap = new HashMap();
    String guid;
    List<GetInvoiceTransactionItem> invoiceTransaction = new ArrayList();
    TextView notesTV;
    TextView pageTitle;
    TextView posoEDTV;
    RecyclerView productRecycler;
    RITaxSummaryItemsAdapter rITaxSummaryItemsAdapter;
    RelativeLayout subTotalInDollar;
    TextView subTotalPaidInDolar;
    TextView subTotalTv;
    TextView subtotalAmountInDolar;
    RecyclerView taxRecycler;
    List<TaxSummaryList> taxSummaryList = new ArrayList();
    TextView totalPaidTV;
    TextView vAddressTV;
    TextView vNamePTV;
    TextView vNameTV;
    TextView vStateTV;
    TextView vemailTV;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_new);
        this.guid = getIntent().getStringExtra("GUID");
        TextView textView = findViewById(R.id.pageTitle);
        this.pageTitle = textView;
        textView.setText("Bill Details");
        inItUi();
        Log.d("guidguid", this.guid);
    }

    private void inItUi() {
        this.approvedLL = findViewById(R.id.approvedLL);
        this.billnoEDTV = findViewById(R.id.billnoEDTV);
        this.posoEDTV = findViewById(R.id.posoEDTV);
        this.dateEDTV = findViewById(R.id.dateEDTV);
        this.duedateEDTV = findViewById(R.id.duedateEDTV);
        this.createdOnTv = findViewById(R.id.createdOnTv);
        this.notesTV = findViewById(R.id.notesTV);
        this.draftBillTV = findViewById(R.id.draftBillTV);
        this.billNumber = findViewById(R.id.billNumber);
        this.vNameTV = findViewById(R.id.vNameTV);
        this.vAddressTV = findViewById(R.id.vAddressTV);
        this.vStateTV = findViewById(R.id.vStateTV);
        this.vemailTV = findViewById(R.id.vemailTV);
        this.vNamePTV = findViewById(R.id.vNamePTV);
        this.amountPTV = findViewById(R.id.amountPTV);
        this.dueOnPTV = findViewById(R.id.dueOnPTV);
        this.subTotalTv = findViewById(R.id.subTotalTv);
        this.totalPaidTV = findViewById(R.id.totalPaidTV);
        this.draftStatusTV = findViewById(R.id.draftStatusTV);
        this.subtotalAmountInDolar = findViewById(R.id.subtotalAmountInDolar);
        this.subTotalInDollar = findViewById(R.id.subTotalInDollar);
        this.subTotalPaidInDolar = findViewById(R.id.subTotalPaidInDolar);
        this.approve = findViewById(R.id.approve);
        TextView textView = findViewById(R.id.editDraft);
        this.editDraft = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                BillDetailsActivity.this.lambda$inItUi$0$BillDetailsActivity(view);
            }
        });
        this.approve.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                BillDetailsActivity.this.lambda$inItUi$1$BillDetailsActivity(view);
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                BillDetailsActivity.this.lambda$inItUi$2$BillDetailsActivity(view);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.productRecycler);
        this.productRecycler = recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView recyclerView2 = findViewById(R.id.taxRecycler);
        this.taxRecycler = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        RITaxSummaryItemsAdapter rITaxSummaryItemsAdapter2 = new RITaxSummaryItemsAdapter(this, this.taxSummaryList);
        this.rITaxSummaryItemsAdapter = rITaxSummaryItemsAdapter2;
        this.taxRecycler.setAdapter(rITaxSummaryItemsAdapter2);
        getBillsDetailsById(this.guid);
    }

    public void lambda$inItUi$0$BillDetailsActivity(View v) {
        if (this.data != null) {
            Intent intent = new Intent(this, CreateBillActivity.class);
            intent.putExtra("GUID", this.data.getGuid());
            intent.putExtra("IS_EDIT", true);
            startActivity(intent);
        }
    }

    public void lambda$inItUi$1$BillDetailsActivity(View v) {
        if (this.data != null) {
            approveBill();
        }
    }

    public void lambda$inItUi$2$BillDetailsActivity(View v) {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    private void approveBill() {
        ApproveBillReq req = new ApproveBillReq();
        req.setBillId(this.data.getId());
        req.setStatus(100);
        RestClient.getInstance(this).updateBillStatus(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), req).enqueue(new CustomCallBack<BillsByIdResponse>(this, null) {
            public void onResponse(Call<BillsByIdResponse> call, Response<BillsByIdResponse> response) {
                super.onResponse(call, response);
                BillDetailsActivity.this.finish();
            }

            public void onFailure(Call<BillsByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    private void getBillsDetailsById(String id) {
        RestClient.getInstance(this).getBillById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<BillsByIdResponse>(this, null) {
            public void onResponse(Call<BillsByIdResponse> call, Response<BillsByIdResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    BillDetailsActivity.this.updateCustomerData(response.body().getData());
                }
            }

            public void onFailure(Call<BillsByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
                UiUtil.showToast(BillDetailsActivity.this, t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateCustomerData(BillsById data2) {
        Double totalWithoutTax;
        BillsById billsById = data2;
        this.data = billsById;
        Log.d("updateCustomerData---", new Gson().toJson(billsById));
        if (data2.getStatus() == 100) {
            this.approve.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
        }
        if (data2.getInvoiceNo() != null) {
            this.billnoEDTV.setText("Bill no : " + data2.getInvoiceNo());
        }
        if (data2.getPoNumber() != null) {
            this.posoEDTV.setText("P.O./S.O. : " + data2.getPoNumber());
        }
        if (data2.getDueAt() != null) {
            this.duedateEDTV.setText("Due date : " + getFormattedDate(data2.getDueAt()));
            this.dueOnPTV.setText(getFormattedDate(data2.getDueAt()));
        }
        if (data2.getDueAt() != null) {
            this.dateEDTV.setText("Date : " + getFormattedDate(data2.getBillAt()));
            this.createdOnTv.setText(getFormattedDate(data2.getBillAt()));
        }
        if (data2.getNotes() != null) {
            this.notesTV.setText(data2.getNotes());
        } else {
            this.notesTV.setVisibility(View.GONE);
        }
        if (!(data2.getVendor() == null || data2.getVendor().getVendorName() == null)) {
            this.vNameTV.setText(data2.getVendor().getVendorName());
            this.vNamePTV.setText(data2.getVendor().getVendorName());
        }
        String vAddress = "";
        if (!(data2.getVendor() == null || data2.getVendor().getBillAddress1() == null)) {
            vAddress = vAddress + data2.getVendor().getBillAddress1();
        }
        if (!(data2.getVendor() == null || data2.getVendor().getBillAddress2() == null)) {
            if (vAddress.length() > 0) {
                vAddress = vAddress + "," + data2.getVendor().getBillAddress2();
            } else {
                vAddress = vAddress + data2.getVendor().getBillAddress2();
            }
        }
        if (vAddress.length() > 0) {
            this.vAddressTV.setText(vAddress);
            this.vAddressTV.setVisibility(View.VISIBLE);
        } else {
            this.vAddressTV.setVisibility(View.GONE);
        }
        if (data2.getVendor() == null || data2.getVendor().getBillCity() == null) {
            this.vStateTV.setVisibility(View.GONE);
        } else {
            this.vStateTV.setText(data2.getVendor().getBillCity());
            this.vStateTV.setVisibility(View.VISIBLE);
        }
        if (data2.getStatus() == 0) {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_draft));
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.VISIBLE);
        } else if (data2.getStatus() == 100) {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_approved));
            this.draftStatusTV.setText("Approved");
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
            this.draftBillTV.setVisibility(View.GONE);
        } else if (data2.getStatus() == 150) {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_partialsettled));
            this.draftStatusTV.setText("Partialsettled");
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
            this.draftBillTV.setVisibility(View.GONE);
            this.approve.setVisibility(View.GONE);
        } else {
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
        }
        if (data2.getInvoiceNo() != null) {
            this.billNumber.setText("Bill # " + data2.getInvoiceNo());
        }
        String dueAMount = new DecimalFormat("##.00").format(data2.getPrice() * data2.getExchangeRate()).replace(',', '.');
        this.amountPTV.setText("$" + dueAMount);
        Double totalWithoutTax2 = Double.valueOf(Utils.DOUBLE_EPSILON);
        if (data2.getBillTransaction() != null) {
            for (GetInvoiceTransactionItem item : data2.getBillTransaction()) {
                double doubleValue = totalWithoutTax2.doubleValue();
                double price = item.getPrice();
                double quantity = item.getQuantity();
                Double.isNaN(quantity);
                Double totalWithoutTax3 = Double.valueOf(doubleValue + (price * quantity));
                if (item.getTaxes() != null) {
                    for (ProductServiceTaxesItem tax : item.getTaxes()) {
                        if (this.finalTaxMap.get(tax.getName()) != null) {
                            double doubleValue2 = this.finalTaxMap.get(tax.getName()).doubleValue();
                            totalWithoutTax = totalWithoutTax3;
                            double quantity2 = item.getQuantity();
                            double rate = tax.getRate();
                            Double.isNaN(quantity2);
                            this.finalTaxMap.put(tax.getName(), Double.valueOf(doubleValue2 + (((quantity2 * rate) * item.getPrice()) / 100.0d)));
                        } else {
                            totalWithoutTax = totalWithoutTax3;
                            double quantity3 = item.getQuantity();
                            double rate2 = tax.getRate();
                            Double.isNaN(quantity3);
                            this.finalTaxMap.put(tax.getName(), Double.valueOf(((quantity3 * rate2) * item.getPrice()) / 100.0d));
                        }
                        totalWithoutTax3 = totalWithoutTax;
                    }
                }
                totalWithoutTax2 = totalWithoutTax3;
            }
        }
        for (Map.Entry<String, Double> entry : this.finalTaxMap.entrySet()) {
            this.taxSummaryList.add(new TaxSummaryList(entry.getKey(), Double.parseDouble(String.format("%.2f", entry.getValue()).replace(",", "."))));
        }
        Currency currency = new Currency();
        currency.setSymbol(data2.getCurrency());
        this.rITaxSummaryItemsAdapter.notifyDataChange(this.taxSummaryList, currency);
        this.subTotalTv.setText(currency.getSymbol() + " " + totalWithoutTax2);
        this.totalPaidTV.setText(currency.getSymbol() + " " + data2.getPrice());
        if (data2.getExchangeRate() != 1.0f) {
            this.subTotalInDollar.setVisibility(View.VISIBLE);
            this.subtotalAmountInDolar.setText("USD " + (data2.getPrice() * data2.getExchangeRate()));
            this.subTotalPaidInDolar.setText("Total (USD at " + data2.getExchangeRate() + ")");
        } else {
            this.subTotalInDollar.setVisibility(View.GONE);
        }
        if (data2.getBillTransaction() != null) {
            this.productRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
            ProductRecyclerAdapter productRecyclerAdapter = new ProductRecyclerAdapter(this, data2.getBillTransaction(), currency);
            this.adapter = productRecyclerAdapter;
            this.productRecycler.setAdapter(productRecyclerAdapter);
        }
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
}
