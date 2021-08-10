package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class CreatingProductsAndServices extends AppCompatActivity implements DeleteTaxClick {
    public static String PRODUCT_DETAIL = "productDetail";
    EditText etDesc;
    EditText etName;
    EditText etPrice;
    Product getProductFromIntent;
    /* access modifiers changed from: private */
    public List<IncomeAccount> incomeAccountList = new ArrayList();
    private PowerSpinnerView incomeAccountSpinner;
    Boolean isEdit = false;
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

    public static Intent buildIntent(Context context, Product product) {
        Intent intent = new Intent(context, CreatingProductsAndServices.class);
        intent.putExtra(PRODUCT_DETAIL, product);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_products_and_services);
        ((TextView) findViewById(R.id.pageTitle)).setText("Creating Products & Services");
        this.selectExpenbseError = findViewById(R.id.selectExpenbseError);
        findViewById(R.id.saveProductButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreatingProductsAndServices.this.lambda$onCreate$0$CreatingProductsAndServices(view);
            }
        });
        findViewById(R.id.addSalesTax).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreatingProductsAndServices.this.lambda$onCreate$1$CreatingProductsAndServices(view);
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initUI();
        getIncomeAccounts();
        if (getIntent().getSerializableExtra(PRODUCT_DETAIL) != null) {
            this.isEdit = true;
            Product product = (Product) getIntent().getSerializableExtra(PRODUCT_DETAIL);
            this.getProductFromIntent = product;
            initUiWithProduct(product);
        }
    }

    public void lambda$onCreate$0$CreatingProductsAndServices(View v) {
        addProduct();
    }

    public void lambda$onCreate$1$CreatingProductsAndServices(View click) {
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
        this.salesTaxSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            public void onItemSelected(int oldIndex, String oldItem, int newIndex, String newItem) {
                try {
                    Log.d("newItemnewItemnewIte", newItem + "--" + oldItem);
                    CreatingProductsAndServices.this.salesTaxSpinner.setText("");
                    CreatingProductsAndServices.this.taxList.add(newItem);
                    CreatingProductsAndServices.this.purchasetaxAddAdapter.notify(CreatingProductsAndServices.this.taxList);
                    CreatingProductsAndServices.this.saleTaxes.remove(newIndex);
                    if (CreatingProductsAndServices.this.saleTaxes.size() == 1) {
                        UiUtil.showProgressDialogue(CreatingProductsAndServices.this, "", "Loading");
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                UiUtil.cancelProgressDialogue();
                            }
                        }, 2000);
                    }
                    CreatingProductsAndServices.this.salesTaxSpinner.setItems(CreatingProductsAndServices.this.saleTaxes);
                } catch (Exception e) {
                }
            }
        });
        try {
            this.salesTaxSpinner.clearSelectedItem();
            RecyclerView recyclerView = findViewById(R.id.taxRecycler);
            this.taxRecycler = recyclerView;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            PurchasetaxAddAdapter purchasetaxAddAdapter2 = new PurchasetaxAddAdapter(getApplicationContext(), this.taxList, this);
            this.purchasetaxAddAdapter = purchasetaxAddAdapter2;
            this.taxRecycler.setAdapter(purchasetaxAddAdapter2);
        } catch (Exception e) {
        }
    }

    private void initUiWithProduct(Product getProductFromIntent2) {
        this.etName.setText(getProductFromIntent2.getName());
        this.etDesc.setText(getProductFromIntent2.getDescription());
        this.etPrice.setText(String.valueOf(getProductFromIntent2.getPrice()));
        this.incomeAccountSpinner.setText(getProductFromIntent2.getIncomeAccountName());
        if (!getProductFromIntent2.getProductServiceTaxes().isEmpty()) {
            for (ProductServiceTaxesItem taxItem : getProductFromIntent2.getProductServiceTaxes()) {
                this.taxList.add(taxItem.getTaxName());
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
            Product product = new Product(true, mDesc, productServiceTaxesItems, Double.parseDouble(mPrice), false, incomeAccountId, 0, mName);
            if (this.isEdit.booleanValue()) {
                product.setId(this.getProductFromIntent.getId());
                Log.d("productproduct", new Gson().toJson(product));
                RestClient.getInstance(this).editProduct(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), product).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(CreatingProductsAndServices.this, "Product Edited");
                            CreatingProductsAndServices.this.finish();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                        UiUtil.showToast(CreatingProductsAndServices.this, "Failed to add product");
                    }
                });
                return;
            }
            Log.d("productproduct", new Gson().toJson(product));
            RestClient.getInstance(this).addProduct(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), product).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"invoicing");
                        b.putString(Constant.ACTION,"add_item");
                        SplashScreenActivity.sendEvent("invoice_add_item",b);
                        UiUtil.showToast(CreatingProductsAndServices.this, "Product Added");
                        CreatingProductsAndServices.this.finish();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                    UiUtil.showToast(CreatingProductsAndServices.this, "Failed to add product");
                }
            });
        }
    }

    public boolean isValid() {
        Boolean isValid = true;
        try {
            String selectedIncomeAccount = (String) this.incomeAccountSpinner.getText();
            Log.d("selectedIncomeAccount", selectedIncomeAccount);
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
        } catch (Exception e) {
        }
        return isValid.booleanValue();
    }

    /* access modifiers changed from: private */
    public void getSalesTaxList() {
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), "").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CreatingProductsAndServices.this.salesTaxResponse = response.body();
                    CreatingProductsAndServices creatingProductsAndServices = CreatingProductsAndServices.this;
                    List unused = creatingProductsAndServices.saleTaxList = creatingProductsAndServices.salesTaxResponse.getData();
                    CreatingProductsAndServices creatingProductsAndServices2 = CreatingProductsAndServices.this;
                    creatingProductsAndServices2.setUpSaleTaxSpinner(creatingProductsAndServices2.saleTaxList);
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
        Product product = this.getProductFromIntent;
        if (product == null || product.getProductServiceTaxes().isEmpty()) {
            return true;
        }
        for (ProductServiceTaxesItem taxItem : this.getProductFromIntent.getProductServiceTaxes()) {
            if (taxItem.getTaxId() == name) {
                return false;
            }
        }
        return true;
    }

    private void getIncomeAccounts() {
        RestClient.getInstance(this).getIncomeAccounts(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<IncomeAccountsResponse>(this, null) {
            public void onResponse(Call<IncomeAccountsResponse> call, Response<IncomeAccountsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List unused = CreatingProductsAndServices.this.incomeAccountList = response.body().getData();
                    CreatingProductsAndServices creatingProductsAndServices = CreatingProductsAndServices.this;
                    creatingProductsAndServices.setUpIncomeAccountSpinner(creatingProductsAndServices.incomeAccountList);
                }
            }

            public void onFailure(Call<IncomeAccountsResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpIncomeAccountSpinner(List<IncomeAccount> incomeAccountList2) {
        ArrayList<String> incomeAccounts = new ArrayList<>();
        for (IncomeAccount incomeAccount : incomeAccountList2) {
            incomeAccounts.add(incomeAccount.getName());
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
        try {
            List<SaleTax> list = this.saleTaxList;
            if (list == null) {
                return selectedSaleTax;
            }
            for (SaleTax SaleTax : list) {
                if (SaleTax.getName().equals(saleTaxName)) {
                    return SaleTax;
                }
            }
        } catch (Exception e) {
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
        mView.findViewById(R.id.cancel_button).setOnClickListener(view -> popUp.dismiss());
        mView.findViewById(R.id.saveButton).setOnClickListener(view -> CreatingProductsAndServices.this.lambda$showPopUpWindow$3$CreatingProductsAndServices(mView, popUp, view));
    }

    public void lambda$showPopUpWindow$3$CreatingProductsAndServices(View mView, PopupWindow popUp, View v) {
        boolean isRecoverableTax;
        boolean isCompoundTaxSelected;
        View view = mView;
        try {
            EditText etTaxName = view.findViewById(R.id.et_taxName);
            String taxName = etTaxName.getText().toString().trim();
            if (taxName.length() == 0) {
                UiUtil.showToast(this, "Please enter valid taxName");
                etTaxName.setError("Please enter valid name");
                return;
            }
            String taxDesc = ((EditText) view.findViewById(R.id.et_taxDesc)).getText().toString().trim();
            String trim = ((EditText) view.findViewById(R.id.et_taxNumber)).getText().toString().trim();
            EditText etTaxRate = view.findViewById(R.id.et_taxRate);
            String taxRate = etTaxRate.getText().toString().trim();
            if (taxRate.length() == 0) {
                UiUtil.showToast(this, "Please enter valid Tax rate");
                etTaxRate.setError("Please enter valid Tax rate");
                return;
            }
            RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            if (radioButton == null || !radioButton.getText().equals("Is Recoverable Tax")) {
                isCompoundTaxSelected = true;
                isRecoverableTax = false;
            } else {
                isCompoundTaxSelected = false;
                isRecoverableTax = true;
            }
            ArrayList<EffectiveTaxesItem> effectiveTaxesItems = new ArrayList<>();
            effectiveTaxesItems.add(new EffectiveTaxesItem(Double.parseDouble(taxRate)));
            ArrayList<EffectiveTaxesItem> effectiveTaxesItems2 = effectiveTaxesItems;
            int i = selectedId;
            RadioGroup radioGroup2 = radioGroup;
            final PopupWindow popupWindow = popUp;
            RestClient.getInstance(this).addSaleTax(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems2)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Sale tax...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(CreatingProductsAndServices.this, "Sale Tax added!");
                        popupWindow.dismiss();
                        CreatingProductsAndServices.this.getSalesTaxList();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getSalesTaxList();
    }

    public void deleteTax(int pos) {
        try {
            this.taxList.remove(pos);
            this.purchasetaxAddAdapter.notify(this.taxList);
            this.saleTaxes.add(this.taxList.get(pos));
            this.salesTaxSpinner.setItems(this.saleTaxes);
        } catch (Exception e) {
        }
    }
}
