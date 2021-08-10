package com.akounto.accountingsoftware.Activity.Invoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.model.Country;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddCustomerRequest;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerOnlyResponse;
import com.akounto.accountingsoftware.response.CustomerUpdateResponse;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.response.currency.Currency;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class AddCustomers extends AppCompatActivity {
    public static CustomerUpdateResponse result_add = null;
    int CompanyId;
    int HeadTransactionId;
    int cid;
    List<Country> countryList = new ArrayList();
    List<String> countryListForSpinner = new ArrayList();
    List<Currency> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    Customer customer;
    int editCont;
    EditText etAccountNumber;
    EditText etAddressLine1;
    EditText etAddressLine2;
    EditText etBillCity;
    EditText etBillPostal;
    EditText etCompanyName;
    EditText etEmail;
    EditText etFax;
    EditText etInternalNotes;
    EditText etMobile;
    EditText etPhone;
    EditText etPortal;
    EditText etShipAddressLine1;
    EditText etShipAddressLine2;
    EditText etShipCity;
    EditText etShipPostal;
    EditText etTollFree;
    boolean isShippingSame = true;
    boolean is_edit;
    TextView pageTitle;
    ImageView back;
    String selectedBillCountry;
    String selectedBillState;
    String selectedCurrencyId;
    String selectedShipCountry;
    String selectedShipState;
    CheckBox shipAddrCheckBox;
    List<String> states = new ArrayList();
    Api api;
    Context mContext;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result_add = null;
        setContentView(R.layout.activity_add_customers_new);
        this.editCont = 0; //getIntent().getIntExtra(Constant.IS_EDIT, 0);
        try {
            initUi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mContext = this;
        api = ApiUtils.getAPIService();
    }

    private void initUi() throws JSONException {
        this.shipAddrCheckBox = findViewById(R.id.shipToDiffCB);
        LinearLayout ll = findViewById(R.id.shipAddrLayout);
        this.shipAddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                AddCustomers.this.lambda$initUi$0$AddCustomersActivity(ll, compoundButton, z);
            }
        });
        fetchCountries();
        fetchCurrencies();
        this.etCompanyName = findViewById(R.id.et_name);
        this.etEmail = findViewById(R.id.et_email);
        this.etPhone = findViewById(R.id.et_phone);
        this.etAddressLine1 = findViewById(R.id.et_billAddr1);
        this.etAddressLine2 = findViewById(R.id.et_billAddr2);
        this.etBillCity = findViewById(R.id.et_billCity);
        this.etBillPostal = findViewById(R.id.et_billPostCode);
        this.etShipAddressLine1 = findViewById(R.id.et_shipAddr1);
        this.etShipAddressLine2 = findViewById(R.id.et_shipAddr2);
        this.etShipCity = findViewById(R.id.et_shipCity);
        this.etShipPostal = findViewById(R.id.et_shipPostCode);
        this.etAccountNumber = findViewById(R.id.et_accountNumber);
        this.etFax = findViewById(R.id.et_fax);
        this.etMobile = findViewById(R.id.et_mobile);
        this.etTollFree = findViewById(R.id.et_tollFree);
        this.etPortal = findViewById(R.id.et_portal);
        this.etInternalNotes = findViewById(R.id.et_notes);
        int i = this.editCont;
        if (i == 1 || i == 2) {
            Customer customer2 = new Gson().fromJson(getIntent().getStringExtra(Constant.CUSTOMER_DATE), Customer.class);
            this.customer = customer2;
            this.cid = customer2.getId();
            this.HeadTransactionId = this.customer.getHeadTransactionId();
            this.CompanyId = this.customer.getCompanyId();
            getCustomerOnly(this.customer.getHeadTransactionId());
        }
        setBillCountrySpinner();
        if (this.editCont == 1) {
            findViewById(R.id.saveButton).setVisibility(View.GONE);
        }
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddCustomers.this.lambda$initUi$1$AddCustomersActivity(view);
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCustomers.this.finish();
            }
        });
    }

    public void lambda$initUi$0$AddCustomersActivity(LinearLayout linearLayout, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            linearLayout.setVisibility(View.VISIBLE);
            setShipCountrySpinner();
            this.isShippingSame = false;
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.isShippingSame = true;
    }

    public void lambda$initUi$1$AddCustomersActivity(View v) {
        addCustomer();
    }

    private void addCustomer() {
        String mName = this.etCompanyName.getText().toString().trim();
        if (mName.length() < 2) {
            UiUtil.showToast(this, "Customer name should be at least 2 character long!");
            this.etCompanyName.setError("Customer name should be at least 2 character long!");
        } else if (this.etEmail.getText().toString().length() <= 0 || isValidEmailId(this.etEmail.getText().toString())) {
            String trim = this.etBillCity.getText().toString().trim();
            String trim2 = this.etBillPostal.getText().toString().trim();
            String trim3 = this.etShipAddressLine2.getText().toString().trim();
            String trim4 = this.etShipCity.getText().toString().trim();
            String trim5 = this.etShipPostal.getText().toString().trim();
            String trim6 = this.etAccountNumber.getText().toString().trim();
            String trim7 = this.etFax.getText().toString().trim();
            String trim8 = this.etMobile.getText().toString().trim();
            String trim9 = this.etTollFree.getText().toString().trim();
            String trim10 = this.etPortal.getText().toString().trim();
            String trim11 = this.etInternalNotes.getText().toString().trim();
            String str = mName;
            AddCustomerRequest addCustomer = new AddCustomerRequest(this.etEmail.getText().toString().trim(), str, trim2, this.etPhone.getText().toString().trim(), this.selectedCurrencyId, trim10, this.selectedBillState, trim11, trim4, this.selectedShipState, trim5, trim, this.selectedBillCountry, trim8, this.etAddressLine1.getText().toString().trim(), null, this.etAddressLine2.getText().toString().trim(), trim6, trim9, this.selectedShipCountry, this.isShippingSame, trim7, trim3, null, this.etShipAddressLine1.getText().toString().trim(), null);
            Log.d("addCustomeraddCustomer", new Gson().toJson(addCustomer));
            int i = this.editCont;
            if (i == 1 || i == 2) {
                addCustomer.setId(this.cid);
                addCustomer.setHeadTransactionId(this.HeadTransactionId);
                addCustomer.setCompanyId(this.CompanyId);
                UiUtil.showProgressDialogue(mContext, "", "Please wait..");
                RestClient.getInstance(this).updateCustomer(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), addCustomer).enqueue(new CustomCallBack<CustomerUpdateResponse>(this, "Adding Customer...") {
                    public void onResponse(Call<CustomerUpdateResponse> call, Response<CustomerUpdateResponse> response) {
                        super.onResponse(call, response);
                        try {
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                result_add = response.body();
                                UiUtil.showToast(AddCustomers.this, "Customer Added");
                                finish();
                            } else {
                                UiUtil.showToast(AddCustomers.this, response.body().getTransactionStatus().getError().getDescription());
                            }
                        } catch (Exception e) {
                            UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                        }
                    }

                    public void onFailure(Call<CustomerUpdateResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                    }
                });
                return;
            }

            RestClient.getInstance(this).addCustomer(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), addCustomer).enqueue(new CustomCallBack<CustomerUpdateResponse>(this, "Adding Customer...") {
                public void onResponse(Call<CustomerUpdateResponse> call, Response<CustomerUpdateResponse> response) {
                    super.onResponse(call, response);

                    if (response.code() == 200) {
                        try {
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                result_add = response.body();
                                UiUtil.showToast(AddCustomers.this, "Customer Added");
                                lunch();
                                Bundle b=new Bundle();
                                b.putString(Constant.CATEGORY,"invoicing");
                                b.putString(Constant.ACTION,"add_customer_success");
                                SplashScreenActivity.sendEvent("invoice_add_customer",b);
                            } else {
                                Bundle b=new Bundle();
                                b.putString(Constant.CATEGORY,"invoicing");
                                b.putString(Constant.ACTION,"add_customer_fail");
                                SplashScreenActivity.sendEvent("invoice_add_customer",b);

                                UiUtil.showToast(AddCustomers.this, response.body().getTransactionStatus().getError().getDescription());
                                UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                            }
                        } catch (Exception e) {
                            UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                        }
                    } else {
                        UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                    }
                }

                public void onFailure(Call<CustomerUpdateResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    UiUtil.showToast(AddCustomers.this, "Fail to add customer");
                }
            });
        } else {
            UiUtil.showToast(this, "Please enter valid Email");
            this.etEmail.setError("Please enter valid Email");
        }
    }

    private void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", result_add.getData().getName());
        setResult(2, intent);
        finish();//finishing activity
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryList.add(new Country(((Integer) jsonObject.get("Id")).intValue(), jsonObject.getString("Name")));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
    }

    private void setBillCountrySpinner() {
        Spinner billCountrySpinner = findViewById(R.id.billCountrySpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, countryListForSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billCountrySpinner.setAdapter(dataAdapter);
        billCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddCustomers.this.lambda$setBillCountrySpinner$2$AddCustomersActivity(countryListForSpinner.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ;
    }

    public void lambda$setBillCountrySpinner$2$AddCustomersActivity(String selectedItem) {
        this.selectedBillCountry = String.valueOf(getCountryId(selectedItem));
        getStatesByCountry(getCountryId(selectedItem), "bill");
    }

    private void setShipCountrySpinner() {
        Spinner shipCountrySpinner = findViewById(R.id.shipCountrySpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, countryListForSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipCountrySpinner.setAdapter(dataAdapter);
        shipCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddCustomers.this.lambda$setShipCountrySpinner$3$AddCustomersActivity(countryListForSpinner.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void lambda$setShipCountrySpinner$3$AddCustomersActivity(String selectedItem) {
        this.selectedShipCountry = String.valueOf(getCountryId(selectedItem));
        getStatesByCountry(getCountryId(selectedItem), "ship");
    }

    private int getCountryId(String selectedCountry) {
        for (Country country : this.countryList) {
            if (country.getName().equals(selectedCountry)) {
                return country.getId();
            }
        }
        return 0;
    }

    private void getStatesByCountry(int countryId, final String billOrShip) {
        RestClient.getInstance(this).getStatesByCountryId(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), countryId).enqueue(new CustomCallBack<GetStatesResponse>(this, "Fetching States...") {
            public void onResponse(Call<GetStatesResponse> call, Response<GetStatesResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    AddCustomers.this.states = response.body().getStates();
                    if (billOrShip.equals("bill")) {
                        AddCustomers addCustomersActivity = AddCustomers.this;
                        addCustomersActivity.setBillStateSpinner(addCustomersActivity.states);
                        return;
                    }
                    AddCustomers addCustomersActivity2 = AddCustomers.this;
                    addCustomersActivity2.setShipStateSpinner(addCustomersActivity2.states);
                }
            }

            public void onFailure(Call<GetStatesResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(AddCustomers.this, "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void setBillStateSpinner(List<String> states2) {
        Spinner billStateSpinner = findViewById(R.id.billStateSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billStateSpinner.setAdapter(dataAdapter);
        billStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddCustomers.this.lambda$setBillStateSpinner$4$AddCustomersActivity(states2.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void lambda$setBillStateSpinner$4$AddCustomersActivity(String selectedItem) {
        this.selectedBillState = selectedItem;
    }

    /* access modifiers changed from: private */
    public void setShipStateSpinner(List<String> states2) {
        Spinner shipStateSpinner = findViewById(R.id.shipStateSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, states2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipStateSpinner.setAdapter(dataAdapter);
        shipStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddCustomers.this.lambda$setShipStateSpinner$5$AddCustomersActivity(states2.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void lambda$setShipStateSpinner$5$AddCustomersActivity(String selectedItem) {
        this.selectedShipState = selectedItem;
    }

    private void fetchCurrencies() throws JSONException {
      /*  String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", this);
//        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new Currency(jsonObject.getString("Symbol"), 0.0, jsonObject.getString("Id"), jsonObject.getString("Name")));
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(this.currencyListForSpinner);*/

        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", this);
        //Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String currency = jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name");
            this.currencyList.add(new Currency(jsonObject.getString("Symbol"), 0.0, jsonObject.getString("Id"), jsonObject.getString("Name")));
            this.currencyListForSpinner.add(currency);
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencies) {
        Spinner currencySpinner = findViewById(R.id.currencySpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.sppiner_text, currencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(dataAdapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddCustomers.this.lambda$setCurrencySpinner$6$AddCustomersActivity(currencies.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void lambda$setCurrencySpinner$6$AddCustomersActivity(String selectedItem) {
        this.selectedCurrencyId = getCurrencyId(selectedItem);
    }

    private String getCurrencyId(String selectedItem) {
        for (Currency currency : this.currencyList) {
            if (currency.getName().equals(selectedItem)) {
                return currency.getId();
            }
        }
        return "ALL";
    }

    private void getCustomerOnly(int id) {
        RestClient.getInstance(this).getCustomerListById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<CustomerOnlyResponse>(this, null) {
            public void onResponse(Call<CustomerOnlyResponse> call, Response<CustomerOnlyResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    AddCustomers.this.updateCustomerData(response.body().getData());
                }
            }

            public void onFailure(Call<CustomerOnlyResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateCustomerData(Customer customer2) {
        if (customer2.getName() != null) {
            this.etCompanyName.setText(customer2.getName());
        }
        if (customer2.getEmail() != null) {
            this.etEmail.setText(customer2.getEmail());
        }
        if (customer2.getPhone() != null) {
            this.etPhone.setText(customer2.getPhone());
        }
        if (customer2.getBillAddress1() != null) {
            this.etAddressLine1.setText(customer2.getBillAddress1());
        }
        if (customer2.getBillAddress2() != null) {
            this.etAddressLine2.setText(customer2.getBillAddress2());
        }
        if (customer2.getBillCity() != null) {
            this.etBillCity.setText(customer2.getBillCity());
        }
        if (customer2.getBillPostCode() != null) {
            this.etBillPostal.setText(customer2.getBillPostCode());
        }
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
