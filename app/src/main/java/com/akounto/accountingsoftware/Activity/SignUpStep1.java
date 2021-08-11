package com.akounto.accountingsoftware.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CountryData;
import com.akounto.accountingsoftware.Listner.IDialogListClickListener;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Repository.LoginRepo;
import com.akounto.accountingsoftware.adapter.AdapterDialogListItem;
import com.akounto.accountingsoftware.util.JsonUtils;
import com.akounto.accountingsoftware.util.UiConstants;
import com.akounto.accountingsoftware.util.UiUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpStep1 extends AppCompatActivity {

    private int selectedPos = 0;
    private ArrayList<CountryData> countryData = new ArrayList<>();
    private EditText f_n, l_n, p_n;
    private TextView f_n_error, l_n_error,p_n_error,edt_phone_number_tooltip,phone_code_tv;
    private ImageView c_care;
    private Context mContext;
    private String countryCode = "US";
    private ImageView couFlagSpinner,couFlagSpinner1;
    private LinearLayout p_n_ll;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signupsetp1);
        mContext=this;
        try {
            f_n = findViewById(R.id.first_name);
            l_n = findViewById(R.id.last_name);
            p_n = findViewById(R.id.edt_phone_number);
            f_n_error = findViewById(R.id.first_name_error);
            l_n_error = findViewById(R.id.last_name_error);
            p_n_error = findViewById(R.id.edt_phone_number_error);
            p_n_ll = findViewById(R.id.phone_number_ll);
            edt_phone_number_tooltip = findViewById(R.id.edt_phone_number_tooltip);
            c_care = findViewById(R.id.cusmoter_care);
            couFlagSpinner = findViewById(R.id.phone_code_spiner);
            couFlagSpinner1 = findViewById(R.id.phone_code_spiner1);
            phone_code_tv = findViewById(R.id.phone_code_tv);
            setfilter(countryCode);
            Picasso.with(mContext).load(Constant.IMG_URL+"US.png").into(couFlagSpinner);
            c_care.setOnClickListener(new View.OnClickListener() {
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
            findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset();
                    if (isValid()) {
                        Bundle b=new Bundle();
                        b.putString(Constant.CATEGORY,"sign_up");
                        b.putString(Constant.ACTION,"personal_info");
                        b.putString(Constant.PHONE,p_n.getText().toString());
                        SplashScreenActivity.sendEvent("sign_up_personal_info",b);
                        Intent i = new Intent(SignUpStep1.this, SignUpStep2.class);
                        i.putExtra(UiConstants.FIRST_NAME, f_n.getText().toString());
                        i.putExtra(UiConstants.LAST_NAME, l_n.getText().toString());
                        i.putExtra(UiConstants.PHONE_NUMBER, p_n.getText().toString());
                        i.putExtra(UiConstants.PHONE_CODE, phone_code_tv.getText().toString());
                        startActivity(i);
                    }
                }
            });

            fetchCountries();
            couFlagSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openListDialog("Select Country", selectedPos, countryData, new IDialogListClickListener() {
                        @Override
                        public void onClick(CountryData data, int selectedPos) {
                            countryCode = data.getCountryCode().trim();
                            phone_code_tv.setText("+" + data.getPhoneCode());
                            Picasso.with(mContext).load(Constant.IMG_URL + data.getCountryCode() + ".png").into(couFlagSpinner);
                            setfilter(countryCode);
                        }
                    });
                }
            });
            couFlagSpinner1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openListDialog("Select Country", selectedPos, countryData, new IDialogListClickListener() {
                        @Override
                        public void onClick(CountryData data, int selectedPos) {
                            countryCode = data.getCountryCode().trim();
                            phone_code_tv.setText("+" + data.getPhoneCode());
                            Picasso.with(mContext).load(Constant.IMG_URL + data.getCountryCode() + ".png").into(couFlagSpinner);
                            setfilter(countryCode);
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }
    private void fetchCountries() throws JSONException {
        String loadJSONFromAsset = JsonUtils.loadJSONFromAsset("country.json", getApplicationContext());
        Objects.requireNonNull(loadJSONFromAsset);
        JSONArray jsonArray = new JSONArray(loadJSONFromAsset);
        CountryData data;
        for (int i = 0; i < jsonArray.length(); i++) {
            data = new CountryData();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            data.setId(jsonObject.get("Id").toString());
            data.setName(jsonObject.getString("Name"));
            data.setCurrency(jsonObject.getString("Currency"));
            data.setPhoneCode(jsonObject.getString("PhoneCode"));
            data.setCountryCode(jsonObject.getString("CountryCode"));
            countryData.add(data);
        }

    }
    private boolean isValid() {
        boolean response = true;
        String focusfield = "";
        if (f_n.getText().toString().trim().length() == 0) {
            f_n_error.setVisibility(View.VISIBLE);
            f_n.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                f_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (f_n.getText().toString().trim().length() < 2) {
            //UiUtil.showToast(this, "Please enter first name min two character");
            f_n_error.setVisibility(View.VISIBLE);
            f_n_error.setText("Please enter first name min 2 character");
            f_n.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                f_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        if (l_n.getText().toString().trim().length() == 0) {
            //UiUtil.showToast(this, "Please enter last name");
            l_n_error.setVisibility(View.VISIBLE);
            l_n.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                l_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (l_n.getText().toString().trim().length() < 2) {
            //UiUtil.showToast(this, "Please enter last name min two character");
            l_n_error.setVisibility(View.VISIBLE);
            l_n_error.setText("Please enter last name min 2 character");
            l_n.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                l_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }

        String phoneNumber = p_n.getText().toString().trim();
        if (phoneNumber.length() == 0) {
            //UiUtil.showToast(this, "Please enter valid phone number");
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                p_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        } else if (((countryCode.equalsIgnoreCase("US") || countryCode.equalsIgnoreCase("CA")) && phoneNumber.length() != 10)
                || (!countryCode.equalsIgnoreCase("US") && !countryCode.equalsIgnoreCase("CA") && (phoneNumber.length() < 10 || phoneNumber.length() > 12))) {
            p_n_error.setVisibility(View.VISIBLE);
            p_n_ll.setBackgroundResource(R.drawable.error);
            if (focusfield.equalsIgnoreCase("")) {
                p_n.requestFocus();
                focusfield = "1";
            }
            response = false;
        }
        return response;
    }
    private void reset() {
        try {
            f_n.setBackgroundResource(R.drawable.sign_in_input);
            l_n.setBackgroundResource(R.drawable.sign_in_input);
            p_n_ll.setBackgroundResource(R.drawable.sign_in_input);

            f_n_error.setVisibility(View.GONE);
            l_n_error.setVisibility(View.GONE);
            p_n_error.setVisibility(View.GONE);
        } catch (Exception e) {
            LoginRepo.prinLogs("" + Log.getStackTraceString(e), 5, "Sign up");
        }
    }

    protected void openListDialog(String title, int selectedPos, final List<CountryData> listData, final IDialogListClickListener listener) {
        com.akounto.accountingsoftware.databinding.MobileCodeDilogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.mobile_code_dilog, null, false);
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        binding.txtViewTitle.setText(title);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayoutManager lLayout = new LinearLayoutManager(mContext);
        RecyclerView rView = binding.rvList;
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        AdapterDialogListItem adapter = new AdapterDialogListItem(mContext, listData, selectedPos);
        rView.setAdapter(adapter);
        adapter.registerOnItemClickListener(new AdapterDialogListItem.IonItemSelect() {
            @Override
            public void onItemSelect(int position) {
                if (listener != null)
                    listener.onClick(listData.get(position), position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void setfilter(String cd) {
        p_n.setText("");
     /*   p_n.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    edt_phone_number_tooltip.setVisibility(View.VISIBLE);
                }else{
                    edt_phone_number_tooltip.setVisibility(View.GONE);
                }
            }
        });*/
        if (countryCode.equals("US") || countryCode.equals("CA")) {
            p_n_error.setText("Contact number should be 10 digit number.");
            edt_phone_number_tooltip.setText("** Number must contain 10 digit without prefixing 0 & 1.\n" +
                    "** Landline number need area code.");
            UiUtil.removeAll(p_n);
            p_n.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            p_n.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0") || x.startsWith("1")) {
                        p_n.setText("");
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        } else {
            p_n_error.setText("Contact number should be 10 to 12 digit number.");
            edt_phone_number_tooltip.setText("** Contact number without prefixing 0.\n" +
                    "** Landline number need area code.");
            UiUtil.removeAll(p_n);
            p_n.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            p_n.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    String x = s.toString();
                    if (x.startsWith("0")) {
                        p_n.setText("");
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        }

    }
}
