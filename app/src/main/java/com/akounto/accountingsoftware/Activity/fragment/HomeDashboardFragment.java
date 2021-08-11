package com.akounto.accountingsoftware.Activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.Bill.BillList;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceList;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.Dashboard.InvoicePurchaseOverdue;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetDashboardRequest;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDashboardFragment extends Fragment {


    BarChart cashFlowChart;
    SearchDashboardData ud;
    AppCompatTextView hint_text;
    CardView no_card_invoice, card_invoice, no_cardBill, cardBill;
    DashboardData dashboardResponse = null;
    LinearLayout noExpenseBreakdownChart, noCashFlowChart;
    PieChart expenseBreakdownChart;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    TextView cashflow, expbrak, noPiaData, noBarData;
    Typeface poppins_regular;
    Typeface poppins_semibold;
    private String textViewDate = "";
    private String toDate = "";
    Spinner spinner;
    String fromDate;
    private ArrayAdapter<String> dataAdapter = null;
    View view;
    public static List<String> filter;
    TextView invoiceNotDueAmount, invoiceOverdueAmount, billNotDueAmount, billOverdueAmount;
    boolean start = true;
    LinearLayout circle_invoice, circle_bill;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.home_dashboard_fragment, container, false);
        noPiaData = view.findViewById(R.id.peiChartNoData);
        noBarData = view.findViewById(R.id.barchNoData);
        hint_text = view.findViewById(R.id.hint_text);
        no_card_invoice = view.findViewById(R.id.no_card_invoice);
        no_cardBill = view.findViewById(R.id.no_cardBill);
        card_invoice = view.findViewById(R.id.card_invoice);
        cardBill = view.findViewById(R.id.cardBill);
        Dexter.withActivity(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
            }
        }).check();
        invoiceNotDueAmount = view.findViewById(R.id.invoice_not_due_amount);
        invoiceOverdueAmount = view.findViewById(R.id.invoice_overdue_amount);
        billNotDueAmount = view.findViewById(R.id.bill_not_due_amount);
        billOverdueAmount = view.findViewById(R.id.bill_overdue_amount);
        view.findViewById(R.id.goActivities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, ActivitiesDashboardFragment.class);
            }
        });
        noExpenseBreakdownChart = view.findViewById(R.id.noExpenseBreakdownChart);
        noCashFlowChart = view.findViewById(R.id.noCashFlowChart);
        init();
        this.poppins_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/poppins_regular.ttf");
        this.poppins_semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/poppins_semibold.ttf");
        circle_invoice = this.view.findViewById(R.id.circle_invoice);
        circle_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InvoiceList.class));
            }
        });
        circle_bill = this.view.findViewById(R.id.circle_bill);
        circle_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BillList.class));
            }
        });
