package com.akounto.accountingsoftware.util;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Created by root on 12-12-2016.
 */

public class FlowOrganizer {

    private static FlowOrganizer _app;
    private static FrameLayout frameParent;

    public static FlowOrganizer getInstance() {
        try {
            if (_app == null) {
                _app = new FlowOrganizer();
            }
        } catch (Exception e) {
        }
        return _app;
    }

    public void initParentFrame(FrameLayout frameParent) {
        this.frameParent = frameParent;
    }

    private FlowOrganizer() {
        try {
            AppSingle.getInstance().getActivity().hideKeyboard();
        } catch (Exception e) {
        }
    }

    public void add(Fragment fragment) {
        try {
            add(fragment, false, null);
        } catch (Exception e) {
        }
    }

    public void add(Fragment fragment, boolean isToAddBack) {
        try {
            add(fragment, isToAddBack, null);
        } catch (Exception e) {
        }
    }

    public void add(Fragment fragment, boolean isToAddBack, Bundle bundle) {
        try {
            if (frameParent == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(bundle);
            ft.add(frameParent.getId(), fragment, fragment.getClass().getName());
            if (isToAddBack)
                ft.addToBackStack(fragment.getClass().getName());
            else
                clearBackStack();
            ft.commit();
        } catch (Exception e) {
        }
    }

    public void replace(Fragment fragment) {
        try {
            replace(fragment, false, null);
        } catch (Exception e) {
        }
    }

    public void replace(Fragment fragment, boolean isToAddBack) {
        try {
            replace(fragment, isToAddBack, null);
        } catch (Exception e) {
        }
    }

    public void replace(Fragment fragment, boolean isToAddBack, Bundle bundle) {
        try {
            if (frameParent == null) {
                Toast.makeText(AppSingle.getInstance(), "No Parant Attached to FlowOrganizer", Toast.LENGTH_SHORT).show();
                return;
            }
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(bundle);
            ft.replace(frameParent.getId(), fragment, fragment.getClass().getName());
            if (isToAddBack)
                ft.addToBackStack(fragment.getClass().getName());
            else
                clearBackStack();
            ft.commit();
        } catch (Exception e) {
        }
    }

    public boolean hasNoMoreBacks() {
        FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count < 1)
            return true;
        else
            return false;
    }

    public void clearBackStack() {
        try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }

    public void popUpBackToMain() {
        try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            int size = fm.getBackStackEntryCount();
            for (int i = 0; i < size; i++) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }

    public void popUpBackTo(int skipNoOfFragment) {
        try {
            FragmentManager fm = AppSingle.getInstance().getActivity().getSupportFragmentManager();
            int size = fm.getBackStackEntryCount();
            if (skipNoOfFragment > size)
                return;
            else
                size = skipNoOfFragment;
            for (int i = 0; i < size; ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }

}
