package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.akounto.accountingsoftware.R;

public class AdvancedFilterActivity extends AppCompatActivity {
    TextView resetFilterTextView;
    TextView titleTextView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_filter);
        this.resetFilterTextView = findViewById(R.id.rightTextView);
        this.titleTextView = findViewById(R.id.pageTitle);
        this.resetFilterTextView.setVisibility(View.VISIBLE);
        this.titleTextView.setText("Advanced Filter");
    }
}
