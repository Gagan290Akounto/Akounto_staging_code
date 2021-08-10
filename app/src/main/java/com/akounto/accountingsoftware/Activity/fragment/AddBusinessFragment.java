package com.akounto.accountingsoftware.Activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.ExternalSignUp;
import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Business;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.BussniessList;
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddCompanyRequest;
import com.akounto.accountingsoftware.response.AddBusinessResponse;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class AddBusinessFragment extends Fragment {

    int financialDayEnd = 31;
    int financialMonthEnd = 12;
    Spinner businessCurrencySpinner;
    Map<Integer, String> dealsMap = new HashMap();
    List<String> dealsinForSpinner = new ArrayList();
    Spinner countrySpinner;
    TextView companyNameErrorTv;
    TextView b_name_error, deals_with_business_error, type_business_error;
    TextView countryErrorTv,call;
    int selectedCountry = 0;
    String selectedCurrencyId = "USD";
    private int businessTypeId = 0, dealsWithId = 0;
    List<String> countryListForSpinner = new ArrayList();
    Map<Integer, String> countryMap = new HashMap();
    List<String> currencyListForSpinner = new ArrayList();
    Map<String, String> currencyMap = new HashMap();
    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    Map<Integer, String> typeBusnisMap = new HashMap();
    List<String> typeBusniSpinner = new ArrayList();
    List<String> countryListFoCurrency = new ArrayList();
    Spinner dealsWithSpinner,monthsSpinner,datesSpinner;
    EditText etCompanyName;
    ArrayList<String> dates = null;
    String selectedDealsWith;
    String selectedTypeOfBusiness;
    TextView typeOfBusinessErrorTv;
    Spinner typeOfBusinessSpinner;
    private LinearLayout b_name_ll, dealsWithBusiness_error, typeBusiness_error, back;
    List<String> typesOfBusinessList = new ArrayList();
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.add_business_fragment, container, false);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, MoreFragment.class);
            }
        });
        try {
            initUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.view;
    }

    private void initUI() throws JSONException {
        this.call=this.view.findViewById(R.id.cusmoter_care);
        this.etCompanyName = this.view.findViewById(R.id.et_companyName);
        this.monthsSpinner = this.view.findViewById(R.id.monthsSpinner);
        this.datesSpinner = this.view.findViewById(R.id.datesSpinner);
        this.typeOfBusinessSpinner = this.view.findViewById(R.id.businessTypeSpinner);
        this.dealsWithSpinner = this.view.findViewById(R.id.dealsWithSpinner);
        this.countrySpinner = this.view.findViewById(R.id.countrySpinner);
        this.businessCurrencySpinner = this.view.findViewById(R.id.businessCurrencySpinner);
        this.b_name_error = this.view.findViewById(R.id.companyNameErrorTv);
        this.type_business_error = this.view.findViewById(R.id.typeOfBusinessErrorTv);
        this.deals_with_business_error = this.view.findViewById(R.id.dealsWithErrorTv);
        this.countryErrorTv = this.view.findViewById(R.id.countryErrorTv);
        b_name_ll = this.view.findViewById(R.id.business_name_ll);
        dealsWithBusiness_error = this.view.findViewById(R.id.dealsWithBusiness_error);
        typeBusiness_error = this.view.findViewById(R.id.typeBusiness_error);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+18332568686"));
                    startActivity(callIntent);
                } catch (Exception e) {
                }
            }
        });
        updateDateSpinner(this.financialMonthEnd - 1);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, this.months);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsSpinner.setAdapter(dateAdapter);
        monthsSpinner.setSelection(months.size()-1);
        monthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    financialMonthEnd = position + 1;

                        updateDateSpinner(position);


                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("type_of_business.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.typeBusnisMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.typeBusniSpinner.add(jsonObject.getString("Name"));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_text, typeBusniSpinner);
        dataAdapter.setDropDownViewResource(R.layout.dropdown_text);
        this.typeOfBusinessSpinner.setAdapter(dataAdapter);
        typeOfBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedTypeOfBusiness = typesOfBusinessList.get(i);
                selectedTypeOfBusiness = typeBusniSpinner.get(i);
                businessTypeId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String loadJSONFromAsset1 = JsonUtils.loadJSONFromAsset("deals_in.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset1);
        JSONArray jsonArray1 = new JSONArray(loadJSONFromAsset1);
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject jsonObject = jsonArray1.getJSONObject(i);
            this.dealsMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.dealsinForSpinner.add(jsonObject.getString("Name"));
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_text, dealsinForSpinner);
        dataAdapter2.setDropDownViewResource(R.layout.dropdown_text);

        this.dealsWithSpinner.setAdapter(dataAdapter2);
        dealsWithSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //selectedDealsWith = dealsWithList.get(i);
                selectedDealsWith = dealsinForSpinner.get(i);
                dealsWithId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        try {
            fetchCountries();
            fetchCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddBusinessFragment.this.lambda$initUI$2$AddBusinessFragment(view);
            }
        });
        this.view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddBusinessFragment.this.lambda$initUI$3$AddBusinessFragment(view);
            }
        });
    }

    public void lambda$initUI$0$AddBusinessFragment(int i, String s, int selectedIndex, String selectedItem) {

    }

    public void lambda$initUI$1$AddBusinessFragment(int i, String s, int selectedIndex, String selectedItem) {

    }

    public void lambda$initUI$2$AddBusinessFragment(View v) {
        reset();
        createNewBusiness();
    }

    public void lambda$initUI$3$AddBusinessFragment(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /*private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
        ////Collections.sort(countryListForSpinner);
        ArrayAdapter dataAdaptercountry = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryListForSpinner);
        dataAdaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(dataAdaptercountry);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = getCountryId(countryListForSpinner.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getCountryId(String selectedCountry2) {
        for (Map.Entry<Integer, String> map : this.countryMap.entrySet()) {
            if (map.getValue().equals(selectedCountry2)) {
                return map.getKey().intValue();
            }
        }
        return 0;
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String currency = jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name");
            this.currencyMap.put((String) jsonObject.get("Id"), currency);
            this.currencyListForSpinner.add(currency);
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencyListForSpinner2) {
        ArrayAdapter dataAdapterCurrency = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currencyListForSpinner2);
        dataAdapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessCurrencySpinner.setAdapter(dataAdapterCurrency);
        businessCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrencyId = currencyListForSpinner2.get(i).substring(0, 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/
    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
            this.countryListFoCurrency.add(jsonObject.getString("Currency"));
        }
        ////Collections.sort(countryListForSpinner);
        ArrayAdapter<String> dataAdaptercountry = new ArrayAdapter<>(getContext(), R.layout.dropdown_text, countryListForSpinner);
        dataAdaptercountry.setDropDownViewResource(R.layout.dropdown_text);
        countrySpinner.setAdapter(dataAdaptercountry);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = getCountryId(countryListForSpinner.get(i));
                Log.e("Country ::", countryListFoCurrency.get(i));
                businessCurrencySpinner.setSelection(searchIndexCur(countryListFoCurrency.get(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getCountryId(String selectedCountry2) {
        for (Map.Entry<Integer, String> map : this.countryMap.entrySet()) {
            if (map.getValue().equals(selectedCountry2)) {
                return map.getKey().intValue();
            }
        }
        return 0;
    }

    private void fetchCurrencies() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("currency.json", getContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String currency = jsonObject.getString("Id") + " (" + jsonObject.getString("Symbol") + ")- " + jsonObject.getString("Name");
            this.currencyMap.put((String) jsonObject.get("Id"), currency);
            this.currencyListForSpinner.add(currency);
        }
        setCurrencySpinner(this.currencyListForSpinner);
    }

    private void setCurrencySpinner(List<String> currencyListForSpinner2) {
        ArrayAdapter<String> dataAdapterCurrency = new ArrayAdapter<>(getContext(), R.layout.dropdown_text, currencyListForSpinner2);
        dataAdapterCurrency.setDropDownViewResource(R.layout.dropdown_text);
        businessCurrencySpinner.setAdapter(dataAdapterCurrency);
        businessCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrencyId = currencyListForSpinner2.get(i).substring(0, 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int searchIndexCur(String cur_id) {
        int i = 0;
        for (int c = 0; c < currencyListForSpinner.size(); c++) {
            if (currencyListForSpinner.get(c).contains(cur_id)) {
                i = c;
            }
        }
        return i;
    }
    private void createNewBusiness() {
       String companyName = this.etCompanyName.getText().toString();
         /*if (companyName.isEmpty()) {
            this.companyNameErrorTv.setText("Please enter company name");
            this.companyNameErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.companyNameErrorTv.setVisibility(View.GONE);
        if (this.selectedTypeOfBusiness.isEmpty()) {
            this.typeOfBusinessErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.typeOfBusinessErrorTv.setVisibility(View.GONE);
        if (this.selectedDealsWith.isEmpty()) {
            this.dealsWithErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.dealsWithErrorTv.setVisibility(View.GONE);
        if (this.selectedCountry == 0) {
            this.countryErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.countryErrorTv.setVisibility(View.GONE);
        if (this.selectedCurrencyId.isEmpty()) {
            this.businessCurrencyErrorTv.setVisibility(View.VISIBLE);
            return;
        }
        this.businessCurrencyErrorTv.setVisibility(View.GONE);*/
        if (isValidAbout())
            save(new AddCompanyRequest(this.dealsWithId + "", this.businessTypeId + "", this.selectedCurrencyId, companyName, this.selectedDealsWith, this.selectedCountry + "",selectedCurrencyId ,this.selectedTypeOfBusiness,financialDayEnd,financialMonthEnd));
    }

    private boolean isValidAbout() {
        boolean response = true;
        boolean focusfield = true;

        if (etCompanyName.getText().toString().trim().length() == 0) {
            response = false;
            b_name_error.setVisibility(View.VISIBLE);
            b_name_ll.setBackgroundResource(R.drawable.error);
            if (focusfield) {
                etCompanyName.requestFocus();
                focusfield = false;
            }
        } else if (etCompanyName.getText().toString().trim().length() < 2) {
            b_name_error.setVisibility(View.VISIBLE);
            b_name_error.setText("Please enter business name min 2 character");
            b_name_ll.setBackgroundResource(R.drawable.error);
            response = false;
            if (focusfield) {
                etCompanyName.requestFocus();
                focusfield = false;
            }
        }
        if (dealsWithId == 0) {
            dealsWithBusiness_error.setBackgroundResource(R.drawable.error);
            deals_with_business_error.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                dealsWithBusiness_error.requestFocus();
                focusfield = false;
            }
        }

        if (businessTypeId == 0) {
            typeBusiness_error.setBackgroundResource(R.drawable.error);
            type_business_error.setVisibility(View.VISIBLE);
            response = false;
            if (focusfield) {
                typeBusiness_error.requestFocus();
                focusfield = false;
            }
        }

        return response;
    }
    private void updateDateSpinner(int financialMonthEnd2) {
        dates = new ArrayList<>();
        Spinner powerSpinnerView = this.datesSpinner;
        int days = Constant.days_month[financialMonthEnd2];
        try {
            this.financialDayEnd = days;
            for (int i = 1; i <= days; i++) {
                List<String> list = this.dates;
                list.add("" + i);
            }
            ArrayAdapter dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, this.dates);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            powerSpinnerView.setAdapter(dataAdapter);
            powerSpinnerView.setSelection(dates.size()-1);
        } catch (Exception e) {
        }
        powerSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                financialDayEnd = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> list2 = this.dates;
    }
    public void save(AddCompanyRequest addCompanyRequest) {
        RestClient.getInstance(getContext()).addCompany(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), addCompanyRequest).enqueue(new CustomCallBack<AddBusinessResponse>(getContext(), "Adding business...") {
            public void onResponse(Call<AddBusinessResponse> call, Response<AddBusinessResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(AddBusinessFragment.this.getContext(), "Business Added!");
                    Bundle b = new Bundle();
                    b.putString(Constant.CATEGORY, "profile");
                    b.putString(Constant.ACTION, "add_business");
                    SplashScreenActivity.sendEvent("profile_add_business", b);
                    UserDetails userDetails = UiUtil.getUserDetail(getContext());
                    SignInResponse signInResponse = UiUtil.getUserDetails(getContext());
                    List<Business> list_buss = userDetails.getBusiness();
                    list_buss.add(response.body().getData());
                    userDetails.setBusiness(list_buss);
                    signInResponse.setUserDetails(new Gson().toJson(userDetails));
                    LoginData loginData = new Gson().fromJson(new Gson().toJson(signInResponse), LoginData.class);
                    loginData.setExpires(signInResponse.getExpires());
                    UiUtil.addUserDetails(getContext(), signInResponse);
                    AddFragments.addFragmentToDrawerActivity(getContext(), null, BussniessList.class);
                    return;
                }
                UiUtil.showToast(AddBusinessFragment.this.getContext(), "Some error occurred while adding.");
            }

            public void onFailure(Call<AddBusinessResponse> call, Throwable t) {
                super.onFailure(call, t);
                UiUtil.showToast(AddBusinessFragment.this.getContext(), "Error occurred while adding.");
            }
        });
    }
    private void reset() {
        try {
            b_name_ll.setBackgroundResource(R.drawable.new_light_blue);
            dealsWithBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            typeBusiness_error.setBackgroundResource(R.drawable.new_light_blue);
            b_name_error.setVisibility(View.GONE);
            deals_with_business_error.setVisibility(View.GONE);
            type_business_error.setVisibility(View.GONE);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "create bussiness");
        }
    }
}
