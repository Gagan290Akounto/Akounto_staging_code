package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.ViewPdfActivity;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.ViewReportRequest;
import com.akounto.accountingsoftware.response.DownloadResponse;
import com.akounto.accountingsoftware.response.viewreport.ViewReportDetails;
import com.akounto.accountingsoftware.util.LocalManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class ReportsDetailedFragment extends Fragment {
    TextView btn_update;
    TextView et_from_date;
    TextView et_from_date2;
    TextView et_to_date;
    TextView et_to_date2;
    String fromDate = "2020-11-01";
    String fromDate2 = "2020-12-31";
    PowerSpinnerView pdf_download;
    ReportDetailsFragment reportDetailsFragment;
    ReportSummaryFragment reportSummaryFragment;
    private TabLayout tabLayout;
    String toEndDate = "2021-01-01";
    String toEndDate2 = "2021-02-28";
    View view;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.report_detailed_fragment, container, false);
        this.view = inflate;
        this.viewPager = inflate.findViewById(R.id.viewpager);
        TabLayout tabLayout2 = this.view.findViewById(R.id.tabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.setupWithViewPager(this.viewPager);
        setupViewPager(this.viewPager);
        initViews();
        getProfitLoss();
        return this.view;
    }

    private void initViews() {
        this.et_from_date = this.view.findViewById(R.id.et_from_date);
        this.et_to_date = this.view.findViewById(R.id.et_to_date);
        this.et_from_date2 = this.view.findViewById(R.id.et_from_date2);
        this.et_to_date2 = this.view.findViewById(R.id.et_to_date2);
        this.btn_update = this.view.findViewById(R.id.btn_update);
        this.pdf_download = this.view.findViewById(R.id.pdf_download);
        this.btn_update.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportsDetailedFragment.this.lambda$initViews$0$ReportsDetailedFragment(view);
            }
        });
        this.pdf_download.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            public void onItemSelected(int i, Object o, int pos, Object t1) {
                if (pos == 0) {
                    ReportsDetailedFragment.this.downloadReport();
                }
            }
        });
        this.et_from_date.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportsDetailedFragment.this.lambda$initViews$1$ReportsDetailedFragment(view);
            }
        });
        this.et_to_date.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportsDetailedFragment.this.lambda$initViews$2$ReportsDetailedFragment(view);
            }
        });
        this.et_from_date2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportsDetailedFragment.this.lambda$initViews$3$ReportsDetailedFragment(view);
            }
        });
        this.et_to_date2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ReportsDetailedFragment.this.lambda$initViews$4$ReportsDetailedFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$initViews$0$ReportsDetailedFragment(View v) {
        getProfitLoss();
    }

    public /* synthetic */ void lambda$initViews$1$ReportsDetailedFragment(View v) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ReportsDetailedFragment reportsDetailedFragment = ReportsDetailedFragment.this;
                reportsDetailedFragment.fromDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                ReportsDetailedFragment.this.et_from_date.setText(ReportsDetailedFragment.this.fromDate);
            }
        }, 2020, 4, 1).show();
    }

    public /* synthetic */ void lambda$initViews$2$ReportsDetailedFragment(View v) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ReportsDetailedFragment reportsDetailedFragment = ReportsDetailedFragment.this;
                reportsDetailedFragment.toEndDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                ReportsDetailedFragment.this.et_to_date.setText(ReportsDetailedFragment.this.toEndDate);
            }
        }, 2020, 4, 1).show();
    }

    public /* synthetic */ void lambda$initViews$3$ReportsDetailedFragment(View v) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ReportsDetailedFragment reportsDetailedFragment = ReportsDetailedFragment.this;
                reportsDetailedFragment.fromDate2 = year + "-" + (month + 1) + "-" + dayOfMonth;
                ReportsDetailedFragment.this.et_from_date2.setText(ReportsDetailedFragment.this.fromDate2);
            }
        }, 2020, 4, 1).show();
    }

    public /* synthetic */ void lambda$initViews$4$ReportsDetailedFragment(View v) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ReportsDetailedFragment reportsDetailedFragment = ReportsDetailedFragment.this;
                reportsDetailedFragment.toEndDate2 = year + "-" + (month + 1) + "-" + dayOfMonth;
                ReportsDetailedFragment.this.et_to_date2.setText(ReportsDetailedFragment.this.toEndDate2);
            }
        }, 2020, 4, 1).show();
    }

    private void getProfitLoss() {
        ViewReportRequest billsReq = new ViewReportRequest();
        billsReq.setStartDate1(this.fromDate);
        billsReq.setStartDate2(this.fromDate2);
        billsReq.setEndDate1(this.toEndDate);
        billsReq.setEndDate2(this.toEndDate2);
        Log.d("billsReqbillsReq", new Gson().toJson(billsReq));
        RestClient.getInstance(getActivity()).getProfitLossReport(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),billsReq).enqueue(new CustomCallBack<ViewReportDetails>(getActivity(), null) {
            public void onResponse(Call<ViewReportDetails> call, Response<ViewReportDetails> response) {
                super.onResponse(call, response);
                ReportsDetailedFragment.this.setData(response.body());
                Log.d("response--111", new Gson().toJson(response.body().getData()));
            }

            public void onFailure(Call<ViewReportDetails> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }

    public void downloadReport() {
        ViewReportRequest billsReq = new ViewReportRequest();
        billsReq.setStartDate1(this.fromDate);
        billsReq.setStartDate2(this.fromDate2);
        billsReq.setEndDate1(this.toEndDate);
        billsReq.setEndDate2(this.toEndDate2);
        RestClient.getInstance(getContext()).downloadSummeryPdf(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),billsReq).enqueue(new CustomCallBack<DownloadResponse>(getContext(), null) {
            public void onResponse(Call<DownloadResponse> call, Response<DownloadResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("getPDFDatagetPDFDat ", response.body().getData().getPDFData());
                    Intent intent = new Intent(ReportsDetailedFragment.this.getActivity(), ViewPdfActivity.class);
                    LocalManager.getInstance().setBase64(response.body().getData().getPDFData());
                    intent.putExtra("base64", response.body().getData().getPDFData());
                    ReportsDetailedFragment.this.startActivity(intent);
                }
            }

            public void onFailure(Call<DownloadResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        this.reportSummaryFragment = new ReportSummaryFragment();
        this.reportDetailsFragment = new ReportDetailsFragment();
        adapter.addFragment(this.reportSummaryFragment, "Summary");
        adapter.addFragment(this.reportDetailsFragment, "Details");
        viewPager2.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getItem(int position) {
            return this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }

    public void setData(ViewReportDetails response) {
        this.reportSummaryFragment.setDataOnViews(response);
        this.reportDetailsFragment.setDataOnViews(response);
    }
}
