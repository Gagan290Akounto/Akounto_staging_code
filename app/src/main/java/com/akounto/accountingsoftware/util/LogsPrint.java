package com.akounto.accountingsoftware.util;

import android.util.Log;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogsPrint {

    private String msg;
    private int label ;
    private String screen;

    public LogsPrint(String msg, int label, String screen) {
        this.msg = msg;
        this.label = label;
        this.screen = screen;
        prinLogs(msg,label,screen);
    }

    public void prinLogs(String msg,int label ,String screen) {

        Api api = ApiUtils.getAPIService();
        api.addErrorLog(Constant.X_SIGNATURE, msg, label,screen).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.code() == 200) {

                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TEG :: ", t.getLocalizedMessage());
            }
        });
    }
}
