package com.akounto.accountingsoftware.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.fragment.CompanyAccountFragment;
import com.akounto.accountingsoftware.Activity.fragment.CompanyProfileFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class CompanyProfileActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        this.viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout2 = findViewById(R.id.tabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.setupWithViewPager(this.viewPager);
        setupViewPager(this.viewPager);
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompanyProfileFragment(), "Company Profile");
        adapter.addFragment(new CompanyAccountFragment(), "Company Account");
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
}
