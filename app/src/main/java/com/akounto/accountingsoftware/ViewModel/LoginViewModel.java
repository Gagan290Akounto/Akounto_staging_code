package com.akounto.accountingsoftware.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.Repository.LoginRepo;

public class LoginViewModel extends AndroidViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<LoginData> loginData;
    private MutableLiveData<SoclData> loginExt;
    private MutableLiveData<String> auth;
    private MutableLiveData<CheckEmailData> checkEmailData;
    private LoginRepo loginRepo;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        init();
    }
    public void init() {
        loginRepo = new LoginRepo();
        loginData = new MutableLiveData<>();
        loginExt = new MutableLiveData<>();
        auth = new MutableLiveData<>();
        checkEmailData = new MutableLiveData<>();
    }
    //we will call this method to get the Login data
    public LiveData<LoginData> login(Context mContext, String username, String pass) {
        //we will load it asynchronously from server in this method
        loginRepo.loadLogin(mContext,username, pass);
        loginData = loginRepo.getLoginData();
        return loginData;
    }
    //we will call this method to get the Login data
    public LiveData<SoclData> extLogin(Context mContext, String p, String at) {
        //we will load it asynchronously from server in this method
        loginRepo.extLogin(mContext,p, at);
        loginExt = loginRepo.getLoginExt();
        return loginExt;
    }
    //we will call this method to get the Login data
  /*  public LiveData<String> authGoogle(String code,String rt, String cid,String csd) {
        //we will load it asynchronously from server in this method
        loginRepo.authLogin(code, rt,cid,csd);
        auth = loginRepo.getAuthData();
        return auth;
    }*/
    //we will call this method to get the Check Email data
    public LiveData<CheckEmailData> checkEmail(Context mContext,String email) {
        //we will load it asynchronously from server in this method
        loginRepo.checkEmail(mContext,email);
        checkEmailData = loginRepo.getEmailCheckData();
        return checkEmailData;
    }

    public MutableLiveData<LoginData> getLoginData() {
        return loginData;
    }
}
