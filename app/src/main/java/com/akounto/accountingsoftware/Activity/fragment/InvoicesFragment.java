package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.Invoice.EditInvoice;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Invoice.CreateInvoice;
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceMenu;
import com.akounto.accountingsoftware.Activity.Invoice.ViewInvoice;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.InvoiceItemClick;
import com.akounto.accountingsoftware.adapter.SalesInvoiceAdapter;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetRecurringInvoiceRequest;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerResponse;
import com.akounto.accountingsoftware.response.GetInvoiceByIdResponse;
import com.akounto.accountingsoftware.response.RecurringInvoicesItem;
import com.akounto.accountingsoftware.response.RecurringInvoicesResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class InvoicesFragment extends Fragment implements InvoiceItemClick {
    ConstraintLayout noDataLayout;
    LinearLayout withDataLayout;
    EditText searchEt;
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
    RelativeLayout searchLayout;
    View view;
    ImageView filter;
    RecyclerView salesInvoicesRV;
    List<Customer> customerList = new ArrayList();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.invoices_fragment, container, false);
        this.view = inflate;

        EditText editText = view.findViewById(R.id.searchET);
        withDataLayout = view.findViewById(R.id.withDataLayout);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, InvoiceMenu.class);
            }
        });
        this.searchEt = editText;
        filter = this.view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDilog();
            }
        });
        inflate.findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                InvoicesFragment.this.lambda$onCreateView$0$InvoicesFragment(view);
            }
        });
        inflate.findViewById(R.id.btn_create_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoicesFragment.this.lambda$onCreateView$0$InvoicesFragment(view);
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

    public void lambda$onCreateView$0$InvoicesFragment(View click) {
        startActivity(new Intent(getContext(), CreateInvoice.class)/*CommonInvoiceActivity.buildIntent(getContext(), Type.INVOICES.name())*/);
    }

    private void initUi(View view2) {
        try {
            this.noDataLayout = view2.findViewById(R.id.noDataLayout);
            this.searchLayout = view2.findViewById(R.id.searchLayout);
            salesInvoicesRV = this.view.findViewById(R.id.invoicesRecyclerView);
            this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
    /*    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return InvoicesFragment.this.lambda$initUi$1$InvoicesFragment(textView, i, keyEvent);
            }
        });*/
            //settypeSpinner();
            //getCustomerList();
        }catch (Exception e){}
    }

    private void performSearch(String searchText) {
        GetRecurringInvoiceRequest inv_request = new GetRecurringInvoiceRequest(searchText);
        getInvoiceList(inv_request);
    }

    public void onResume() {
        super.onResume();
        GetRecurringInvoiceRequest inv_request = new GetRecurringInvoiceRequest(null, 1, 1, 50);
        getInvoiceList(inv_request);
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
        ImageView close=dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
                int stat = -1;
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
                getInvoiceList(inv_request);
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

    private void getInvoiceList(GetRecurringInvoiceRequest request) {
        //new GetRecurringInvoiceRequest(searchString, 1, 1, 50)
        RestClient.getInstance(getContext()).getInvoice(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "invoice", request).enqueue(new CustomCallBack<RecurringInvoicesResponse>(getContext(), null) {
            public void onResponse(Call<RecurringInvoicesResponse> call, Response<RecurringInvoicesResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.isSuccessful() && response.body().getRecurringInvoices().getInvoices() != null) {
                        if (response.body().getRecurringInvoices().getInvoices().size() != 0) {
                            InvoicesFragment.this.populateData(response.body().getRecurringInvoices().getInvoices());
                            InvoicesFragment.this.salesInvoicesRV.setVisibility(View.VISIBLE);
                            InvoicesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                            withDataLayout.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                        } else {
                            UiUtil.showToast(InvoicesFragment.this.getContext(), "No Invoice found.");
                            InvoicesFragment.this.salesInvoicesRV.setVisibility(View.GONE);
                            InvoicesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                            withDataLayout.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        InvoicesFragment.this.salesInvoicesRV.setVisibility(View.GONE);
                        InvoicesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                        withDataLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    InvoicesFragment.this.salesInvoicesRV.setVisibility(View.GONE);
                    InvoicesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                    withDataLayout.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }

            public void onFailure(Call<RecurringInvoicesResponse> call, Throwable t) {
                super.onFailure(call, t);
                InvoicesFragment.this.salesInvoicesRV.setVisibility(View.GONE);
                InvoicesFragment.this.searchLayout.setVisibility(View.VISIBLE);
                withDataLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        });
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
                        InvoicesFragment.this.customerList = response.body().getData();
                        InvoicesFragment commonInvoiceActivity = InvoicesFragment.this;
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
    public void populateData(List<RecurringInvoicesItem> invoices) {
        SalesInvoiceAdapter salesInvoiceAdapter = new SalesInvoiceAdapter(getContext(), invoices, this);
        salesInvoicesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        salesInvoicesRV.setAdapter(salesInvoiceAdapter);
    }

    public void viewItem(RecurringInvoicesItem recurringInvoicesItem, int type) {
        fetchInvoiceDetails(recurringInvoicesItem.getGuid(), type);
    }

    private void fetchInvoiceDetails(String guid, final int type) {
        RestClient.getInstance(getContext()).getInvoiceById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "invoice", guid).enqueue(new CustomCallBack<GetInvoiceByIdResponse>(getContext(), null) {
            public void onResponse(Call<GetInvoiceByIdResponse> call, Response<GetInvoiceByIdResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    int i = type;
                    if (i == 2) {
                        Intent intent = new Intent(InvoicesFragment.this.getContext(), ViewInvoice.class);
                        intent.putExtra("estimateType", "invoice");
                        intent.putExtra("DATA", new Gson().toJson(response.body().getInvoiceDetails()));
                        InvoicesFragment.this.startActivity(intent);
                    } else if (i == 1) {
                        InvoicesFragment invoicesFragment = InvoicesFragment.this;
                        invoicesFragment.startActivity(EditInvoice.buildIntentWithData(invoicesFragment.getContext(), Type.INVOICES.name(), response.body().getInvoiceDetails()));
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
