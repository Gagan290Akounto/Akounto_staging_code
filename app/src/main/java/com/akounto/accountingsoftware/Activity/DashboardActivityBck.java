package com.akounto.accountingsoftware.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.HomeDashboardFragment;
import com.akounto.accountingsoftware.adapter.DashboardItemAdapter;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivityBck extends AppCompatActivity {
    TextView HelloNorman;
    List<String> itemNameList = new ArrayList();

    ArrayList personImages;
    ArrayList personNames = new ArrayList(Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4"));
    RelativeLayout relativeLayoutplusbutton;
    SignInResponse signInResponse;

    public DashboardActivityBck() {
        Integer valueOf = Integer.valueOf(R.drawable.ic_dashboard_item);
        this.personImages = new ArrayList(Arrays.asList(valueOf, valueOf, valueOf, valueOf));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_parent_layout);
        AddFragments.addFragmentToDrawerActivity(this, null, HomeDashboardFragment.class);
        this.signInResponse = UiUtil.getUserDetails(this);
        TextView textView = findViewById(R.id.HelloNorman);
        this.HelloNorman = textView;
        textView.setText("Hello " + this.signInResponse.getFirstName() + " !");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new DashboardItemAdapter(this, this.personNames, this.personImages, null));
        RelativeLayout relativeLayout = findViewById(R.id.plus);
        this.relativeLayoutplusbutton = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DashboardActivityBck.this.startActivity(new Intent(DashboardActivityBck.this, CreateBillActivity.class));
            }
        });
    }


    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

}
