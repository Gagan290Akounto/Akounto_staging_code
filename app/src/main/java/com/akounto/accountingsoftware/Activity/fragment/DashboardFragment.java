package com.akounto.accountingsoftware.Activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddCustomersActivity;
import com.akounto.accountingsoftware.Activity.AddVendorActivity;
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.Activity.CreatingProductsAndServices;
import com.akounto.accountingsoftware.Activity.FragmentReportTest;
import com.akounto.accountingsoftware.Activity.Type;
import com.akounto.accountingsoftware.adapter.DashboardItemAdapter;
import com.akounto.accountingsoftware.response.SignInResponse;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.UiUtil;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    View.OnClickListener clickListener;
    FloatingActionButton fab;
    boolean isPopupOpen = false;
    ArrayList personImages;
    ArrayList personNames = new ArrayList(Arrays.asList("Person 1", "Person 2", "Person 3", "Person 4"));
    RelativeLayout relativeLayoutplusbutton;
    Float rotationDuration = Float.valueOf(135.0f);
    SignInResponse signInResponse;
    private TabLayout tabLayout;
    TextView userName;
    View view;
    private ViewPager viewPager;

    public DashboardFragment() {
        Integer valueOf = Integer.valueOf(R.drawable.ic_dashboard_item);
        this.personImages = new ArrayList(Arrays.asList(valueOf, valueOf, valueOf, valueOf));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        this.clickListener = this;
        this.signInResponse = UiUtil.getUserDetails(getActivity());
        inItUi();
        return this.view;
    }

    private void inItUi() {
        TextView textView = this.view.findViewById(R.id.HelloNorman);
        this.userName = textView;
        try {
            textView.setText("Hello " + this.signInResponse.getFirstName() + " !");
        } catch (Exception e) {
        }
        this.fab = this.view.findViewById(R.id.fab);
        this.tabLayout = this.view.findViewById(R.id.tabLayout);

        ViewPager viewPager2 = this.view.findViewById(R.id.viewpager);
        this.viewPager = viewPager2;
        setupViewPager(viewPager2);
        this.tabLayout.setupWithViewPager(this.viewPager);
        RecyclerView recyclerView = this.view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new DashboardItemAdapter(getActivity(), this.personNames, this.personImages, this));
        RelativeLayout relativeLayout = this.view.findViewById(R.id.plus);
        this.relativeLayoutplusbutton = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DashboardFragment.this.startActivity(new Intent(DashboardFragment.this.getActivity(), CreateBillActivity.class));
            }
        });
        this.view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DashboardFragment.this.lambda$inItUi$6$DashboardFragment(view);
            }
        });
        this.view.findViewById(R.id.gotoDashboardButton).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DashboardFragment.this.lambda$inItUi$7$DashboardFragment(view);
            }
        });
        this.view.findViewById(R.id.completeProfile).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DashboardFragment.this.lambda$inItUi$8$DashboardFragment(view);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void lambda$inItUi$6$DashboardFragment(View click) {
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.dashboard_floating_dialog, null);
        PopupWindow popUp = new PopupWindow(popupView, -2, -2);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAtLocation(getActivity().getWindow().getDecorView(), 80, 0, 350);
        View container = (View) popUp.getContentView().getParent();
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= 2;
        p.dimAmount = 0.3f;
        ((WindowManager) getActivity().getSystemService("window")).updateViewLayout(container, p);
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        popupView.findViewById(R.id.estimateLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$0$DashboardFragment(popUp, view));
        popupView.findViewById(R.id.invoiceLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$1$DashboardFragment(popUp, view));
        popupView.findViewById(R.id.billLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$2$DashboardFragment(popUp, view));
        popupView.findViewById(R.id.customerLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$3$DashboardFragment(popUp, view));
        popupView.findViewById(R.id.productLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$4$DashboardFragment(popUp, view));
        popupView.findViewById(R.id.vendorLL).setOnClickListener(view -> DashboardFragment.this.lambda$null$5$DashboardFragment(popUp, view));
        popUp.setOnDismissListener(() -> ViewCompat.animate(DashboardFragment.this.fab).rotationBy(DashboardFragment.this.rotationDuration.floatValue()).withLayer().setDuration(300).setInterpolator(interpolator).start());
        ViewCompat.animate(this.fab).rotationBy(this.rotationDuration.floatValue()).withLayer().setDuration(300).setInterpolator(interpolator).start();
    }

    public void lambda$null$0$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(CommonInvoiceActivity.buildIntentWithData(getContext(), Type.ESTIMATES.name(), null));
        popUp.dismiss();
    }

    public void lambda$null$1$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(CommonInvoiceActivity.buildIntentWithData(getContext(), Type.INVOICES.name(), null));
        popUp.dismiss();
    }

    public void lambda$null$2$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(new Intent(getActivity(), CreateBillActivity.class));
        popUp.dismiss();
    }

    public void lambda$null$3$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(new Intent(getActivity(), AddCustomersActivity.class));
        popUp.dismiss();
    }

    public void lambda$null$4$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(new Intent(getActivity(), CreatingProductsAndServices.class));
        popUp.dismiss();
    }

    public void lambda$null$5$DashboardFragment(PopupWindow popUp, View v) {
        startActivity(new Intent(getActivity(), AddVendorActivity.class));
        popUp.dismiss();
    }

    public void lambda$inItUi$7$DashboardFragment(View v) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, HomeDashboardFragment.class);
    }

    public void lambda$inItUi$8$DashboardFragment(View v) {
        AddFragments.addFragmentToDrawerActivity(getActivity(), null, ProfileFragment.class);
    }

    public void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HomeDashboardFragment(), "HOME");
        adapter.addFragment(new ActivitiesDashboardFragment(), "Activities");
        viewPager2.setAdapter(adapter);
    }

    public void onClick(View v) {
        int id = v.getId();
        Log.d("Akram Id : ", v.getTag() + "");
        int position = ((Integer) v.getTag()).intValue();
        if (position == 0) {
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, PurchaseFragment.class);
        } else if (position == 1) {
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, SalesFragment.class);
        } else if (position == 2) {
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, AccountingFragment.class);
        } else if (position == 3) {
            AddFragments.addFragmentToDrawerActivity(getActivity(), null, FragmentReportTest.class);
        }
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
