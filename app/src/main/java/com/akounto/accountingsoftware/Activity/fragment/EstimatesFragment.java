package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.Activity.EstimateDetailsActivity;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceMenu;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.Activity.ViewPdfActivity;
import com.akounto.accountingsoftware.adapter.InvoiceItemClick;
import com.akounto.accountingsoftware.adapter.SalesEstimateAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetRecurringInvoiceRequest;
import com.akounto.accountingsoftware.request.UpdateStatus;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.DownloadResponse;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.RecurringInvoicesItem;
import com.akounto.accountingsoftware.response.RecurringInvoicesResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class EstimatesFragment extends Fragment implements InvoiceItemClick {

    RelativeLayout noDataLayout;
    RelativeLayout searchLayout;
    ArrayList<String> finalCategories;
    ArrayList<Integer> status_number;
    int status_id = 0;
    int cid;
    private int mDay;
    private int mMonth;
    private int mYear;
    SimpleDateFormat simpleDateFormat;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String dueDateFormatted;
    View view;
    ImageView filter;
    RecyclerView estimateRecyclerView;
    List<Customer> customerList = new ArrayList();
    EditText searchEt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.estimates_fragment, container, false);
        this.view = inflate;
        filter = this.view.findViewById(R.id.filter);
        EditText editText = view.findViewById(R.id.searchET);
        this.searchEt = editText;
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDilog();
            }
        });
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, InvoiceMenu.class);
            }
        });
        inflate.findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EstimatesFragment.this.lambda$onCreateView$0$EstimatesFragment(view);
            }
        });
        view.findViewById(R.id.searchIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(searchEt.getText().toString().trim());
            }
        });
        initUi(this.view);
        return this.view;
    }

    public void lambda$onCreateView$0$EstimatesFragment(View click) {
        startActivity(CommonInvoiceActivity.buildIntent(getContext(), Type.ESTIMATES.name()));
    }

    private void initUi(View view2) {
        this.noDataLayout = view2.findViewById(R.id.noDataLayout);
        this.searchLayout = view2.findViewById(R.id.searchLayout);
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        Date time = Calendar.getInstance().getTime();
        this.estimateRecyclerView = view2.findViewById(R.id.estimateRecyclerView);
        EditText searchEt = view2.findViewById(R.id.searchET);
        searchEt.setOnEditorActionListener((v, actionId, event) -> false);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return EstimatesFragment.this.lambda$initUi$1$EstimatesFragment(searchEt, textView, i, keyEvent);
            }
        });
    }

    public boolean lambda$initUi$1$EstimatesFragment(EditText searchEt, TextView v, int actionId, KeyEvent event) {
        if (actionId != 3 && actionId != 2) {
            return false;
        }
        performSearch(searchEt.getText().toString().trim());
        return true;
    }

    private void performSearch(String searchText) {
        GetRecurringInvoiceRequest inv_request = new GetRecurringInvoiceRequest(searchText);
        getEstimatesList(inv_request);
    }

    public void onResume() {
        super.onResume();
        GetRecurringInvoiceRequest inv_request = new GetRecurringInvoiceRequest(null, 1, 1, 50);
        getEstimatesList(inv_request);
    }

    public void openFilterDilog() {
        cid = 0;
        status_id = 0;
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.filter_dilog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        PowerSpinnerView status = dialog.findViewById(R.id.typeStatus);
        PowerSpinnerView client = dialog.findViewById(R.id.typeClient);
        EditText inv_no = dialog.findViewById(R.id.dtInvoiceNumber);
        TextView tv_fromdate = dialog.findViewById(R.id.tv_from_date);
        TextView tv_todate = dialog.findViewById(R.id.tv_to_date);
        dialog.findViewById(R.id.dateTo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDilog(tv_todate);
            }
        });
        dialog.findViewById(R.id.dateFrom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDilog(tv_fromdate);
            }
        });
        dialog.findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int stat =-1;
                String f_date = null;
                String t_date = null;
                String i_no = null;
                try {
                    stat = status_number.get(status_id);
                } catch (Exception e) {
                }
                try {
                    f_date = tv_fromdate.getText().toString();
                    t_date = tv_todate.getText().toString();
                    i_no = inv_no.getText().toString();
                } catch (Exception e) {
                }
                GetRecurringInvoiceRequest inv_request = new GetRecurringInvoiceRequest(stat, cid, f_date, t_date, i_no);
                getEstimatesList(inv_request);
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        settypeSpinner(status);
        getCustomerList(client);
        dialog.show();
    }

    private void settypeSpinner(PowerSpinnerView status) {
        finalCategories = new ArrayList<>();
        finalCategories.add("All Status");//0
        finalCategories.add("Draft");//1
        finalCategories.add("Approved");//2
        finalCategories.add("Partial Settled");//3
        finalCategories.add("Settled");//4
        finalCategories.add("Cancelled");//5
        status.setItems(finalCategories);
        status_number = new ArrayList<>();
        status_number.add(null);
        status_number.add(0);
        status_number.add(100);
        status_number.add(150);
        status_number.add(200);
        status_number.add(400);
        status.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o, int i1, Object t1) {
                status_id = i1;
            }
        });
    }

    private void getCustomerList(PowerSpinnerView client) {
        RestClient.getInstance(getContext()).getCustomerList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "").enqueue(new CustomCallBack<CustomerResponse>(getContext(), (String) null) {
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                super.onResponse(call, response);
                try {
                    Log.d("CustomerResponse---", response.toString());
                    if (response.isSuccessful()) {
                        EstimatesFragment.this.customerList = response.body().getData();
                        EstimatesFragment commonInvoiceActivity = EstimatesFragment.this;
                        setUpCustomerSpinner(commonInvoiceActivity.customerList, client);
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

    /* access modifiers changed from: private */
    public void populateData(List<RecurringInvoicesItem> estimates) {
        SalesEstimateAdapter salesEstimateAdapter = new SalesEstimateAdapter(getContext(), estimates, this);
        new SalesEstimateAdapter(getContext(), estimates, this);
        estimateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        estimateRecyclerView.setAdapter(salesEstimateAdapter);
    }

    public void setUpCustomerSpinner(List<Customer> data, PowerSpinnerView client) {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("All Client");
        if (data != null) {
            for (Customer customer : data) {
                customers.add(customer.getName());
            }
            client.setItems(customers);
            client.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                    try {
                        cid = data.get(i2 - 1).getHeadTransactionId();
                    } catch (Exception e) {
                    }
                    ///getInvoiceList(cid);
                }
            });
        }
    }

    private void getEstimatesList(GetRecurringInvoiceRequest request) {
        //new GetRecurringInvoiceRequest(searchString, 1, 1, 50)
        RestClient.getInstance(getContext()).getInvoice(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "estimate", request).enqueue(new CustomCallBack<RecurringInvoicesResponse>(getContext(), null) {
            public void onResponse(Call<RecurringInvoicesResponse> call, Response<RecurringInvoicesResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful() && response.body().getRecurringInvoices().getInvoices() != null) {
                        if (response.body().getRecurringInvoices().getInvoices().size() != 0) {
                            EstimatesFragment.this.populateData(response.body().getRecurringInvoices().getInvoices());
                            EstimatesFragment.this.estimateRecyclerView.setVisibility(View.VISIBLE);
                            EstimatesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                            EstimatesFragment.this.noDataLayout.setVisibility(View.GONE);
                        } else {
                            UiUtil.showToast(EstimatesFragment.this.getContext(), "No Invoice found.");
                            EstimatesFragment.this.estimateRecyclerView.setVisibility(View.GONE);
                            EstimatesFragment.this.searchLayout.setVisibility(View.GONE);
                            EstimatesFragment.this.noDataLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        EstimatesFragment.this.estimateRecyclerView.setVisibility(View.GONE);
                        EstimatesFragment.this.searchLayout.setVisibility(View.GONE);
                        EstimatesFragment.this.noDataLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    EstimatesFragment.this.estimateRecyclerView.setVisibility(View.GONE);
                    EstimatesFragment.this.searchLayout.setVisibility(View.GONE);
                    EstimatesFragment.this.noDataLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<RecurringInvoicesResponse> call, Throwable t) {
                super.onFailure(call, t);
                EstimatesFragment.this.estimateRecyclerView.setVisibility(View.GONE);
                EstimatesFragment.this.searchLayout.setVisibility(View.GONE);
                EstimatesFragment.this.noDataLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void viewItem(RecurringInvoicesItem recurringInvoicesItem, int type) {
        if (type == 3) {
            Log.d("recurringInvoicesItem", recurringInvoicesItem.getId() + "");
            UpdateStatus status = new UpdateStatus();
            status.setInvoiceId(recurringInvoicesItem.getId());
            RestClient.getInstance(getContext()).downloadPdf(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), status).enqueue(new CustomCallBack<DownloadResponse>(getContext(), null) {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onResponse(Call<DownloadResponse> call, Response<DownloadResponse> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("getPDFDatagetPDFDat ", response.body().getData().getPDFData());
                        new Intent(EstimatesFragment.this.getActivity(), ViewPdfActivity.class);
                        LocalManager.getInstance().setBase64(response.body().getData().getPDFData());
                        Base64DecodePdf(getContext(), response.body().getData().getPDFData());
                    }
                }

                public void onFailure(Call<DownloadResponse> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
            return;
        }
        fetchInvoiceDetails(recurringInvoicesItem.getGuid(), type);
    }


    public static void Base64DecodePdf(Context context, String base) {

        String root = Environment.getExternalStorageDirectory().toString();

        Log.d("ResponseEnv", root);

        File myDir = new File(root + "/WorkBox");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "Attachments-" + n + ".pdf";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {

            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = Base64.decode(base, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        File dir = new File(Environment.getExternalStorageDirectory(), "WorkBox");
        File imgFile = new File(dir, fname);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);

        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse("file://" + imgFile); // My work-around for new SDKs, causes ActivityNotFoundException in API 10.
        }

        sendIntent.setDataAndType(uri, "application/pdf");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(sendIntent);

    }

    private void fetchInvoiceDetails(String guid, final int type) {
        RestClient.getInstance(getContext()).getInvoiceById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "estimate", guid).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(getContext(), null) {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    int i = type;
                    if (i == 2) {
                        Intent intent = new Intent(EstimatesFragment.this.getContext(), EstimateDetailsActivity.class);
                        intent.putExtra("estimateType", "estimate");
                        intent.putExtra("DATA", new Gson().toJson(response.body().getInvoiceDetails()));
                        EstimatesFragment.this.startActivity(intent);
                    } else if (i == 1) {
                        EstimatesFragment estimatesFragment = EstimatesFragment.this;
                        estimatesFragment.startActivity(CommonInvoiceActivity.buildIntentWithData(estimatesFragment.getContext(), Type.ESTIMATES.name(), response.body().getInvoiceDetails()));
                    }
                }
            }

            public void onFailure(Call<GetInvoiceByIdResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void openDateDilog(TextView tv) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                dt_dilog(datePicker, i, i2, i3, tv);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void dt_dilog(DatePicker view, int year, int monthOfYear, int dayOfMonth, TextView tv) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        this.dueDateFormatted = this.simpleDateFormat.format(calendar.getTime());
        tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }
}
