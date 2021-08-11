package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.EditCompanyRequest;
import com.akounto.accountingsoftware.response.BusinessDetails;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class EditBusinessFragment extends Fragment {
    BusinessDetails businessDetails;
    TextView companyNameErrorTv;
    TextView countryErrorTv;
    List<String> countryListForSpinner = new ArrayList();
    Map<Integer, String> countryMap = new HashMap();
    PowerSpinnerView countrySpinner;
    TextView currencyTv;
    EditText etAddress1;
    EditText etAddress2;
    EditText etCity;
    EditText etCompanyName;
    EditText etFax;
    EditText etMobile;
    EditText etPhone;
    EditText etPostCode;
    EditText etState;
    EditText etTollFree;
    int selectedCountry = 0;
    String selectedTimeZone = "";
    List<String> timeZoneListForSpinner = new ArrayList();
    PowerSpinnerView timeZoneSpinner;
    TextView title;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.edit_business_fragment, container, false);
        if (getArguments() != null) {
            this.businessDetails = (BusinessDetails) getArguments().getSerializable("businessDetails");
        }
        initUI();
        return this.view;
    }

    private void initUI() {
        this.title = this.view.findViewById(R.id.title);
        this.etCompanyName = this.view.findViewById(R.id.et_companyName);
        this.countrySpinner = this.view.findViewById(R.id.countrySpinner);
        this.timeZoneSpinner = this.view.findViewById(R.id.timeZoneSpinner);
        this.etAddress1 = this.view.findViewById(R.id.et_address1);
        this.etAddress2 = this.view.findViewById(R.id.et_address2);
        this.etCity = this.view.findViewById(R.id.et_city);
        this.etPostCode = this.view.findViewById(R.id.et_postCode);
        this.etState = this.view.findViewById(R.id.et_state);
        this.etPhone = this.view.findViewById(R.id.et_phone);
        this.etFax = this.view.findViewById(R.id.et_fax);
        this.etMobile = this.view.findViewById(R.id.et_mobile);
        this.etTollFree = this.view.findViewById(R.id.et_tollFree);
        this.currencyTv = this.view.findViewById(R.id.currencyTv);
        this.companyNameErrorTv = this.view.findViewById(R.id.companyNameErrorTv);
        this.countryErrorTv = this.view.findViewById(R.id.countryErrorTv);
        try {
            fetchCountries();
            fetchTimeZones();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.businessDetails != null) {
            TextView textView = this.title;
            textView.setText("Edit " + this.businessDetails.getBusinessName());
            this.etCompanyName.setText(this.businessDetails.getBusinessName());
            this.etAddress1.setText(this.businessDetails.getAddress1());
            this.etAddress2.setText(this.businessDetails.getAddress2());
            this.etCity.setText(this.businessDetails.getCity());
            this.etState.setText(this.businessDetails.getState());
            this.etPostCode.setText(this.businessDetails.getPostCode());
            this.etPhone.setText(this.businessDetails.getPhone());
            this.etFax.setText(this.businessDetails.getFax());
            this.etMobile.setText(this.businessDetails.getMobile());
            this.etTollFree.setText(this.businessDetails.getTollFree());
            this.countrySpinner.setText(this.countryMap.get(Integer.valueOf(this.businessDetails.getCountry())));
            this.selectedCountry = this.businessDetails.getCountry();
            this.selectedTimeZone = this.businessDetails.getTimeZone();
            this.timeZoneSpinner.setText(this.businessDetails.getTimeZone());
            this.currencyTv.setText(this.businessDetails.getBusinessCurrency());
            this.view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    EditBusinessFragment.this.lambda$initUI$0$EditBusinessFragment(view);
                }
            });
        }
        this.view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                EditBusinessFragment.this.lambda$initUI$1$EditBusinessFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$initUI$0$EditBusinessFragment(View v) {
        editBusiness(this.businessDetails);
    }

    public /* synthetic */ void lambda$initUI$1$EditBusinessFragment(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
        this.countrySpinner.setItems(this.countryListForSpinner);
        this.countrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                EditBusinessFragment.this.lambda$fetchCountries$2$EditBusinessFragment(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public /* synthetic */ void lambda$fetchCountries$2$EditBusinessFragment(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedCountry = getCountryId(selectedItem);
    }

    private int getCountryId(String selectedCountry2) {
        for (Map.Entry<Integer, String> map : this.countryMap.entrySet()) {
            if (map.getValue().equals(selectedCountry2)) {
                return map.getKey().intValue();
            }
        }
        return 0;
    }

    private void fetchTimeZones() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("time-zone.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            this.timeZoneListForSpinner.add(jsonArray.getJSONObject(i).getString(AppMeasurementSdk.ConditionalUserProperty.NAME));
        }
        setCurrencySpinner(this.timeZoneListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencyListForSpinner) {
        this.timeZoneSpinner.setItems(currencyListForSpinner);
        this.timeZoneSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                EditBusinessFragment.this.lambda$setCurrencySpinner$3$EditBusinessFragment(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public /* synthetic */ void lambda$setCurrencySpinner$3$EditBusinessFragment(int i, String s, int selectedIndex, String selectedItem) {
        this.selectedTimeZone = selectedItem;
    }

    private void editBusiness(BusinessDetails businessDetails2) {
        String companyName = this.etCompanyName.getText().toString();
        if (companyName.isEmpty()) {
            this.companyNameErrorTv.setText("Please enter company name");
            this.companyNameErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.companyNameErrorTv.setVisibility(View.GONE);
        if (this.selectedCountry == 0) {
            this.countryErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.countryErrorTv.setVisibility(View.GONE);
        String address1 = this.etAddress1.getText().toString();
        String address2 = this.etAddress2.getText().toString();
        String city = this.etCity.getText().toString();
        String state = this.etState.getText().toString();
        String postcode = this.etPostCode.getText().toString();
        String phone = this.etPhone.getText().toString();
        String fax = this.etFax.getText().toString();
        String mobile = this.etMobile.getText().toString();
        EditCompanyRequest editCompanyRequest = new EditCompanyRequest(this.selectedTimeZone, businessDetails2.getBusinessCurrency(), companyName, address2, address1, city, mobile, this.etTollFree.getText().toString(), state, phone, this.selectedCountry, businessDetails2.getId(), postcode, fax);
        Log.d("sd", editCompanyRequest.toString());
        save(editCompanyRequest);
    }

    public void save(EditCompanyRequest editCompanyRequest) {
        RestClient.getInstance(getContext()).editCompany(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), editCompanyRequest).enqueue(new CustomCallBack<ResponseBody>(getContext(), "Modifying...") {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(EditBusinessFragment.this.getContext(), "Updated!");
                    EditBusinessFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(EditBusinessFragment.this.getContext(), "Error occurred while updating.");
            }
        });
    }
}
