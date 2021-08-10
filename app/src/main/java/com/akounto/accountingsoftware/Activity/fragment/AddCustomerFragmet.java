package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.model.Country;
import com.akounto.accountingsoftware.model.Currency;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddCustomerRequest;
import com.akounto.accountingsoftware.response.Customer;
import com.akounto.accountingsoftware.response.CustomerOnlyResponse;
import com.akounto.accountingsoftware.response.CustomerUpdateResponse;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class AddCustomerFragmet extends Fragment {

    int CompanyId;
    int HeadTransactionId;
    int cid;
    List<Country> countryList = new ArrayList();
    List<String> countryListForSpinner = new ArrayList();
    List<Currency> currencyList = new ArrayList();
    List<String> currencyListForSpinner = new ArrayList();
    public static Customer customer;
    public static int editCont;
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
    public static boolean is_edit;
    TextView pageTitle;
    ImageView back;
    String selectedBillCountry;
    String selectedBillState;
    String selectedCurrencyId;
    String selectedShipCountry;
    String selectedShipState;
    CheckBox shipAddrCheckBox;
    List<String> states = new ArrayList();
    private View view;

    public void setData(int editCont, Customer customer) {
        this.editCont = editCont;
        this.customer = customer;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_add_customers, container, false);

        try {
            initUi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.view;
    }

    private void initUi() throws JSONException {
        TextView textView = view.findViewById(R.id.pageTitle);
        this.pageTitle = textView;
        textView.setText("Add Customer");
        this.shipAddrCheckBox = view.findViewById(R.id.shipToDiffCB);
        fetchCountries();
        fetchCurrencies();
        LinearLayout ll = view.findViewById(R.id.shipAddrLayout);
        back = view.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish();
                AddFragments.addFragmentToDrawerActivity(getContext(), null, CustomersFragment.class);
            }
        });
        this.shipAddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                lambda$initUi$0$AddCustomerFragmet(ll, compoundButton, z);
            }
        });
        this.etCompanyName = view.findViewById(R.id.et_name);
        this.etEmail = view.findViewById(R.id.et_email);
        this.etPhone = view.findViewById(R.id.et_phone);
        this.etAddressLine1 = view.findViewById(R.id.et_billAddr1);
        this.etAddressLine2 = view.findViewById(R.id.et_billAddr2);
        this.etBillCity = view.findViewById(R.id.et_billCity);
        this.etBillPostal = view.findViewById(R.id.et_billPostCode);
        this.etShipAddressLine1 = view.findViewById(R.id.et_shipAddr1);
        this.etShipAddressLine2 = view.findViewById(R.id.et_shipAddr2);
        this.etShipCity = view.findViewById(R.id.et_shipCity);
        this.etShipPostal = view.findViewById(R.id.et_shipPostCode);
        this.etAccountNumber = view.findViewById(R.id.et_accountNumber);
        this.etFax = view.findViewById(R.id.et_fax);
        this.etMobile = view.findViewById(R.id.et_mobile);
        this.etTollFree = view.findViewById(R.id.et_tollFree);
        this.etPortal = view.findViewById(R.id.et_portal);
        this.etInternalNotes = view.findViewById(R.id.et_notes);
        int i = this.editCont;
        if (i == 1 || i == 2) {
            //Customer customer2 = new Gson().fromJson(getIntent().getStringExtra(UiConstants.CUSTOMER_DATE), Customer.class);
            //this.customer = customer2;
            this.cid = customer.getId();
            this.HeadTransactionId = this.customer.getHeadTransactionId();
            this.CompanyId = this.customer.getCompanyId();
            getCustomerOnly(this.customer.getHeadTransactionId());
        }
        setBillCountrySpinner();
        if (this.editCont == 1) {
            view.findViewById(R.id.saveButton).setVisibility(View.GONE);
        }
        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initUi$1$AddCustomerFragmet(view);
            }
        });
    }


    public void lambda$initUi$0$AddCustomerFragmet(LinearLayout linearLayout, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            linearLayout.setVisibility(View.VISIBLE);
            setShipCountrySpinner();
            this.isShippingSame = false;
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.isShippingSame = true;
    }

    public void lambda$initUi$1$AddCustomerFragmet(View v) {
        addCustomer();
    }

    private void addCustomer() {
        String mName = this.etCompanyName.getText().toString().trim();
        if (mName.length() < 2) {
            UiUtil.showToast(getContext(), "Customer name should be at least 2 character long!");
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
                RestClient.getInstance(getContext()).updateCustomer(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), addCustomer).enqueue(new CustomCallBack<CustomerUpdateResponse>(getContext(), "Adding Customer...") {
                    public void onResponse(Call<CustomerUpdateResponse> call, Response<CustomerUpdateResponse> response) {
                        super.onResponse(call, response);
                        try {
                            if (response.body().getTransactionStatus().isIsSuccess()) {
                                UiUtil.showToast(getContext(), "Customer details updated");
                                AddFragments.addFragmentToDrawerActivity(getContext(), null, CustomersFragment.class);
                                //AddFragments.addFragmentToDrawerActivity(getActivity(), null, CustomersFragment.class);
                            } else {
                                UiUtil.showToast(getContext(), response.body().getTransactionStatus().getError().getDescription());
                            }
                        } catch (Exception e) {
                            UiUtil.showToast(getContext(), "Fail to update");
                        }
                    }

                    public void onFailure(Call<CustomerUpdateResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        UiUtil.showToast(getContext(), "Fail to update");
                    }
                });
                return;
            }
            RestClient.getInstance(getContext()).addCustomer(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), addCustomer).enqueue(new CustomCallBack<CustomerUpdateResponse>(getContext(), "Adding Customer...") {
                public void onResponse(Call<CustomerUpdateResponse> call, Response<CustomerUpdateResponse> response) {
                    super.onResponse(call, response);
                    try {
                        if (response.body().getTransactionStatus().isIsSuccess()) {
                            UiUtil.showToast(getContext(), "Customer Added");
                            //finish();
                            AddFragments.addFragmentToDrawerActivity(getContext(), null, CustomersFragment.class);
                        } else {
                            UiUtil.showToast(getContext(), response.body().getTransactionStatus().getError().getDescription());
                        }
                    } catch (Exception e) {
                        UiUtil.showToast(getContext(), "Fail to update");
                    }
                }

                public void onFailure(Call<CustomerUpdateResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    UiUtil.showToast(getContext(), "Fail to update");
                }
            });
        } else {
            UiUtil.showToast(getContext(), "Please enter valid Email");
            this.etEmail.setError("Please enter valid Email");
        }
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryList.add(new Country(((Integer) jsonObject.get("Id")).intValue(), jsonObject.getString("Name")));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
    }

    private void setBillCountrySpinner() {
        PowerSpinnerView billCountrySpinner = view.findViewById(R.id.billCountrySpinner);
        billCountrySpinner.setItems(this.countryListForSpinner);
        billCountrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                lambda$setBillCountrySpinner$2$AddCustomerFragmet(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setBillCountrySpinner$2$AddCustomerFragmet(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedBillCountry = String.valueOf(getCountryId(selectedItem));
        getStatesByCountry(getCountryId(selectedItem), "bill");
    }

    private void setShipCountrySpinner() {
        PowerSpinnerView shipCountrySpinner = view.findViewById(R.id.shipCountrySpinner);
        shipCountrySpinner.setItems(this.countryListForSpinner);
        shipCountrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                lambda$setShipCountrySpinner$3$AddCustomerFragmet(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setShipCountrySpinner$3$AddCustomerFragmet(int i, String s, int selectedIndex, String selectedItem) {
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
        RestClient.getInstance(getContext()).getStatesByCountryId(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), countryId).enqueue(new CustomCallBack<GetStatesResponse>(getContext(), "Fetching States...") {
            public void onResponse(Call<GetStatesResponse> call, Response<GetStatesResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    states = response.body().getStates();
                    if (billOrShip.equals("bill")) {
                        setBillStateSpinner(states);
                        return;
                    }
                    setShipStateSpinner(states);
                }
            }

            public void onFailure(Call<GetStatesResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(getContext(), "Failed to fetch states");
            }
        });
    }

    /* access modifiers changed from: private */
    public void setBillStateSpinner(List<String> states2) {
        PowerSpinnerView billStateSpinner = view.findViewById(R.id.billStateSpinner);
        billStateSpinner.setItems(states2);
        billStateSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                lambda$setBillStateSpinner$4$AddCustomerFragmet(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setBillStateSpinner$4$AddCustomerFragmet(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedBillState = selectedItem;
    }

    /* access modifiers changed from: private */
    public void setShipStateSpinner(List<String> states2) {
        PowerSpinnerView shipStateSpinner = view.findViewById(R.id.shipStateSpinner);
        shipStateSpinner.setItems(states2);
        shipStateSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                lambda$setShipStateSpinner$5$AddCustomerFragmet(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setShipStateSpinner$5$AddCustomerFragmet(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedShipState = selectedItem;
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getContext());
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String currency = jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name");
            this.currencyList.add(new Currency(jsonObject.getString("Symbol"), jsonObject.getString("Id"), jsonObject.getString("Name")));
            this.currencyListForSpinner.add(currency);
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencies) {
        PowerSpinnerView currencySpinner = view.findViewById(R.id.currencySpinner);
        currencySpinner.setItems(currencies);
        currencySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                lambda$setCurrencySpinner$6$AddCustomerFragmet(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$setCurrencySpinner$6$AddCustomerFragmet(int i, String s, int selectedIndex, String selectedItem) {
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
        RestClient.getInstance(getContext()).getCustomerListById(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), id).enqueue(new CustomCallBack<CustomerOnlyResponse>(getContext(), null) {
            public void onResponse(Call<CustomerOnlyResponse> call, Response<CustomerOnlyResponse> response) {
                super.onResponse(call, response);
                Log.d("CustomerResponse---", response.toString());
                if (response.isSuccessful()) {
                    updateCustomerData(response.body().getData());
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
        Log.e("Customer Data :;", new Gson().toJson(customer2));
        if (customer2.getName() != null) {
            this.etCompanyName.setText(customer2.getName());
        }
        if (customer2.getEmail() != null) {
            this.etEmail.setText(customer2.getEmail());
        }
        if (customer2.getPhone() != null) {
            this.etPhone.setText(customer2.getPhone());
        }
        if (customer2.getAccountNumber() != null) {
            this.etAccountNumber.setText(customer2.getAccountNumber());
        }
        if (customer2.getFax() != null) {
            this.etFax.setText(customer2.getFax());
        }
        if (customer2.getTollFree() != null) {
            this.etTollFree.setText(customer2.getTollFree());
        }
        if (customer2.getMobile() != null) {
            this.etMobile.setText(customer2.getMobile());
        }
        if (customer2.getPortal() != null) {
            this.etPortal.setText(customer2.getPortal());
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
        this.shipAddrCheckBox.setChecked(!customer2.isIsShippingSame());
        if (customer2.getShipAddress1() != null) {
            this.etShipAddressLine1.setText(customer2.getShipAddress1());
        }
        if (customer2.getShipAddress2() != null) {
            this.etShipAddressLine2.setText(customer2.getShipAddress2());
        }
        if (customer2.getShipCity() != null) {
            this.etShipCity.setText(customer2.getShipCity());
        }
        if (customer2.getShipPostCode() != null) {
            this.etShipPostal.setText(customer2.getShipPostCode());
        }
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
