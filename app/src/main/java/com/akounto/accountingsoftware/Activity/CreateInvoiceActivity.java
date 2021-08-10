package com.akounto.accountingsoftware.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.akounto.accountingsoftware.R;
import com.skydoves.powerspinner.PowerSpinnerView;

public class CreateInvoiceActivity extends AppCompatActivity {
    PowerSpinnerView powerSpinnerView;
    TextView resetFilterTextView;
    TextView titleTextView;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongViewCast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        this.resetFilterTextView = findViewById(R.id.rightTextView);
        this.titleTextView = findViewById(R.id.pageTitle);
        this.resetFilterTextView.setVisibility(View.VISIBLE);
        this.titleTextView.setText("Create Invoice");
        this.powerSpinnerView = findViewById(R.id.powerSpinner_preference);
    }
}
