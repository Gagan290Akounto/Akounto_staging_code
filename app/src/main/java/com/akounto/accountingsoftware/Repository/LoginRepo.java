package com.akounto.accountingsoftware.Repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.akounto.accountingsoftware.Data.Profile.UserRegister;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.util.UiUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<LoginData> loginData;
    private MutableLiveData<SoclData> loginExt;
    private MutableLiveData<SoclData> extRegData;
    private MutableLiveData<UserRegister> userRegData;
    private MutableLiveData<CheckEmailData> emailCheckData;

    public void loadLogin(Context mContext,String username, String pass) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (loginData == null) {
            loginData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.loginRequest(Constant.X_SIGNATURE, username, pass, Constant.GRANT_TYPE).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                UiUtil.cancelProgressDialogue();
                LoginData ud = new LoginData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus()==null) {
                            ud.setStatusMessage("Success");
                            ud = response.body();
                        } else {
                            ud.setStatus(444);
                            ud.setStatusMessage(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatusMessage(error.getError_description());
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                loginData.postValue(ud);
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                LoginData ud = new LoginData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                loginData.postValue(ud);
            }
        });
    }

    public static void prinLogs(String msg,int label ,String screen) {

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
   /* public void authLogin(String code,String refresh_token,String c_id,String c_sec) {
        if (auth == null) {
            auth = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIServiceGoogle();
        api.getToken("refresh_token",refresh_token ,c_id, c_sec,"https://local.akounto.com/signin-google").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    auth.postValue(response.body());
                } catch (Exception e) {
                    auth.postValue("Something in wrong");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                auth.postValue("Something in wrong");
            }
        });
    }*/

    public void extLogin(Context mContext,String provider, String accessToken) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (loginExt == null) {
            loginExt = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.extLogin(Constant.X_SIGNATURE, provider, accessToken).enqueue(new Callback<SoclData>() {
            @Override
            public void onResponse(Call<SoclData> call, Response<SoclData> response) {
                UiUtil.cancelProgressDialogue();
                SoclData ud = new SoclData();
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
                        ud.setStatusMessage(error.getError_description());
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                loginExt.postValue(ud);
            }

            @Override
            public void onFailure(Call<SoclData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                SoclData ud = new SoclData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                loginExt.postValue(ud);
            }
        });
    }

    public void checkEmail(Context mContext,String email) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (emailCheckData == null) {
            emailCheckData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.checkEmailExistRequest(Constant.X_SIGNATURE, email).enqueue(new Callback<CheckEmailData>()  {
            @Override
            public void onResponse(Call<CheckEmailData> call, Response<CheckEmailData> response) {
                UiUtil.cancelProgressDialogue();
                CheckEmailData ud = new CheckEmailData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        ud.setStatusMessage("Success");
                        ud = response.body();
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatusMessage(error.getError_description());
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                emailCheckData.postValue(ud);
            }

            @Override
            public void onFailure(Call<CheckEmailData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                CheckEmailData ud = new CheckEmailData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                emailCheckData.postValue(ud);
            }
        });
    }
    public void loadExtRegister(Context mContext, JsonObject request) {
        try{
        UiUtil.showProgressDialogue(mContext,"","Please wait..");}catch(Exception e){}
        if (extRegData == null) {
            extRegData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.externalRegister(Constant.X_SIGNATURE,request).enqueue(new Callback<SoclData>() {
            @Override
            public void onResponse(Call<SoclData> call, Response<SoclData> response) {
                UiUtil.cancelProgressDialogue();
                SoclData ud = new SoclData();
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
                        ud.setStatusMessage(error.getError_description());
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                extRegData.postValue(ud);
            }

            @Override
            public void onFailure(Call<SoclData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                SoclData ud = new SoclData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                extRegData.postValue(ud);
            }
        });
    }
    public void userRegister(Context mContext,JsonObject request) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (userRegData == null) {
            userRegData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.userRegister(Constant.X_SIGNATURE,request).enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                UiUtil.cancelProgressDialogue();
                UserRegister ud = new UserRegister();
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
                        ud.setStatusMessage(error.getError_description());
                        Log.d("ERROR :: ", error.getError_description());
                    }
                } catch (Exception e) {
                    Log.d("TEG :: ", e.getLocalizedMessage());
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                userRegData.postValue(ud);
            }

            @Override
            public void onFailure(Call<UserRegister> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                UserRegister ud = new UserRegister();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                userRegData.postValue(ud);
            }
        });
    }


    public MutableLiveData<LoginData> getLoginData() {
        return loginData;
    }
    public MutableLiveData<SoclData> getLoginExt() {
        return loginExt;
    }
    public MutableLiveData<CheckEmailData> getEmailCheckData() {
        return emailCheckData;
    }
    public MutableLiveData<SoclData> getExtregData() {
        return extRegData;
    }
    public MutableLiveData<UserRegister> getRegisterUser() {
        return userRegData;
    }
}
