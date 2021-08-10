package com.akounto.accountingsoftware.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.request.RegisterBusiness;
import com.akounto.accountingsoftware.request.User;
import com.akounto.accountingsoftware.util.LocalManager;
import java.util.ArrayList;
import java.util.List;

public class GetStartedActivity extends AppCompatActivity {
    Animation animationLeftToRight;
    Animation animationRightToLeft;
    String businessType;
    int businessTypeId;
    EditText businessTypeET;
    TextView continueButton;
    String dealsWith;
    EditText dealsWithET;
    int dealsWithId;
    EditText editTextMyNameIs;
    RegisterBusiness registerBusiness;
    RelativeLayout relativeLayoutBusinessType;
    RelativeLayout relativeLayoutDealsWith;
    RelativeLayout relativeLayoutMyName;
    RelativeLayout relativeLayoutrunABusiness;
    EditText runBusinessET;
    Spinner typeBusinessSpinner;
    Spinner dealsWithBusinessSpinner;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        this.registerBusiness = LocalManager.getInstance().getRegisterBusiness();
        this.relativeLayoutMyName = findViewById(R.id.relMyNameIs);
        this.relativeLayoutrunABusiness = findViewById(R.id.relativeLayoutBusiness);
        this.relativeLayoutBusinessType = findViewById(R.id.relativeTypeBusiness);
        this.continueButton = findViewById(R.id.continueButton);
        settypeBusinessSpinner();
        this.relativeLayoutDealsWith = findViewById(R.id.dealsWithRL);
        this.editTextMyNameIs = findViewById(R.id.myNameIsET);
        this.runBusinessET = findViewById(R.id.runABusinessET);
        this.businessTypeET = findViewById(R.id.typeBusinessET);
        this.dealsWithET = findViewById(R.id.dealsWithET);
        this.animationRightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        this.animationLeftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        this.editTextMyNameIs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GetStartedActivity.this.relativeLayoutrunABusiness.setVisibility(View.VISIBLE);
                GetStartedActivity.this.animationLeftToRight.reset();
                GetStartedActivity.this.relativeLayoutrunABusiness.clearAnimation();
                GetStartedActivity.this.relativeLayoutrunABusiness.startAnimation(GetStartedActivity.this.animationLeftToRight);
                return false;
            }
        });
        this.runBusinessET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GetStartedActivity.this.relativeLayoutBusinessType.setVisibility(View.VISIBLE);
                GetStartedActivity.this.animationLeftToRight.reset();
                GetStartedActivity.this.relativeLayoutBusinessType.clearAnimation();
                GetStartedActivity.this.relativeLayoutBusinessType.startAnimation(GetStartedActivity.this.animationLeftToRight);
                GetStartedActivity.this.relativeLayoutDealsWith.setVisibility(View.VISIBLE);
                GetStartedActivity.this.continueButton.setVisibility(View.VISIBLE);
                GetStartedActivity.this.relativeLayoutDealsWith.startAnimation(GetStartedActivity.this.animationLeftToRight);
                GetStartedActivity.this.relativeLayoutDealsWith.startAnimation(GetStartedActivity.this.animationLeftToRight);
                return false;
            }
        });
        this.businessTypeET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GetStartedActivity.this.relativeLayoutDealsWith.setVisibility(View.VISIBLE);
                GetStartedActivity.this.continueButton.setVisibility(View.VISIBLE);
                GetStartedActivity.this.animationLeftToRight.reset();
                GetStartedActivity.this.relativeLayoutDealsWith.clearAnimation();
                GetStartedActivity.this.relativeLayoutDealsWith.startAnimation(GetStartedActivity.this.animationLeftToRight);
                return false;
            }
        });
        this.dealsWithET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GetStartedActivity.this.startActivity(new Intent(GetStartedActivity.this, HangTightActivity.class));
                return false;
            }
        });
        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                GetStartedActivity.this.lambda$onCreate$0$GetStartedActivity(view);
            }
        });
    }

    public void lambda$onCreate$0$GetStartedActivity(View click) {
        User user = this.registerBusiness.getUser();
        user.setFirstName(this.editTextMyNameIs.getText().toString());
        this.registerBusiness.setUser(user);
        this.registerBusiness.setBusinessName(this.runBusinessET.getText().toString());
        this.registerBusiness.setBusinessEntity(this.businessType);
        this.registerBusiness.setBusinessEntityId(this.businessTypeId + 1);
        this.registerBusiness.setIndustryTypeId(this.dealsWithId + 1);
        this.registerBusiness.setIndustryTypeName(this.dealsWith);
        this.registerBusiness.setCountry(1);
        this.registerBusiness.setBusinessCurrency("USD");
        LocalManager.getInstance().setRegisterBusiness(this.registerBusiness);
        startActivity(new Intent(this, HangTightActivity.class));
    }

    @SuppressLint("ResourceType")
    private void settypeBusinessSpinner() {
        this.typeBusinessSpinner = findViewById(R.id.typeBusinessSpinner);
        final List<String> finalCategories = new ArrayList<>();
        finalCategories.add("LLC");
        finalCategories.add("Non Profit Organization");
        finalCategories.add("Partnership or LLP");
        finalCategories.add("Proprietorship");
        finalCategories.add("Others");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, finalCategories);
        dataAdapter.setDropDownViewResource(17367049);
        this.typeBusinessSpinner.setAdapter(dataAdapter);
        this.typeBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                GetStartedActivity.this.businessTypeId = pos;
                GetStartedActivity.this.businessType = finalCategories.get(pos);
                Log.e("pospospos", finalCategories.get(pos));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.dealsWithBusinessSpinner = findViewById(R.id.dealsWithBusinessSpinner);
        final List<String> finalCategories2 = new ArrayList<>();
        finalCategories2.add("Book-keeping");
        finalCategories2.add("Others");
        finalCategories2.add("Construction");
        finalCategories2.add("Financial Services");
        finalCategories2.add("Health Services");
        finalCategories2.add("Information Technology Services");
        finalCategories2.add("Marketing Services");
        finalCategories2.add("Medical Services");
        finalCategories2.add("Non Profit Organizations");
        finalCategories2.add("Photography and creative services");
        finalCategories2.add("Real Estate");
        finalCategories2.add("Retailers and Resellers");
        finalCategories2.add("Others");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, finalCategories2);
        dataAdapter.setDropDownViewResource(17367049);
        this.dealsWithBusinessSpinner.setAdapter(dataAdapter2);
        this.dealsWithBusinessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
                GetStartedActivity.this.dealsWithId = pos;
                GetStartedActivity.this.dealsWith = finalCategories2.get(pos);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
