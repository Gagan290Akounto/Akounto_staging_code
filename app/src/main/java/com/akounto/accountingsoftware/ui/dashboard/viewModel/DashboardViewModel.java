package com.akounto.accountingsoftware.ui.dashboard.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.ui.dashboard.repository.DashboardRepository;
import com.akounto.accountingsoftware.request.GetDashboardRequest;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<DashboardData> dashBoard;
    private MutableLiveData<SearchDashboardData> searchDashboard;
    private DashboardRepository dashRepo;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init() {
        dashRepo = new DashboardRepository();
        dashBoard = new MutableLiveData<>();
        searchDashboard = new MutableLiveData<>();
    }

    //we will call this method to get the data
    /*public LiveData<DashboardData> getDataDashboard(JSONObject request) {
//        dashBoard = dashRepo.getDashData();
        dashBoard = dashRepo.loadDashboard(getApplication().getApplicationContext(), request);
        return dashBoard;
    }*/

    public LiveData<DashboardData> getDataDashboard(GetDashboardRequest request) {
        dashBoard = dashRepo.loadDashboard(getApplication().getApplicationContext(), request);
        return dashBoard;
    }

    //we will call this method to get the data
    public LiveData<SearchDashboardData> getSearch(Context mContext) {
        searchDashboard = dashRepo.loadSearch(mContext);
//        searchDashboard = dashRepo.getSearchData();
        return searchDashboard;
    }
}
