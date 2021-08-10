package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Accounting.AccountingMenu;
import com.akounto.accountingsoftware.Activity.AddJournalTransactionActivity;
import com.akounto.accountingsoftware.Activity.AddTransactionActivity;
import com.akounto.accountingsoftware.Activity.Bank.BankListActivity;
import com.akounto.accountingsoftware.Activity.TransactionCategoryClick;
import com.akounto.accountingsoftware.Activity.UploadBankStatementActivity;
import com.akounto.accountingsoftware.adapter.AddWithdrawalCategoryAdapter;
import com.akounto.accountingsoftware.adapter.TransactionAdapter;
import com.akounto.accountingsoftware.adapter.TransactionIconClick;
import com.akounto.accountingsoftware.adapter.TransactionItemClick;
import com.akounto.accountingsoftware.model.AddWithDrawalCategory;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.TransectionRequest;
import com.akounto.accountingsoftware.response.Transaction;
import com.akounto.accountingsoftware.response.TransectionDetails;
import com.akounto.accountingsoftware.response.accounting.BanksItem;
import com.akounto.accountingsoftware.response.accounting.GetBankResponse;
import com.akounto.accountingsoftware.response.accounting.JournalTransactionDetailByIdResponse;
import com.akounto.accountingsoftware.response.accounting.TransactionDetailByIdResponse;
import com.akounto.accountingsoftware.response.chartaccount.GetChartData;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.response.chartaccount.HeadSubType;
import com.akounto.accountingsoftware.response.chartaccount.HeadTransactions;
import com.akounto.accountingsoftware.response.chartaccount.HeadType;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends Fragment implements View.OnClickListener, TransactionCategoryClick, TransactionItemClick, TransactionIconClick {
    int RecordsPerPage = 10;
    Spinner accountSpinner;
    TransactionAdapter adapter;
    TextView addDeposite;
    TextView addWithDrawal;
    double balance = 0.0;
    List<BanksItem> banksItems = new ArrayList();
    TextView categoriesTextView;
    Dialog dialog;
    ConstraintLayout filterLayout;
    ImageView fromDate;
    TextView fromDateTv;
    List<AddWithdrawalCategorySpinnerItem> list = new ArrayList();
    LinearLayout ll_upload_bankstmt;
    private int mDay;
    private int mMonth;
    private int mYear;
    TextView moreButton;
    LinearLayout nextPrevLL;
    TextView nextTv;
    int pageNum = 1;
    TextView prevTv;
    TextView priceTotal;
    List<Transaction> recurringInvoicesItems = new ArrayList();
    int selectedFilterCategory = 0;
    String selectedFromDate = null;
    String selectedToDate = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    int status = 0;
    ImageView toDate;
    TextView toDateTv;
    RecyclerView transectionRecycler;
    LinearLayout noData,more_top;
    int type = 0;
    View view;
    boolean start = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.transactions_fragment, container, false);
        setHasOptionsMenu(true);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, AccountingMenu.class);
            }
        });
        inItUi();
        return this.view;
    }

    private void inItUi() {
        this.nextPrevLL = this.view.findViewById(R.id.nextPrevLL);
        this.more_top=this.view.findViewById(R.id.more_top);
        this.more_top.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$inItUi$3$TransactionsFragment(view);
            }
        });
        TextView textView = this.view.findViewById(R.id.prevTv);
        this.prevTv = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$inItUi$0$TransactionsFragment(view);
            }
        });
        if (this.pageNum == 1) {
            this.prevTv.setVisibility(View.GONE);
        }
        TextView textView2 = this.view.findViewById(R.id.nextTv);
        this.nextTv = textView2;
        textView2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$inItUi$1$TransactionsFragment(view);
            }
        });
        this.noData = this.view.findViewById(R.id.noData);
        this.addWithDrawal = this.view.findViewById(R.id.addWithDrawal);
        this.addDeposite = this.view.findViewById(R.id.addDeposit);
        this.ll_upload_bankstmt = this.view.findViewById(R.id.ll_upload_bankstmt);
        this.ll_upload_bankstmt.setEnabled(false);
        this.addWithDrawal.setOnClickListener(this);
        this.addDeposite.setOnClickListener(this);
        this.ll_upload_bankstmt.setOnClickListener(this);
        this.accountSpinner = this.view.findViewById(R.id.accountSpinner);
        this.transectionRecycler = this.view.findViewById(R.id.transectionRecycler);
        this.moreButton = this.view.findViewById(R.id.moreButton);
        this.priceTotal = this.view.findViewById(R.id.priceTotal);
        this.view.findViewById(R.id.addAccount).setOnClickListener($$Lambda$TransactionsFragment$Uh_qoYxkW8remCENv5Z00Ak58.INSTANCE);
        registerForContextMenu(this.moreButton);
        this.moreButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$inItUi$3$TransactionsFragment(view);
            }
        });
        EditText searchEt = this.view.findViewById(R.id.searchET);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return TransactionsFragment.this.lambda$inItUi$4$TransactionsFragment(searchEt, textView, i, keyEvent);
            }
        });
        this.filterLayout = this.view.findViewById(R.id.filterLayout);
        this.view.findViewById(R.id.filterIcon).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$inItUi$5$TransactionsFragment(view);
            }
        });
    }

    public void lambda$inItUi$0$TransactionsFragment(View v) {
        this.pageNum--;
        getTransectionList();
    }

    public void lambda$inItUi$1$TransactionsFragment(View v) {
        this.pageNum++;
        getTransectionList();
    }

    static void lambda$inItUi$2(View v) {
    }

    public void lambda$inItUi$3$TransactionsFragment(View v) {
        this.more_top.showContextMenu();
    }

    public boolean lambda$inItUi$4$TransactionsFragment(EditText searchEt, TextView v, int actionId, KeyEvent event) {
        if (actionId != 3 && actionId != 2) {
            return false;
        }
        performSearch(searchEt.getText().toString().trim());
        return true;
    }

    public void lambda$inItUi$5$TransactionsFragment(View v) {
        openFilterDialog();
    }

    private void openFilterDialog() {
        this.status = 0;
        this.type = 0;
        this.selectedFilterCategory = 0;
        if (this.filterLayout.getVisibility() == View.VISIBLE) {
            this.filterLayout.setVisibility(View.GONE);
        } else {
            this.filterLayout.setVisibility(View.VISIBLE);
        }
        PowerSpinnerView dialogStatusSpinner = this.filterLayout.findViewById(R.id.statusSpinner);
        dialogStatusSpinner.clearSelectedItem();
        TextView textView = this.filterLayout.findViewById(R.id.categoriesSpinner);
        this.categoriesTextView = textView;
        textView.setText("All Categories");
        PowerSpinnerView dialogTypesSpinner = this.filterLayout.findViewById(R.id.typesSpinner);
        dialogTypesSpinner.clearSelectedItem();
        this.categoriesTextView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$openFilterDialog$6$TransactionsFragment(view);
            }
        });
        dialogStatusSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                TransactionsFragment.this.lambda$openFilterDialog$7$TransactionsFragment(i, (String) obj, i2, (String) obj2);
            }
        });
        dialogTypesSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                TransactionsFragment.this.lambda$openFilterDialog$8$TransactionsFragment(i, (String) obj, i2, (String) obj2);
            }
        });
        Locale.setDefault(Locale.US);
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        this.fromDate = this.filterLayout.findViewById(R.id.fromDateButton);
        this.fromDateTv = this.filterLayout.findViewById(R.id.fromDateTv);
        this.toDate = this.filterLayout.findViewById(R.id.toDateButton);
        this.toDateTv = this.filterLayout.findViewById(R.id.toDateTv);
        this.fromDateTv.setText("From");
        this.toDateTv.setText("To");
        this.fromDate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$openFilterDialog$9$TransactionsFragment(view);
            }
        });
        this.toDate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$openFilterDialog$10$TransactionsFragment(view);
            }
        });
        filterLayout.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("All Categories");
                fromDateTv.setText("From");
                toDateTv.setText("To");
                dialogStatusSpinner.clearSelectedItem();
                dialogTypesSpinner.clearSelectedItem();
                getTrasList(new TransectionRequest(null, null, null, null, null));
            }
        });
        this.filterLayout.findViewById(R.id.applyButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsFragment.this.lambda$openFilterDialog$11$TransactionsFragment(view);
            }
        });
    }

    public void lambda$openFilterDialog$6$TransactionsFragment(View v) {
        selectCategoryDialog();
    }

    public void lambda$openFilterDialog$7$TransactionsFragment(int i, String s, int selectedIndex, String selectedItem) {
        this.status = selectedIndex + 1;
    }

    public void lambda$openFilterDialog$8$TransactionsFragment(int i, String s, int selectedIndex, String selectedItem) {
        this.type = selectedIndex + 1;
    }

    public void lambda$openFilterDialog$9$TransactionsFragment(View v) {
        openDatePicker(this.fromDateTv);
    }

    public void lambda$openFilterDialog$10$TransactionsFragment(View v) {
        openDatePickerto(this.toDateTv);
    }

    public void lambda$openFilterDialog$11$TransactionsFragment(View v) {
        String str;
        String str2;
        Integer num;
        Integer num2;
        if (this.fromDateTv.getText().equals("From")) {
            str = null;
        } else {
            str = this.fromDateTv.getText().toString();
        }
        this.selectedFromDate = str;
        if (this.toDateTv.getText().equals("To")) {
            str2 = null;
        } else {
            str2 = this.toDateTv.getText().toString();
        }
        this.selectedToDate = str2;
        int i = this.type;
        int i2 = this.selectedFilterCategory;
        if (i2 == 0) {
            num = null;
        } else {
            num = Integer.valueOf(i2);
        }
        String str3 = this.selectedFromDate;
        String str4 = this.selectedToDate;
        int i3 = this.status;
        if (i3 == 0) {
            num2 = null;
        } else {
            num2 = Integer.valueOf(i3);
        }
        getTrasList(new TransectionRequest(i, num, str3, str4, num2));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void selectCategoryDialog() {
        Dialog dialog2 = new Dialog(getContext());
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialogue_add_bill_item);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
        AddWithdrawalCategoryAdapter adapter2 = new AddWithdrawalCategoryAdapter(getContext(), this.list, this);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        listItemRecycler.setAdapter(adapter2);
        this.dialog.show();
    }

    private void openDatePicker(TextView textView) {
        DatePickerDialog dilog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

                TransactionsFragment.this.lambda$openDatePicker$12$TransactionsFragment(textView, datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        dilog.getDatePicker().setMaxDate(new Date().getTime());
        dilog.show();
    }

    private void openDatePickerto(TextView textView) {
        DatePickerDialog dilog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

                TransactionsFragment.this.lambda$openDatePicker$12$TransactionsFragment(textView, datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        Date date = null;
        try {
            date = simpleDateFormat.parse(fromDateTv.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            dilog.getDatePicker().setMinDate(date.getTime());
        } catch (Exception e) {
        }
        dilog.getDatePicker().setMaxDate(new Date().getTime());
        dilog.show();

    }

    public void lambda$openDatePicker$12$TransactionsFragment(TextView textView, DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        textView.setText(this.simpleDateFormat.format(calendar.getTime()));
    }

    private void performSearch(String searchText) {
        TransectionRequest request = new TransectionRequest();
        request.setBankId(0);
        request.setTransactionType(0);
        if (!TextUtils.isEmpty(searchText)) {
            request.setFilterKeyword(searchText);
        }
        getTrasList(request);
    }

    public void onResume() {
        super.onResume();
        getTransectionList();
        getBanks();
        getCharts();
    }

    private void getTransectionList() {
        TransectionRequest request = new TransectionRequest();
        request.setBankId(0);
        request.setTransactionType(0);
        request.setPageNumber(this.pageNum);
        request.setRecordsPerPage(this.RecordsPerPage);
        getTrasList(request);
    }

    private void getCharts() {
        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        RestClient.getInstance(getContext()).getChart(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), "").enqueue(new Callback<GetChartResponse>() {

            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                UiUtil.cancelProgressDialogue();
//                Log.d("111111----2222", new Gson().toJson((Object) response.body().getData()));
                if (response.isSuccessful()) {
                    TransactionsFragment.this.prepareCategorySpinner(response.body().getData());
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
            }
        });
/*        Api api = ApiUtils.getAPIService();
        api.getChart("Bearer " + UiUtil.getUserDetails(getContext()).getAccess_token(), "268", "requet").enqueue(new Callback<GetChartResponse>() {
            @Override
            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsFragment.this.prepareCategorySpinner(response.body().getData());
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
            }
        });*/
    }

    /* access modifiers changed from: private */
    public void prepareCategorySpinner(GetChartData data) {
        for (HeadType outer : data.getHeadTypes()) {
            for (HeadSubType headSubType : outer.getHeadSubTypes()) {
                if (!headSubType.getHeadTransactions().isEmpty()) {
                    this.list.add(new AddWithdrawalCategorySpinnerItem(headSubType.getName(), AddWithDrawalCategory.HEADER, headSubType.getId()));
                    for (HeadTransactions headTransactions : headSubType.getHeadTransactions()) {
                        this.list.add(new AddWithdrawalCategorySpinnerItem(headTransactions.getName(), AddWithDrawalCategory.CATEGORY_ITEM, headTransactions.getId()));
                    }
                }
            }
        }
    }

    private void getTrasList(TransectionRequest request) {
        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        RestClient.getInstance(getContext()).getTransectionList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), request).enqueue(new Callback<TransectionDetails>() {
            public void onResponse(Call<TransectionDetails> call, Response<TransectionDetails> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsFragment.this.setTransactionAdapter(response);
                } catch (Exception e) {
                    UiUtil.showToast(getContext(), e.getMessage());
                }
            }

            public void onFailure(Call<TransectionDetails> call, Throwable t) {
                Log.d("error", t.toString());
                UiUtil.cancelProgressDialogue();
            }
        });

       /* Api api = ApiUtils.getAPIService();
        String s = new GsonBuilder().setPrettyPrinting().create().toJson(request);
        JsonObject requet = new JsonParser().parse(s).getAsJsonObject();
        api.getTransectionList("Bearer " + UiUtil.getUserDetails(getContext()).getAccess_token(), "268", requet).enqueue(new Callback<TransectionDetails>() {
            @Override
            public void onResponse(Call<TransectionDetails> call, Response<TransectionDetails> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsFragment.this.setTransactionAdapter(response);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TransectionDetails> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
            }
        });*/
    }

    /* access modifiers changed from: private */
    public void setTransactionAdapter(Response<TransectionDetails> response) {
        if (response.body().getData().getTotalRecords() > 10) {
            this.nextPrevLL.setVisibility(View.VISIBLE);
        } else {
            this.nextPrevLL.setVisibility(View.GONE);
        }
        if (this.pageNum == 1) {
            this.prevTv.setVisibility(View.GONE);
        } else {
            this.prevTv.setVisibility(View.VISIBLE);
        }
        if (response.body().getData().getTotalRecords() == response.body().getData().getTo()) {
            this.nextTv.setVisibility(View.GONE);
        } else {
            this.nextTv.setVisibility(View.VISIBLE);
        }
        if (response.body().getData().getTransactions() != null) {
            if (response.body().getData().getTransactions().size() != 0) {
                this.transectionRecycler.setVisibility(View.VISIBLE);
                this.noData.setVisibility(View.GONE);
                this.adapter = new TransactionAdapter(getActivity(), response.body().getData().getTransactions(), this, this);
                this.transectionRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                this.transectionRecycler.setAdapter(this.adapter);
            } else {
                this.transectionRecycler.setVisibility(View.GONE);
                this.noData.setVisibility(View.VISIBLE);
            }
        } else {
            this.transectionRecycler.setVisibility(View.GONE);
            this.noData.setVisibility(View.VISIBLE);
        }
    }

    private void getBanks() {
        this.balance = 0.0;
        UiUtil.showProgressDialogue(getContext(), "", "Please wait..");
        RestClient.getInstance(getContext()).getBanks(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext())).enqueue(new Callback<GetBankResponse>() {
            static final boolean $assertionsDisabled = false;

            public void onResponse(Call<GetBankResponse> call, Response<GetBankResponse> response) {
                UiUtil.cancelProgressDialogue();
                Log.d("Banks Response---", response.toString());
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    TransactionsFragment.this.banksItems = response.body().getBankResponse().getBanks();
                    TransactionsFragment transactionsFragment = TransactionsFragment.this;
                    transactionsFragment.setAccountSpinner(transactionsFragment.banksItems);
                    return;
                }
                throw new AssertionError();
            }

            public void onFailure(Call<GetBankResponse> call, Throwable t) {
                Log.d("error", t.toString());
                UiUtil.cancelProgressDialogue();
            }
        });
    }

    /* access modifiers changed from: private */
    public void setAccountSpinner(List<BanksItem> bankResponse) {
        ArrayList<String> accounts = new ArrayList<>();

        if (bankResponse != null) {
            for (BanksItem innerBanksItem : bankResponse) {
                accounts.add(innerBanksItem.getInstitutionName());
                this.balance += innerBanksItem.getBankAccounts().getAvailableBalance();
            }
            try {
                this.priceTotal.setText(this.balance + " USD");
                ArrayAdapter dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, accounts);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                accountSpinner.setAdapter(dataAdapter);
            } catch (Exception e) {
            }
            accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (start)
                        TransactionsFragment.this.lambda$setAccountSpinner$13$TransactionsFragment(bankResponse, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void lambda$setAccountSpinner$13$TransactionsFragment(List bankResponse, int selectedIndex) {
        start = true;
        TransectionRequest transectionRequest = new TransectionRequest(((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getId(), ((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getBankId(), 0);
        TextView textView = this.priceTotal;
        textView.setText(((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getAvailableBalance() + " USD");
        getTrasList(transectionRequest);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addDeposit) {
            /*Intent intent1 = new Intent(getActivity(), AddTransactionActivity.class);
            intent1.putExtra("depositOrWithdrawal", "Deposit");
            startActivity(intent1);*/
            Bundle b=new Bundle();
            b.putString(Constant.CATEGORY,"accounting");
            b.putString(Constant.ACTION,"add_deposit");
            SplashScreenActivity.sendEvent("accounting_add_deposit",b);
            AddTransactionFragment f = new AddTransactionFragment();
            f.setData("Deposit");
            AddFragments.addFragmentToDrawerActivity(getContext(), null, f.getClass());
        } else if (id == R.id.addWithDrawal) {
            /*Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            intent.putExtra("depositOrWithdrawal", "Withdrawal");
            startActivity(intent);*/
            Bundle b=new Bundle();
            b.putString(Constant.CATEGORY,"accounting");
            b.putString(Constant.ACTION,"add_with_drawal");
            SplashScreenActivity.sendEvent("accounting_add_with_drawal",b);
            AddTransactionFragment f = new AddTransactionFragment();
            f.setData("Deposit");
            AddFragments.addFragmentToDrawerActivity(getContext(), null, f.getClass());
        } else if (id == R.id.ll_upload_bankstmt) {
            Intent intent2 = new Intent(getActivity(), UploadBankStatementActivity.class);
            intent2.putExtra("bankAccounts", (ArrayList) this.banksItems);
            startActivity(intent2);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.more_transactions_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.journalTransaction) {
            startActivity(new Intent(getActivity(), AddJournalTransactionActivity.class));
        } else if (item.getItemId() == R.id.connectYourBank) {
            startActivity(new Intent(getActivity(), BankListActivity.class));
        }
        return true;
    }

    public void onCategoryClick(AddWithdrawalCategorySpinnerItem item, TextView textView, EditText editText) {
        this.selectedFilterCategory = item.getId();
        this.dialog.dismiss();
        this.categoriesTextView.setText(item.getTitle());
    }

    public void viewItem(Transaction transaction, int type2) {
        if (type2 == 3) {
            RestClient.getInstance(getContext()).getJournalTransactionDetailsById("Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), transaction.getId()).enqueue(new CustomCallBack<JournalTransactionDetailByIdResponse>(getContext(), null) {
                static final boolean $assertionsDisabled = false;

                public void onResponse(Call<JournalTransactionDetailByIdResponse> call, Response<JournalTransactionDetailByIdResponse> response) {
                    super.onResponse(call, response);
                    Log.d("Banks Response---", response.toString());
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        TransactionsFragment transactionsFragment = TransactionsFragment.this;
                        transactionsFragment.startActivity(AddJournalTransactionActivity.buildIntent(transactionsFragment.getContext(), response.body().getData()));
                        return;
                    }
                    throw new AssertionError();
                }

                public void onFailure(Call<JournalTransactionDetailByIdResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.d("error", t.toString());
                }
            });
        } else {
            RestClient.getInstance(getContext()).getTransactionDetailsById("Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), transaction.getId()).enqueue(new CustomCallBack<TransactionDetailByIdResponse>(getContext(), null) {
                public void onResponse(Call<TransactionDetailByIdResponse> call, Response<TransactionDetailByIdResponse> response) {
                    super.onResponse(call, response);
                    Log.d("Banks Response---", response.toString());
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        //TransactionsFragment transactionsFragment = TransactionsFragment.this;
                        //transactionsFragment.startActivity(AddTransactionActivity.buildIntent(transactionsFragment.getContext(), response.body().getData()));
                        TransactionAddFragment fragment = new TransactionAddFragment();
                        TransactionAddFragment.receivedData = response.body().getData();
                        fragment.setData(response.body().getData());
                        AddFragments.addFragmentToDrawerActivity(getContext(), null, fragment.getClass());
                        return;
                    }
                    throw new AssertionError();
                }

                public void onFailure(Call<TransactionDetailByIdResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.d("error", t.toString());
                }
            });
        }
    }

    public void updateAction(Transaction transaction) {
        transaction.setContinueWithReconcile(false);
        transaction.setReconcileTransaction(false);
        transaction.setReview(1);
        RestClient.getInstance(getContext()).updateCatAction(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), transaction).enqueue(new CustomCallBack<Transaction>(getContext(), null) {

            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                super.onResponse(call, response);
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"accounting");
                b.putString(Constant.ACTION,"update_cat_action");
                SplashScreenActivity.sendEvent("accounting_update_cat_action",b);
                if (!response.isSuccessful()) {
                    UiUtil.showToast(TransactionsFragment.this.getContext(), "Failed. Please try after sometime");
                } else if (response.body() != null) {
                    UiUtil.showToast(TransactionsFragment.this.getContext(), "Successfully updated");
                    TransactionsFragment.this.defaultServiceCall();
                } else {
                    throw new AssertionError();
                }
            }

            public void onFailure(Call<Transaction> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    public void defaultServiceCall() {
        TransectionRequest request = new TransectionRequest();
        request.setBankId(0);
        request.setTransactionType(0);
        getTrasList(request);
        getBanks();
        getCharts();
    }
}
