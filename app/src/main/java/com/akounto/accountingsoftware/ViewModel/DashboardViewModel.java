package com.akounto.accountingsoftware.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.JsonObject;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.Repository.DashboardRepo;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<DashboardData> dashBoard;
    private MutableLiveData<SearchDashboardData> searchDashboard;
    private DashboardRepo dashRepo;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        dashRepo = new DashboardRepo();
        dashBoard = new MutableLiveData<>();
        searchDashboard = new MutableLiveData<>();
    }

    //we will call this method to get the data
    public LiveData<DashboardData> getDataDashboard(Context mContext, JsonObject request) {
        //we will load it asynchronously from server in this method
        dashRepo.loadDashboard(mContext,request);
        dashBoard = dashRepo.getDashData();
        return dashBoard;
    }

    //we will call this method to get the data
    public LiveData<SearchDashboardData> getSearch(Context mContext) {
        dashRepo.loadSearch(mContext);
        searchDashboard = dashRepo.getSearchData();
        return searchDashboard;
    }
}
