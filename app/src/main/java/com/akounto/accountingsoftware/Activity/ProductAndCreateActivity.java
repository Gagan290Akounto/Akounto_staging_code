package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.akounto.accountingsoftware.R;

public class ProductAndCreateActivity extends AppCompatActivity {
    TextView pageTitle;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_and_services);
        inItUi();
    }

    private void inItUi() {
        TextView textView = findViewById(R.id.pageTitle);
        this.pageTitle = textView;
        textView.setText("Products & Services");
    }
}
