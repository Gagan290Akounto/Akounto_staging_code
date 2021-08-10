package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.Activity.Invoice.AddCustomers;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.model.Country;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddVendorRequest;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.response.Vendor;
import com.akounto.accountingsoftware.response.VendorDetailsResponse;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class AddVendorActivity extends AppCompatActivity {
    EditText address1;
    EditText address2;
    PowerSpinnerView billCountrySpinner;
    PowerSpinnerView billStateSpinner;
    List<Country> countryList = new ArrayList();
    List<String> countryListForSpinner = new ArrayList();
    List<Currency> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    private List<String> currencyListForDisplay = new ArrayList();
    PowerSpinnerView currencySpinner;
    int editType = 1;
    EditText et_billCity;
    EditText et_billPostCode;
    EditText et_firstname;
    EditText et_lname;
    EditText et_vemail;
    EditText et_vname;
    boolean isShippingSame = true;
    TextView pageTitle;
    String selectedBillCountry;
    String selectedBillState;
    int selectedCountry;
    int selectedCurrency;
    String selectedCurrencyId;
    String selectedShipCountry;
    String selectedShipState;
    int selectedState;
    CheckBox shipAddrCheckBox;
    PowerSpinnerView shipCountrySpinner;
    PowerSpinnerView shipStateSpinner;
    List<String> states = new ArrayList();
    Vendor vendor;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);
        this.editType = getIntent().getIntExtra(UiConstants.IS_EDIT, 0);
        try {
            initUi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUi() throws JSONException {
        try {
            TextView textView = findViewById(R.id.pageTitle);
            this.pageTitle = textView;
            textView.setText("Add Vendor");
            if (this.editType == 2) {
                this.pageTitle.setText("Edit Vendor");
            }
            this.shipAddrCheckBox = findViewById(R.id.shipToDiffCB);
            this.et_vname = findViewById(R.id.et_vname);
            this.et_vemail = findViewById(R.id.et_vemail);
            this.et_firstname = findViewById(R.id.et_fname);
            this.et_lname = findViewById(R.id.et_lname);
            this.address1 = findViewById(R.id.et_billAddr1);
            this.address2 = findViewById(R.id.et_billAddr2);
            this.et_billCity = findViewById(R.id.et_billCity);
            this.et_billPostCode = findViewById(R.id.et_billPostCode);
            LinearLayout linearLayout = findViewById(R.id.shipAddrLayout);
            fetchCountries();
            fetchCurrencies();
            setBillCountrySpinner();
            if (this.editType == 2) {
                Vendor vendor2 = new Gson().fromJson(getIntent().getStringExtra(UiConstants.CUSTOMER_DATE), Vendor.class);
                this.vendor = vendor2;
                getVendorById(vendor2.getHeadTransactionId());
            }

            findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AddVendorActivity.this.lambda$initUi$0$AddVendorActivity(view);
                }
            });
            findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddVendorActivity.this.finish();
                }
            });
        } catch (Exception e) {
        }
    }

    public void lambda$initUi$0$AddVendorActivity(View v) {
        addCustomer();
    }

    private void getVendorById(int id) {
        RestClient.getInstance(this).getVendorDetails(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), id).enqueue(new CustomCallBack<VendorDetailsResponse>(this, "Fetching States...") {
            public void onResponse(Call<VendorDetailsResponse> call, Response<VendorDetailsResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    AddVendorActivity.this.updateVendor(response.body().getData());
                }
            }

            public void onFailure(Call<VendorDetailsResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(AddVendorActivity.this, "Failed to fetch states");
            }
        });
    }

    public void lunch() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        setResult(11, intent);
        finish();//finishing activity
    }

    /* access modifiers changed from: private */
    public void updateVendor(AddVendorRequest vendor2) {
        if (vendor2.getVendorName() != null) {
            this.et_vname.setText(vendor2.getVendorName());
        }
        if (vendor2.getEmail() != null) {
            this.et_vemail.setText(vendor2.getEmail());
        }
        if (vendor2.getFirstName() != null) {
            this.et_firstname.setText(vendor2.getFirstName());
        }
        if (vendor2.getLastName() != null) {
            this.et_lname.setText(vendor2.getLastName());
        }
        if (vendor2.getBillAddress1() != null) {
            this.address1.setText(vendor2.getBillAddress1());
        }
        if (vendor2.getBillAddress2() != null) {
            this.address2.setText(vendor2.getBillAddress2());
        }
        if (vendor2.getBillCity() != null) {
            this.et_billCity.setText(vendor2.getBillCity());
        }
        if (vendor2.getBillCity() != null) {
            this.et_billPostCode.setText(vendor2.getBillPostCode());
        }
        int pos = 0;
        for (Country country : this.countryList) {
            if (country.getId() == Integer.parseInt(vendor2.getBillCountry())) {
                this.billCountrySpinner.selectItemByIndex(pos);
            }
            pos++;
        }
        int currPos = 0;
        for (Currency currency : this.currencyList) {
            if (currency.getId().equals(vendor2.getCurrency())) {
                this.currencySpinner.selectItemByIndex(currPos);
            }
            currPos++;
        }
    }

    private void setBillCountrySpinner() {
        PowerSpinnerView powerSpinnerView = findViewById(R.id.billCountrySpinner);
        this.billCountrySpinner = powerSpinnerView;
        powerSpinnerView.setItems(this.countryListForSpinner);
        this.billCountrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddVendorActivity.this.lambda$setBillCountrySpinner$1$AddVendorActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setBillCountrySpinner$1$AddVendorActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedBillCountry = String.valueOf(getCountryId(selectedItem));
        getStatesByCountry(getCountryId(selectedItem), "bill");
    }

    private boolean isValidData() {
        if (this.et_vname.getText().toString().length() == 0) {
            this.et_vname.setError("Vendor name can't be blank!");
            return false;
        } else if (this.et_vemail.getText().toString().length() <= 0 || !UiUtil.isValidEmail(this.et_vemail.getText().toString())) {
            return true;
        } else {
            UiUtil.showToast(this, "Please enter valid Email");
            this.et_vemail.setError("Please enter valid Email");
            return false;
        }
    }

    private void addCustomer() {
        if (isValidData()) {
            AddVendorRequest addVendorRequest = new AddVendorRequest();
            addVendorRequest.setFirstName(this.et_firstname.getText().toString());
            addVendorRequest.setLastName(this.et_lname.getText().toString());
            addVendorRequest.setVendorName(this.et_vname.getText().toString());
            addVendorRequest.setEmail(this.et_vemail.getText().toString());
            addVendorRequest.setBillAddress1(this.address1.getText().toString());
            addVendorRequest.setBillAddress2(this.address2.getText().toString());
            addVendorRequest.setBillCity(this.et_billCity.getText().toString());
            addVendorRequest.setBillPostCode(this.et_billPostCode.getText().toString());
            addVendorRequest.setBillCountry(this.selectedBillCountry);
            addVendorRequest.setBillState(this.selectedBillState);
            addVendorRequest.setCurrency(this.selectedCurrencyId);
            if (this.editType == 2) {
                addVendorRequest.setId(this.vendor.getId());
                addVendorRequest.setHeadTransactionId(this.vendor.getHeadTransactionId());
                RestClient.getInstance(this).editVendor(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), addVendorRequest).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Vendor...") {
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            UiUtil.showToast(AddVendorActivity.this, "Vendor Added");
                            lunch();
                        }
                    }

                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
                return;
            }
            RestClient.getInstance(this).addVendor(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getApplicationContext()), UiUtil.getComp_Id(getApplicationContext()), addVendorRequest).enqueue(new CustomCallBack<ResponseBody>(this, "Adding Vendor...") {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        UiUtil.showToast(AddVendorActivity.this, "Vendor Added");
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"billing");
                        b.putString(Constant.ACTION,"add_vendor");
                        SplashScreenActivity.sendEvent("bill_add_vendor",b);
                        lunch();
                    }
                }

                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
        }
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", this);
        //Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryList.add(new Country(((Integer) jsonObject.get("Id")).intValue(), jsonObject.getString("Name")));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
    }

    private void setShipCountrySpinner() {
        PowerSpinnerView powerSpinnerView = findViewById(R.id.shipCountrySpinner);
        this.shipCountrySpinner = powerSpinnerView;
        powerSpinnerView.setItems(this.countryListForSpinner);
        this.shipCountrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddVendorActivity.this.lambda$setShipCountrySpinner$2$AddVendorActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setShipCountrySpinner$2$AddVendorActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedShipCountry = String.valueOf(getCountryId(selectedItem));
        getStatesByCountry(getCountryId(selectedItem), "ship");
    }

    private int getCountryId(String selectedCountry2) {
        for (Country country : this.countryList) {
            if (country.getName().equals(selectedCountry2)) {
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
                    AddVendorActivity.this.states = response.body().getStates();
                    if (billOrShip.equals("bill")) {
                        AddVendorActivity addVendorActivity = AddVendorActivity.this;
                        addVendorActivity.setBillStateSpinner(addVendorActivity.states);
                        return;
                    }
                    AddVendorActivity addVendorActivity2 = AddVendorActivity.this;
                    addVendorActivity2.setShipStateSpinner(addVendorActivity2.states);
                }
            }

            public void onFailure(Call<GetStatesResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(AddVendorActivity.this, "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void setBillStateSpinner(List<String> states2) {
        PowerSpinnerView powerSpinnerView = findViewById(R.id.billStateSpinner);
        this.billStateSpinner = powerSpinnerView;
        powerSpinnerView.setItems(states2);
        this.billStateSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddVendorActivity.this.lambda$setBillStateSpinner$3$AddVendorActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setBillStateSpinner$3$AddVendorActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedBillState = selectedItem;
    }

    /* access modifiers changed from: private */
    public void setShipStateSpinner(List<String> states2) {
        PowerSpinnerView powerSpinnerView = findViewById(R.id.shipStateSpinner);
        this.shipStateSpinner = powerSpinnerView;
        powerSpinnerView.setItems(states2);
        this.shipStateSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddVendorActivity.this.lambda$setShipStateSpinner$4$AddVendorActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setShipStateSpinner$4$AddVendorActivity(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedShipState = selectedItem;
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getApplicationContext());
        //Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.currencyList.add(new com.akounto.accountingsoftware.model.Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            currencyListForDisplay.add( jsonObject.getString("Id") + "-"+jsonObject.getString("Name") );
            this.currencyListForSpinner.add(jsonObject.getString("Name"));
        }
        setCurrencySpinner(currencyListForSpinner, this.currencyListForDisplay);
    }

    private void setCurrencySpinner(List<String> currencies,List<String> display) {
        PowerSpinnerView powerSpinnerView = findViewById(R.id.currencySpinner);
        this.currencySpinner = powerSpinnerView;
        powerSpinnerView.setItems(display);
        this.currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                AddVendorActivity.this.lambda$setCurrencySpinner$5$AddVendorActivity(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setCurrencySpinner$5$AddVendorActivity(int i, String s, int selectedIndex, String selectedItem) {
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

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
