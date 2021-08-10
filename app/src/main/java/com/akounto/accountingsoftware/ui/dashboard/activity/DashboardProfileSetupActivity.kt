package com.akounto.accountingsoftware.ui.dashboard.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.ui.base.activity.BaseActivity
import com.akounto.accountingsoftware.ui.dashboard.fragments.AddBusinessDetailsFragment
import com.akounto.accountingsoftware.util.CommonMethod


class DashboardProfileSetupActivity : BaseActivity() {
    var flDashboardProfileSetup: FrameLayout? = null
    private var linBack: LinearLayout? = null
    private var tvToolbarTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_profile_setup)

        initView()
        clickListener()
        setInitialData()
    }

    private fun initView() {
        flDashboardProfileSetup = findViewById(R.id.flDashboardProfileSetup)
        linBack = findViewById(R.id.linBack)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
    }

    private fun clickListener() {
        linBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setInitialData() {
        setToolbarTitle("First Time Setup");

        flDashboardProfileSetup?.let {
            CommonMethod.replaceFragmentNotToStack(
                this, it, AddBusinessDetailsFragment(), false
            )
        }
    }

    fun setToolbarTitle(toolbarTitle: String) {
        if (!TextUtils.isEmpty(toolbarTitle)) {
            tvToolbarTitle?.text = toolbarTitle
        } else {
            tvToolbarTitle?.text = "First Time Setup"
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            0, 1 -> {
                finish()
            }
            else -> {
                supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.flDashboardProfileSetup)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}