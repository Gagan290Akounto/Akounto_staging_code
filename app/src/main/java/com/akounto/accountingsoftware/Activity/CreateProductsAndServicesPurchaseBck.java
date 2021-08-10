package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddSaleTaxRequest;
import com.akounto.accountingsoftware.response.EffectiveTaxesItem;
import com.akounto.accountingsoftware.response.IncomeAccount;
import com.akounto.accountingsoftware.response.IncomeAccountsResponse;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreateProductsAndServicesPurchaseBck extends AppCompatActivity {
    public static String PRODUCT_DETAIL = "productDetail";
    private static boolean isEdit;
    String data;
    EditText etDesc;
    EditText etName;
    EditText etPrice;
    Product getProductFromIntent;
    /* access modifiers changed from: private */
    public List<IncomeAccount> incomeAccountList = new ArrayList();
    private PowerSpinnerView incomeAccountSpinner;
    Product product;
    PurchaseItem purchaseItem;
    /* access modifiers changed from: private */
    public List<SaleTax> saleTaxList = new ArrayList();
    SalesProductResponse salesProductResponse;
    SalesTaxResponse salesTaxResponse;
    /* access modifiers changed from: private */
    public PowerSpinnerView salesTaxSpinner;
    TextView selectExpenbseError;

    public static Intent buildIntent(Context context, Product product2) {
        Intent intent = new Intent(context, CreateProductsAndServicesPurchaseBck.class);
        intent.putExtra(PRODUCT_DETAIL, product2);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_products_and_services);
        String stringExtra = getIntent().getStringExtra("DATA");
        this.data = stringExtra;
        if (stringExtra != null) {
            this.purchaseItem = new Gson().fromJson(this.data, PurchaseItem.class);
            Log.d("purchase--111", new Gson().toJson(this.purchaseItem));
        }
        ((TextView) findViewById(R.id.pageTitle)).setText("Creating Products & Services");
        this.selectExpenbseError = findViewById(R.id.selectExpenbseError);
        findViewById(R.id.saveProductButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateProductsAndServicesPurchaseBck.this.lambda$onCreate$0$CreateProductsAndServicesPurchaseBck(view);
            }
        });
        findViewById(R.id.addSalesTax).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateProductsAndServicesPurchaseBck.this.lambda$onCreate$1$CreateProductsAndServicesPurchaseBck(view);
            }
        });
        initUI();
        getIncomeAccounts();
        if (getIntent().getSerializableExtra(PRODUCT_DETAIL) != null) {
            isEdit = true;
            Product product2 = (Product) getIntent().getSerializableExtra(PRODUCT_DETAIL);
            this.getProductFromIntent = product2;
            initUiWithProduct(product2);
        }
    }

    public /* synthetic */ void lambda$onCreate$0$CreateProductsAndServicesPurchaseBck(View v) {
        addProduct();
    }

    public /* synthetic */ void lambda$onCreate$1$CreateProductsAndServicesPurchaseBck(View click) {
        showPopUpWindow();
    }

    private void initUI() {
        this.etName = findViewById(R.id.et_name);
        this.etDesc = findViewById(R.id.et_desc);
        this.etPrice = findViewById(R.id.et_price);
        this.incomeAccountSpinner = findViewById(R.id.incomeAccountSpinner);
        PowerSpinnerView powerSpinnerView = findViewById(R.id.salesTaxSpinner);
        this.salesTaxSpinner = powerSpinnerView;
        powerSpinnerView.setItems(new ArrayList());
        PurchaseItem purchaseItem2 = this.purchaseItem;
        if (purchaseItem2 != null) {
            isEdit = true;
            this.etName.setText(purchaseItem2.getName());
            this.etDesc.setText(this.purchaseItem.getDescription());
            EditText editText = this.etPrice;
            editText.setText(this.purchaseItem.getPrice() + "");
            this.incomeAccountSpinner.setText(this.purchaseItem.getIncomeAccountName());
            if (!this.purchaseItem.getProductServiceTaxes().isEmpty()) {
                this.salesTaxSpinner.setText(this.purchaseItem.getProductServiceTaxes().get(0).getTaxName());
            }
        }
    }

    private void initUiWithProduct(Product getProductFromIntent2) {
        this.etName.setText(getProductFromIntent2.getName());
        this.etDesc.setText(getProductFromIntent2.getDescription());
        this.etPrice.setText(String.valueOf(getProductFromIntent2.getPrice()));
        this.incomeAccountSpinner.setText(getProductFromIntent2.getIncomeAccountName());
        if (!getProductFromIntent2.getProductServiceTaxes().isEmpty()) {
            this.salesTaxSpinner.setText(getProductFromIntent2.getProductServiceTaxes().get(0).getTaxName());
        }
    }

    private void addProduct() {
        if (isValid()) {
            int incomeAccountId = getIncomeAccountByName((String) this.incomeAccountSpinner.getText()).getId();
            String selectedSaleTax = (String) this.salesTaxSpinner.getText();
            int saleHeadTransactionId = getSaleTaxByName(selectedSaleTax).getId();
            String saleTaxName = getSaleTaxByName(selectedSaleTax).getName();
            String mName = ((EditText) findViewById(R.id.et_name)).getText().toString().trim();
            String mDesc = ((EditText) findViewById(R.id.et_desc)).getText().toString().trim();
            String mPrice = ((EditText) findViewById(R.id.et_price)).getText().toString().trim();
            ArrayList<ProductServiceTaxesItem> productServiceTaxesItems = new ArrayList<>();
            if (!(saleTaxName == null || saleTaxName.length() == 0)) {
                productServiceTaxesItems.add(new ProductServiceTaxesItem(saleTaxName, saleHeadTransactionId));
            }
            ArrayList<ProductServiceTaxesItem> arrayList = productServiceTaxesItems;
            Product product2 = new Product(false, mDesc, productServiceTaxesItems, Double.parseDouble(mPrice), true, 0, incomeAccountId, mName);
            Log.d("isEditisEdit", isEdit + "");
            if (isEdit) {
                product2.setId(this.purchaseItem.getId());
                RestClient.getInstance(this).editProduct(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),product2).enqueue(new CustomCallBack<ResponseBody>(this, "Editing Product...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(CreateProductsAndServicesPurchaseBck.this, "Product Added");
                            CreateProductsAndServicesPurchaseBck.this.finish();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
                return;
            }
            RestClient.getInstance(this).addProduct(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),product2).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(CreateProductsAndServicesPurchaseBck.this, "Product Added");
                        CreateProductsAndServicesPurchaseBck.this.finish();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void getSalesTaxList() {
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CreateProductsAndServicesPurchaseBck.this.salesTaxResponse = response.body();
                    CreateProductsAndServicesPurchaseBck createProductsAndServicesPurchaseBck = CreateProductsAndServicesPurchaseBck.this;
                    List unused = createProductsAndServicesPurchaseBck.saleTaxList = createProductsAndServicesPurchaseBck.salesTaxResponse.getData();
                    CreateProductsAndServicesPurchaseBck createProductsAndServicesPurchaseBck2 = CreateProductsAndServicesPurchaseBck.this;
                    createProductsAndServicesPurchaseBck2.setUpSaleTaxSpinner(createProductsAndServicesPurchaseBck2.saleTaxList);
                }
            }

            public void onFailure(Call<SalesTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpSaleTaxSpinner(List<SaleTax> saleTaxList2) {
        ArrayList<String> saleTaxes = new ArrayList<>();
        if (saleTaxList2 != null) {
            for (SaleTax saleTax : saleTaxList2) {
                saleTaxes.add(saleTax.getName());
            }
            this.salesTaxSpinner.setItems(saleTaxes);
            this.salesTaxSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (oldIndex, oldItem, newIndex, newItem) -> {
                Log.d("newItemnewItem", newItem);
                CreateProductsAndServicesPurchaseBck.this.salesTaxSpinner.setText("");
            });
        }
    }

    /* access modifiers changed from: private */
    public void setUpIncomeAccountSpinner(List<IncomeAccount> incomeAccountList2) {
        ArrayList<String> incomeAccounts = new ArrayList<>();
        for (IncomeAccount incomeAccount : incomeAccountList2) {
            incomeAccounts.add(incomeAccount.getName());
            PurchaseItem purchaseItem2 = this.purchaseItem;
            if (purchaseItem2 != null && purchaseItem2.getExpenseAccountId() == incomeAccount.getId()) {
                this.incomeAccountSpinner.setText(incomeAccount.getName());
            }
        }
        this.incomeAccountSpinner.setItems(incomeAccounts);
    }

    private IncomeAccount getIncomeAccountByName(String incomeAccountName) {
        IncomeAccount selectedIncomeAccount = new IncomeAccount();
        for (IncomeAccount incomeAccount : this.incomeAccountList) {
            if (incomeAccount.getName().equals(incomeAccountName)) {
                return incomeAccount;
            }
        }
        return selectedIncomeAccount;
    }

    private SaleTax getSaleTaxByName(String saleTaxName) {
        SaleTax selectedSaleTax = new SaleTax();
        for (SaleTax SaleTax : this.saleTaxList) {
            if (SaleTax.getName().equals(saleTaxName)) {
                return SaleTax;
            }
        }
        return selectedSaleTax;
    }

    public void showPopUpWindow() {
        View mView = LayoutInflater.from(this).inflate(R.layout.add_salestax_popup_layout, null, false);
        PopupWindow popUp = new PopupWindow(mView, -1, -2, false);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAtLocation(getWindow().getDecorView(), 17, 50, 50);
        View container = (View) popUp.getContentView().getParent();
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= 2;
        p.dimAmount = 0.3f;
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(container, p);
        mView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                popUp.dismiss();
            }
        });
        mView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                CreateProductsAndServicesPurchaseBck.this.lambda$showPopUpWindow$3$CreateProductsAndServicesPurchaseBck(mView, popUp, view);
            }
        });
    }

    public /* synthetic */ void lambda$showPopUpWindow$3$CreateProductsAndServicesPurchaseBck(View mView, PopupWindow popUp, View v) {
        boolean isRecoverableTax;
        boolean isCompoundTaxSelected;
        View view = mView;
        EditText etTaxName = view.findViewById(R.id.et_taxName);
        if (etTaxName.getText().toString().length() == 0) {
            UiUtil.showToast(this, "Please enter Tax Name");
            return;
        }
        String taxName = etTaxName.getText().toString().trim();
        String taxDesc = ((EditText) view.findViewById(R.id.et_taxDesc)).getText().toString().trim();
        String trim = ((EditText) view.findViewById(R.id.et_taxNumber)).getText().toString().trim();
        String taxRate = ((EditText) view.findViewById(R.id.et_taxRate)).getText().toString().trim();
        if (taxRate.length() == 0) {
            UiUtil.showToast(this, "Please enter Tax Rate");
            return;
        }
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (((RadioButton) radioGroup.findViewById(selectedId)).getText().equals("Is Recoverable Tax")) {
            isCompoundTaxSelected = false;
            isRecoverableTax = true;
        } else {
            isCompoundTaxSelected = true;
            isRecoverableTax = false;
        }
        ArrayList<EffectiveTaxesItem> effectiveTaxesItems = new ArrayList<>();
        if (!(taxName == null || taxName.length() == 0)) {
            effectiveTaxesItems.add(new EffectiveTaxesItem(Double.parseDouble(taxRate)));
        }
        ArrayList<EffectiveTaxesItem> effectiveTaxesItems2 = effectiveTaxesItems;
        int i = selectedId;
        RadioGroup radioGroup2 = radioGroup;
        final PopupWindow popupWindow = popUp;
        RestClient.getInstance(this).addSaleTax(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems2)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Sale tax...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(CreateProductsAndServicesPurchaseBck.this, "Sale Tax added!");
                    popupWindow.dismiss();
                    CreateProductsAndServicesPurchaseBck.this.getSalesTaxList();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getSalesTaxList();
    }

    private void getIncomeAccounts() {
        RestClient.getInstance(this).getIncomeAccountsPurchase(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<IncomeAccountsResponse>(this, null) {
            public void onResponse(Call<IncomeAccountsResponse> call, Response<IncomeAccountsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List unused = CreateProductsAndServicesPurchaseBck.this.incomeAccountList = response.body().getData();
                    CreateProductsAndServicesPurchaseBck createProductsAndServicesPurchaseBck = CreateProductsAndServicesPurchaseBck.this;
                    createProductsAndServicesPurchaseBck.setUpIncomeAccountSpinner(createProductsAndServicesPurchaseBck.incomeAccountList);
                }
            }

            public void onFailure(Call<IncomeAccountsResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public boolean isValid() {
        String selectedIncomeAccount = (String) this.incomeAccountSpinner.getText();
        Log.d("selectedIncomeAccount", selectedIncomeAccount);
        Boolean isValid = true;
        if (this.etName.getText().toString().length() == 0) {
            this.etName.setError("Please enter valid name");
            isValid = false;
        }
        if (selectedIncomeAccount.length() == 0) {
            this.selectExpenbseError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            this.selectExpenbseError.setVisibility(View.GONE);
        }
        if (this.etPrice.getText().toString().length() == 0) {
            this.etPrice.setError("Please enter valid format & Maximum of 2 decimal places");
            isValid = false;
        }
        return isValid.booleanValue();
    }
}
