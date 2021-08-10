package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.UserDetails;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Setting.SettingMenu;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DateAndCurrencyFragment extends Fragment {

    ArrayList<String> dates = null;
    Spinner datesSpinner;
    int financialDayEnd = 31;
    int financialMonthEnd = 12;
    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    Spinner monthsSpinner;
    TextView tipTv;
    View view;
    int d = 0;
    int m = 0;
    int count = 0;
    UserDetails userDetails;
    SignInResponse signInResponse;
    boolean start = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.dates_and_currency_fragment, container, false);
        try {
            String endfy = UiUtil.getUserDetail(getContext()).getActiveBusiness().getFinancialYearEnd();
            d = Integer.parseInt(endfy.split("-")[0]);
            m = Integer.parseInt(endfy.split("-")[1]) - 1;
            userDetails = UiUtil.getUserDetail(getContext());
            signInResponse = UiUtil.getUserDetails(getContext());
        } catch (Exception e) {

        }
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, SettingMenu.class);
            }
        });
        initUI();
        return this.view;
    }

    private void initUI() {
        this.tipTv = this.view.findViewById(R.id.tipTV);
        this.monthsSpinner = this.view.findViewById(R.id.monthsSpinner);
        this.datesSpinner = this.view.findViewById(R.id.datesSpinner);
        updateDateSpinner(this.financialMonthEnd - 1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, this.months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsSpinner.setAdapter(dataAdapter);
        try {
            monthsSpinner.setSelection(m);
        } catch (Exception e) {
        }
        monthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    financialMonthEnd = position + 1;
                    if (start) {
                        updateDateSpinner(position);
                    } else {
                        start = true;
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                Map<String, Integer> map = new HashMap<>();
                map.put("FinancialMonthEnd", Integer.valueOf(financialMonthEnd));
                map.put("FinancialDayEnd", Integer.valueOf(financialDayEnd));
                submitRequest(map);
            }
        });
        this.view.findViewById(R.id.infoIv).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                if (tipTv.getVisibility() == View.VISIBLE) {
                    tipTv.setVisibility(View.GONE);
                } else {
                    tipTv.setVisibility(View.VISIBLE);
                }
            }
        });
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
            if (start) {
                d=dates.size();
            }
            ArrayAdapter dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, this.dates);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            powerSpinnerView.setAdapter(dataAdapter);
            count++;
            Log.e("Count Date :: ", count + " " + d);
            try {
                datesSpinner.setSelection(d - 1);
            } catch (Exception e) {
                Log.e("error :: ", e.toString());
            }


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

    private void submitRequest(Map<String, Integer> map) {
        RestClient.getInstance(getContext()).editCompanyFinancialYear(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(getContext()), UiUtil.getComp_Id(getContext()), map).enqueue(new CustomCallBack<ResponseBody>(getContext(), null) {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"setting");
                    b.putString(Constant.ACTION,"end_fin_year");
                    SplashScreenActivity.sendEvent("setting_end_fin_year",b);
                    UiUtil.showToast(DateAndCurrencyFragment.this.getContext(), "Saved");
                    userDetails.getActiveBusiness().setFinancialYearEnd(financialDayEnd + "-" + financialMonthEnd);
                    signInResponse.setUserDetails(new Gson().toJson(userDetails));
                    LoginData loginData = new Gson().fromJson(new Gson().toJson(signInResponse), LoginData.class);
                    loginData.setExpires(signInResponse.getExpires());
                    UiUtil.addUserDetails(getContext(), signInResponse);
                } else {
                    UiUtil.showToast(DateAndCurrencyFragment.this.getContext(), "Error while Saving");
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }
}
