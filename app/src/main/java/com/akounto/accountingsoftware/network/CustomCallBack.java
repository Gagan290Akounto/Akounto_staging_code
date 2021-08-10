package com.akounto.accountingsoftware.network;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallBack<T> implements Callback<T> {
    Context context;

    public CustomCallBack(Context context2, String msg) {
        this.context = context2;
        try {
            ((Activity) context2).getWindow().setFlags(16, 16);
            UiUtil.showProgressDialogue(context2, "", TextUtils.isEmpty(msg) ? "Loading..." : msg);
        } catch (Exception e) {

        }
    }

    public void onResponse(Call<T> call, Response<T> response) {
        UiUtil.cancelProgressDialogue();
        try {
            ((Activity) this.context).getWindow().clearFlags(16);
            Log.d("*****Response*****", response.toString());
            if (response.code() == 401) {
                UiUtil.showToast(this.context, "Session time out");
                UiUtil.addLoginToSharedPref(this.context, false);
                UiUtil.moveToSignUp(this.context);
                ((Activity) this.context).finish();
            }
        } catch (Exception e) {

        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        ((Activity) this.context).getWindow().clearFlags(16);
        UiUtil.cancelProgressDialogue();
        Log.e("*****Error*****", t.toString());
    }
}
