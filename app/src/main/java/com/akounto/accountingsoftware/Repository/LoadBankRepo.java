package com.akounto.accountingsoftware.Repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData2;
import com.akounto.accountingsoftware.request.RegisterBankRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.BankLinkData;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Data.RegisterBank.AutoSynData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.Data.RegisterBank.TransactionStatus;
import com.akounto.accountingsoftware.util.UiUtil;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadBankRepo {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<BankAccountData> bankData;
    private MutableLiveData<BankAccountData2> bankDataOption;
    private MutableLiveData<AutoSynData> autoSyncData;
    private MutableLiveData<BankLinkData> linkBankData;
    //This method is using Retrofit to get the JSON data from URL
    public void loadRegister(Context mContext, String x_comp, String access_token, String public_token, String institution_id, String institution_name) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (bankDataOption == null) {
            bankDataOption = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.registerBankRequest(Constant.X_SIGNATURE, x_comp, "Bearer " + access_token, public_token, institution_id, institution_name).enqueue(new Callback<BankAccountData2>()   {
            @Override
            public void onResponse(Call<BankAccountData2> call, Response<BankAccountData2> response) {
                UiUtil.cancelProgressDialogue();
                BankAccountData2 ud = new BankAccountData2();
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
                bankDataOption.postValue(ud);
            }

            @Override
            public void onFailure(Call<BankAccountData2> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                BankAccountData2 ud = new BankAccountData2();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                bankDataOption.postValue(ud);
            }
        });
    }
    public void loadRegisterBank(Context mContext, String x_company, String access_token, RegisterBankRequest req) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (bankData == null) {
            bankData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.registerBank(Constant.X_SIGNATURE, x_company, "Bearer " + access_token, req).enqueue(new Callback<BankAccountData>()  {
            @Override
            public void onResponse(@NotNull Call<BankAccountData> call, @NotNull Response<BankAccountData> response) {
                UiUtil.cancelProgressDialogue();
                BankAccountData ud = new BankAccountData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
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
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                bankData.postValue(ud);
            }

            @Override
            public void onFailure(Call<BankAccountData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                BankAccountData bld = new BankAccountData();
                bld.setStatus(444);
                bld.setStatusMessage("Something went wrong");
                bankData.postValue(bld);
            }
        });
    }
    //This method is using Retrofit to get the JSON data from URL
    public void loadBank(Context mContext,String x_company, String access_token) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (bankData == null) {
            bankData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.getBankRequest(Constant.X_SIGNATURE, x_company, "Bearer " + access_token, "").enqueue(new Callback<BankAccountData>()  {
            @Override
            public void onResponse(@NotNull Call<BankAccountData> call, @NotNull Response<BankAccountData> response) {
                UiUtil.cancelProgressDialogue();
                BankAccountData ud = new BankAccountData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
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
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                bankData.postValue(ud);
            }

            @Override
            public void onFailure(Call<BankAccountData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                BankAccountData bld = new BankAccountData();
                bld.setStatus(444);
                bld.setStatusMessage("Something went wrong");
                bankData.postValue(bld);
            }
        });
    }
    //This method is using Retrofit to get the JSON data from URL
    public void autoSyncChange(Context mContext,String x_company, String access_token, String bid, String aid, boolean state) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (autoSyncData == null) {
            autoSyncData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.autoImportSetRequest(Constant.X_SIGNATURE, x_company, "Bearer " + access_token, bid, aid, state).enqueue(new Callback<AutoSynData>() {
            @Override
            public void onResponse(Call<AutoSynData> call, Response<AutoSynData> response) {
                UiUtil.cancelProgressDialogue();
                AutoSynData ud = new AutoSynData();
                ud.setStatus(response.code());
                try {
                    if (response.code() == 200) {
                        ud = response.body();
                        if (state) {
                            //importBankTrans(mContext,x_company, access_token, bid);
                        }
                    } else {
                        TransactionStatus ts = new TransactionStatus();
                        ts.setIsSuccess(false);
                        ud.setStatus(444);
                        ud.setStatusMessage("Something went wrong");
                        ud.setTransactionStatus(ts);
                    }
                } catch (Exception e) {
                    TransactionStatus ts = new TransactionStatus();
                    ts.setIsSuccess(false);
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                    ud.setTransactionStatus(ts);
                }
                autoSyncData.postValue(ud);
            }

            @Override
            public void onFailure(Call<AutoSynData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                AutoSynData bld = new AutoSynData();
                TransactionStatus ts = new TransactionStatus();
                ts.setIsSuccess(false);
                bld.setStatus(444);
                bld.setTransactionStatus(ts);
                bld.setStatusMessage("Something went wrong");
                autoSyncData.postValue(bld);
            }
        });
    }
    public void importBankTrans(Context mContext,String x_comp, String access_token, String bank_id) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (bankData == null) {
            bankData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.importBankTransactionRequest(Constant.X_SIGNATURE, x_comp, "Bearer " + access_token, bank_id).enqueue(new Callback<BankAccountData>() {
            @Override
            public void onResponse(Call<BankAccountData> call, Response<BankAccountData> response) {
                UiUtil.cancelProgressDialogue();
                BankAccountData ud = new BankAccountData();
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
                    ud.setStatusMessage("Something went wrong");
                }
                bankData.postValue(ud);
            }

            @Override
            public void onFailure(Call<BankAccountData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                Log.d("TEG :: ", t.getLocalizedMessage());
                BankAccountData ud = new BankAccountData();
                ud.setStatus(444);
                ud.setStatusMessage("Something went wrong");
                bankData.postValue(ud);
            }
        });
    }
    //This method is using Retrofit to get the JSON data from URL
    public void linkBank(Context mContext,String x_company, String access_token) {
        UiUtil.showProgressDialogue(mContext,"","Please wait..");
        if (linkBankData == null) {
            linkBankData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.bankLinkRequest(Constant.X_SIGNATURE, x_company, "Bearer " + access_token, "").enqueue(new Callback<BankLinkData>() {
            @Override
            public void onResponse(Call<BankLinkData> call, Response<BankLinkData> response) {
                UiUtil.cancelProgressDialogue();
                BankLinkData ud = new BankLinkData();
                try {
                    ud.setStatus(response.code());
                    if (response.code() == 200) {
                        if (response.body().getTransactionStatus().getIsSuccess()) {
                            ud = response.body();
                        } else {
                            ud.setStatus(444);
                            ud.setStatusMessage(((LinkedTreeMap) (response.body().getTransactionStatus().getError())).get("Description").toString());
                        }
                    } else {
                        ErrorData error = new Gson().fromJson(response.errorBody().string(), ErrorData.class);
                        ud.setStatusMessage(error.getError_description());
                    }
                } catch (Exception e) {
                    ud.setStatus(444);
                    ud.setStatusMessage("Something went wrong");
                }
                linkBankData.postValue(ud);
            }

            @Override
            public void onFailure(Call<BankLinkData> call, Throwable t) {
                UiUtil.cancelProgressDialogue();
                BankLinkData bld = new BankLinkData();
                bld.setStatus(444);
                bld.setStatusMessage("Something went wrong");
                linkBankData.postValue(bld);
            }
        });
    }
    public MutableLiveData<BankAccountData> getBankData() {
        return bankData;
    }
    public MutableLiveData<BankLinkData> getBankLinkData() {
        return linkBankData;
    }
    public MutableLiveData<AutoSynData> getAutoSynData() {
        return autoSyncData;
    }
    public MutableLiveData<BankAccountData2> getBankDataOption() {
        return bankDataOption;
    }
}
