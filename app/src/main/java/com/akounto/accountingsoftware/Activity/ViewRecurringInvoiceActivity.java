package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.ProductRecyclerAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddSchedulerReq;
import com.akounto.accountingsoftware.request.ApproveRecurringInvoice;
import com.akounto.accountingsoftware.response.AddSchedulerResponse;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Response;

public class ViewRecurringInvoiceActivity extends AppCompatActivity {
    ProductRecyclerAdapter adapter;
    TextView amountPTV;
    TextView approveTv;
    TextView billNumber;
    TextView billnoEDTV;
    TextView dateEDTV;
    TextView draftBillTV;
    TextView draftStatusTV;
    TextView dueOnPTV;
    TextView duedateEDTV;
    TextView editDraft;
    Group everyDay;
    LinearLayout everyDayLayout;
    Group everyGroup;
    InvoiceDetails invoiceDetails;
    TextView pageTitle;
    TextView posoEDTV;
    RecyclerView recyclerView;
    ConstraintLayout repeatEveryLayout;
    PowerSpinnerView repeatThisInvoiceSpinner;
    TextView rollBackToDraftButton;
    TextView statusDesc;
    TextView subTotalTV;
    TextView totalPaidTV;
    TextView vAddressTV;
    TextView vNamePTV;
    TextView vNameTV;
    TextView vStateTV;
    TextView vemailTV;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recurring_invoice);
        TextView textView = findViewById(R.id.pageTitle);
        this.pageTitle = textView;
        textView.setText("View Recurring Invoice");
        Log.d("DATADATA", getIntent().getStringExtra("DATA"));
        InvoiceDetails invoiceDetails2 = new Gson().fromJson(getIntent().getStringExtra("DATA"), InvoiceDetails.class);
        this.invoiceDetails = invoiceDetails2;
        initUI(invoiceDetails2);
    }

    /* access modifiers changed from: private */
    public void initUI(InvoiceDetails invoiceDetails2) {
        this.draftStatusTV = findViewById(R.id.draftStatusTV);
        this.vemailTV = findViewById(R.id.vemailTV);
        this.vNamePTV = findViewById(R.id.vNamePTV);
        this.amountPTV = findViewById(R.id.amountPTV);
        this.dueOnPTV = findViewById(R.id.dueOnPTV);
        this.vAddressTV = findViewById(R.id.vAddressTV);
        this.amountPTV = findViewById(R.id.amountPTV);
        this.vNamePTV = findViewById(R.id.vNamePTV);
        this.vNameTV = findViewById(R.id.vNameTV);
        this.dueOnPTV = findViewById(R.id.dueOnPTV);
        TextView textView = this.amountPTV;
        textView.setText("$" + invoiceDetails2.getAmount());
        this.vStateTV = findViewById(R.id.vStateTV);
        this.vemailTV = findViewById(R.id.vemailTV);
        this.subTotalTV = findViewById(R.id.subTotalTv);
        this.totalPaidTV = findViewById(R.id.totalPaidTV);
        this.recyclerView = findViewById(R.id.productRecycler);
        this.rollBackToDraftButton = findViewById(R.id.rollbackToDraft);
        this.statusDesc = findViewById(R.id.statusDesc);
        this.approveTv = findViewById(R.id.approve);
        this.editDraft = findViewById(R.id.editDraft);
        this.repeatThisInvoiceSpinner = findViewById(R.id.repeat_this_invoice);
        this.repeatEveryLayout = findViewById(R.id.repeatEveryLayout);
        this.everyDayLayout = findViewById(R.id.everyDayLayout);
        this.everyDay = findViewById(R.id.groupDay);
        this.everyGroup = findViewById(R.id.groupEvery);
        if (invoiceDetails2.getStatus() == 0) {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_draft));
            this.draftStatusTV.setText("Draft");
        } else if (invoiceDetails2.getStatus() == 100) {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_approved));
            this.draftStatusTV.setText("Approved");
            this.rollBackToDraftButton.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    ViewRecurringInvoiceActivity.this.lambda$initUI$0$ViewRecurringInvoiceActivity(view);
                }
            });
            setUpRollback(true);
        } else {
            this.draftStatusTV.setText("Converted to invoice");
        }
        if (invoiceDetails2.getCustomer().getName() != null) {
            this.vNamePTV.setText(invoiceDetails2.getCustomer().getName());
        }
        if (invoiceDetails2.getInvoiceDate() != null) {
            this.dueOnPTV.setText(getFormattedDate(invoiceDetails2.getInvoiceDate()));
        }
        if (invoiceDetails2.getCustomer().getName() != null) {
            this.vNameTV.setText(invoiceDetails2.getCustomer().getName());
            this.vNamePTV.setText(invoiceDetails2.getCustomer().getName());
        }
        if (invoiceDetails2.getCustomer().getBillAddress1() != null) {
            TextView textView2 = this.vAddressTV;
            textView2.setText(invoiceDetails2.getCustomer().getBillAddress1() + "" + invoiceDetails2.getCustomer().getBillAddress2());
        }
        if (invoiceDetails2.getCustomer().getBillCity() != null) {
            TextView textView3 = this.vStateTV;
            textView3.setText(invoiceDetails2.getCustomer().getBillCity() + "" + invoiceDetails2.getCustomer().getBillState() + "" + invoiceDetails2.getCustomer().getBillPostCode());
        }
        TextView textView4 = this.totalPaidTV;
        textView4.setText("$" + invoiceDetails2.getAmount());
        TextView textView5 = this.subTotalTV;
        textView5.setText("$" + invoiceDetails2.getAmount());
        this.repeatThisInvoiceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                ViewRecurringInvoiceActivity.this.lambda$initUI$1$ViewRecurringInvoiceActivity(i, (String) obj, i2, (String) obj2);
            }
        });
        this.repeatThisInvoiceSpinner.selectItemByIndex(0);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        ProductRecyclerAdapter productRecyclerAdapter = new ProductRecyclerAdapter(this, invoiceDetails2.getInvoiceTransaction(), null);
        this.adapter = productRecyclerAdapter;
        this.recyclerView.setAdapter(productRecyclerAdapter);
        findViewById(R.id.approve).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ViewRecurringInvoiceActivity.this.lambda$initUI$2$ViewRecurringInvoiceActivity(view);
            }
        });
    }

    public /* synthetic */ void lambda$initUI$0$ViewRecurringInvoiceActivity(View v) {
        rollBackToDraft();
    }

    public /* synthetic */ void lambda$initUI$1$ViewRecurringInvoiceActivity(int i, String s, int selectedIndex, String selectedItem) {
        if (selectedIndex == 0) {
            this.repeatEveryLayout.setVisibility(View.GONE);
            this.everyDayLayout.setVisibility(View.GONE);
        } else if (selectedIndex == 1) {
            this.repeatEveryLayout.setVisibility(View.VISIBLE);
            this.everyDay.setVisibility(View.GONE);
            this.everyGroup.setVisibility(View.VISIBLE);
            this.everyDayLayout.setVisibility(View.GONE);
        } else if (selectedIndex == 2) {
            this.repeatEveryLayout.setVisibility(View.VISIBLE);
            this.everyGroup.setVisibility(View.GONE);
            this.everyDay.setVisibility(View.VISIBLE);
            this.everyDayLayout.setVisibility(View.GONE);
        } else if (selectedIndex != 3) {
            this.repeatEveryLayout.setVisibility(View.GONE);
            this.everyDayLayout.setVisibility(View.VISIBLE);
        } else {
            this.repeatEveryLayout.setVisibility(View.VISIBLE);
            this.everyGroup.setVisibility(View.VISIBLE);
            this.everyDay.setVisibility(View.VISIBLE);
            this.everyDayLayout.setVisibility(View.GONE);
        }
    }

    public /* synthetic */ void lambda$initUI$2$ViewRecurringInvoiceActivity(View v) {
        UpdateInvoice(100);
    }

    /* access modifiers changed from: private */
    public void setUpRollback(boolean bool) {
        if (bool) {
            this.rollBackToDraftButton.setVisibility(View.VISIBLE);
            this.approveTv.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
            this.statusDesc.setText("If you want any thing changes in APPROVED recurring invoice and schedular to click on Rollback to Draft status.");
            return;
        }
        this.rollBackToDraftButton.setVisibility(View.GONE);
        this.approveTv.setVisibility(View.GONE);
        this.editDraft.setVisibility(View.VISIBLE);
        this.statusDesc.setText("This is a Draft invoice. You can take further actions once you approve it.");
    }

    private void rollBackToDraft() {
        UpdateInvoice(0);
    }

    private void UpdateInvoice(final int status) {
        ApproveRecurringInvoice req = new ApproveRecurringInvoice();
        req.setInvoiceId(this.invoiceDetails.getId());
        req.setStatus(status);
        RestClient.getInstance(this).updateRecurring(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),req).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(this, "Updating...") {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    int i = status;
                    if (i == 0) {
                        ViewRecurringInvoiceActivity.this.setUpRollback(false);
                    } else if (i == 100) {
                        ViewRecurringInvoiceActivity.this.setUpRollback(true);
                    }
                    ViewRecurringInvoiceActivity.this.initUI(response.body().getInvoiceDetails());
                }
            }

            public void onFailure(Call<GetInvoiceByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    private void addScheduler() {
        AddSchedulerReq req = new AddSchedulerReq();
        req.setInvoiceId(this.invoiceDetails.getId());
        req.setScheduleEndType(this.invoiceDetails.getSchedule().getScheduleEndType());
        req.setInvoiceAmount(Double.valueOf(this.invoiceDetails.getSchedule().getInvoiceAmount()));
        req.setAggrementAmount(Double.valueOf(this.invoiceDetails.getSchedule().getAggrementAmount()));
        RestClient.getInstance(this).addScheduler(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),req).enqueue(new CustomCallBack<AddSchedulerResponse>(this, null) {
            public void onResponse(Call<AddSchedulerResponse> call, Response<AddSchedulerResponse> response) {
                super.onResponse(call, response);
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    UiUtil.showToast(ViewRecurringInvoiceActivity.this, "Scheduled");
                } else {
                    UiUtil.showToast(ViewRecurringInvoiceActivity.this, "Update scheduler, before approving");
                }
            }

            public void onFailure(Call<AddSchedulerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
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
