package com.akounto.accountingsoftware.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.util.UiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepo {

    private MutableLiveData<DashboardData> dashboardData;
    private MutableLiveData<SearchDashboardData> dashboardSearch;


    public void loadSearch(Context mContext) {
        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        if (dashboardSearch == null) {
            dashboardSearch = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.searchDashboard(Constant.X_SIGNATURE, "Bearer " + UiUtil.getAcccessToken(mContext),UiUtil.getComp_Id(mContext)).enqueue(new Callback<SearchDashboardData>() {
            @Override
            public void onResponse(Call<SearchDashboardData> call, Response<SearchDashboardData> response) {
                UiUtil.cancelProgressDialogue();
                SearchDashboardData ud = new SearchDashboardData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
                            ud.setStatusMessage("Success");
                            ud = response.body();
                        } else {
                            ud.setStatus(444);
                            ud.setStatusMessage(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatus(444);
                        if (error.getError_description() == null) {
                            ud.setStatusMessage("Something went wrong");
                        } else {
                            ud.setStatusMessage(error.getError_description());
                            Log.d("ERROR :: ", error.getError_description());
                        }
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                dashboardSearch.postValue(ud);
            }

            @Override
            public void onFailure(Call<SearchDashboardData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                SearchDashboardData ud = new SearchDashboardData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                dashboardSearch.postValue(ud);
            }
        });
    }
    public void loadDashboard(Context mContext,JsonObject request) {

        UiUtil.showProgressDialogue(mContext, "", "Please wait..");
        if (dashboardData == null) {
            dashboardData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.getDashboard(Constant.X_SIGNATURE, UiUtil.getComp_Id(mContext),"Bearer " + UiUtil.getAcccessToken(mContext), request).enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                UiUtil.cancelProgressDialogue();
                DashboardData ud = new DashboardData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
                            ud.setStatusMessage("Success");
                            ud = response.body();
                        } else {
                            ud.setStatus(444);
                            ud.setStatusMessage(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatus(444);
                        if (error.getError_description() == null) {
                            ud.setStatusMessage("Something went wrong");
                        } else {
                            ud.setStatusMessage(error.getError_description());
                            Log.d("ERROR :: ", error.getError_description());
                        }
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                dashboardData.postValue(ud);
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                DashboardData ud = new DashboardData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                dashboardData.postValue(ud);
            }
        });
    }

    public MutableLiveData<DashboardData> getDashData() {
        return dashboardData;
    }
    public MutableLiveData<SearchDashboardData> getSearchData() {
        return dashboardSearch;
    }
}
