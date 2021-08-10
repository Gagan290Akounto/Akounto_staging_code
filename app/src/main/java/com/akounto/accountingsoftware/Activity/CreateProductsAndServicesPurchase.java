package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.adapter.DeleteTaxClick;
import com.akounto.accountingsoftware.adapter.PurchasetaxAddAdapter;
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
import com.akounto.accountingsoftware.response.CustomeResponse;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreateProductsAndServicesPurchase extends AppCompatActivity implements DeleteTaxClick {

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
    PurchasetaxAddAdapter purchasetaxAddAdapter;
    /* access modifiers changed from: private */
    public List<SaleTax> saleTaxList = new ArrayList();
    ArrayList<String> saleTaxes = new ArrayList<>();
    SalesProductResponse salesProductResponse;
    SalesTaxResponse salesTaxResponse;
    /* access modifiers changed from: private */
    public PowerSpinnerView salesTaxSpinner;
    TextView selectExpenbseError;
    List<String> taxList = new ArrayList();
    RecyclerView taxRecycler;
    TextView textViewIncomeAccount;

    public static Intent buildIntent(Context context, Product product2) {
        Intent intent = new Intent(context, CreateProductsAndServicesPurchase.class);
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
        TextView textView = findViewById(R.id.textViewIncomeAccount);
        this.textViewIncomeAccount = textView;
        textView.setText("Expense Account*");
        findViewById(R.id.saveProductButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateProductsAndServicesPurchase.this.lambda$onCreate$0$CreateProductsAndServicesPurchase(view);
            }
        });
        findViewById(R.id.addSalesTax).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreateProductsAndServicesPurchase.this.lambda$onCreate$1$CreateProductsAndServicesPurchase(view);
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

    public void lambda$onCreate$0$CreateProductsAndServicesPurchase(View v) {
        addProduct();
    }

    public void lambda$onCreate$1$CreateProductsAndServicesPurchase(View click) {
        showPopUpWindow();
    }

    private void initUI() {
        this.etName = findViewById(R.id.et_name);
        this.etDesc = findViewById(R.id.et_desc);
        this.etPrice = findViewById(R.id.et_price);
        PowerSpinnerView powerSpinnerView = findViewById(R.id.incomeAccountSpinner);
        this.incomeAccountSpinner = powerSpinnerView;
        powerSpinnerView.setHint("Please select expense account");
        PowerSpinnerView powerSpinnerView2 = findViewById(R.id.salesTaxSpinner);
        this.salesTaxSpinner = powerSpinnerView2;
        powerSpinnerView2.setItems(new ArrayList());
        this.salesTaxSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            public void onItemSelected(int oldIndex, String oldItem, int newIndex, String newItem) {
                Log.d("newItemnewItemnewIte", newItem + "--" + oldItem);
                CreateProductsAndServicesPurchase.this.salesTaxSpinner.setText("");
                CreateProductsAndServicesPurchase.this.taxList.add(newItem);
                CreateProductsAndServicesPurchase.this.purchasetaxAddAdapter.notify(CreateProductsAndServicesPurchase.this.taxList);
                CreateProductsAndServicesPurchase.this.saleTaxes.remove(newIndex);
                CreateProductsAndServicesPurchase.this.salesTaxSpinner.setItems(CreateProductsAndServicesPurchase.this.saleTaxes);
            }
        });
        this.salesTaxSpinner.clearSelectedItem();
        RecyclerView recyclerView = findViewById(R.id.taxRecycler);
        this.taxRecycler = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        PurchasetaxAddAdapter purchasetaxAddAdapter2 = new PurchasetaxAddAdapter(getApplicationContext(), this.taxList, this);
        this.purchasetaxAddAdapter = purchasetaxAddAdapter2;
        this.taxRecycler.setAdapter(purchasetaxAddAdapter2);
        PurchaseItem purchaseItem2 = this.purchaseItem;
        if (purchaseItem2 != null) {
            isEdit = true;
            this.etName.setText(purchaseItem2.getName());
            this.etDesc.setText(this.purchaseItem.getDescription());
            EditText editText = this.etPrice;
            editText.setText(this.purchaseItem.getPrice() + "");
            this.incomeAccountSpinner.setText(this.purchaseItem.getIncomeAccountName());
            if (!this.purchaseItem.getProductServiceTaxes().isEmpty()) {
                for (ProductServiceTaxesItem taxItem : this.purchaseItem.getProductServiceTaxes()) {
                    this.taxList.add(taxItem.getTaxName());
                    this.purchasetaxAddAdapter.notify(this.taxList);
                }
            }
        }
    }

    private void initUiWithProduct(Product getProductFromIntent2) {
        this.etName.setText(getProductFromIntent2.getName());
        this.etDesc.setText(getProductFromIntent2.getDescription());
        this.etPrice.setText(String.valueOf(getProductFromIntent2.getPrice()));
        this.incomeAccountSpinner.setText(getProductFromIntent2.getIncomeAccountName());
        if (!getProductFromIntent2.getProductServiceTaxes().isEmpty()) {
            for (ProductServiceTaxesItem taxItem : getProductFromIntent2.getProductServiceTaxes()) {
                this.taxList.add(taxItem.getName());
                this.purchasetaxAddAdapter.notify(this.taxList);
            }
        }
    }

    private void addProduct() {
        if (isValid()) {
            int incomeAccountId = getIncomeAccountByName((String) this.incomeAccountSpinner.getText()).getId();
            String str = (String) this.salesTaxSpinner.getText();
            String mName = ((EditText) findViewById(R.id.et_name)).getText().toString().trim();
            String mDesc = ((EditText) findViewById(R.id.et_desc)).getText().toString().trim();
            String mPrice = ((EditText) findViewById(R.id.et_price)).getText().toString().trim();
            ArrayList<ProductServiceTaxesItem> productServiceTaxesItems = new ArrayList<>();
            List<SaleTax> list = this.saleTaxList;
            if (!(list == null || list.size() == 0)) {
                for (String taxsItem : this.taxList) {
                    productServiceTaxesItems.add(new ProductServiceTaxesItem(getSaleTaxByName(taxsItem).getName(), getSaleTaxByName(taxsItem).getId()));
                }
            }
            ArrayList<ProductServiceTaxesItem> arrayList = productServiceTaxesItems;
            Product product2 = new Product(false, mDesc, productServiceTaxesItems, Double.parseDouble(mPrice), true, 0, incomeAccountId, mName);
            Log.d("isEditisEdit", isEdit + "");
            if (isEdit) {
                product2.setId(this.purchaseItem.getId());
                RestClient.getInstance(this).editProduct(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), product2).enqueue(new CustomCallBack<ResponseBody>(this, "Editing Product...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(CreateProductsAndServicesPurchase.this, "Product Added");
                            CreateProductsAndServicesPurchase.this.finish();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
                return;
            }
            RestClient.getInstance(this).addProduct(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), product2).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"billing");
                        b.putString(Constant.ACTION,"add_item");
                        SplashScreenActivity.sendEvent("bill_add_item",b);
                        UiUtil.showToast(CreateProductsAndServicesPurchase.this, "Product Added");
                        CreateProductsAndServicesPurchase.this.finish();
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
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                try {
                    if (response.body().getTransactionStatus().isIsSuccess()) {
                        CreateProductsAndServicesPurchase.this.salesTaxResponse = response.body();
                        CreateProductsAndServicesPurchase createProductsAndServicesPurchase = CreateProductsAndServicesPurchase.this;
                        List unused = createProductsAndServicesPurchase.saleTaxList = createProductsAndServicesPurchase.salesTaxResponse.getData();
                        CreateProductsAndServicesPurchase createProductsAndServicesPurchase2 = CreateProductsAndServicesPurchase.this;
                        createProductsAndServicesPurchase2.setUpSaleTaxSpinner(createProductsAndServicesPurchase2.saleTaxList);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<SalesTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpSaleTaxSpinner(List<SaleTax> saleTaxList2) {
        this.saleTaxes.clear();
        if (saleTaxList2 != null) {
            for (SaleTax saleTax : saleTaxList2) {
                if (isNotContainsInExisting(Integer.valueOf(saleTax.getId()).intValue())) {
                    this.saleTaxes.add(saleTax.getName());
                }
            }
            this.salesTaxSpinner.setItems(this.saleTaxes);
        }
    }

    private boolean isNotContainsInExisting(int name) {
        PurchaseItem purchaseItem2 = this.purchaseItem;
        if (purchaseItem2 == null || purchaseItem2.getProductServiceTaxes().isEmpty()) {
            return true;
        }
        for (ProductServiceTaxesItem taxItem : this.purchaseItem.getProductServiceTaxes()) {
            if (taxItem.getTaxId() == name) {
                return false;
            }
        }
        return true;
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
        PopupWindow popUp = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAtLocation(getWindow().getDecorView(), 17, 50, 50);
        View container = (View) popUp.getContentView().getParent();
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= 2;
        p.dimAmount = 0.3f;
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(container, p);
        mView.findViewById(R.id.cancel_button).setOnClickListener(view -> popUp.dismiss());
        mView.findViewById(R.id.saveButton).setOnClickListener(view -> CreateProductsAndServicesPurchase.this.lambda$showPopUpWindow$3$CreateProductsAndServicesPurchase(mView, popUp, view));
    }

    public void lambda$showPopUpWindow$3$CreateProductsAndServicesPurchase(View mView, PopupWindow popUp, View v) {
        boolean isRecoverableTax;
        boolean isCompoundTaxSelected;
        View view = mView;
        try {
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
            RestClient.getInstance(this).addSaleTaxB(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems2)).enqueue(new CustomCallBack<CustomeResponse>(this, "Adding Sale tax...") {
                public void onResponse(Call<CustomeResponse> call, Response<CustomeResponse> response) {
                    super.onResponse(call, response);
                    try {
                        if (response.code() == 200) {
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                UiUtil.showToast(CreateProductsAndServicesPurchase.this, "Sale Tax added!");
                                popupWindow.dismiss();
                                CreateProductsAndServicesPurchase.this.getSalesTaxList();
                            } else {
                                Toast.makeText(CreateProductsAndServicesPurchase.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CreateProductsAndServicesPurchase.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                public void onFailure(Call<CustomeResponse> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
        } catch (Exception e) {
            Log.e("Error :: ", e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getSalesTaxList();
    }

    private void getIncomeAccounts() {
        RestClient.getInstance(this).getIncomeAccountsPurchase(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<IncomeAccountsResponse>(this, null) {
            public void onResponse(Call<IncomeAccountsResponse> call, Response<IncomeAccountsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List unused = CreateProductsAndServicesPurchase.this.incomeAccountList = response.body().getData();
                    CreateProductsAndServicesPurchase createProductsAndServicesPurchase = CreateProductsAndServicesPurchase.this;
                    createProductsAndServicesPurchase.setUpIncomeAccountSpinner(createProductsAndServicesPurchase.incomeAccountList);
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

    public void deleteTax(int pos) {
        this.taxList.remove(pos);
        this.purchasetaxAddAdapter.notify(this.taxList);
        this.saleTaxes.add(this.taxList.get(pos));
        this.salesTaxSpinner.setItems(this.saleTaxes);
    }
}
