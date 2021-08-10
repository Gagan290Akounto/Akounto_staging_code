package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Invoice.ViewInvoice;
import com.akounto.accountingsoftware.Activity.fragment.GetShareLinkFragment;
import com.akounto.accountingsoftware.Activity.fragment.SendEstimateHomeFragment;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.request.GetMailRequest;
import com.akounto.accountingsoftware.response.mailreq.MailReqDetails;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class SendEstimateActivity extends AppCompatActivity {
    ImageView backButton;
    GetShareLinkFragment getShareLinkFragment = new GetShareLinkFragment();
    SendEstimateHomeFragment sendEstimateHomeFragment = new SendEstimateHomeFragment();
    private TabLayout tabLayout;
    TextView titleTextView;
    private ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_estimate);
        this.viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout2 = findViewById(R.id.tabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.setupWithViewPager(this.viewPager);
        setupViewPager(this.viewPager);
        this.titleTextView = findViewById(R.id.pageTitle);
        this.backButton = findViewById(R.id.backButton);
        this.titleTextView.setText("Send this Mail");
        this.backButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SendEstimateActivity.this.lambda$onCreate$0$SendEstimateActivity(view);
            }
        });
        getMailReceipant();
    }

    public /* synthetic */ void lambda$onCreate$0$SendEstimateActivity(View click) {
        finish();
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(this.sendEstimateHomeFragment, "Send estimation");
        adapter.addFragment(this.getShareLinkFragment, "Get share link");
        viewPager2.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getItem(int position) {
            return this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }

    private void getMailReceipant() {
        GetMailRequest req = new GetMailRequest();
        req.setId(ViewInvoice.receivedData.getId());
        req.setHeadTrnsactionId(ViewInvoice.receivedData.getHeadTransactionCustomerId());
        req.setInvoiceNo(PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW);
        req.setMailType(1);
        RestClient.getInstance(this).getMailReceipant(Constant.X_SIGNATURE,"Bearer " + UiUtil.getAcccessToken(getApplicationContext()),UiUtil.getComp_Id(getApplicationContext()),req).enqueue(new CustomCallBack<MailReqDetails>(this, null) {
            public void onResponse(Call<MailReqDetails> call, Response<MailReqDetails> response) {
                super.onResponse(call, response);
                Log.d("MailReqDetails----", new Gson().toJson(response.body().getData()));
                SendEstimateActivity.this.sendEstimateHomeFragment.update(response.body().getData());
                SendEstimateActivity.this.getShareLinkFragment.update(response.body().getData());
            }

            public void onFailure(Call<MailReqDetails> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("errorerror-bill", t.toString());
            }
        });
    }
}
