package com.akounto.accountingsoftware.ui.dashboard.activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.akounto.accountingsoftware.Activity.Dashboard.MoreFragment
import com.akounto.accountingsoftware.Activity.Invoice.InvoiceList
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.ui.base.activity.BaseActivity
import com.akounto.accountingsoftware.ui.dashboard.fragments.DashboardFragment
import com.akounto.accountingsoftware.util.AppSingle

class DashboardMainActivity : BaseActivity() {
    //TextView helloNorman;
    //Context mContext;
    //SignInResponse signInResponse;
    var fl: FrameLayout? = null
    var linHome: LinearLayout? = null
    var linInvoice: LinearLayout? = null
    var linMore: LinearLayout? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_main)

        // mContext = this;
        // this.signInResponse = UiUtil.getUserDetails(this);

        // this.helloNorman = findViewById(R.id.tv_company_name);

        //findViewById(R.id.bell).setOnClickListener(view -> DashboardMainActivity.this.lambda$onCreate$5$DashboardActivity(view));
        //findViewById(R.id.notification).setOnClickListener(view -> DashboardMainActivity.this.lambda$onCreate$6$DashboardActivity(view));
        initView()
        clickListener()
        setInitialData()
    }

    private fun setInitialData() {
        addFragmentNotToStack(DashboardFragment())
    }

    private fun initView() {
        fl = findViewById(R.id.content_frame)
        linHome = findViewById(R.id.footer_home)
        linInvoice = findViewById(R.id.footer_invoice)
        linMore = findViewById(R.id.footer_more)
    }

    private fun clickListener() {
        linHome!!.setOnClickListener { view: View? -> addFragmentNotToStack(DashboardFragment()) }
        linInvoice!!.setOnClickListener { view: View? ->
            //addFragmentNotToStack(new InvoicesFragment());
            startActivity(Intent(applicationContext, InvoiceList::class.java))
        }
        linMore!!.setOnClickListener { view: View? -> addFragmentNotToStack(MoreFragment()) }
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        /*fl = findViewById(R.id.content_frame);
//        addFragmentNotToStack(new HomeDashboardFragment());
        addFragmentNotToStack(new DashboardFragment());*/
        //findViewById(R.id.settingLayout).setOnClickListener(view -> DashboardActivity.this.lambda$onCreate$8$DashboardActivity(view));
        /*try {
            helloNorman.setText(AppSingle.getInstance().getComp_name());
        } catch (Exception e) {
        }*/
    }

    fun addFragmentNotToStack(fragment: Fragment) {
        try {
            if (fl == null) {
                Toast.makeText(
                    AppSingle.getInstance(), "No Parent Attached to FlowOrganizer",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            fragment.arguments = null
            ft.add(fl!!.id, fragment, fragment.javaClass.name)
            if (false) ft.addToBackStack(fragment.javaClass.name) else clearBackStack()
            ft.commit()
        } catch (e: Exception) {
        }
    }

    fun hideKeyboard() {
        try {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun clearBackStack() {
        /* try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }*/
    } /* public void addFragmentToStack(Fragment fragment) {
        try {
            if (fl == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(null);
            ft.add(fl.getId(), fragment, fragment.getClass().getName());
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        } catch (Exception e) {
        }
    }*/
    /* public void changename(String name) {
        try {
            helloNorman.setText(name);
        } catch (Exception e) {
        }
    }*/
    /* public void lambda$onCreate$5$DashboardActivity(View click) {
        //startActivity(new Intent(getApplicationContext(), NotifcationActivity.class));
        UiUtil.showToast(mContext, "Coming Soon");

    }

    public void lambda$onCreate$6$DashboardActivity(View clickLogout) {
        //startActivity(new Intent(this, NotifcationActivity.class));
        AddFragments.addFragmentToDrawerActivity(this, null, MainMenu.class);
    }

    public void lambda$onCreate$8$DashboardActivity(View v) {
        startActivity(new Intent(this, SettingActivity.class));
    }*/
    /* public void addFragmentBackAllow(Fragment fragment) {
        try {
            if (fl == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(null);
            ft.add(fl.getId(), fragment, fragment.getClass().getName());

            ft.addToBackStack(fragment.getClass().getName());

            ft.commit();
        } catch (Exception e) {
        }
    }*/
}