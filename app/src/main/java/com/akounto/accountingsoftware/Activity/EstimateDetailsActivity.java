package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.ProductRecyclerAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ApproveRecurringInvoice;
import com.akounto.accountingsoftware.request.ConvertToInvoice;
import com.akounto.accountingsoftware.request.UpdateStatus;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.GetInvoiceTransactionItem;
import com.akounto.accountingsoftware.response.InvoiceDetails;
import com.akounto.accountingsoftware.response.UpdateStatusResponse;
import com.akounto.accountingsoftware.response.currency.Currency;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Response;

public class EstimateDetailsActivity extends AppCompatActivity {
    ProductRecyclerAdapter adapter;
    TextView amountPTV;
    TextView approveTv;
    LinearLayout approvedLL;
    TextView billNumber;
    TextView billnoEDTV;
    TextView cacncelInvoice;
    TextView convertToInvoice;
    String data;
    TextView dateEDTV;
    TextView draftBillTV;
    TextView draftStatusTV;
    TextView dueOnPTV;
    TextView duedateEDTV;
    TextView editDraft;
    String estimateDate = "Invoice Date";
    String estimateExpiry = "Payment Due";
    String estimateNo = "Invoice No";
    String estimateType = "estimate";
    TextView estimatedAmountTV;
    TextView expireOn;
    InvoiceDetails invoiceDetails;
    List<GetInvoiceTransactionItem> invoiceTransaction = new ArrayList();
    TextView pageTitle;
    String poNO = "P.O. number";
    TextView posoEDTV;
    RecyclerView productRecycler;
    CardView sendCV;
    TextView sendEstimate;
    TextView subTotalTV;
    TextView subTotalTaxTV;
    TextView totalEstimateTV;
    TextView totalPaidTV;
    TextView vAddressTV;
    TextView vNamePTV;
    TextView vNameTV;
    TextView vStateTV;
    TextView vemailTV;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation_new);
        String stringExtra = getIntent().getStringExtra("DATA");
        this.data = stringExtra;
        Log.d("datadatadata", stringExtra);
        this.estimateType = getIntent().getStringExtra("estimateType");
        this.pageTitle = findViewById(R.id.pageTitle);
        this.estimatedAmountTV = findViewById(R.id.estimatedAmountTV);
        this.expireOn = findViewById(R.id.expireOn);
        this.sendEstimate = findViewById(R.id.sendEstimate);
        this.posoEDTV = findViewById(R.id.posoEDTV);
        if (this.estimateType.equals("estimate")) {
            this.estimateNo = "Estimate no";
            this.poNO = "P.O. number";
            this.estimateDate = "Estimate date";
            this.estimateExpiry = "Estimate expiry on";
            this.pageTitle.setText("Estimate Details");
            this.estimatedAmountTV.setText("Estimated Amount");
            this.expireOn.setText("Expires on");
            this.sendEstimate.setText("Send Estimate");
            this.posoEDTV.setVisibility(View.VISIBLE);
        } else {
            this.estimateNo = "Invoice No";
            this.poNO = " ";
            this.estimateDate = "Invoice Date";
            this.estimateExpiry = "Payment Due";
            this.pageTitle.setText("Invoice Details");
            this.estimatedAmountTV.setText("Due Amount");
            this.expireOn.setText("Due on");
            this.sendEstimate.setText("Send Invoice");
            this.posoEDTV.setVisibility(View.GONE);
        }
        inItUi();
        InvoiceDetails invoiceDetails2 = new Gson().fromJson(this.data, InvoiceDetails.class);
        this.invoiceDetails = invoiceDetails2;
        updateCustomerData(invoiceDetails2);
    }

    private void inItUi() {
        this.cacncelInvoice = findViewById(R.id.cacncelInvoice);
        this.convertToInvoice = findViewById(R.id.convertToInvoice);
        this.approvedLL = findViewById(R.id.approvedLL);
        this.sendCV = findViewById(R.id.sendCV);
        this.billnoEDTV = findViewById(R.id.billnoEDTV);
        this.dateEDTV = findViewById(R.id.dateEDTV);
        this.duedateEDTV = findViewById(R.id.duedateEDTV);
        this.vNameTV = findViewById(R.id.vNameTV);
        this.vAddressTV = findViewById(R.id.vAddressTV);
        this.vStateTV = findViewById(R.id.vStateTV);
        this.vemailTV = findViewById(R.id.vemailTV);
        this.vNamePTV = findViewById(R.id.vNamePTV);
        this.amountPTV = findViewById(R.id.amountPTV);
        this.dueOnPTV = findViewById(R.id.dueOnPTV);
        this.subTotalTV = findViewById(R.id.subTotalTv);
        this.approveTv = findViewById(R.id.approve);
        this.draftStatusTV = findViewById(R.id.draftStatusTV);
        this.totalPaidTV = findViewById(R.id.totalPaidTV);
        this.editDraft = findViewById(R.id.editDraft);
        this.billNumber = findViewById(R.id.billNumber);
        this.draftBillTV = findViewById(R.id.draftBillTV);
        this.productRecycler = findViewById(R.id.productRecycler);
        findViewById(R.id.sendEstimate).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$0$EstimateDetailsActivity(view);
            }
        });
        this.editDraft.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$1$EstimateDetailsActivity(view);
            }
        });
        findViewById(R.id.approve).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$2$EstimateDetailsActivity(view);
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$3$EstimateDetailsActivity(view);
            }
        });
        findViewById(R.id.convertToInvoice).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$4$EstimateDetailsActivity(view);
            }
        });
        this.cacncelInvoice.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimateDetailsActivity.this.lambda$inItUi$5$EstimateDetailsActivity(view);
            }
        });
    }

    public void lambda$inItUi$0$EstimateDetailsActivity(View v) {
        startActivity(new Intent(this, SendEstimateActivity.class));
    }

    public void lambda$inItUi$1$EstimateDetailsActivity(View v) {
        startActivity(CommonInvoiceActivity.buildIntentWithData(this, Type.ESTIMATES.name(), this.invoiceDetails));
    }

    public void lambda$inItUi$2$EstimateDetailsActivity(View v) {
        UpdateStatus status = new UpdateStatus();
        status.setInvoiceId(this.invoiceDetails.getId());
        if (this.invoiceDetails.getStatus() == 0) {
            status.setStatus(100);
        } else if (this.invoiceDetails.getStatus() == 100) {
            status.setStatus(0);
        }
        RestClient.getInstance(this).updateEstimateStatus(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),this.estimateType, status).enqueue(new CustomCallBack<UpdateStatusResponse>(this, null) {
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                super.onResponse(call, response);
                EstimateDetailsActivity.this.finish();
            }

            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    public void lambda$inItUi$3$EstimateDetailsActivity(View v) {
        finish();
    }

    public void lambda$inItUi$4$EstimateDetailsActivity(View v) {
        ConvertToInvoice invoice = new ConvertToInvoice();
        invoice.setEstimateId(this.invoiceDetails.getId());
        convertToInvoice(invoice);
    }

    public void lambda$inItUi$5$EstimateDetailsActivity(View v) {
        deleteCard();
    }

    public void deleteCard() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.delete_invoice);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView = dialog.findViewById(R.id.head);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EstimateDetailsActivity.this.cancelInvoice();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void cancelInvoice() {
        ApproveRecurringInvoice req = new ApproveRecurringInvoice();
        req.setInvoiceId(this.invoiceDetails.getId());
        req.setStatus(400);
        RestClient.getInstance(this).invoiceCacel(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),req).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(this, null) {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                if (response.body().getTransactionStatus().isIsSuccess()) {
                    UiUtil.showToast(EstimateDetailsActivity.this, "cancelled");
                    EstimateDetailsActivity.this.finish();
                    return;
                }
                UiUtil.showToast(EstimateDetailsActivity.this, "Update scheduler, before approving");
            }

            public void onFailure(Call<GetInvoiceByIdResponse> call, Throwable t) {
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

    private void updateCustomerData(InvoiceDetails data2) {
        TextView textView = this.amountPTV;
        textView.setText("$" + data2.getAmount());
        Log.d("updateCustomerData---", new Gson().toJson(data2));
        this.draftBillTV.setText(Html.fromHtml(getResources().getString(R.string.this_is_adraft)));
        if (this.estimateType.equals("estimate")) {
            TextView textView2 = this.billNumber;
            textView2.setText("Estimate #" + data2.getInvoiceNo());
        } else {
            TextView textView3 = this.billNumber;
            textView3.setText("Invoice #" + data2.getInvoiceNo());
        }
        if (this.invoiceDetails.getStatus() == 0) {
            this.approveTv.setText("Approve draft");
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_draft));
            this.sendCV.setVisibility(View.GONE);
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.VISIBLE);
            if (this.estimateType.equals("estimate")) {
                this.draftBillTV.setText(Html.fromHtml(getResources().getString(R.string.this_is_adraft_estimate)));
            }
        } else if (this.invoiceDetails.getStatus() == 100) {
            this.approveTv.setText("Rollback to draft");
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_approved));
            this.draftStatusTV.setText("Approved");
            this.sendCV.setVisibility(View.VISIBLE);
            this.approvedLL.setVisibility(View.VISIBLE);
            this.editDraft.setVisibility(View.GONE);
            if (this.estimateType.equals("estimate")) {
                this.draftBillTV.setText(Html.fromHtml(getResources().getString(R.string.this_is_adraft_estimate_approved)));
                this.cacncelInvoice.setVisibility(View.GONE);
                this.convertToInvoice.setVisibility(View.VISIBLE);
                this.approveTv.setVisibility(View.VISIBLE);
            } else {
                this.cacncelInvoice.setVisibility(View.VISIBLE);
                this.convertToInvoice.setVisibility(View.GONE);
                this.approveTv.setVisibility(View.GONE);
                this.draftBillTV.setText(Html.fromHtml(getResources().getString(R.string.this_is_adraft_invoice_approved)));
            }
        } else if (this.invoiceDetails.getStatus() == 400) {
            this.cacncelInvoice.setVisibility(View.GONE);
            this.sendCV.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
            this.draftBillTV.setVisibility(View.GONE);
            this.sendEstimate.setVisibility(View.GONE);
            this.convertToInvoice.setVisibility(View.GONE);
            this.approveTv.setVisibility(View.GONE);
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_cancelled));
            this.draftStatusTV.setText("Cancelled");
        } else {
            this.draftStatusTV.setBackground(getResources().getDrawable(R.drawable.right_rounded_converted));
            this.draftStatusTV.setText("Converted to invoice");
            this.approvedLL.setVisibility(View.GONE);
            this.editDraft.setVisibility(View.GONE);
            this.approveTv.setVisibility(View.GONE);
            this.draftBillTV.setVisibility(View.GONE);
        }
        TextView textView4 = this.billnoEDTV;
        textView4.setText(this.estimateNo + " : " + data2.getInvoiceNo());
        if (data2.getPoNumber() != null) {
            TextView textView5 = this.posoEDTV;
            textView5.setText(this.poNO + " : " + data2.getPoNumber());
        }
        if (data2.getInvoiceDate() != null) {
            TextView textView6 = this.duedateEDTV;
            textView6.setText(this.estimateExpiry + " : " + getFormattedDate(data2.getInvoiceDate()));
            this.dueOnPTV.setText(getFormattedDate(data2.getInvoiceDate()));
        }
        if (data2.getInvoiceDate() != null) {
            TextView textView7 = this.dateEDTV;
            textView7.setText(this.estimateDate + " : " + getFormattedDate(data2.getInvoiceDate()));
        }
        if (data2.getCustomer().getName() != null) {
            this.vNameTV.setText(data2.getCustomer().getName());
            this.vNamePTV.setText(data2.getCustomer().getName());
        }
        if (data2.getCustomer().getBillAddress1() != null) {
            TextView textView8 = this.vAddressTV;
            textView8.setText(data2.getCustomer().getBillAddress1() + "" + data2.getCustomer().getBillAddress2());
        }
        if (data2.getCustomer().getBillCity() != null) {
            TextView textView9 = this.vStateTV;
            textView9.setText(data2.getCustomer().getBillCity() + "" + data2.getCustomer().getBillState() + "" + data2.getCustomer().getBillPostCode());
        }
        TextView textView10 = this.totalPaidTV;
        textView10.setText("$" + data2.getAmount());
        TextView textView11 = this.subTotalTV;
        textView11.setText("$" + data2.getAmount());
        Currency currency = new Currency();
        currency.setSymbol(data2.getCustCurrency());
        this.productRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        ProductRecyclerAdapter productRecyclerAdapter = new ProductRecyclerAdapter(this, data2.getInvoiceTransaction(), currency);
        this.adapter = productRecyclerAdapter;
        this.productRecycler.setAdapter(productRecyclerAdapter);
    }

    private void convertToInvoice(ConvertToInvoice convertToInvoice2) {
        RestClient.getInstance(this).estimateToInvoice(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),convertToInvoice2).enqueue(new CustomCallBack<UpdateStatusResponse>(this, "Adding Journal Transaction...") {
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(EstimateDetailsActivity.this, "Succefully Converted");
                    EstimateDetailsActivity.this.finish();
                    return;
                }
                UiUtil.showToast(EstimateDetailsActivity.this, "Error while adding");
            }

            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
}
