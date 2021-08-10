package com.akounto.accountingsoftware.Activity.Accounting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.AddJournalTransactionActivity;
import com.akounto.accountingsoftware.Activity.AddTransactionActivity;
import com.akounto.accountingsoftware.Activity.Bank.BankListActivity;
import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Activity.TransactionCategoryClick;
import com.akounto.accountingsoftware.Activity.UploadBankStatementActivity;
import com.akounto.accountingsoftware.Activity.fragment.AddTransactionFragment;
import com.akounto.accountingsoftware.Activity.fragment.TransactionAddFragment;
import com.akounto.accountingsoftware.Activity.fragment.TransactionsFragment;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Currency;
import com.akounto.accountingsoftware.Data.CurrencyData;
import com.akounto.accountingsoftware.Data.RegisterBank.Account;
import com.akounto.accountingsoftware.R;
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
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

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


public class TransactionsActivity  extends AppCompatActivity implements View.OnClickListener, TransactionCategoryClick, TransactionItemClick, TransactionIconClick {

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
    private CurrencyData currencyData;
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
    Context mContext;String bank_id;
    public String getcurrencyData(String curreCode) {
        Currency result = null;
        try {
            if (curreCode != null) {
                for (int i = 0; i < currencyData.getCurrency().size(); i++) {
                    if (currencyData.getCurrency().get(i).getId().endsWith(curreCode)) {
                        result = currencyData.getCurrency().get(i);
                    }
                }
            } else {
                result = currencyData.getCurrency().get(0);
            }
        } catch (Exception e) {
            result = currencyData.getCurrency().get(0);
        }
        return result.getSymbol();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_fragment);
        //setHasOptionsMenu(true);
        mContext=this;
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("test.json", mContext);
        currencyData = new Gson().fromJson(loadJSONFromAsset, CurrencyData.class);
        bank_id = getIntent().getExtras().getString(Constant.BANK_ID);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       more_top=findViewById(R.id.more_top);
        more_top.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                more_top.showContextMenu();
            }
        });
        inItUi();
    }

    @Override
    public void onBackPressed() {

    }

    private void inItUi() {
        this.nextPrevLL = findViewById(R.id.nextPrevLL);
        TextView textView = findViewById(R.id.prevTv);
        this.prevTv = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsActivity.this.pageDecrise(view);
            }
        });
        if (this.pageNum == 1) {
            this.prevTv.setVisibility(View.GONE);
        }
        TextView textView2 = findViewById(R.id.nextTv);
        this.nextTv = textView2;
        textView2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsActivity.this.pagIncres(view);
            }
        });
        this.noData = findViewById(R.id.noData);
        this.addWithDrawal = findViewById(R.id.addWithDrawal);
        this.addDeposite = findViewById(R.id.addDeposit);
        this.ll_upload_bankstmt = findViewById(R.id.ll_upload_bankstmt);
        this.ll_upload_bankstmt.setEnabled(false);
        this.addWithDrawal.setOnClickListener(this);
        this.addDeposite.setOnClickListener(this);
        this.ll_upload_bankstmt.setOnClickListener(this);
        this.accountSpinner = findViewById(R.id.accountSpinner);
        this.transectionRecycler = findViewById(R.id.transectionRecycler);
        this.moreButton = findViewById(R.id.moreButton);
        this.priceTotal = findViewById(R.id.priceTotal);
        //findViewById(R.id.addAccount).setOnClickListener($$Lambda$TransactionsActivity$Uh_qoYxkW8remCENv5Z00Ak58.INSTANCE);
        registerForContextMenu(this.more_top);
        this.moreButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                moreButton.showContextMenu();
            }
        });
        EditText searchEt = findViewById(R.id.searchET);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return TransactionsActivity.this.performSearchTF(searchEt, textView, i, keyEvent);
            }
        });
        this.filterLayout = findViewById(R.id.filterLayout);
        findViewById(R.id.filterIcon).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                openFilterDialog();
            }
        });
    }

    public boolean performSearchTF(EditText searchEt, TextView v, int actionId, KeyEvent event) {
        if (actionId != 3 && actionId != 2) {
            return false;
        }
        performSearch(searchEt.getText().toString().trim());
        return true;
    }

    public void pageDecrise(View v) {
        this.pageNum--;
        getTransectionList();
    }

    public void pagIncres(View v) {
        this.pageNum++;
        getTransectionList();
    }


    public boolean lambda$inItUi$4$TransactionsActivity(EditText searchEt, TextView v, int actionId, KeyEvent event) {
        if (actionId != 3 && actionId != 2) {
            return false;
        }
        performSearch(searchEt.getText().toString().trim());
        return true;
    }

    public void lambda$inItUi$5$TransactionsActivity(View v) {
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
                TransactionsActivity.this.lambda$openFilterDialog$6$TransactionsActivity(view);
            }
        });
        dialogStatusSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                TransactionsActivity.this.lambda$openFilterDialog$7$TransactionsActivity(i, (String) obj, i2, (String) obj2);
            }
        });
        dialogTypesSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                TransactionsActivity.this.lambda$openFilterDialog$8$TransactionsActivity(i, (String) obj, i2, (String) obj2);
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
                TransactionsActivity.this.lambda$openFilterDialog$9$TransactionsActivity(view);
            }
        });
        this.toDate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                TransactionsActivity.this.lambda$openFilterDialog$10$TransactionsActivity(view);
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
                TransactionsActivity.this.lambda$openFilterDialog$11$TransactionsActivity(view);
            }
        });
    }

    public void lambda$openFilterDialog$6$TransactionsActivity(View v) {
        selectCategoryDialog();
    }

    public void lambda$openFilterDialog$7$TransactionsActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.status = selectedIndex + 1;
    }

    public void lambda$openFilterDialog$8$TransactionsActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.type = selectedIndex + 1;
    }

    public void lambda$openFilterDialog$9$TransactionsActivity(View v) {
        openDatePicker(this.fromDateTv);
    }

    public void lambda$openFilterDialog$10$TransactionsActivity(View v) {
        openDatePickerto(this.toDateTv);
    }

    public void lambda$openFilterDialog$11$TransactionsActivity(View v) {
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

    private void selectCategoryDialog() {
        Dialog dialog2 = new Dialog(mContext);
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialogue_add_bill_item);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
        AddWithdrawalCategoryAdapter adapter2 = new AddWithdrawalCategoryAdapter(mContext, this.list, this);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        listItemRecycler.setAdapter(adapter2);
        this.dialog.show();
    }

    private void openDatePicker(TextView textView) {
        DatePickerDialog dilog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

                TransactionsActivity.this.lambda$openDatePicker$12$TransactionsActivity(textView, datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        dilog.getDatePicker().setMaxDate(new Date().getTime());
        dilog.show();
    }

    private void openDatePickerto(TextView textView) {
        DatePickerDialog dilog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {

                TransactionsActivity.this.lambda$openDatePicker$12$TransactionsActivity(textView, datePicker, i, i2, i3);
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

    public void lambda$openDatePicker$12$TransactionsActivity(TextView textView, DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
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
        try{
        request.setBankId(Integer.parseInt(bank_id));}catch (Exception e){}
        request.setTransactionType(0);
        request.setPageNumber(this.pageNum);
        request.setRecordsPerPage(this.RecordsPerPage);
        getTrasList(request);
    }

    private void getCharts() {
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        RestClient.getInstance(mContext).getChart(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), "").enqueue(new Callback<GetChartResponse>() {

            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                UiUtil.cancelProgressDialogue();
//                Log.d("111111----2222", new Gson().toJson((Object) response.body().getData()));
                if (response.isSuccessful()) {
                    TransactionsActivity.this.prepareCategorySpinner(response.body().getData());
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
            }
        });
/*        Api api = ApiUtils.getAPIService();
        api.getChart("Bearer " + UiUtil.getUserDetails(mContext).getAccess_token(), "268", "requet").enqueue(new Callback<GetChartResponse>() {
            @Override
            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsActivity.this.prepareCategorySpinner(response.body().getData());
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
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        RestClient.getInstance(mContext).getTransectionList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), request).enqueue(new Callback<TransectionDetails>() {
            public void onResponse(Call<TransectionDetails> call, Response<TransectionDetails> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsActivity.this.setTransactionAdapter(response);
                } catch (Exception e) {
                    UiUtil.showToast(mContext, e.getMessage());
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
        api.getTransectionList("Bearer " + UiUtil.getUserDetails(mContext).getAccess_token(), "268", requet).enqueue(new Callback<TransectionDetails>() {
            @Override
            public void onResponse(Call<TransectionDetails> call, Response<TransectionDetails> response) {
                UiUtil.cancelProgressDialogue();
                try {
                    TransactionsActivity.this.setTransactionAdapter(response);
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
                this.adapter = new TransactionAdapter(mContext, response.body().getData().getTransactions(), this, this);
                this.transectionRecycler.setLayoutManager(new LinearLayoutManager(mContext));
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
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        RestClient.getInstance(mContext).getBanks(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext)).enqueue(new Callback<GetBankResponse>() {
            static final boolean $assertionsDisabled = false;

            public void onResponse(Call<GetBankResponse> call, Response<GetBankResponse> response) {
                UiUtil.cancelProgressDialogue();
                Log.d("Banks Response---", response.toString());
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    TransactionsActivity.this.banksItems = response.body().getBankResponse().getBanks();
                    TransactionsActivity TransactionsActivity = TransactionsActivity.this;
                    TransactionsActivity.setAccountSpinner(TransactionsActivity.banksItems);
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
            String cur="USD";
            for (BanksItem innerBanksItem : bankResponse) {
                accounts.add(innerBanksItem.getInstitutionName());
                this.balance += innerBanksItem.getBankAccounts().getAvailableBalance();
                cur=innerBanksItem.getBankAccounts().getCurrency();
            }
            try {
                this.priceTotal.setText(getcurrencyData(cur)+" "+this.balance);
                ArrayAdapter dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, accounts);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                accountSpinner.setAdapter(dataAdapter);
            } catch (Exception e) {
            }
            accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (start)
                        TransactionsActivity.this.lambda$setAccountSpinner$13$TransactionsActivity(bankResponse, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void lambda$setAccountSpinner$13$TransactionsActivity(List bankResponse, int selectedIndex) {
        start = true;
        TransectionRequest transectionRequest = new TransectionRequest(((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getId(), ((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getBankId(), 0);
        TextView textView = this.priceTotal;
        textView.setText(getcurrencyData(((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getCurrency())+" "+((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getAvailableBalance());
        getTrasList(transectionRequest);
    }
   /* public void featchTars(int id, int bank_id) {
        start = true;
        TransectionRequest transectionRequest = new TransectionRequest(id, bank_id, 0);
        //this.priceTotal.setText(((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getAvailableBalance() + " USD");
        getTrasList(transectionRequest);
    }*/
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addDeposit) {
            /*Intent intent1 = new Intent(mContext, AddTransactionActivity.class);
            intent1.putExtra("depositOrWithdrawal", "Deposit");
            startActivity(intent1);*/
            Bundle b=new Bundle();
            b.putString(Constant.CATEGORY,"accounting");
            b.putString(Constant.ACTION,"add_deposit");
            SplashScreenActivity.sendEvent("accounting_add_deposit",b);
            Intent intent1 = new Intent(mContext, AddTransactionActivity.class);
            intent1.putExtra("depositOrWithdrawal", "Deposit");
            startActivity(intent1);
        } else if (id == R.id.addWithDrawal) {
            /*Intent intent = new Intent(mContext, AddTransactionActivity.class);
            intent.putExtra("depositOrWithdrawal", "Withdrawal");
            startActivity(intent);*/
            Bundle b=new Bundle();
            b.putString(Constant.CATEGORY,"accounting");
            b.putString(Constant.ACTION,"add_with_drawal");
            SplashScreenActivity.sendEvent("accounting_add_with_drawal",b);
            Intent intent = new Intent(mContext, AddTransactionActivity.class);
            intent.putExtra("depositOrWithdrawal", "Withdrawal");
            startActivity(intent);
        } else if (id == R.id.ll_upload_bankstmt) {
            Intent intent2 = new Intent(mContext, UploadBankStatementActivity.class);
            intent2.putExtra("bankAccounts", (ArrayList) this.banksItems);
            startActivity(intent2);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.more_transactions_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.journalTransaction) {
            startActivity(new Intent(mContext, AddJournalTransactionActivity.class));
        } else if (item.getItemId() == R.id.connectYourBank) {
            startActivity(new Intent(mContext, BankListActivity.class));
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
            RestClient.getInstance(mContext).getJournalTransactionDetailsById("Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), transaction.getId()).enqueue(new CustomCallBack<JournalTransactionDetailByIdResponse>(mContext, null) {
                static final boolean $assertionsDisabled = false;

                public void onResponse(Call<JournalTransactionDetailByIdResponse> call, Response<JournalTransactionDetailByIdResponse> response) {
                    super.onResponse(call, response);
                    Log.d("Banks Response---", response.toString());
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        TransactionsActivity TransactionsActivity = TransactionsActivity.this;
                        TransactionsActivity.startActivity(AddJournalTransactionActivity.buildIntent(TransactionsActivity.mContext, response.body().getData()));
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
            RestClient.getInstance(mContext).getTransactionDetailsById("Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), transaction.getId()).enqueue(new CustomCallBack<TransactionDetailByIdResponse>(mContext, null) {
                public void onResponse(Call<TransactionDetailByIdResponse> call, Response<TransactionDetailByIdResponse> response) {
                    super.onResponse(call, response);
                    Log.d("Banks Response---", response.toString());
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        TransactionsActivity TransactionsActivity = TransactionsActivity.this;
                        TransactionsActivity.startActivity(AddTransactionActivity.buildIntent(TransactionsActivity.mContext, response.body().getData()));
                       /* TransactionAddFragment fragment = new TransactionAddFragment();
                        TransactionAddFragment.receivedData = response.body().getData();
                        fragment.setData(response.body().getData());
                        AddFragments.addFragmentToDrawerActivity(mContext, null, fragment.getClass());*/
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
        RestClient.getInstance(mContext).updateCatAction(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext), UiUtil.getComp_Id(mContext), transaction).enqueue(new CustomCallBack<Transaction>(mContext, null) {

            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                super.onResponse(call, response);
                Bundle b=new Bundle();
                b.putString(Constant.CATEGORY,"accounting");
                b.putString(Constant.ACTION,"update_cat_action");
                SplashScreenActivity.sendEvent("accounting_update_cat_action",b);
                if (!response.isSuccessful()) {
                    UiUtil.showToast(TransactionsActivity.this.mContext, "Failed. Please try after sometime");
                } else if (response.body() != null) {
                    UiUtil.showToast(TransactionsActivity.this.mContext, "Successfully updated");
                    defaultServiceCall();
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
