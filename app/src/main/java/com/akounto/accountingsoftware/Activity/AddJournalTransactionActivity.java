package com.akounto.accountingsoftware.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
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
import com.akounto.accountingsoftware.model.AddWithDrawalCategory;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddJournalTransactionRequest;
import com.akounto.accountingsoftware.request.JournalTransactionsItem;
import com.akounto.accountingsoftware.response.chartaccount.GetChartData;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.response.chartaccount.HeadSubType;
import com.akounto.accountingsoftware.response.chartaccount.HeadTransactions;
import com.akounto.accountingsoftware.response.chartaccount.HeadType;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.mikephil.charting.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AddJournalTransactionActivity extends AppCompatActivity implements TransactionCategoryClick {
    double creditAmountSum = Utils.DOUBLE_EPSILON;
    LinearLayout creditList;
    View creditView;
    double debitAmountSum = Utils.DOUBLE_EPSILON;
    LinearLayout debitList;
    View debitView;
    EditText descEditText;
    Dialog dialog;
    TextView errorTv;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss";
    List<AddWithdrawalCategorySpinnerItem> list = new ArrayList();
    List<AddWithdrawalCategorySpinnerItem> localCategoriesList = new ArrayList();
    private int mDay;
    private int mMonth;
    private int mYear;
    EditText notesEditText;
    TextView pageTitle;
    List<JournalTransactionsItem> receivedCreditJournalTransactions = new ArrayList();
    AddJournalTransactionRequest receivedData;
    private int receivedDataId = 0;
    List<JournalTransactionsItem> receivedDebitJournalTransactions = new ArrayList();
    TextView selectDate;
    int selectedBankAccountId = 0;
    String selectedFormattedDate;
    SimpleDateFormat simpleDateFormat;
    TextView totalAmountTv;

    public static Intent buildIntent(Context context, AddJournalTransactionRequest transaction) {
        Intent intent = new Intent(context, AddJournalTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal_transaction_activity);
        this.pageTitle = findViewById(R.id.pageTitle);
        ((ImageView) findViewById(R.id.backButton)).setImageResource(R.drawable.ic_baseline_close_24);
        this.pageTitle.setText("Add Journal transaction details");
        this.totalAmountTv = findViewById(R.id.totalAmount);
        this.simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        if (getIntent().getSerializableExtra("transaction") != null) {
            this.receivedData = (AddJournalTransactionRequest) getIntent().getSerializableExtra("transaction");
        }
        this.debitList = findViewById(R.id.debitList);
        addDebit();
        this.creditList = findViewById(R.id.creditList);
        addCredit();
        this.errorTv = findViewById(R.id.errorNote);
        this.notesEditText = findViewById(R.id.notes);
        this.descEditText = findViewById(R.id.et_desc);
        Locale.setDefault(Locale.US);
        this.selectDate = findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        AddJournalTransactionRequest addJournalTransactionRequest = this.receivedData;
        if (addJournalTransactionRequest != null) {
            try {
                c.setTime(this.simpleDateFormat.parse(addJournalTransactionRequest.getTransactionDate()));
                this.descEditText.setText(this.receivedData.getDescription());
                this.notesEditText.setText(this.receivedData.getNotes().toString());
                TextView textView = this.totalAmountTv;
                textView.setText("" + this.receivedData.getAmount());
                this.receivedDataId = this.receivedData.getId();
                for (JournalTransactionsItem journalTransactionsItem : this.receivedData.getJournalTransactions()) {
                    if (journalTransactionsItem.getTransactionType() == 1) {
                        this.receivedCreditJournalTransactions.add(journalTransactionsItem);
                    } else {
                        this.receivedDebitJournalTransactions.add(journalTransactionsItem);
                    }
                }
                disableView();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        TextView textView2 = this.selectDate;
        textView2.setText(this.mDay + "-" + (this.mMonth + 1) + "-" + this.mYear);
        this.selectedFormattedDate = this.simpleDateFormat.format(c.getTime());
        this.selectDate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$onCreate$1$AddJournalTransactionActivity(view);
            }
        });
        findViewById(R.id.addDebit).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$onCreate$2$AddJournalTransactionActivity(view);
            }
        });
        findViewById(R.id.addCredit).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$onCreate$3$AddJournalTransactionActivity(view);
            }
        });
        findViewById(R.id.applyButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$onCreate$4$AddJournalTransactionActivity(view);
            }
        });
    }

    public void lambda$onCreate$1$AddJournalTransactionActivity(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                AddJournalTransactionActivity.this.lambda$null$0$AddJournalTransactionActivity(datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void lambda$null$0$AddJournalTransactionActivity(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, dayOfMonth);
        calendar.set(2, monthOfYear);
        calendar.set(1, year);
        TextView textView = this.selectDate;
        textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        this.selectedFormattedDate = this.simpleDateFormat.format(calendar.getTime());
    }

    public void lambda$onCreate$2$AddJournalTransactionActivity(View v) {
        addDebit();
    }

    public void lambda$onCreate$3$AddJournalTransactionActivity(View v) {
        addCredit();
    }

    public void lambda$onCreate$4$AddJournalTransactionActivity(View v) {
        addTransaction();
    }

    private void disableView() {
        this.selectDate.setEnabled(false);
        this.selectDate.setAlpha(0.6f);
        this.totalAmountTv.setEnabled(false);
        this.totalAmountTv.setAlpha(0.6f);
    }

    private void updateCategories(List<JournalTransactionsItem> receivedDebitJournalTransactions2, List<JournalTransactionsItem> receivedCreditJournalTransactions2) {
        this.debitList.removeAllViews();
        for (JournalTransactionsItem journalTransactionsItem : receivedDebitJournalTransactions2) {
            View newCategoryView = getLayoutInflater().inflate(R.layout.item_add_transaction_category_layout, null, false);
            EditText debitAmountEt = newCategoryView.findViewById(R.id.cattotalAMout);
            debitAmountEt.setText(journalTransactionsItem.getAmount());
            onCategoryClick(getCategoryDetails(journalTransactionsItem.getTransactionHeadId()), newCategoryView.findViewById(R.id.category), newCategoryView.findViewById(R.id.cattotalAMout));
            this.debitList.addView(newCategoryView);
            newCategoryView.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    AddJournalTransactionActivity.this.lambda$updateCategories$5$AddJournalTransactionActivity(newCategoryView, debitAmountEt, view);
                }
            });
            ((TextView) newCategoryView.findViewById(R.id.title)).setText("Debit");
            updateTotalAmount(debitAmountEt, 2);
            ImageView deleteCategory = newCategoryView.findViewById(R.id.deleteCategory);
            if (this.debitList.getChildCount() > 1) {
                deleteCategory.setVisibility(View.VISIBLE);
            }
            deleteCategory.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AddJournalTransactionActivity.this.lambda$updateCategories$6$AddJournalTransactionActivity(newCategoryView, view);
                }
            });
        }
        this.creditList.removeAllViews();
        for (JournalTransactionsItem journalTransactionsItem2 : receivedCreditJournalTransactions2) {
            View newCategoryView2 = getLayoutInflater().inflate(R.layout.item_add_transaction_category_layout, null, false);
            EditText creditAmountEt = newCategoryView2.findViewById(R.id.cattotalAMout);
            creditAmountEt.setText(journalTransactionsItem2.getAmount());
            onCategoryClick(getCategoryDetails(journalTransactionsItem2.getTransactionHeadId()), newCategoryView2.findViewById(R.id.category), newCategoryView2.findViewById(R.id.cattotalAMout));
            this.creditList.addView(newCategoryView2);
            ((TextView) newCategoryView2.findViewById(R.id.title)).setText("Credit");
            newCategoryView2.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {

                public final void onClick(View view) {
                    AddJournalTransactionActivity.this.lambda$updateCategories$7$AddJournalTransactionActivity(newCategoryView2, creditAmountEt, view);
                }
            });
            updateTotalAmount(creditAmountEt, 1);
            ImageView deleteCategory2 = newCategoryView2.findViewById(R.id.deleteCategory);
            if (this.creditList.getChildCount() > 1) {
                deleteCategory2.setVisibility(View.VISIBLE);
            }
            deleteCategory2.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AddJournalTransactionActivity.this.lambda$updateCategories$8$AddJournalTransactionActivity(newCategoryView2, view);
                }
            });
        }
    }

    public void lambda$updateCategories$5$AddJournalTransactionActivity(View newCategoryView, EditText debitAmountEt, View v) {
        selectCategoryDialog(newCategoryView.findViewById(R.id.category), debitAmountEt);
    }

    public void lambda$updateCategories$6$AddJournalTransactionActivity(View newCategoryView, View v) {
        this.debitList.removeView(newCategoryView);
    }

    public void lambda$updateCategories$7$AddJournalTransactionActivity(View newCategoryView, EditText creditAmountEt, View v) {
        selectCategoryDialog(newCategoryView.findViewById(R.id.category), creditAmountEt);
    }

    public void lambda$updateCategories$8$AddJournalTransactionActivity(View newCategoryView, View v) {
        this.creditList.removeView(newCategoryView);
    }

    private AddWithdrawalCategorySpinnerItem getCategoryDetails(int transactionHeadId) {
        for (AddWithdrawalCategorySpinnerItem item : this.list) {
            if (transactionHeadId == item.getId()) {
                return item;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getCharts();
    }

    private void getCharts() {
        RestClient.getInstance(this).getChart(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<GetChartResponse>(this, null) {
            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                super.onResponse(call, response);
                //Log.d("111111----2222", new Gson().toJson((Object) response.body().getData()));
                if (response.isSuccessful()) {
                    AddJournalTransactionActivity.this.prepareCategorySpinner(response.body().getData());
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
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
        if (this.receivedData != null) {
            updateCategories(this.receivedDebitJournalTransactions, this.receivedCreditJournalTransactions);
        }
    }

    private void selectCategoryDialog(TextView textView, EditText editText) {
        Dialog dialog2 = new Dialog(this);
        this.dialog = dialog2;
        dialog2.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialogue_add_bill_item);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(true);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView listItemRecycler = this.dialog.findViewById(R.id.listItemRecycler);
        AddWithdrawalCategoryAdapter adapter = new AddWithdrawalCategoryAdapter(this, this.list, textView, editText, this);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(this));
        listItemRecycler.setAdapter(adapter);
        this.dialog.show();
    }

    private void addDebit() {
        View newCategoryView = getLayoutInflater().inflate(R.layout.item_add_transaction_category_layout, null, false);
        this.debitList.addView(newCategoryView);
        EditText debitAmountEt = newCategoryView.findViewById(R.id.cattotalAMout);
        newCategoryView.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$addDebit$9$AddJournalTransactionActivity(newCategoryView, debitAmountEt, view);
            }
        });
        ((TextView) newCategoryView.findViewById(R.id.title)).setText("Debit");
        updateTotalAmount(debitAmountEt, 2);
        ImageView deleteCategory = newCategoryView.findViewById(R.id.deleteCategory);
        if (this.debitList.getChildCount() > 1) {
            deleteCategory.setVisibility(View.VISIBLE);
        }
        deleteCategory.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$addDebit$10$AddJournalTransactionActivity(newCategoryView, view);
            }
        });
    }

    public void lambda$addDebit$9$AddJournalTransactionActivity(View newCategoryView, EditText debitAmountEt, View v) {
        selectCategoryDialog(newCategoryView.findViewById(R.id.category), debitAmountEt);
    }

    public void lambda$addDebit$10$AddJournalTransactionActivity(View newCategoryView, View v) {
        this.debitList.removeView(newCategoryView);
    }

    private void addCredit() {
        View newCategoryView = getLayoutInflater().inflate(R.layout.item_add_transaction_category_layout, null, false);
        EditText creditAmountEt = newCategoryView.findViewById(R.id.cattotalAMout);
        this.creditList.addView(newCategoryView);
        ((TextView) newCategoryView.findViewById(R.id.title)).setText("Credit");
        newCategoryView.findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$addCredit$11$AddJournalTransactionActivity(newCategoryView, creditAmountEt, view);
            }
        });
        updateTotalAmount(creditAmountEt, 1);
        ImageView deleteCategory = newCategoryView.findViewById(R.id.deleteCategory);
        if (this.creditList.getChildCount() > 1) {
            deleteCategory.setVisibility(View.VISIBLE);
        }
        deleteCategory.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                AddJournalTransactionActivity.this.lambda$addCredit$12$AddJournalTransactionActivity(newCategoryView, view);
            }
        });
    }

    public void lambda$addCredit$11$AddJournalTransactionActivity(View newCategoryView, EditText creditAmountEt, View v) {
        selectCategoryDialog(newCategoryView.findViewById(R.id.category), creditAmountEt);
    }

    public void lambda$addCredit$12$AddJournalTransactionActivity(View newCategoryView, View v) {
        this.creditList.removeView(newCategoryView);
    }

    private void addTransaction() {
        int i;
        boolean debitAdded = false;
        boolean creditAdded = false;
        this.debitAmountSum = Utils.DOUBLE_EPSILON;
        this.creditAmountSum = Utils.DOUBLE_EPSILON;
        List<JournalTransactionsItem> journalTransactionsItems = new ArrayList<>();
        int i2 = 0;
        while (true) {
            int childCount = this.debitList.getChildCount();
            i = R.id.cattotalAMout;
            if (i2 >= childCount) {
                break;
            }
            View view = this.debitList.getChildAt(i2);
            int debitId = getTransactionHeadIdForCategory((String) ((TextView) view.findViewById(R.id.category)).getText());
            EditText debitAmountEt = view.findViewById(R.id.cattotalAMout);
            debitAdded = debitAmountEt.isEnabled();
            String debitAmount = debitAmountEt.getText().toString();
            if (debitAmount.equals("")) {
                debitAmount = "0";
            }
            String debitAmount2 = debitAmount;
            this.debitAmountSum += Double.parseDouble(debitAmount2);
           // JournalTransactionsItem journalTransactionsItem = null;
            JournalTransactionsItem journalTransactionsItem2 = new JournalTransactionsItem(2, 0, debitAmount2, debitId, 0, null);
            journalTransactionsItems.add(journalTransactionsItem2);
            i2++;
        }
        int i3 = 0;
        while (i3 < this.creditList.getChildCount()) {
            View view2 = this.creditList.getChildAt(i3);
            int creditId = getTransactionHeadIdForCategory((String) ((TextView) view2.findViewById(R.id.category)).getText());
            EditText creditAmountEt = view2.findViewById(i);
            creditAdded = creditAmountEt.isEnabled();
            String creditAmount = creditAmountEt.getText().toString();
            if (creditAmount.equals("")) {
                creditAmount = "0";
            }
            String creditAmount2 = creditAmount;
            this.creditAmountSum += Double.parseDouble(creditAmount2);
            //JournalTransactionsItem journalTransactionsItem3 = r11;
            JournalTransactionsItem journalTransactionsItem3 = new JournalTransactionsItem(1, 0, creditAmount2, creditId, 0, null);
            journalTransactionsItems.add(journalTransactionsItem3);
            i3++;
            i = R.id.cattotalAMout;
        }
        String desc = this.descEditText.getText().toString();
        if (desc.isEmpty()) {
            showErrorTv("Please enter description");
            return;
        }
        hideErrorTv();
        if (!debitAdded) {
            showErrorTv("Please select Debit Category");
            return;
        }
        hideErrorTv();
        if (!creditAdded) {
            showErrorTv("Please select Credit Category");
            return;
        }
        hideErrorTv();
        if (this.creditAmountSum != this.debitAmountSum) {
            this.totalAmountTv.setText("Unbalanced");
            showErrorTv("Sum of credit and debit should be same");
            return;
        }
        hideErrorTv();
        this.totalAmountTv.setText(String.valueOf(this.debitAmountSum));
        submitTransaction(new AddJournalTransactionRequest(desc, (int) this.debitAmountSum, journalTransactionsItems, this.receivedDataId, this.notesEditText.getText().toString(), this.selectedFormattedDate));
    }

    private void updateTotalAmount(EditText editText, int transactionType) {
       editText.setOnEditorActionListener((textView, i, keyEvent) -> AddJournalTransactionActivity.this.lambda$updateTotalAmount$13$AddJournalTransactionActivity(transactionType, textView, i, keyEvent));
    }

    public boolean lambda$updateTotalAmount$13$AddJournalTransactionActivity(int transactionType, TextView v, int actionId, KeyEvent event) {
        String str;
        if (actionId != 6 && actionId != 2) {
            return false;
        }
        if (transactionType == 1) {
            this.creditAmountSum = Utils.DOUBLE_EPSILON;
            for (int i = 0; i < this.creditList.getChildCount(); i++) {
                String creditAmount = ((EditText) this.creditList.getChildAt(i).findViewById(R.id.cattotalAMout)).getText().toString();
                if (creditAmount.equals("")) {
                    creditAmount = "0";
                }
                this.creditAmountSum += Double.parseDouble(creditAmount);
            }
        } else {
            this.debitAmountSum = Utils.DOUBLE_EPSILON;
            for (int i2 = 0; i2 < this.debitList.getChildCount(); i2++) {
                String debitAmount = ((EditText) this.debitList.getChildAt(i2).findViewById(R.id.cattotalAMout)).getText().toString();
                if (debitAmount.equals("")) {
                    debitAmount = "0";
                }
                this.debitAmountSum += Double.parseDouble(debitAmount);
            }
        }
        TextView textView = this.totalAmountTv;
        if (this.creditAmountSum == this.debitAmountSum) {
            str = this.debitAmountSum + "";
        } else {
            str = "Unbalanced";
        }
        textView.setText(str);
        return true;
    }

    private void showErrorTv(String message) {
        this.errorTv.setText(message);
        this.errorTv.setVisibility(View.VISIBLE);
        this.errorTv.requestFocus();
    }

    private void hideErrorTv() {
        this.errorTv.setVisibility(View.GONE);
    }

    private int getTransactionHeadIdForCategory(String text) {
        for (AddWithdrawalCategorySpinnerItem localList : this.localCategoriesList) {
            if (localList.getTitle().equalsIgnoreCase(text)) {
                return localList.getId();
            }
        }
        return 4261;
    }

    private void submitTransaction(AddJournalTransactionRequest addTransactionRequest) {
        RestClient.getInstance(this).addJournalTransaction(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),addTransactionRequest).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Journal Transaction...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"accounting");
                    b.putString(Constant.ACTION,"add_journal_transaction");
                    SplashScreenActivity.sendEvent("accounting_add_journal_transaction",b);
                    UiUtil.showToast(AddJournalTransactionActivity.this, "Added");
                    AddJournalTransactionActivity.this.finish();
                    return;
                }
                UiUtil.showToast(AddJournalTransactionActivity.this, "Error while adding");
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void onCategoryClick(AddWithdrawalCategorySpinnerItem item, TextView textView, EditText editText) {
        this.localCategoriesList.add(item);
        textView.setText(item.getTitle());
        textView.setTextColor(getResources().getColor(R.color.blackTextColor));
        editText.setEnabled(true);
        editText.setAlpha(1.0f);
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
    }
}
