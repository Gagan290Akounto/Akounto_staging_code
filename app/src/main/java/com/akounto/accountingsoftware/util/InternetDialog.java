package com.akounto.accountingsoftware.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import com.akounto.accountingsoftware.R;

public class InternetDialog {
    private Context context;

    public InternetDialog() {
    }

    public InternetDialog(Context context2) {
        this.context = context2;
    }

    public void showNoInternetDialog(Context context2) {
        final Dialog dialog1 = new Dialog(context2, R.style.df_dialog);
        dialog1.setContentView(R.layout.dialog_no_internet);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        UiUtil.cancelProgressDialogue();
        dialog1.show();
    }

    public boolean getInternetStatus(Context context2) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetwork = ((ConnectivityManager) context2.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            showNoInternetDialog(context2);
        }
        return isConnected;
    }
}
