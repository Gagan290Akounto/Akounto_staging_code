package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.SettingsFragment;

public class SettingActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ((TextView) findViewById(R.id.pageTitle)).setText("Settings");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.activity_open_translate, R.anim.activity_close_scale, R.anim.activity_open_translate, R.anim.activity_close_scale);
        SettingsFragment newFragment = new SettingsFragment();
        ft.add(R.id.fragment_container, newFragment, newFragment.getClass().getSimpleName());
        ft.addToBackStack(newFragment.getClass().getSimpleName());
        ft.commit();
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