///        getHomeDashBoardData(new GetDashboardRequest(fromDate, ud.getData().get(0).getStart(), ud.getData().get(0).getEnd(), UiUtil.getAccountingType(getContext()), 0), false);
        return this.view;
    }

    private void init() {
        this.cashFlowChart = this.view.findViewById(R.id.cashFlowChart);
        this.expenseBreakdownChart = this.view.findViewById(R.id.expenseBreakdownChart);
        cashflow = this.view.findViewById(R.id.cashFlowTv);
        expbrak = this.view.findViewById(R.id.expenseBreakdownTv);
        if (UiUtil.getAccountingType(getContext()) == 2) {
            hint_text.setVisibility(View.GONE);
            cashflow.setText("Income and Expense");
            expbrak.setText("Top Expenses");
            noPiaData.setText("Transaction not found.");
            noBarData.setText("Cash income/expense not found.");
        }
        if (UiUtil.getAccountingType(getContext()) == 1) {
            hint_text.setVisibility(View.VISIBLE);
            cashflow.setText("Cash flow");
            expbrak.setText("Breakup of cash outflow");
            noPiaData.setText("Transaction not found.");
            noBarData.setText("Cash Inflow/Outflow not found.");
        }
        spinner = this.view.findViewById(R.id.sp_filter);
        loadSearch(getContext());
    }

    private void setSpiner() {
        dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, filter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (start) {
                    getHomeDashBoardData(new GetDashboardRequest(fromDate, ud.getData().get(0).getStart(), ud.getData().get(0).getEnd(), UiUtil.getAccountingType(getContext()), 0), false);
                } else
                    getHomeDashBoardData(new GetDashboardRequest(fromDate, ud.getData().get(position).getStart(), ud.getData().get(position).getEnd(), UiUtil.getAccountingType(getContext()), 1), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void invoiceDueUI(InvoicePurchaseOverdue data) {

        this.invoiceNotDueAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + data.getDueAmount());
        try {
            this.invoiceOverdueAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + data.getOverdueAmount());
        } catch (Exception e) {
        }
    }

    private void billDueUI(InvoicePurchaseOverdue data) {
        this.billNotDueAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + data.getDueAmount());
        this.billOverdueAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + data.getOverdueAmount());
    }

    public void onResume() {
        super.onResume();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Calendar c = Calendar.getInstance();
        fromDate = simpleDateFormat.format(c.getTime());
    }

    private void getHomeDashBoardData(GetDashboardRequest getDashboardRequest, boolean refreshWithDates) {
        start = false;
        RestClient.getInstance(getActivity()).getDashboard(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), getDashboardRequest).enqueue(new CustomCallBack<DashboardData>(getContext(), null) {
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                super.onResponse(call, response);
                dashboardResponse = response.body();
                ActivitiesDashboardFragment.dashboardResponse = response.body();
                try {
                    if (dashboardResponse.getTransactionStatus().getIsSuccess()) {

                        if (dashboardResponse == null) {
                            return;
                        }
                        try {
                            if (dashboardResponse.getData().getCashFlow() == null || dashboardResponse.getData().getCashFlow().size() == 0) {
                                noCashFlowChart.setVisibility(View.VISIBLE);
                                cashFlowChart.setVisibility(View.GONE);
                            } else {
                                noCashFlowChart.setVisibility(View.GONE);
                                cashFlowChart.setVisibility(View.VISIBLE);
                                UiUtil.setCashFlowGraph(getContext(), cashFlowChart, dashboardResponse);
                            }
                        } catch (Exception e) {
                            noCashFlowChart.setVisibility(View.VISIBLE);
                            cashFlowChart.setVisibility(View.GONE);
                        }
                        try {
                            if (dashboardResponse.getData().getExpenseBreakdown() == null || dashboardResponse.getData().getExpenseBreakdown().size() == 0) {
                                noExpenseBreakdownChart.setVisibility(View.VISIBLE);
                                expenseBreakdownChart.setVisibility(View.GONE);
                            } else {
                                noExpenseBreakdownChart.setVisibility(View.GONE);
                                expenseBreakdownChart.setVisibility(View.VISIBLE);
                                UiUtil.setExpenseBreakdownGraph(getContext(), expenseBreakdownChart, dashboardResponse);
                            }
                        } catch (Exception e) {
                            noExpenseBreakdownChart.setVisibility(View.VISIBLE);
                            expenseBreakdownChart.setVisibility(View.GONE);
                        }

                        try {

                            if (dashboardResponse.getData().getInvoicePurchaseOverdues() != null) {
                                card_invoice.setVisibility(View.VISIBLE);
                                no_card_invoice.setVisibility(View.GONE);
                                invoiceDueUI(dashboardResponse.getData().getInvoicePurchaseOverdues().get(0));
                            } else {
                                card_invoice.setVisibility(View.GONE);
                                no_card_invoice.setVisibility(View.VISIBLE);
                            }
                            if (dashboardResponse.getData().getInvoicePurchaseOverdues() != null) {
                                cardBill.setVisibility(View.VISIBLE);
                                no_cardBill.setVisibility(View.GONE);
                                billDueUI(dashboardResponse.getData().getInvoicePurchaseOverdues().get(1));
                            } else {
                                cardBill.setVisibility(View.GONE);
                                no_cardBill.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                        }
                    } else {
                        Toast.makeText(getContext(), ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<DashboardData> call, Throwable t) {
                super.onFailure(call, t);
                noCashFlowChart.setVisibility(View.VISIBLE);
                noExpenseBreakdownChart.setVisibility(View.GONE);
            }
        });
    }

    public void loadSearch(Context mContext) {
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        Api api = ApiUtils.getAPIService();
        api.searchDashboard(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext())).enqueue(new Callback<SearchDashboardData>() {
            @Override
            public void onResponse(Call<SearchDashboardData> call, Response<SearchDashboardData> response) {
                UiUtil.cancelProgressDialogue();
                ud = new SearchDashboardData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 401) {
                        //UiUtil.showToast(mContext, "Session time out");
                        UiUtil.addLoginToSharedPref(mContext, false);
                        UiUtil.moveToSignUp(mContext);
                        ((Activity) mContext).finish();
                        return;
                    }
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
                            ud.setStatusMessage("Success");
                            ud = response.body();
                            if (ud != null) {
                                filter = SearchDashboardData.setFilter(ud.getData());
                                setSpiner();
                            }
                        } else {
                            ud.setStatus(444);
                            Toast.makeText(getContext(), ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString(), Toast.LENGTH_SHORT).show();
                            ud.setStatusMessage(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatus(444);
                        if (error.getError_description() == null) {
                            ud.setStatusMessage("Something went wrong");
                            //Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        } else {
                            ud.setStatusMessage(error.getError_description());
                            Log.d("ERROR :: ", error.getError_description());
                            Toast.makeText(getContext(), error.getError_description(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
//                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchDashboardData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                SearchDashboardData ud = new SearchDashboardData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
