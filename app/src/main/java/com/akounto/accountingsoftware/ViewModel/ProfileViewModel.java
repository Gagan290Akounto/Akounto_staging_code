package com.akounto.accountingsoftware.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.JsonObject;
import com.akounto.accountingsoftware.Data.Profile.UserRegister;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.Repository.LoginRepo;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<SoclData> data;
    private final MutableLiveData<UserRegister> regUserData;
    private final LoginRepo loadBankRepo;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        loadBankRepo = new LoginRepo();
        data=new MutableLiveData<>();
        regUserData=new MutableLiveData<>();
    }

    //we will call this method to get the data
    public LiveData<SoclData> extReg(Context mContext, JsonObject request) {
        //we will load it asynchronously from server in this method
        loadBankRepo.loadExtRegister(mContext,request);
        data.setValue(loadBankRepo.getExtregData().getValue());
        return loadBankRepo.getExtregData();
    }
    //we will call this method to get the data
    public LiveData<UserRegister> userReg(Context mContext, JsonObject request) {
        //we will load it asynchronously from server in this method
        loadBankRepo.userRegister(mContext,request);
        regUserData.setValue(loadBankRepo.getRegisterUser().getValue());
        return loadBankRepo.getRegisterUser();
    }
}
