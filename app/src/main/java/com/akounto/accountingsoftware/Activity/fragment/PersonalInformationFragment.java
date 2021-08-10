package com.akounto.accountingsoftware.Activity.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.SplashScreenActivity;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment;
import com.akounto.accountingsoftware.model.Country;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.AddUserRequest;
import com.akounto.accountingsoftware.response.UserInfo;
import com.akounto.accountingsoftware.response.UserInfoResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiUtil;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class PersonalInformationFragment extends Fragment {
    ImageView calendar;
    TextView countryErrorTv;
    List<Country> countryList = new ArrayList();
    List<String> countryListForSpinner = new ArrayList();
    Map<Integer, String> countryMap = new HashMap();
    PowerSpinnerView countrySpinner;
    EditText etCity;
    TextView etDob;
    EditText etFirstName;
    EditText etLastName;
    EditText etZipCode;
    TextView fnameErrorTv;
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    TextView lnameErrorTv;
    private int mDay;
    private int mMonth;
    private int mYear;
    String receivedDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private int selectedCountry = 0;
    String selectedDob = null;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.personal_information_fragment, container, false);
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getContext(), null, MoreFragment.class);
            }
        });
        initUI();
        return this.view;
    }

    private void initUI() {
        this.etFirstName = this.view.findViewById(R.id.et_fname);
        this.etLastName = this.view.findViewById(R.id.et_lname);
        this.etCity = this.view.findViewById(R.id.et_city);
        this.etDob = this.view.findViewById(R.id.et_dob);
        this.etZipCode = this.view.findViewById(R.id.et_zipcode);
        this.countrySpinner = this.view.findViewById(R.id.countrySpinner);
        this.fnameErrorTv = this.view.findViewById(R.id.fNameErrorTv);
        this.lnameErrorTv = this.view.findViewById(R.id.lNameErrorTv);
        this.countryErrorTv = this.view.findViewById(R.id.countryErrorTv);
        this.calendar = this.view.findViewById(R.id.calendar);
        try {
            fetchCountries();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        getPersonalInformation();
    }

    private void getPersonalInformation() {
        RestClient.getInstance(getContext()).getUserInfo(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext())).enqueue(new CustomCallBack<UserInfoResponse>(getContext(), null) {
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    PersonalInformationFragment.this.setUpInformation(response.body().getUserInfo());
                }
            }

            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    public void setUpInformation(UserInfo userInfo) {
        this.etFirstName.setText(userInfo.getFirstName());
        this.etLastName.setText(userInfo.getLastName());
        this.etZipCode.setText(userInfo.getPostCode());
        this.etCity.setText(userInfo.getCity());
        this.view.findViewById(R.id.btn_update).setOnClickListener(view -> PersonalInformationFragment.this.lambda$setUpInformation$0$PersonalInformationFragment(userInfo, view));
        this.selectedCountry = userInfo.getCountry();
        this.countrySpinner.setText(this.countryMap.get(Integer.valueOf(userInfo.getCountry())));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.isoDatePattern, Locale.US);
        if (userInfo.getDOB() != null) {
            SimpleDateFormat receivedDateFormatter = new SimpleDateFormat(this.receivedDateFormat, Locale.US);
            this.etDob.setText(formatDate(userInfo.getDOB()));
            try {
                this.selectedDob = simpleDateFormat.format(receivedDateFormatter.parse(userInfo.getDOB()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Locale.setDefault(Locale.US);
        Calendar c = Calendar.getInstance();
        this.mYear = c.get(1);
        this.mMonth = c.get(2);
        this.mDay = c.get(5);
        this.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PersonalInformationFragment.this.lambda$setUpInformation$2$PersonalInformationFragment(simpleDateFormat, view);
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog   mDatePicker =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(5, selectedday);
                        calendar2.set(2, selectedmonth);
                        calendar2.set(1, selectedyear);
                        etDob.setText(selectedday + "-" + (selectedmonth + 1) + "-" + selectedyear);
                        selectedDob = simpleDateFormat.format(calendar2.getTime());

                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Please select date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });


    }

    public void lambda$setUpInformation$0$PersonalInformationFragment(UserInfo userInfo, View v) {
        save(userInfo);
    }

    public void lambda$setUpInformation$2$PersonalInformationFragment(SimpleDateFormat simpleDateFormat, View v) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                PersonalInformationFragment.this.lambda$null$1$PersonalInformationFragment(simpleDateFormat, datePicker, i, i2, i3);
            }
        }, this.mYear, this.mMonth, this.mDay).show();

    }

    public void lambda$null$1$PersonalInformationFragment(SimpleDateFormat simpleDateFormat, DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(5, dayOfMonth);
        calendar2.set(2, monthOfYear);
        calendar2.set(1, year);
        TextView editText = this.etDob;
        editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        this.selectedDob = simpleDateFormat.format(calendar2.getTime());
    }


    private String formatDate(String dob) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(new SimpleDateFormat(this.receivedDateFormat, Locale.US).parse(dob));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json",getContext());
