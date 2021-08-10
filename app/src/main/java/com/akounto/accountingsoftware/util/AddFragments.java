package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Activity.DashboardActivity;

public class AddFragments {
    public static void addFragmentToDrawerActivity(Context context, Bundle bundle, Class<? extends Fragment> fragmentClass) {
        if (context instanceof DashboardActivity) {
            addFragmentActivity(context, bundle, fragmentClass);
        }
    }
    public static void addFragmentToDrawerActivity(Context context, Bundle bundle, Class<? extends Fragment> fragmentClass,boolean isBackAllow) {
        if (context instanceof DashboardActivity) {
            ((DashboardActivity) context).addFragmentBackAllow(Fragment.instantiate(context, fragmentClass.getName(), bundle));
        }
    }
    public static void changgeName(Context context, String name) {
        if (context instanceof DashboardActivity) {
            ((DashboardActivity) context).changename(name);
        }
    }

    private static void addFragmentActivityTotack(Context context, Bundle bundle, Class<? extends Fragment> fragmentClass) {
        if (context instanceof DashboardActivity) {
            ((DashboardActivity) context).addFragmentToStack(Fragment.instantiate(context, fragmentClass.getName(), bundle));
        }
    }

    private static void addFragmentActivity(Context context, Bundle bundle, Class<? extends Fragment> fragmentClass) {
        if (context instanceof DashboardActivity) {
            ((DashboardActivity) context).addFragmentToStack(Fragment.instantiate(context, fragmentClass.getName(), bundle));
        }
    }
}
