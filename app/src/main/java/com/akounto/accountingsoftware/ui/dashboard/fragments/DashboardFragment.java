package com.akounto.accountingsoftware.ui.dashboard.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Data.Dashboard.BankBalance;
import com.akounto.accountingsoftware.Data.Dashboard.CashFlow;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.Dashboard.Data;
import com.akounto.accountingsoftware.Data.Dashboard.ExpenseBreakdown;
import com.akounto.accountingsoftware.Data.Dashboard.InvoicePurchaseOverdue;
import com.akounto.accountingsoftware.Data.Dashboard.ProjectedProfit;
import com.akounto.accountingsoftware.Data.DashboardSearchData.Datum;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.request.GetDashboardRequest;
import com.akounto.accountingsoftware.ui.base.fragments.BaseFragment;
import com.akounto.accountingsoftware.ui.dashboard.activity.DashboardProfileSetupActivity;
import com.akounto.accountingsoftware.ui.dashboard.adapter.DashboardOtherAdapter;
import com.akounto.accountingsoftware.ui.dashboard.adapter.DashboardSpinnerAdapter;
import com.akounto.accountingsoftware.ui.dashboard.viewModel.DashboardViewModel;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.akounto.accountingsoftware.util.UiUtil.isPieChartDataEmpty;

public class DashboardFragment extends BaseFragment {

    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String transactionDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    String fromDate = "";
    Context context;
    boolean start = true;
    SearchDashboardData searchDashboard = null;
    DashboardData dashboardDataModel = null;
    private DashboardViewModel dashboardViewModel;
    private TextView tvCashflow, tvCashflowBreakdown, tvIncome, tvExpense,
            tvSaleNotDue, tvSaleOverDue, tvCashflowDescription,
            tvCashflowBreakdownDescription, tvProjectedProfileDescription,
            tvSalesInvoiceDescription, tvPurchaseInvoiceDescription,
            tvPurchaseNotDue, tvPurchaseOverDue, tvPieTotalAmount, tvCompleteSetup;
    private LinearLayout linSaleInvoice, linPurchaseInvoice,
            linCashflowNoDataFound, linCashflowBreakdownNoDataFound,
            linProjectedProfileNoDataFound, linBreakDownCategories, linProjectedProfit;
    private CardView cvSalesNoDataFound, cvPurchaseNoDataFound,
            cvConnectedAccounts, cvBankTransactions, cvRecentActivity, cvProjectedProfit;
    private Spinner spYearFilter;
    private ScrollView svDashboard;

