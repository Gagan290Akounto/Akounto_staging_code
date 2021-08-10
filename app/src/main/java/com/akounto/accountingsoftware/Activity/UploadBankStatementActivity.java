package com.akounto.accountingsoftware.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddBankStatementRequest;
import com.akounto.accountingsoftware.request.CSVBankTransactionItem;
import com.akounto.accountingsoftware.response.accounting.BanksItem;
import com.akounto.accountingsoftware.util.FileUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.levitnudi.legacytableview.LegacyTableView;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadBankStatementActivity extends AppCompatActivity {

    TextView accountIdErrorTv;
    PowerSpinnerView accountSpinner;
    TextView addAccount;
    Context mContext;
    List<BanksItem> banksItems = new ArrayList();
    List<CSVBankTransactionItem> cSVBankTransaction;
    Dialog dialog;
    EditText ob;
    List<List<String>> rows = new ArrayList();
    private int selectedBankAccountId = 0;
    String amount = "";
    String path = "";
    File f = null;
    Uri fileUri = null;
    final String uploadFilePath = "/mnt/sdcard/";
    Activity activity;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bank_statement);
        mContext = this;
        activity=this;
        ((TextView) findViewById(R.id.pageTitle)).setText("Uploaded Statement");
        ob = findViewById(R.id.ed_opening_balance);
        initUI();
        ArrayList arrayList = (ArrayList) getIntent().getSerializableExtra("bankAccounts");
        this.banksItems = arrayList;
        setAccountSpinner(arrayList);
    }

    private void initUI() {
        this.accountSpinner = findViewById(R.id.accountSpinner);
        this.addAccount = findViewById(R.id.addAccount);
        this.accountIdErrorTv = findViewById(R.id.accountIdErrorTV);
        findViewById(R.id.add_file).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("*/*");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "Select file"), 1001);
            }
        });
        findViewById(R.id.proceedButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                upload();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileUri=data.getData();
        f = new File(FileUtils.getRealPath(mContext,data.getData()));
    }

    private void showPreview(List<List<String>> cSVBankTransaction2) {
        Dialog dialog2 = new Dialog(this);
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.upload_statement_preview_layout);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LegacyTableView legacyTableView = this.dialog.findViewById(R.id.legacy_table_view);
        LegacyTableView.insertLegacyTitle("Date", "Descriptions", "Deposits", "Withdrawal");
        String[] itemsArray = new String[(cSVBankTransaction2.size() * 4)];
        List<String> allItems = new ArrayList<>();
        for (List<String> row : cSVBankTransaction2) {
            allItems.addAll(row);
        }
        LegacyTableView.insertLegacyContent(allItems.toArray(itemsArray));
        legacyTableView.setTitleTextSize(32);
        legacyTableView.setContentTextSize(24);
        legacyTableView.setTitle(LegacyTableView.readLegacyTitle());
        legacyTableView.setContent(LegacyTableView.readLegacyContent());
        legacyTableView.setTablePadding(10);
        legacyTableView.build();
        this.dialog.findViewById(R.id.uploadButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                UploadBankStatementActivity.this.lambda$showPreview$2$UploadBankStatementActivity(view);
            }
        });
        this.dialog.show();
    }

    public void lambda$showPreview$2$UploadBankStatementActivity(View v) {
        upload();
        this.dialog.dismiss();
    }

    private void prepareUpload() {
        RestClient.getInstance(this).uploadBankStatement(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), new AddBankStatementRequest(0, this.selectedBankAccountId, this.cSVBankTransaction)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Statement...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(UploadBankStatementActivity.this, "Added");
                    UploadBankStatementActivity.this.finish();
                    return;
                }
                UiUtil.showToast(UploadBankStatementActivity.this, "Error while adding");
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void setAccountSpinner(List<BanksItem> bankResponse) {
        ArrayList<String> accounts = new ArrayList<>();
        if (bankResponse != null) {
            for (BanksItem banksItem : bankResponse) {
                accounts.add(banksItem.getInstitutionName());
            }
            this.accountSpinner.setItems(accounts);
            this.accountSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {

                public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                    UploadBankStatementActivity.this.lambda$setAccountSpinner$3$UploadBankStatementActivity(bankResponse, i, (String) obj, i2, (String) obj2);
                    //Toast.makeText(UploadBankStatementActivity.this, bankResponse.get(i).getBankAccounts().getCurrentBalance()+"", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void upload() {
        // use the FileUtils to get the actual file by uri
        //File file = FileUtils.getFile(this, fileUri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        f
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", f.getName(), requestFile);

        RequestBody bank_account_id = RequestBody.create(MediaType.parse("BankAccountId"), String.valueOf(selectedBankAccountId));
        RequestBody opbal = RequestBody.create(MediaType.parse("OpeningBallance"), ob.getText().toString());
        RequestBody sig = RequestBody.create(MediaType.parse("X-Signature"), Constant.X_SIGNATURE);
        RequestBody auth = RequestBody.create(MediaType.parse("Authorization"), "Bearer " + UiUtil.getAcccessToken(getApplicationContext()));
        RequestBody copm_id = RequestBody.create(MediaType.parse("X-Company"), UiUtil.getComp_Id(getApplicationContext()));

        if (f != null) {
            RestClient.getInstance(mContext).uploadBankCSV(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), body, bank_account_id, opbal).enqueue(new Callback<BankAccountData>() {
                public void onResponse(Call<BankAccountData> call, Response<BankAccountData> response) {
                    UiUtil.cancelProgressDialogue();
                    try {
                        if (response.isSuccessful()) {
                            //et_invoiceNo.setText(response.body().getData());
                        }
                    } catch (Exception e) {
                    }
                }

                public void onFailure(Call<BankAccountData> call, Throwable t) {
                    Log.d("error", t.toString());
                    UiUtil.cancelProgressDialogue();
                }
            });
        } else {
            Toast.makeText(mContext, "Please upload csv first", Toast.LENGTH_SHORT).show();
        }
    }

    public void lambda$setAccountSpinner$3$UploadBankStatementActivity(List bankResponse, int i, String s, int selectedIndex, String selectedItem) {
        this.selectedBankAccountId = ((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getId();
        this.amount = ((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().getCurrentBalance() + "";
        if (!((BanksItem) bankResponse.get(selectedIndex)).getBankAccounts().isIsFirtUpload()) {
            ob.setText("");
            ob.setVisibility(View.VISIBLE);
        } else {
            ob.setText("0.0");
            ob.setVisibility(View.GONE);
        }
    }
}
