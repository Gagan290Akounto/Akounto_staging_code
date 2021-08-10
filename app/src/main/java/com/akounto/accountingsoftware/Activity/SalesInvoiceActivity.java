package com.akounto.accountingsoftware.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.InvoiceDetails;

public class SalesInvoiceActivity extends AppCompatActivity{


    public static Intent buildIntent(Context context, String type) {
        Intent intent = new Intent(context, SalesInvoiceActivity.class);
        intent.putExtra("typeEnum", type);
        return intent;
    }

    public static Intent buildIntentWithData(Context context, String type, InvoiceDetails invoiceDetails) {
        Intent intent = new Intent(context, SalesInvoiceActivity.class);
        intent.putExtra("typeEnum", type);
        intent.putExtra("invoiceDetails", invoiceDetails);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_invoice);
    }
}