    private BarChart barChartCashFlowChart;
    //    private HorizontalBarChart barChartProjectedProfile;
    private BarChart barChartProjectedProfile;
    //    private CombinedChart barChartProjectedProfile;
    private PieChart pieChartCashFlowBreakdown;
    private ArrayList<Datum> filter = new ArrayList<>();
    private DashboardSpinnerAdapter dataAdapter = null;
    private int ConnectedAccounts = 1;
    private int BankTransactions = 2;
    private int RecentActivities = 3;

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard2, container, false);

        initView(view);
        clickListener();
        setInitialData();

        setAdapter();

        getDashboardSearchData();

        return view;
    }

    private void initView(View view) {
        svDashboard = view.findViewById((R.id.svDashboard));

        //Cash Flow
        tvCashflow = view.findViewById((R.id.tvCashflow));
        tvCashflowDescription = view.findViewById(R.id.tvCashflowDescription);
        linCashflowNoDataFound = view.findViewById(R.id.linCashflowNoDataFound);
        barChartCashFlowChart = view.findViewById(R.id.barChartCashFlowChart);
        tvIncome = view.findViewById(R.id.tvIncome);
        tvExpense = view.findViewById(R.id.tvExpense);


        //Cash Flow Break Down
        tvCashflowBreakdown = view.findViewById(R.id.tvCashflowBreakdown);
        tvCashflowBreakdownDescription = view.findViewById(R.id.tvCashflowBreakdownDescription);
        linCashflowBreakdownNoDataFound = view.findViewById(R.id.linCashflowBreakdownNoDataFound);
        pieChartCashFlowBreakdown = view.findViewById(R.id.pieChartCashFlowBreakdown);
        linBreakDownCategories = view.findViewById(R.id.linBreakDownCategories);

        //Projected Profit
        tvProjectedProfileDescription = view.findViewById(R.id.tvProjectedProfileDescription);
        linProjectedProfileNoDataFound = view.findViewById(R.id.linProjectedProfileNoDataFound);
        barChartProjectedProfile = view.findViewById(R.id.barChartProjectedProfile);
        linProjectedProfit = view.findViewById(R.id.linProjectedProfit);
        cvProjectedProfit = view.findViewById(R.id.cvProjectedProfit);

        //Sales Invoice
        tvSalesInvoiceDescription = view.findViewById(R.id.tvSalesInvoiceDescription);

        //Purchase Invoice
        tvPurchaseInvoiceDescription = view.findViewById(R.id.tvPurchaseInvoiceDescription);


        tvSaleNotDue = view.findViewById(R.id.tvSaleNotDue);
        tvSaleOverDue = view.findViewById(R.id.tvSaleOverDue);
        tvPurchaseNotDue = view.findViewById(R.id.tvPurchaseNotDue);
        tvPurchaseOverDue = view.findViewById(R.id.tvPurchaseOverDue);
        tvPieTotalAmount = view.findViewById(R.id.tvPieTotalAmount);

        linSaleInvoice = view.findViewById(R.id.linSaleInvoice);
        linPurchaseInvoice = view.findViewById(R.id.linPurchaseInvoice);

        cvSalesNoDataFound = view.findViewById(R.id.cvSalesNoDataFound);
        cvPurchaseNoDataFound = view.findViewById(R.id.cvPurchaseNoDataFound);

        cvConnectedAccounts = view.findViewById(R.id.cvConnectedAccounts);
        cvBankTransactions = view.findViewById(R.id.cvBankTransactions);
        cvRecentActivity = view.findViewById(R.id.cvRecentActivity);

        spYearFilter = view.findViewById(R.id.spYearFilter);

        tvCompleteSetup = view.findViewById(R.id.tvCompleteSetup);
    }

    private void clickListener() {
        cvConnectedAccounts.setOnClickListener(
                v -> showBottomSheetDialog(ConnectedAccounts)
        );

        cvBankTransactions.setOnClickListener(
                v -> showBottomSheetDialog(BankTransactions)
        );

        cvRecentActivity.setOnClickListener(
                v -> showBottomSheetDialog(RecentActivities)
        );

        tvCompleteSetup.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DashboardProfileSetupActivity.class));
        });

        spYearFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fromDate = getCurrentDate();
                GetDashboardRequest dashboardRequest = null;

                if (searchDashboard.getData() != null) {
                    if (start) {
                        dashboardRequest = new GetDashboardRequest(
                                fromDate,
                                searchDashboard.getData().get(position).getStart(),
                                searchDashboard.getData().get(position).getEnd(),
                                UiUtil.getAccountingType(getContext()),
                                0);
                    } else {
                        dashboardRequest = new GetDashboardRequest(
                                fromDate,
                                searchDashboard.getData().get(position).getStart(),
                                searchDashboard.getData().get(position).getEnd(),
                                UiUtil.getAccountingType(getContext()),
                                1);
                    }
                    if (dashboardRequest != null) {
                        getHomeDashBoardData(dashboardRequest);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setInitialData() {
        Dexter.withActivity(getActivity())
                .withPermissions("android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE")
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                    }
                })
                .check();

        this.context = getActivity();
        dashboardViewModel = new ViewModelProviders().of(getActivity()).get(DashboardViewModel.class);

        if (UiUtil.getAccountingType(getContext()) == 2) {
            //show the projected profit only in case of AccrualBasis
            cvProjectedProfit.setVisibility(View.VISIBLE);

            tvCashflow.setText("Income and Expense");
            tvCashflowDescription.setText("Income earned and expenses incurred over a selected period of time");
            tvIncome.setText("Income");
            tvExpense.setText("Expense");


            tvCashflowBreakdown.setText("Top Expenses");
            tvCashflowBreakdownDescription.setText("List of top expenses over a selected period of time.");

            tvProjectedProfileDescription.setText("Estimated profit for the current month.");

            tvSalesInvoiceDescription.setText("Total incoming which are to be received.");

            tvPurchaseInvoiceDescription.setText("Total expenses which are to be paid.");

        } else if (UiUtil.getAccountingType(getContext()) == 1) {
            //Hide the projected profit  in case of CashBasis
            cvProjectedProfit.setVisibility(View.GONE);

            tvCashflow.setText("Cash flow");
            tvCashflowDescription.setText("Summary of cash received and paid during a selected period of time.");
            tvIncome.setText("Inflow");
            tvExpense.setText("Outflow");


            tvCashflowBreakdown.setText("Breakup of cash outflow");
            tvCashflowBreakdownDescription.setText("Breakdown of cash outflow for a selected period of time.");

            tvProjectedProfileDescription.setText("");

            tvSalesInvoiceDescription.setText("");

            tvPurchaseInvoiceDescription.setText("");
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Calendar c = Calendar.getInstance();
        String currentDateTime = "";
        currentDateTime = simpleDateFormat.format(c.getTime());

        return currentDateTime;
    }

    private void setAdapter() {
        dataAdapter = new DashboardSpinnerAdapter(getContext(), filter);
        spYearFilter.setAdapter(dataAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(int dialogType) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = View.inflate(context, R.layout.bottom_sheet_dialog_layout, null);
        bottomSheetDialog.setContentView(view);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetBehavior.setPeekHeight(0);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheetBehavior.setPeekHeight(0);
                    //bottomSheetBehavior.setHideable(true);

                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheetBehavior.setPeekHeight(0);
                    //bottomSheetBehavior.setHideable(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        TextView tvBottonSheetTitle = bottomSheetDialog.findViewById(R.id.tvBottonSheetTitle);
        TextView tvBottonSheetDescription = bottomSheetDialog.findViewById(R.id.tvBottonSheetDescription);
        TextView tvViewAllButton = bottomSheetDialog.findViewById(R.id.tvViewAllButton);

        //CardView cvViewAllButton = dialog.findViewById(R.id.cvViewAllButton);
        //LinearLayout linBottomSheetOthers = dialog.findViewById(R.id.linBottomSheetOthers);

        RecyclerView rvBottomSheetOthers = bottomSheetDialog.findViewById(R.id.rvBottomSheetOthers1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvBottomSheetOthers.setLayoutManager(linearLayoutManager);

        DashboardOtherAdapter adapter = null;

        if (dialogType == ConnectedAccounts) {
            tvBottonSheetTitle.setText("Connected Accounts");
            tvBottonSheetDescription.setText("List of connected bank accounts");
            tvViewAllButton.setText("View all Accounts");

            Log.e("DashboardFragment", "ConnectedAccounts Size "
                    + dashboardDataModel.getData().getBankBalance().size());

            List<BankBalance> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(dashboardDataModel.getData().getBankBalance().get(i));
            }

            adapter = new DashboardOtherAdapter(getActivity(), dialogType);
            adapter.setConnectedAccountList(list);

        } else if (dialogType == BankTransactions) {
            tvBottonSheetTitle.setText("Bank transactions");
            tvBottonSheetDescription.setText("List of recent bank transaction");
            tvViewAllButton.setText("View Bank transactions");

            Log.e("DashboardFragment", "BankTransactions Size "
                    + dashboardDataModel.getData().getLastBankTransactions().size());

            adapter = new DashboardOtherAdapter(getActivity(), dialogType);
            adapter.setBankTransactionList(dashboardDataModel.getData().getLastBankTransactions());

        } else if (dialogType == RecentActivities) {
            tvBottonSheetTitle.setText("Recent activities");
            tvBottonSheetDescription.setText("List of recent activities in your account");
            tvViewAllButton.setText("View all activities");

            Log.e("DashboardFragment", "RecentActivities Size "
                    + dashboardDataModel.getData().getLastActivities().size());

            adapter = new DashboardOtherAdapter(getActivity(), dialogType);
            adapter.setRecentActivityList(dashboardDataModel.getData().getLastActivities());
        }

        if (adapter != null) {
            rvBottomSheetOthers.setAdapter(adapter);
        }

        bottomSheetDialog.show();
    }


    //************* Api Calling *************
    private void getDashboardSearchData() {
        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        dashboardViewModel.getSearch(context).observe(getActivity(), new Observer<SearchDashboardData>() {
            @Override
            public void onChanged(SearchDashboardData searchDashboardData) {
                UiUtil.cancelProgressDialogue();

                if (searchDashboardData != null) {
                    searchDashboard = new SearchDashboardData();
                    searchDashboard = searchDashboardData;
                    if (searchDashboardData.getData() != null) {
                        if (filter.size() > 0) {
                            filter.clear();
                        }

                        filter.addAll(searchDashboardData.getData());
                        if (filter.size() > 0) {
                            dataAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void getHomeDashBoardData(GetDashboardRequest getDashboardRequest) {
        start = false;

        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        try {
            dashboardViewModel.getDataDashboard(getDashboardRequest).observe(getActivity(),
                    new Observer<DashboardData>() {
                        @Override
                        public void onChanged(DashboardData dashboardData) {
                            //if (dashboardData.getStatus() == 200) {

                            if (dashboardData != null) {
                                dashboardDataModel = dashboardData;

                                Data data = dashboardData.getData();

                                if (data != null) {
                                    //Cash Flow
                                    setCashFlowChart(dashboardData);

                                    //Cash flow Breakdown
                                    setCashFlowBreakDownChart(dashboardData);

                                    //Projected Profit
                                    setProjectedProfitChart(dashboardData);

                                    //Sales and Purchase Invoice
                                    if (data.getInvoicePurchaseOverdues() != null
                                            && data.getInvoicePurchaseOverdues().size() > 0) {
                                        setSalesAndPurchaseInvoice(data.getInvoicePurchaseOverdues());
                                    } else {
                                        linSaleInvoice.setVisibility(View.GONE);
                                        linPurchaseInvoice.setVisibility(View.GONE);

                                        cvSalesNoDataFound.setVisibility(View.VISIBLE);
                                        cvPurchaseNoDataFound.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            //}
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Projected Profit
    @SuppressLint("SetTextI18n")
    private void setProjectedProfitChart(DashboardData dashboardData) {

        //show the projected profit only in case of AccrualBasis
        if (UiUtil.getAccountingType(getContext()) == 2) {
            Data data = dashboardData.getData();

            if (data.getCashFlow() != null && data.getCashFlow().size() > 0) {
                linProjectedProfileNoDataFound.setVisibility(View.GONE);
                barChartProjectedProfile.setVisibility(View.VISIBLE);

                //setProjectedProfitGraph(getContext(), barChartProjectedProfile, dashboardData);
                setProjectedProfitGraph(getContext(), barChartProjectedProfile, dashboardData);
                getProjectedProfitIndicator(data);


            } else {
                linProjectedProfileNoDataFound.setVisibility(View.VISIBLE);
                barChartProjectedProfile.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void getProjectedProfitIndicator(Data data) {

        //if any data in the linProjectedProfitIndicator list
        if (linProjectedProfit.getChildCount() > 0) {
            linProjectedProfit.removeAllViews();
        }

        for (int i = 0; i < data.getProjectedProfits().size(); i++) {
            ProjectedProfit projectedProfit = data.getProjectedProfits().get(i);

            //Categories Indicator View Start
            View view = getLayoutInflater().inflate(R.layout.item_categories_expense_break_layout, null);

            View viewCircleIndicator = view.findViewById(R.id.viewCircleIndicator);
            View viewBarIndicator = view.findViewById(R.id.viewBarIndicator);
            TextView tvIndicatorTitle = view.findViewById(R.id.tvIndicatorTitle);
            TextView tvIndicatorAmount = view.findViewById(R.id.tvIndicatorAmount);

            tvIndicatorAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext())
                    + " " + getAmountWithSymbol(projectedProfit.getAmount()));

            int color = 0;
            if (projectedProfit.getType() == 100) { //Actual
                color = Color.parseColor("#1a8b8c");
                tvIndicatorTitle.setText("Actual");

                viewBarIndicator.setVisibility(View.GONE);
                viewCircleIndicator.setVisibility(View.VISIBLE);

                GradientDrawable drawable = (GradientDrawable) viewCircleIndicator.getBackground();
                drawable.setStroke(2, color);
                drawable.setColor(color);

            } else if (projectedProfit.getType() == 200) {  // Projected
                color = Color.parseColor("#f68477");
                tvIndicatorTitle.setText("Projected Profit");

                viewBarIndicator.setVisibility(View.GONE);
                viewCircleIndicator.setVisibility(View.VISIBLE);

                GradientDrawable drawable = (GradientDrawable) viewCircleIndicator.getBackground();
                drawable.setStroke(2, color);
                drawable.setColor(color);

                //linProjectedProfit.addView(view, 1);

            } else if (projectedProfit.getType() == 300) {  //Target or goal
                color = Color.parseColor("#BDBDBD");
                tvIndicatorTitle.setText("Goal");

                viewBarIndicator.setVisibility(View.VISIBLE);
                viewCircleIndicator.setVisibility(View.GONE);

                viewBarIndicator.setBackgroundColor(color);

                //linProjectedProfit.addView(view, 2);
            }

            linProjectedProfit.addView(view);
            //Categories Indicator View End
        }
    }

    /*public void setProjectedProfitGraph(Context mContext, HorizontalBarChart barChart, DashboardData dbdata) {
        try {
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on clik
            barChart.setScaleEnabled(false);// disable the barChart scaling

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);

            // draw shadows for each bar that show the maximum value
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on clik
            barChart.setScaleEnabled(false);// disable the barChart scaling

//            barChart.getAxisLeft().setDrawAxisLine(false);
//            barChart.getXAxis().setDrawAxisLine(false);
//            barChart.getAxisRight().setEnabled(false);


            // if more than 60 entries are displayed in the chart, no values will be drawn
            barChart.setMaxVisibleValueCount(50);

            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");

            ArrayList<String> xAxisLabels = new ArrayList<>();
            xAxisLabels.add("100.0K");
            xAxisLabels.add("200.0K");
            xAxisLabels.add("300.0K");
            xAxisLabels.add("400.0K");
            xAxisLabels.add("500.0K");
            IndexAxisValueFormatter custom = new IndexAxisValueFormatter((Collection<String>) xAxisLabels);


            //Graph Left side axis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(poppins_regular);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);  //line enable when true and disable when false
//            xAxis.setGranularity(10f);
//            xAxis.setLabelCount(5);
//            xAxis.setValueFormatter(custom);
            xAxis.setAxisMinimum(0.0f);


            //graph top axis
            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setTypeface(poppins_regular);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawGridLines(false);
            yAxis.setDrawLabels(false);
            //yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            //yAxis.setInverted(true);


            //graph Bottom axis
            YAxis yr = barChart.getAxisRight();
            yr.setTypeface(poppins_regular);
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(true);
            //yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            //yr.setInverted(true);


            barChart.setFitBars(true);
            barChart.animateY(2500);

            // setting data
            setData(barChart, 3, 10);


            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setFormSize(80f);
            l.setXEntrySpace(4f);
            l.setEnabled(false);

        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }*/

    private void setData(HorizontalBarChart chart, int count, float range) {

        float barWidth = 5f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            Log.e("DashboardFragment", "i * spaceForBar " + (i * spaceForBar) + " val " + val);
            values.add(new BarEntry(i * spaceForBar, val));
        }

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "DataSet 1");
            set1.setColor(Color.parseColor("#1a8b8c"));// green
            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            chart.setData(data);
        }
    }

    /*public void setProjectedProfitGraph(Context mContext, BarChart barChart, DashboardData dbdata) {
        try {
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on click
            barChart.setScaleEnabled(false);// disable the barChart scaling

            barChart.getAxisLeft().setDrawAxisLine(true);
            barChart.getXAxis().setDrawAxisLine(true);
            barChart.getAxisRight().setEnabled(false);

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(false);

            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(0.0f);
            xAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);  //Line dash
            xAxis.setDrawGridLines(false);   //line enable when true and disable when false
            xAxis.setTypeface(poppins_regular);

            //xAxis.setCenterAxisLabels(true);
//            xAxis.setLabelCount(xAxisItemsCount);
//            xAxis.setValueFormatter(custom);
            //xAxis.setAxisMinimum(0.0f);


            YAxis yAxis = barChart.getAxisLeft();
            //yAxis.setSpaceTop(35.0f);  // set the difference between value in y-axis
            //yAxis.setAxisMinimum(0.0f);
            yAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);   //Line dash
            yAxis.setDrawGridLines(false);   //line enable when true and disable when false
            yAxis.setTypeface(poppins_regular);
            //yAxis.setValueFormatter(new LargeValueFormatter());

            int i = 0;
            ArrayList<BarEntry> actual = new ArrayList<>();
            ArrayList<BarEntry> projected = new ArrayList<>();
            ArrayList<BarEntry> goal = new ArrayList<>();

            BarDataSet actualDataSet = null, projectedDataSet = null, goalDataSet = null;

            ArrayList<IBarDataSet> barDataSetList = new ArrayList<>();

            for (ProjectedProfit projectedProfit : dbdata.getData().getProjectedProfits()) {
                Log.e("DashboardFragment", "Type : " + projectedProfit.getType()
                        + " Amount : " + projectedProfit.getAmount());

                if (projectedProfit.getType() == 100) { //Actual
                    actual.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    actualDataSet = new BarDataSet(actual, "Actual");
                    actualDataSet.setColor(Color.parseColor("#1a8b8c"));// green

                    barDataSetList.add(actualDataSet);

                } else if (projectedProfit.getType() == 200) {  // Projected
                    projected.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    projectedDataSet = new BarDataSet(projected, "Projected");
                    projectedDataSet.setColor(Color.parseColor("#F68477"));//red

                    barDataSetList.add(projectedDataSet);

                } else if (projectedProfit.getType() == 300) {  //Target or goal
                    goal.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    goalDataSet = new BarDataSet(goal, "Goal");
                    goalDataSet.setColor(Color.parseColor("#BDBDBD"));//grey

                    barDataSetList.add(goalDataSet);
                }
                //i++;
            }

            ArrayList<String> monthList = new ArrayList<>();
            monthList.add("Jan");
            monthList.add("Feb");
            monthList.add("Mar");

            BarData barData = new BarData(barDataSetList);
            barChart.setData(barData);
            //barChart.invalidate();

        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }*/

    public void setProjectedProfitGraph(Context mContext, BarChart barChart, DashboardData dbdata) {
        try {
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on click
            barChart.setScaleEnabled(false);// disable the barChart scaling

            barChart.getAxisLeft().setDrawAxisLine(true);
            barChart.getXAxis().setDrawAxisLine(true);
            barChart.getAxisRight().setEnabled(false);

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(false);
            /*l.setYOffset(10.0f);
            l.setXOffset(0.0f);
            l.setYEntrySpace(0.0f);
            l.setTextSize(10.0f);*/

            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");

            ArrayList<String> xAxisLabels = new ArrayList<>();
            xAxisLabels.add("August");

            int xAxisItemsCount = 1;
            IndexAxisValueFormatter custom = new IndexAxisValueFormatter((Collection<String>) xAxisLabels);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(0.0f);
            xAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);  //Line dash
            xAxis.setDrawGridLines(false);   //line enable when true and disable when false
            xAxis.setTypeface(poppins_regular);
            xAxis.setLabelCount(xAxisItemsCount);
            xAxis.setValueFormatter(custom);
//            xAxis.setCenterAxisLabels(true);
//            xAxis.setAxisMinimum(0.0f);


            YAxis yAxis = barChart.getAxisLeft();
            //yAxis.setSpaceTop(35.0f);  // set the difference between value in y-axis
            //yAxis.setAxisMinimum(0.0f);
            yAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);   //Line dash
            yAxis.setDrawGridLines(false);   //line enable when true and disable when false
            yAxis.setTypeface(poppins_regular);
            //yAxis.setValueFormatter(new LargeValueFormatter());

            int i = 0;
            ArrayList<BarEntry> actual = new ArrayList<>();
            ArrayList<BarEntry> projected = new ArrayList<>();
            ArrayList<BarEntry> goal = new ArrayList<>();

            BarDataSet actualDataSet = null, projectedDataSet = null, goalDataSet = null;

            ArrayList<IBarDataSet> barDataSetList = new ArrayList<>();

            for (ProjectedProfit projectedProfit : dbdata.getData().getProjectedProfits()) {
                Log.e("DashboardFragment", "Type : " + projectedProfit.getType()
                        + " Amount : " + projectedProfit.getAmount());

                if (projectedProfit.getType() == 100) { //Actual
                    actual.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    actualDataSet = new BarDataSet(actual, "Actual");
                    actualDataSet.setColor(Color.parseColor("#1a8b8c"));// green

                    barDataSetList.add(actualDataSet);

                } else if (projectedProfit.getType() == 200) {  // Projected
                    projected.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    projectedDataSet = new BarDataSet(projected, "Projected");
                    projectedDataSet.setColor(Color.parseColor("#F68477"));//red

                    barDataSetList.add(projectedDataSet);

                } else if (projectedProfit.getType() == 300) {  //Target or goal
                    goal.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    goalDataSet = new BarDataSet(goal, "Goal");
                    goalDataSet.setColor(Color.parseColor("#EEEEEE"));//grey

                    barDataSetList.add(goalDataSet);
                }
                //i++;
            }

            BarData barData = new BarData(barDataSetList);
            barChart.setData(barData);
            //barChart.invalidate();

        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }

    public void setProjectedProfitGraph(Context mContext, CombinedChart barChart, DashboardData dbdata) {
        try {
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on click
            barChart.setScaleEnabled(false);// disable the barChart scaling

            barChart.getAxisLeft().setDrawAxisLine(true);
            barChart.getXAxis().setDrawAxisLine(true);
            barChart.getAxisRight().setEnabled(false);

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setEnabled(false);

            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");

            ArrayList<String> xAxisLabels = new ArrayList<>();
            xAxisLabels.add("August");

            int xAxisItemsCount = 1;
            IndexAxisValueFormatter custom = new IndexAxisValueFormatter((Collection<String>) xAxisLabels);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(0.0f);
            xAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);  //Line dash
            xAxis.setDrawGridLines(false);   //line enable when true and disable when false
            xAxis.setTypeface(poppins_regular);
            xAxis.setLabelCount(xAxisItemsCount);
            xAxis.setValueFormatter(custom);
//            xAxis.setCenterAxisLabels(true);
//            xAxis.setAxisMinimum(0.0f);


            YAxis yAxis = barChart.getAxisLeft();
            //yAxis.setSpaceTop(35.0f);  // set the difference between value in y-axis
            //yAxis.setAxisMinimum(0.0f);
            yAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);   //Line dash
            yAxis.setDrawGridLines(false);   //line enable when true and disable when false
            yAxis.setTypeface(poppins_regular);
            //yAxis.setValueFormatter(new LargeValueFormatter());
            //yAxis.setAxisMinimum(0.0f);


            int i = 0;
            ArrayList<BarEntry> actual = new ArrayList<>();
            ArrayList<BarEntry> projected = new ArrayList<>();
            ArrayList<BarEntry> goal = new ArrayList<>();
            ArrayList<Entry> entries = new ArrayList<Entry>();

            BarDataSet actualDataSet = null, projectedDataSet = null, goalDataSet = null;

            ArrayList<IBarDataSet> barDataSetList = new ArrayList<>();

            for (ProjectedProfit projectedProfit : dbdata.getData().getProjectedProfits()) {
                Log.e("DashboardFragment", "Type : " + projectedProfit.getType()
                        + " Amount : " + projectedProfit.getAmount());

                if (projectedProfit.getType() == 100) { //Actual
                    actual.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    actualDataSet = new BarDataSet(actual, "Actual");
                    actualDataSet.setColor(Color.parseColor("#1a8b8c"));// green

                    barDataSetList.add(actualDataSet);

                } else if (projectedProfit.getType() == 200) {  // Projected
                    projected.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    projectedDataSet = new BarDataSet(projected, "Projected");
                    projectedDataSet.setColor(Color.parseColor("#F68477"));//red

                    barDataSetList.add(projectedDataSet);

                } else if (projectedProfit.getType() == 300) {  //Target or goal
//                    goal.add(new BarEntry(i, Float.parseFloat(projectedProfit.getAmount().toString())));
                    entries.add(new Entry(i, Float.parseFloat(projectedProfit.getAmount().toString())));

                    /*goalDataSet = new BarDataSet(goal, "Goal");
                    goalDataSet.setColor(Color.parseColor("#EEEEEE"));//grey

                    barDataSetList.add(goalDataSet);*/
                }
                //i++;
            }


            LineDataSet set = new LineDataSet(entries, "Line DataSet");
            set.setColors(Color.parseColor("#000000"));
//            set.setLineWidth(20f);
            //set.setCircleColor(Color.rgb(240, 238, 70));
            //set.setCircleRadius(5f);
            //set.setFillColor(Color.rgb(240, 238, 70));
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawValues(true);
            //set.setValueTextSize(10f);
            set.setValueTextColor(Color.rgb(115, 180, 236));
//            set.setValueTextColor(Color.rgb(0, 0, 0));
            set.setAxisDependency(YAxis.AxisDependency.LEFT);


            LineData d = new LineData();
            d.addDataSet(set);


            BarData barData = new BarData(barDataSetList);
//            barChart.setData(barData);

            CombinedData combinedData = new CombinedData();
            combinedData.setData(barData);
            combinedData.setData(d);
            combinedData.setValueTypeface(poppins_regular);
//
            barChart.setData(combinedData);

            barChart.setScaleMinima((float) (barData.getXMax() + 0.25f) / 10f, 1f);
            for (IDataSet set11 : barChart.getData().getDataSets()) {
                if (set11 instanceof BarDataSet)
                    set11.setDrawValues(!set11.isDrawValuesEnabled());
            }

            barChart.invalidate();

            //barChart.invalidate();

        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }


    //Cash Flow Break Down Pie Chart
    @SuppressLint("SetTextI18n")
    private void setCashFlowBreakDownChart(DashboardData dashboardData) {
        Data data = dashboardData.getData();

        if (data.getCashFlow() != null && data.getCashFlow().size() > 0) {
            linCashflowBreakdownNoDataFound.setVisibility(View.GONE);
            pieChartCashFlowBreakdown.setVisibility(View.VISIBLE);

            setExpenseBreakdownGraph(getContext(), pieChartCashFlowBreakdown, dashboardData);

            double total = 0;
            for (int i = 0; i < data.getExpenseBreakdown().size(); i++) {
                ExpenseBreakdown expenseBreakdownItem = data.getExpenseBreakdown().get(i);
                total = total + expenseBreakdownItem.getAmount();
            }
            tvPieTotalAmount.setText("Total amount: "
                    + UiUtil.getBussinessCurrenSymbul(getContext()) + " " + total);

            getBreakDownPieChartIndicator(data);

        } else {
            linCashflowBreakdownNoDataFound.setVisibility(View.VISIBLE);
            pieChartCashFlowBreakdown.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getBreakDownPieChartIndicator(Data data) {

        //if any data in the linProjectedProfitIndicator list
        if (linBreakDownCategories.getChildCount() > 0) {
            linBreakDownCategories.removeAllViews();
        }

        for (int i = 0; i < data.getExpenseBreakdown().size(); i++) {
            ExpenseBreakdown expenseBreakdownItem = data.getExpenseBreakdown().get(i);

            //Categories Indicator View Start
            View view = getLayoutInflater().inflate(R.layout.item_categories_expense_break_layout, null);

            View viewCircleIndicator = view.findViewById(R.id.viewCircleIndicator);
            TextView tvIndicatorTitle = view.findViewById(R.id.tvIndicatorTitle);
            TextView tvIndicatorAmount = view.findViewById(R.id.tvIndicatorAmount);

            int color = 0;
            if (i == 0) {
                color = Color.parseColor("#f8b886");
            } else if (i == 1) {
                color = Color.parseColor("#c96666");
            } else if (i == 2) {
                color = Color.parseColor("#66c2c9");
            } else if (i == 3) {
                color = Color.parseColor("#6677c9");
            } else if (i == 4) {
                color = Color.parseColor("#6693c9");
            }

            GradientDrawable drawable = (GradientDrawable) viewCircleIndicator.getBackground();
            drawable.setStroke(2, color);
            drawable.setColor(color);

            if (!TextUtils.isEmpty(expenseBreakdownItem.getName())) {
                tvIndicatorTitle.setText(expenseBreakdownItem.getName());
            }
            /*tvIndicatorAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext())
                    + " " + expenseBreakdownItem.getAmount().toString() + "K");*/

            tvIndicatorAmount.setText(UiUtil.getBussinessCurrenSymbul(getContext())
                    + " " + getAmountWithSymbol(expenseBreakdownItem.getAmount()));

            linBreakDownCategories.addView(view);
            //Categories Indicator View End
        }
    }

    public void setExpenseBreakdownGraph(Context mContext, PieChart peiChart, DashboardData dbdata) {
        try {
            peiChart.getLegend().setEnabled(false);

            peiChart.setUsePercentValues(true);
            peiChart.setClickable(false);
            peiChart.getDescription().setEnabled(false);
            peiChart.setExtraOffsets(5.0f, 10.0f, 5.0f, 5.0f);

            peiChart.setDragDecelerationFrictionCoef(0.95f);

            //Enable hole in the pie chart and color
            peiChart.setDrawHoleEnabled(true);
            peiChart.setHoleColor(Color.WHITE);

            peiChart.setTransparentCircleColor(Color.WHITE);
            peiChart.setTransparentCircleAlpha(110);

            //set the hole radius
            peiChart.setHoleRadius(70f);
            peiChart.setTransparentCircleRadius(60f);

            //Rotation angle and enable or disable rotation and disable highlightPerTab
            peiChart.setRotationAngle(0);
            peiChart.setRotationEnabled(false);
            peiChart.setHighlightPerTapEnabled(false);


            peiChart.animateY(1400, Easing.EaseInOutQuad);
            peiChart.setEntryLabelColor(mContext.getResources().getColor(R.color.text_color_blue));
            peiChart.setEntryLabelTextSize(12.0f);


            Typeface poppins_semibold = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_semibold.ttf");
            peiChart.setEntryLabelTypeface(poppins_semibold);

            ArrayList<PieEntry> expenses = new ArrayList<>();
            for (ExpenseBreakdown expenseBreakdownItem : dbdata.getData().getExpenseBreakdown()) {
                expenses.add(new PieEntry(Float.parseFloat(expenseBreakdownItem.getAmount().toString()),
                        expenseBreakdownItem.getName()));
            }

            PieDataSet pieDataSet = new PieDataSet(expenses, "Expense Breakdown");
            pieDataSet.setDrawIcons(false);
            pieDataSet.setSliceSpace(0.5f);
            pieDataSet.setSelectionShift(5.0f);


            //Set the colors in the pie chart
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#f8b886"));
            colors.add(Color.parseColor("#c96666"));
            colors.add(Color.parseColor("#66c2c9"));
            colors.add(Color.parseColor("#6677c9"));
            colors.add(Color.parseColor("#6693c9"));

            for (int c : ColorTemplate.LIBERTY_COLORS) {
                colors.add(Integer.valueOf(c));
            }

            colors.add(Integer.valueOf(ColorTemplate.getHoloBlue()));
            pieDataSet.setColors((List<Integer>) colors);

            pieDataSet.setValueLinePart1OffsetPercentage(80.0f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.5f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setUsingSliceColorAsValueLineColor(true);
            pieDataSet.setValueLineWidth(2.0f);
            //pieDataSet.setValueFormatter(new UiUtil.PercentageValueFormatter());
            pieDataSet.setValueFormatter(new PercentFormatter());
            pieDataSet.setValueLineVariableLength(true);


            PieData data = new PieData(pieDataSet);
            data.setValueTypeface(poppins_semibold);
            data.setValueTextSize(16.0f);
            data.setValueTextColor(mContext.getResources().getColor(R.color.text_color));

            if (isPieChartDataEmpty(expenses)) {
                peiChart.setData(data);
                peiChart.highlightValues((Highlight[]) null);

                //remove the labels
                peiChart.setDrawEntryLabels(false);

                for (IDataSet<?> set : peiChart.getData().getDataSets()) {
                    set.setDrawValues(false);
                }
                peiChart.invalidate();
            }
        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }


    //Cash Flow Bar Chart
    private void setCashFlowChart(DashboardData dashboardData) {
        Data data = dashboardData.getData();

        if (data.getCashFlow() != null && data.getCashFlow().size() > 0) {
            linCashflowNoDataFound.setVisibility(View.GONE);
            barChartCashFlowChart.setVisibility(View.VISIBLE);
            setCashFlowGraph(getContext(), barChartCashFlowChart, dashboardData);

        } else {
            linCashflowNoDataFound.setVisibility(View.VISIBLE);
            barChartCashFlowChart.setVisibility(View.GONE);
        }
    }

    public void setCashFlowGraph(Context mContext, BarChart barChart, DashboardData dbdata) {
        try {
            barChart.getDescription().setEnabled(false);
            barChart.setPinchZoom(false);
            barChart.setClickable(false);
            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
            barChart.setHighlightPerTapEnabled(false);// disable the bar data on clik
            barChart.setScaleEnabled(false);// disable the barChart scaling

            barChart.getAxisLeft().setDrawAxisLine(false);
            barChart.getXAxis().setDrawAxisLine(false);
            barChart.getAxisRight().setEnabled(false);


            Typeface poppins_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/poppins_regular.ttf");

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // set the label and indicator alignment like above graph or bottom graph
            l.setEnabled(false);    // this line remove the label and indicator from the graph like in our case Income and Expense
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setYOffset(10.0f);
            l.setXOffset(0.0f);
            l.setYEntrySpace(0.0f);
            l.setTextSize(10.0f);
            //l.setTextColor(getResources().getColor(R.color.grey_text));


            ArrayList<BarEntry> incomeValues = new ArrayList<>();
            ArrayList<BarEntry> outComeValues = new ArrayList<>();
            ArrayList<String> xAxisLabels = new ArrayList<>();

            int i = 0;
            for (CashFlow cashFlowItem : dbdata.getData().getCashFlow()) {
                incomeValues.add(new BarEntry((float) i, Float.parseFloat(cashFlowItem.getCredit().toString())));
                outComeValues.add(new BarEntry((float) i, Float.parseFloat(cashFlowItem.getDredit().toString())));
                xAxisLabels.add(cashFlowItem.getLabel().substring(0, 3));
                i++;
            }

            float groupSpace = 0.3f;
            float barSpace = 0.03f;
            float barWidth = 0.32f;

            int xAxisItemsCount = Math.max(incomeValues.size(), outComeValues.size());
            IndexAxisValueFormatter custom = new IndexAxisValueFormatter((Collection<String>) xAxisLabels);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(270.0f);  // for the x-axis label rotation
//            xAxis.setLabelRotationAngle(0.0f);
            xAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);  //Line dash
            xAxis.setDrawGridLines(false);   //line enable when true and disable when false
            xAxis.setTypeface(poppins_regular);
            xAxis.setCenterAxisLabels(true);
            xAxis.setLabelCount(xAxisItemsCount);
            xAxis.setValueFormatter(custom);
            xAxis.setAxisMinimum(0.0f);


            YAxis yAxis = barChart.getAxisLeft();
            //yAxis.setSpaceTop(35.0f);  // set the difference between value in y-axis
            yAxis.setAxisMinimum(0.0f);
            yAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);   //Line dash
            yAxis.setDrawGridLines(true);   //line enable when true and disable when false
            yAxis.setTypeface(poppins_regular);
            yAxis.setValueFormatter(new LargeValueFormatter());
            /*yAxis.setAxisMaximum(200);      //Maximum value of Y axis
            yAxis.setAxisMinimum(0);      //Y-axis minimum value
            //The number of Y-axis coordinates The second parameter is generally false true to indicate that the number of labels is mandatory, which may cause problems such as incomplete display of X-axis coordinates.
            yAxis.setLabelCount(10, false);*/


            BarDataSet inCome = null, outCome = null;
            if (UiUtil.getAccountingType(mContext) == 2) {
                inCome = new BarDataSet(incomeValues, "Income");
                inCome.setColor(Color.parseColor("#1a8b8c"));// green

                outCome = new BarDataSet(outComeValues, "Expense");
                outCome.setColor(Color.parseColor("#f68477"));//red

                //barChart.setData(new BarData(inCome, outCome));

            } else {
                inCome = new BarDataSet(incomeValues, "Inflow");
                inCome.setColor(Color.parseColor("#1a8b8c"));//green

                outCome = new BarDataSet(outComeValues, "Outflow");
                outCome.setColor(Color.parseColor("#f68477"));//red

                //barChart.setData(new BarData(inCome, outCome));
            }

            /*if (barChart.getData() == null || (barChart.getData()).getDataSetCount() <= 0) {
                if (UiUtil.getAccountingType(mContext) == 2) {
                    inCome = new BarDataSet(incomeValues, "Income");
                    inCome.setColor(Color.parseColor("#1a8b8c"));// green

                    outCome = new BarDataSet(outComeValues, "Expense");
                    outCome.setColor(Color.parseColor("#f68477"));//red

                    barChart.setData(new BarData(inCome, outCome));

                    //BarDataSet barDataSet = outCome;
                } else {
                    inCome = new BarDataSet(incomeValues, "Inflow");
                    inCome.setColor(Color.parseColor("#1a8b8c"));//green

                    outCome = new BarDataSet(outComeValues, "Outflow");
                    outCome.setColor(Color.parseColor("#f68477"));//red

                    barChart.setData(new BarData(inCome, outCome));

                    //BarDataSet barDataSet = outCome;
                }
            } else {
                ((BarDataSet) barChart.getData().getDataSetByIndex(0)).setValues(incomeValues);
                ((BarDataSet) barChart.getData().getDataSetByIndex(1)).setValues(outComeValues);
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            }*/

            if (inCome != null && outCome != null) {
                barChart.setData(new BarData(inCome, outCome));
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            }

            //Value display over the bar
            for (IBarDataSet set : barChart.getData().getDataSets()) {
                set.setDrawValues(false);
            }


            barChart.getData().setHighlightEnabled(false);
            barChart.animateXY(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED,
                    ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
            barChart.getBarData().setBarWidth(barWidth);//Bar width

            barChart.getXAxis().setAxisMaximum((barChart.getBarData()
                    .getGroupWidth(groupSpace, barSpace) * ((float) xAxisItemsCount)) + 0.0f); //set the month spacing
            barChart.groupBars(0.0f, groupSpace, barSpace); //set the gap between bars

            barChart.invalidate();

        } catch (Exception e) {
            Log.e("Error :: ", e.toString());
        }
    }


    //Sales and Purchase Invoice
    @SuppressLint("SetTextI18n")
    private void setSalesAndPurchaseInvoice(List<InvoicePurchaseOverdue> list) {
        linSaleInvoice.setVisibility(View.GONE);
        linPurchaseInvoice.setVisibility(View.GONE);

        cvSalesNoDataFound.setVisibility(View.VISIBLE);
        cvPurchaseNoDataFound.setVisibility(View.VISIBLE);

        list.forEach(model -> {
            if (model.getType() == 1) { //sales invoice
                linSaleInvoice.setVisibility(View.VISIBLE);
                cvSalesNoDataFound.setVisibility(View.GONE);

                tvSaleNotDue.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + model.getDueAmount());
                tvSaleOverDue.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + model.getOverdueAmount());

            } else if (model.getType() == 2) { //purchase invoice
                linPurchaseInvoice.setVisibility(View.VISIBLE);
                cvPurchaseNoDataFound.setVisibility(View.GONE);

                tvPurchaseNotDue.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + model.getDueAmount());
                tvPurchaseOverDue.setText(UiUtil.getBussinessCurrenSymbul(getContext()) + " " + model.getOverdueAmount());
            }
        });
    }

    public String getAmountWithSymbol(Double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        Double BILLION = 1000000000.0;
        Double MILLION = 1000000.0;
        Double THOUSAND = 1000.0;

        String amountReturn = "";
        if (amount >= BILLION) {
            amountReturn = df.format(amount / BILLION) + "B";
        } else if (amount >= MILLION) {
            amountReturn = df.format(amount / MILLION) + "M";
        } else if (amount >= THOUSAND) {
            amountReturn = df.format(amount / THOUSAND) + "K";
        } else {
            amount.toString();
        }
        return amountReturn;
    }
}