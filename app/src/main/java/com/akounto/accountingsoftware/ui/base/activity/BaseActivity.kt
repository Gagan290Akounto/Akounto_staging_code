package com.akounto.accountingsoftware.ui.base.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akounto.accountingsoftware.util.AppSingle

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun clearBackStack() {
        /* try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }*/
    }
}