//        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.countryList.add(new Country(((Integer) jsonObject.get("Id")).intValue(), jsonObject.getString("Name")));
            this.countryMap.put((Integer) jsonObject.get("Id"), jsonObject.getString("Name"));
            this.countryListForSpinner.add(jsonObject.getString("Name"));
        }
        this.countrySpinner.setItems(this.countryListForSpinner);
        this.countrySpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            public final void onItemSelected(int i, Object obj, int i2, Object obj2) {
                PersonalInformationFragment.this.lambda$fetchCountries$4$PersonalInformationFragment(i, (String) obj, i2, (String) obj2);
            }
        });
    }

    public void lambda$fetchCountries$4$PersonalInformationFragment(int i, String s, int selectedIndex, String selectedItem) {
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

    private void save(UserInfo userInfo) {
        String firstName = this.etFirstName.getText().toString();
        if (firstName.isEmpty()) {
            this.fnameErrorTv.setText("Please enter first name");
            this.fnameErrorTv.setVisibility(View.VISIBLE);
        } else if (firstName.length() < 3) {
            this.fnameErrorTv.setText("First name is too small. (min 3 chars)");
            this.fnameErrorTv.setVisibility(View.VISIBLE);
        } else {
            this.fnameErrorTv.setVisibility(View.GONE);
            String lastName = this.etLastName.getText().toString();
            if (lastName.isEmpty()) {
                this.lnameErrorTv.setText("Please enter last name");
                this.lnameErrorTv.setVisibility(View.VISIBLE);
            } else if (lastName.length() < 3) {
                this.lnameErrorTv.setText("Last name is too small. (min 3 chars)");
                this.lnameErrorTv.setVisibility(View.VISIBLE);
            } else {
                this.lnameErrorTv.setVisibility(View.GONE);
                String city = this.etCity.getText().toString();
                String city2 = city.isEmpty() ? null : city;
                String postalCode = this.etZipCode.getText().toString();
                String postalCode2 = postalCode.isEmpty() ? null : postalCode;
                if (this.selectedCountry == 0) {
                    this.countryErrorTv.setVisibility(View.VISIBLE);
                    return;
                }
                this.countryErrorTv.setVisibility(View.GONE);
                submitRequest(new AddUserRequest(userInfo.getRole(), userInfo.getEmail(), firstName, userInfo.getId(), lastName, city2, this.selectedCountry, this.selectedDob, postalCode2));
            }
        }
    }

    private void submitRequest(AddUserRequest addUserRequest) {
        RestClient.getInstance(getContext()).createUser(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),addUserRequest, "edit-user").enqueue(new CustomCallBack<ResponseBody>(getContext(), null) {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    UiUtil.showToast(PersonalInformationFragment.this.getContext(), "Updated");
                    Bundle b=new Bundle();
                    b.putString(Constant.CATEGORY,"profile");
                    b.putString(Constant.ACTION,"update");
                    SplashScreenActivity.sendEvent("profile_update",b);
                } else {
                    UiUtil.showToast(PersonalInformationFragment.this.getContext(), "Error while updating");
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("error", t.toString());
            }
        });
    }
}
