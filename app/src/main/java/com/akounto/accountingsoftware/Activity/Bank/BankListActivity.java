package com.akounto.accountingsoftware.Activity.Bank;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Activity.Accounting.TransactionsActivity;
import com.akounto.accountingsoftware.Activity.fragment.TransactionsFragment;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Currency;
import com.akounto.accountingsoftware.Data.CurrencyData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Data.RegisterBank.Account;
import com.akounto.accountingsoftware.Data.RegisterBank.Bank;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData2;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.ViewModel.BankListViewModel;
import com.akounto.accountingsoftware.adapter.AccountList;
import com.akounto.accountingsoftware.adapter.BankListAdapter;
import com.akounto.accountingsoftware.databinding.LayoutBankImportLisBinding;
import com.akounto.accountingsoftware.request.RegisterBankRequest;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.plaid.link.Plaid;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkError;
import com.plaid.link.result.LinkResultHandler;
import com.plaid.link.result.LinkSuccessMetadata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankListActivity extends AppCompatActivity implements BankListAdapter.OnItemClickListener {

    private int mDay;
    private int mMonth;
    private int mYear;
    private boolean dilog_flag = true;
    private BankListViewModel model;
    private ArrayList<Bank> arr = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context mContext;
    private ImageView btn_coonect_bank, bck;
    private Dialog dialog, dialog2;
    private LifecycleOwner owner;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Account account = null;
    private boolean isRegister = true;
    private LinearLayout more_top;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);

        btn_coonect_bank = findViewById(R.id.btn_connect_bank);
        bck = findViewById(R.id.iv_back);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<BankAccountData2> data2s = new ArrayList<>();
        model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(BankListViewModel.class);
        recyclerView = findViewById(R.id.tv_bank_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;
        owner = this;
        btn_coonect_bank.setOnClickListener(v -> {
            model.linkBank(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext)).observe(owner, bankLinkData -> {
                if (bankLinkData.getStatus() == 0) {
                    //Toast.makeText(getApplicationContext(), bankLinkData.getBankData().getLinkToken(), Toast.LENGTH_SHORT).show();
                    onLinkTokenSuccess(bankLinkData.getBankData().getLinkToken());
                } else {
                    Toast.makeText(mContext, bankLinkData.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        model.loadBank(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext)).observe(this, bankAccountData -> {
            if (bankAccountData.getStatus() == 0) {
                if (bankAccountData.getData().getBanks().size() != 0) {
                    setAdapter(bankAccountData.getData().getBanks());
                } else {
                    Toast.makeText(getApplicationContext(), "Not Data for display", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), bankAccountData.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        model.loadBank(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext));
    }

    @Override
    public void onItemClick(Bank bank, int position, boolean state) {

        model.autoSync(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext), bank.getId().toString(), bank.getBankAccounts().getId().toString(), state).observe(this, bankAccountData -> {
            if (bankAccountData.getStatus() == 0) {
                setAdapter(bankAccountData.getBanks());
            } else {
                Toast.makeText(getApplicationContext(), bankAccountData.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemDownload(Bank bank, int position, boolean state) {
        showDialog(mContext, bank);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
            Log.i(BankListActivity.class.getSimpleName(), "Not handled");
        }
    }

    private void onLinkTokenSuccess(String token) {
        Plaid.create(
                getApplication(),
                new LinkTokenConfiguration.Builder()
                        .token(token)
                        .build())
                .open(this);
    }

    private final LinkResultHandler myPlaidResultHandler = new LinkResultHandler(
            linkSuccess -> {
                LinkSuccessMetadata metadata = linkSuccess.getMetadata();
                String publicToken = linkSuccess.getPublicToken();
                String institutionId = metadata.getInstitution().getId();
                String institutionName = metadata.getInstitution().getName();
                if (isRegister) {
                    loadRegister(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext), publicToken, institutionId, institutionName);
             /*       model.registerBank(mContext, publicToken, institutionId, institutionName).observe(this, new Observer<BankAccountData2>() {
                        @Override
                        public void onChanged(BankAccountData2 bankAccountData2) {
                            try {
                                isRegister = false;
                                if (bankAccountData2.getStatus() == 0) {
                                    if (bankAccountData2.getData().getAccounts().size() != 0) {
                                        openListDialog(bankAccountData2);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Not data for display", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //Toast.makeText(getApplicationContext(), bankAccountData.getStatusMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {

                            }
                        }
                    });*/
                }
                return Unit.INSTANCE;
            },
            linkExit -> {
                LinkError error = linkExit.getError();
                // String displayMessage = error.getDisplayMessage();
                //Toast.makeText(this, displayMessage, Toast.LENGTH_SHORT).show();
                   /* if (linkExit.getError() != null) {
                        Toast.makeText(this, getString(
                                R.string.content_exit,
                                linkExit.getError().getDisplayMessage(),
                                linkExit.getError().getErrorCode()), Toast.LENGTH_SHORT).show();
                    } else {
                     Toast.makeText(this, getString(
                                R.string.content_cancel,
                                linkExit.getMetadata().getStatus() != null ? linkExit.getMetadata()
                                        .getStatus()
                                        .getJsonValue() : "unknown"), Toast.LENGTH_SHORT).show();
                    }*/
                return Unit.INSTANCE;
            }
    );

    public void setAdapter(List<Bank> banks) {
        arr.clear();
        arr.addAll(banks);
        recyclerView.removeAllViews();
        recyclerView.setAdapter(new BankListAdapter(arr, BankListActivity.this, mContext));
        recyclerView.invalidate();
    }

    public void setAdapter() {
        recyclerView.removeAllViews();
        recyclerView.setAdapter(new BankListAdapter(arr, BankListActivity.this, mContext));
        recyclerView.invalidate();
    }

    public void showDialog(Context activity, Bank bank) {

        dialog = new Dialog(activity);
        if (!dialog.isShowing()) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dilog_bank_import);
            Button btn_import = dialog.findViewById(R.id.btn_import);
            Button btn_cancle = dialog.findViewById(R.id.btn_cancel);
            btn_import.setOnClickListener(v -> {
                dialog.dismiss();
                model.import_trans(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext), bank.getId().toString()).observe(this, bankAccountData -> {
                    if (bankAccountData.getStatus() == 0) {
                        Intent i=new Intent(mContext, TransactionsActivity.class);
                        i.putExtra(Constant.BANK_ID,bank.getId().toString());
                        startActivity(i);
                        //setAdapter(bankAccountData.getData().getBanks());
                    } else {
                        Toast.makeText(getApplicationContext(), bankAccountData.getStatusMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            });
            btn_cancle.setOnClickListener(v -> {
                dialog.dismiss();
                setAdapter();
            });
            dialog.show();
        }
    }

    protected void openListDialog(BankAccountData2 data) {
        LayoutBankImportLisBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.layout_bank_import_lis, null, false);
        dialog2 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        if (!dialog2.isShowing()) {
            dialog2.requestWindowFeature(1);
            dialog2.setCancelable(true);
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.setContentView(mBinding.getRoot());
            mBinding.tvBankName.setText(data.getData().getBankName());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1);
            calendar.add(Calendar.MONTH, -1);
            Date date = calendar.getTime();
            String dateString = sdf.format(date);
            mBinding.tvDate.setText(dateString);

            mBinding.tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initUiDate(mBinding.tvDate, mBinding.tvDate.getText().toString());
                }
            });

            recyclerView.setHasFixedSize(true);
            mBinding.list.setLayoutManager(new GridLayoutManager(mContext, 1));
            RecyclerView rView = mBinding.list;
            AccountList adapter = new AccountList(data.getData().getAccounts(), mContext);
            rView.setAdapter(adapter);
            mBinding.btnCancel.setOnClickListener(v -> {
                isRegister = true;
                dialog2.dismiss();
            });
            mBinding.btnImport.setOnClickListener(v -> {
                isRegister = true;
                dialog2.dismiss();
                try {
                    account = adapter.getSelectedAccount();
                } catch (Exception e) {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                }
                if (account != null) {
                    RegisterBankRequest req = new RegisterBankRequest("" + data.getData().getBankId(), account.getName(), account.getMaskAccountNumber(), account.getAccountNumber(), "" + account.getAccountType(), account.getAccountSubType(), account.getCurrency(), mBinding.tvDate.getText().toString());
                    model.loadregisterBank(mContext, UiUtil.getComp_Id(mContext), UiUtil.getAcccessToken(mContext), req);
                    Log.e("JSON :: ", new Gson().toJson(req));
                } else {
                    Toast.makeText(mContext, "Bank account not selected.", Toast.LENGTH_LONG).show();
                }
            });
            dialog2.show();
        }
    }

    public void initUiDate(TextView v, String da) {
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                DatePopup(v, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        String[] d = da.split("-");
        datePickerDialog.updateDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.YEAR, -2); // subtract 2 years from now
        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void DatePopup(TextView view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        view.setText(sdf.format(calendar.getTime()));
    }

    public void loadRegister(Context mContext, String x_comp, String access_token, String public_token, String institution_id, String institution_name) {
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        Api api = ApiUtils.getAPIService();
        api.registerBankRequest(Constant.X_SIGNATURE, x_comp, "Bearer " + access_token, public_token, institution_id, institution_name).enqueue(new Callback<BankAccountData2>() {
            @Override
            public void onResponse(Call<BankAccountData2> call, Response<BankAccountData2> response) {
                UiUtil.cancelProgressDialogue();
                BankAccountData2 ud = new BankAccountData2();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
                            if (response.body().getData().getAccounts().size() != 0) {
                                openListDialog(response.body());
                            } else {
                                Toast.makeText(getApplicationContext(), "Not data for display", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext, ((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        Toast.makeText(mContext, error.getError_description(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<BankAccountData2> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
