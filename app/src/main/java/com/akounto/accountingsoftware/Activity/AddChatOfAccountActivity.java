package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.AddWithdrawalCategoryAdapter;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddAccountRequest;
import com.akounto.accountingsoftware.response.accounting.AccountDetailById;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

public class AddChatOfAccountActivity extends AppCompatActivity implements TransactionCategoryClick {
    AccountDetailById accountDetailById;
    EditText accountIdEDTV;
    TextView accountIdErrorTV;
    TextView accountNameErrorTV;
    EditText accountNmeEDTV;
    private List<AddWithdrawalCategorySpinnerItem> categories = new ArrayList();
    int categoryId = 0;
    TextView chooseAccountTv;
    List<String> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    PowerSpinnerView currencySpinner;
    EditText descriptionEDTV;
    Dialog dialog;
    LinearLayout editAccountIdOrDesc;
    TextView editNote;

    /* renamed from: id */
    int f88id = 0;
    String selectedCurrencyId = "USD";
    String subHeadId = "";

    public static Intent buildIntent(Context context, int categoryId2) {
        Intent intent = new Intent(context, AddChatOfAccountActivity.class);
        intent.putExtra("selectedCategory", categoryId2);
        return intent;
    }

    public static Intent buildIntentForEdit(Context context, AccountDetailById accountDetailById2) {
        Intent intent = new Intent(context, AddChatOfAccountActivity.class);
        intent.putExtra("accountDetailById", accountDetailById2);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chart_of_account);
        TextView pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Add Account");
        ((ImageView) findViewById(R.id.backButton)).setImageResource(R.drawable.ic_baseline_close_24);
        inItUi();
        this.categories = LocalManager.getInstance().getCategoryList();
        this.categoryId = getIntent().getIntExtra("selectedCategory", 0);
        this.accountDetailById = (AccountDetailById) getIntent().getSerializableExtra("accountDetailById");
        try {
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.accountDetailById != null) {
            pageTitle.setText("Edit Account");
            this.chooseAccountTv.setEnabled(false);
            this.chooseAccountTv.setAlpha(0.5f);
            this.editNote.setVisibility(View.VISIBLE);
            this.editAccountIdOrDesc.setVisibility(View.GONE);
            this.editNote.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AddChatOfAccountActivity.this.lambda$onCreate$0$AddChatOfAccountActivity(view);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onCreate$0$AddChatOfAccountActivity(View v) {
        this.editAccountIdOrDesc.setVisibility(View.VISIBLE);
        this.editNote.setVisibility(View.GONE);
    }

    private void inItUi() {
        this.chooseAccountTv = findViewById(R.id.accountNameSpinner);
        this.currencySpinner = findViewById(R.id.accountCurrencySpinner);
        this.accountNameErrorTV = findViewById(R.id.accountNameErrorTV);
        this.accountIdErrorTV = findViewById(R.id.accountIdErrorTV);
        this.accountNmeEDTV = findViewById(R.id.accountNmeEDTV);
        this.accountIdEDTV = findViewById(R.id.accountIdEDTV);
        this.descriptionEDTV = findViewById(R.id.descriptionEDTV);
        this.editNote = findViewById(R.id.editAccountIdOrDesc);
        this.chooseAccountTv.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddChatOfAccountActivity.this.lambda$inItUi$1$AddChatOfAccountActivity(view);
            }
        });
        this.editAccountIdOrDesc = findViewById(R.id.accountIdAndDescLayout);
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddChatOfAccountActivity.this.lambda$inItUi$2$AddChatOfAccountActivity(view);
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddChatOfAccountActivity.this.lambda$inItUi$3$AddChatOfAccountActivity(view);
            }
        });
    }

    public /* synthetic */ void lambda$inItUi$1$AddChatOfAccountActivity(View v) {
        selectCategoryDialog();
    }

    public /* synthetic */ void lambda$inItUi$2$AddChatOfAccountActivity(View v) {
        addAccount();
    }

    public /* synthetic */ void lambda$inItUi$3$AddChatOfAccountActivity(View v) {
        finish();
    }

    private void selectCategoryDialog() {
        if (this.categories != null) {
            Dialog dialog2 = new Dialog(this);
            this.dialog = dialog2;
            dialog2.requestWindowFeature(1);
            this.dialog.setContentView(R.layout.dialogue_add_bill_item);
            this.dialog.setCancelable(true);
            this.dialog.setCanceledOnTouchOutside(true);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.dialog.findViewById(R.id.addNewItem).setVisibility(View.GONE);
            RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
            AddWithdrawalCategoryAdapter adapter = new AddWithdrawalCategoryAdapter(this, this.categories, this);
            listItemRecycler.setLayoutManager(new LinearLayoutManager(this));
            listItemRecycler.setAdapter(adapter);
            this.dialog.show();
        }
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", this);
//        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(jsonObject.getString("Id"));
            this.currencyListForSpinner.add(jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name"));
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencies) {
        this.currencySpinner.setItems(currencies);
        this.currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddChatOfAccountActivity.this.lambda$setCurrencySpinner$4$AddChatOfAccountActivity(i, (String) obj, i2, (String) obj2);
            }
        });
        AccountDetailById accountDetailById2 = this.accountDetailById;
        if (accountDetailById2 != null) {
            fetchReceivedData(accountDetailById2);
        } else {
            updateAccountDetails(this.categoryId);
        }
    }

    public /* synthetic */ void lambda$setCurrencySpinner$4$AddChatOfAccountActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedCurrencyId = selectedItem.substring(0, 3);
    }

    private void updateAccountDetails(int accountId) {
        try {
            this.subHeadId = String.valueOf(accountId);
            this.chooseAccountTv.setText(getCategoryDetails(accountId).getTitle());
        } catch (Exception e) {
        }
    }

    private AddWithdrawalCategorySpinnerItem getCategoryDetails(int transactionHeadId) {
        for (AddWithdrawalCategorySpinnerItem item : this.categories) {
            if (transactionHeadId == item.getId()) {
                return item;
            }
        }
        return null;
    }

    private void fetchReceivedData(AccountDetailById accountDetailById2) {
        this.f88id = accountDetailById2.getId();
        this.descriptionEDTV.setText(accountDetailById2.getDescription());
        this.accountNmeEDTV.setText(accountDetailById2.getName());
        this.accountIdEDTV.setText(accountDetailById2.getAccountTaxId());
        int subHeadsId = accountDetailById2.getSubHeadsId();
        this.categoryId = subHeadsId;
        updateAccountDetails(subHeadsId);
        this.currencySpinner.selectItemByIndex(this.currencyList.indexOf(accountDetailById2.getCurrency()));
    }

    public void onCategoryClick(AddWithdrawalCategorySpinnerItem item, TextView textView, EditText editText) {
        this.chooseAccountTv.setText(item.getTitle());
        this.subHeadId = "" + item.getId();
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
    }

    private void addAccount() {
        String description;
        String mode;
        String accountName = this.accountNmeEDTV.getText().toString();
        if (accountName.isEmpty()) {
            this.accountNameErrorTV.setVisibility(View.VISIBLE);
        } else {
            this.accountNameErrorTV.setVisibility(View.GONE);
        }
        String accountId = this.accountIdEDTV.getText().toString();
        if (accountId.isEmpty()) {
            this.accountIdErrorTV.setVisibility(View.VISIBLE);
        } else {
            this.accountIdErrorTV.setVisibility(View.GONE);
        }
        if (this.descriptionEDTV.getText().toString().isEmpty()) {
            description = "";
        } else {
            description = this.descriptionEDTV.getText().toString();
        }
        AddAccountRequest addAccountRequest = new AddAccountRequest(description, this.subHeadId, this.selectedCurrencyId, accountId, accountName, this.f88id);
        if (this.accountDetailById != null) {
            mode = "edit";
        } else {
            mode = "add";
        }
        RestClient.getInstance(this).addAccount(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), addAccountRequest, mode).enqueue(new CustomCallBack<ResponseBody>(this, null) {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(AddChatOfAccountActivity.this, "Added");
                    AddChatOfAccountActivity.this.finish();
                    return;
                }
                UiUtil.showToast(AddChatOfAccountActivity.this, "Error while adding");
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }
}
