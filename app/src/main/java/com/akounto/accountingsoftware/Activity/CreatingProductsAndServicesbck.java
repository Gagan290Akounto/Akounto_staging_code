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
import com.akounto.accountingsoftware.response.SaleTax;
import com.akounto.accountingsoftware.response.SalesProductResponse;
import com.akounto.accountingsoftware.response.SalesTaxResponse;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreatingProductsAndServicesbck extends AppCompatActivity {
    public static String PRODUCT_DETAIL = "productDetail";
    EditText etDesc;
    EditText etName;
    EditText etPrice;
    Product getProductFromIntent;
    /* access modifiers changed from: private */
    public List<IncomeAccount> incomeAccountList = new ArrayList();
    private PowerSpinnerView incomeAccountSpinner;
    Boolean isEdit = false;
    /* access modifiers changed from: private */
    public List<SaleTax> saleTaxList = new ArrayList();
    SalesProductResponse salesProductResponse;
    SalesTaxResponse salesTaxResponse;
    private PowerSpinnerView salesTaxSpinner;

    public static Intent buildIntent(Context context, Product product) {
        Intent intent = new Intent(context, CreatingProductsAndServicesbck.class);
        intent.putExtra(PRODUCT_DETAIL, product);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_products_and_services);
        ((TextView) findViewById(R.id.pageTitle)).setText("Creating Products & Services");
        findViewById(R.id.saveProductButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreatingProductsAndServicesbck.this.lambda$onCreate$0$CreatingProductsAndServicesbck(view);
            }
        });
        findViewById(R.id.addSalesTax).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CreatingProductsAndServicesbck.this.lambda$onCreate$1$CreatingProductsAndServicesbck(view);
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

    public void lambda$onCreate$0$CreatingProductsAndServicesbck(View v) {
        addProduct();
    }

    public void lambda$onCreate$1$CreatingProductsAndServicesbck(View click) {
        showPopUpWindow();
    }

    private void initUI() {
        this.etName = findViewById(R.id.et_name);
        this.etDesc = findViewById(R.id.et_desc);
        this.etPrice = findViewById(R.id.et_price);
        this.incomeAccountSpinner = findViewById(R.id.incomeAccountSpinner);
        this.salesTaxSpinner = findViewById(R.id.salesTaxSpinner);
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
        int incomeAccountId = getIncomeAccountByName((String) this.incomeAccountSpinner.getText()).getId();
        String selectedSaleTax = (String) this.salesTaxSpinner.getText();
        int saleHeadTransactionId = getSaleTaxByName(selectedSaleTax).getId();
        String saleTaxName = getSaleTaxByName(selectedSaleTax).getName();
        EditText etName2 = findViewById(R.id.et_name);
        String mName = etName2.getText().toString().trim();
        if (mName.length() == 0) {
            UiUtil.showToast(this, "Please enter valid name");
            etName2.setError("Please enter valid name");
            return;
        }
        EditText etDesc2 = findViewById(R.id.et_desc);
        String mDesc = etDesc2.getText().toString().trim();
        if (mDesc.length() == 0) {
            UiUtil.showToast(this, "Please enter valid description");
            etDesc2.setError("Please enter valid description");
            return;
        }
        EditText etPrice2 = findViewById(R.id.et_price);
        String mPrice = etPrice2.getText().toString().trim();
        if (mPrice.length() == 0) {
            UiUtil.showToast(this, "Please enter valid Price");
            etPrice2.setError("Please enter valid Price");
            return;
        }
        if (selectedSaleTax == null) {
            EditText editText = etDesc2;
            EditText editText2 = etName2;
        } else if (selectedSaleTax.length() == 0) {
            EditText editText3 = etPrice2;
            EditText editText4 = etDesc2;
            EditText editText5 = etName2;
        } else {
            ArrayList<ProductServiceTaxesItem> productServiceTaxesItems = new ArrayList<>();
            if (saleTaxName != null) {
                productServiceTaxesItems.add(new ProductServiceTaxesItem(saleTaxName, saleHeadTransactionId));
            }
            ArrayList<ProductServiceTaxesItem> arrayList = productServiceTaxesItems;
            EditText editText6 = etPrice2;
            EditText editText7 = etDesc2;
            EditText editText8 = etName2;
            Product product = new Product(true, mDesc, productServiceTaxesItems, Double.parseDouble(mPrice), false, incomeAccountId, 0, mName);
            if (this.isEdit.booleanValue()) {
                product.setId(this.getProductFromIntent.getId());
                Log.d("productproduct", new Gson().toJson(product));
                RestClient.getInstance(this).editProduct(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),product).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(CreatingProductsAndServicesbck.this, "Product Edited");
                            CreatingProductsAndServicesbck.this.finish();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                        UiUtil.showToast(CreatingProductsAndServicesbck.this, "Failed to add product");
                    }
                });
                return;
            }
            Log.d("productproduct", new Gson().toJson(product));
            RestClient.getInstance(this).addProduct(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),product).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Product...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(CreatingProductsAndServicesbck.this, "Product Added");
                        CreatingProductsAndServicesbck.this.finish();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                    UiUtil.showToast(CreatingProductsAndServicesbck.this, "Failed to add product");
                }
            });
            return;
        }
        UiUtil.showToast(this, "Please select income account");
    }

    /* access modifiers changed from: private */
    public void getSalesTaxList() {
        RestClient.getInstance(this).getSalesTaxList(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),"").enqueue(new CustomCallBack<SalesTaxResponse>(this, null) {
            public void onResponse(Call<SalesTaxResponse> call, Response<SalesTaxResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    CreatingProductsAndServicesbck.this.salesTaxResponse = response.body();
                    CreatingProductsAndServicesbck creatingProductsAndServicesbck = CreatingProductsAndServicesbck.this;
                    List unused = creatingProductsAndServicesbck.saleTaxList = creatingProductsAndServicesbck.salesTaxResponse.getData();
                    CreatingProductsAndServicesbck creatingProductsAndServicesbck2 = CreatingProductsAndServicesbck.this;
                    creatingProductsAndServicesbck2.setUpSaleTaxSpinner(creatingProductsAndServicesbck2.saleTaxList);
                }
            }

            public void onFailure(Call<SalesTaxResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUpSaleTaxSpinner(List<SaleTax> saleTaxList2) {
        Log.d("saleTaxList---", new Gson().toJson(saleTaxList2));
        ArrayList<String> saleTaxes = new ArrayList<>();
        if (saleTaxList2 == null) {
            saleTaxList2 = new ArrayList<>();
        }
        for (SaleTax saleTax : saleTaxList2) {
            saleTaxes.add(saleTax.getName());
        }
        this.salesTaxSpinner.setItems(saleTaxes);
    }

    private void getIncomeAccounts() {
        RestClient.getInstance(this).getIncomeAccounts(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext())).enqueue(new CustomCallBack<IncomeAccountsResponse>(this, null) {
            public void onResponse(Call<IncomeAccountsResponse> call, Response<IncomeAccountsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    List unused = CreatingProductsAndServicesbck.this.incomeAccountList = response.body().getData();
                    CreatingProductsAndServicesbck creatingProductsAndServicesbck = CreatingProductsAndServicesbck.this;
                    creatingProductsAndServicesbck.setUpIncomeAccountSpinner(creatingProductsAndServicesbck.incomeAccountList);
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
        List<SaleTax> list = this.saleTaxList;
        if (list == null) {
            return selectedSaleTax;
        }
        for (SaleTax SaleTax : list) {
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
        mView.findViewById(R.id.cancel_button).setOnClickListener(view -> popUp.dismiss());
        mView.findViewById(R.id.saveButton).setOnClickListener(view -> CreatingProductsAndServicesbck.this.lambda$showPopUpWindow$3$CreatingProductsAndServicesbck(mView, popUp, view));
    }

    public void lambda$showPopUpWindow$3$CreatingProductsAndServicesbck(View mView, PopupWindow popUp, View v) {
        boolean isRecoverableTax;
        boolean isCompoundTaxSelected;
        View view = mView;
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
        RestClient.getInstance(this).addSaleTax(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),new AddSaleTaxRequest(taxName, taxDesc, Boolean.valueOf(isRecoverableTax), Boolean.valueOf(isCompoundTaxSelected), effectiveTaxesItems2)).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Sale tax...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(CreatingProductsAndServicesbck.this, "Sale Tax added!");
                    popupWindow.dismiss();
                    CreatingProductsAndServicesbck.this.getSalesTaxList();
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
}
