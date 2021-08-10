package com.akounto.accountingsoftware.ui.dashboard.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.ErrorData;
import com.akounto.accountingsoftware.Services.Api;
import com.akounto.accountingsoftware.Services.ApiUtils;
import com.akounto.accountingsoftware.response.GetCompanyResponse;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.ui.base.model.BaseResponseModel;
import com.akounto.accountingsoftware.util.UiUtil;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardProfileSetupRepository {
    private MutableLiveData<GetCompanyResponse> companyData;
    private MutableLiveData<GetStatesResponse> stateData;
    private MutableLiveData<BaseResponseModel> saveResponseData;
    private MutableLiveData<BaseResponseModel> uploadLogoResponseData;

    public MutableLiveData<GetCompanyResponse> getBusinessById(Context context, int companyId) {
        UiUtil.showProgressDialogue(context, "", "Please wait..");
        if (companyData == null) {
            companyData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.getBusinessById(Constant.X_SIGNATURE,
                "Bearer " + UiUtil.getAcccessToken(context),
                UiUtil.getComp_Id(context), companyId)
                .enqueue(new Callback<GetCompanyResponse>() {
                    @Override
                    public void onResponse(Call<GetCompanyResponse> call, Response<GetCompanyResponse> response) {
                        UiUtil.cancelProgressDialogue();

                        GetCompanyResponse ud = new GetCompanyResponse();
                        ud.setStatus(response.code());

                        try {
                            if (response.code() == 200) {
                                if (response.body().getTransactionStatus().isIsSuccess()) {
                                    ud.setStatusMessage("Success");
                                    ud = response.body();
                                } else {
                                    ud.setStatus(444);
                                    ud.setStatusMessage((response.body().getTransactionStatus().getError()).getDescription());
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
                        companyData.postValue(ud);
                    }

                    @Override
                    public void onFailure(Call<GetCompanyResponse> call, Throwable t) {
                        UiUtil.cancelProgressDialogue();
                        Log.d("TEG :: ", t.getLocalizedMessage());

                        GetCompanyResponse ud = new GetCompanyResponse();
                        ud.setStatus(444);
                        ud.setStatusMessage("Something went wrong");
                        companyData.postValue(ud);
                    }
                });

        return companyData;
    }

    public MutableLiveData<GetStatesResponse> getStateByCompanyId(Context context, int countryId) {
        UiUtil.showProgressDialogue(context, "", "Please wait..");
        if (stateData == null) {
            stateData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.getStatesByCountryId(Constant.X_SIGNATURE,
                "Bearer " + UiUtil.getAcccessToken(context),
                UiUtil.getComp_Id(context), countryId)
                .enqueue(new Callback<GetStatesResponse>() {
                    @Override
                    public void onResponse(Call<GetStatesResponse> call, Response<GetStatesResponse> response) {
                        UiUtil.cancelProgressDialogue();

                        GetStatesResponse ud = new GetStatesResponse();
                        ud.setStatus(response.code());

                        try {
                            if (response.code() == 200) {
                                if (response.body().getTransactionStatus().isIsSuccess()) {
                                    ud.setStatusMessage("Success");
                                    ud = response.body();
                                } else {
                                    ud.setStatus(444);
                                    ud.setStatusMessage((response.body().getTransactionStatus().getError()).getDescription());
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
                        stateData.postValue(ud);
                    }

                    @Override
                    public void onFailure(Call<GetStatesResponse> call, Throwable t) {
                        UiUtil.cancelProgressDialogue();
                        Log.d("TEG :: ", t.getLocalizedMessage());

                        GetStatesResponse ud = new GetStatesResponse();
                        ud.setStatus(444);
                        ud.setStatusMessage("Something went wrong");
                        stateData.postValue(ud);
                    }
                });

        return stateData;
    }

    public MutableLiveData<BaseResponseModel> saveAddBusinessData(Context context, HashMap<String, String> hashMap) {
        UiUtil.showProgressDialogue(context, "", "Please wait..");
        if (saveResponseData == null) {
            saveResponseData = new MutableLiveData<>();
        }

        /*RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(hashMap)).toString());*/

        /*BusinessDetails model = new BusinessDetails();
        model.setId(Integer.parseInt(hashMap.get("Id")));
        model.setBusinessName(hashMap.get("BusinessName"));
        model.setCountry(Integer.parseInt(hashMap.get("Country")));
        model.setBusinessCurrency(hashMap.get("BusinessCurrency"));
        model.setAddress1(hashMap.get("Address1"));
        model.setAddress2(hashMap.get("Address2"));
        model.setCity(hashMap.get("City"));
        model.setState(hashMap.get("State"));
        model.setPostCode(hashMap.get("PostCode"));
        model.setTimeZone(hashMap.get("TimeZone"));
        model.setPhone(hashMap.get("Phone"));
        model.setMobile(hashMap.get("Mobile"));
        model.setFax(hashMap.get("Fax"));
        model.setTollFree(hashMap.get("TollFree"));
        model.setDomainUrl(hashMap.get("DomainUrl"));
        model.setLogo(hashMap.get("Logo"));*/


        Api api = ApiUtils.getAPIService();
        api.editCompany(Constant.X_SIGNATURE,
                "Bearer " + UiUtil.getAcccessToken(context),
                UiUtil.getComp_Id(context), hashMap)
                .enqueue(new Callback<BaseResponseModel>() {
                    @Override
                    public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                        UiUtil.cancelProgressDialogue();

                        BaseResponseModel ud = new BaseResponseModel();
                        ud.setStatus(response.code());

                        try {
                            if (response.code() == 200) {
                                if (response.body().getTransactionStatus().isIsSuccess()) {
                                    ud.setStatusMessage("Success");
                                    ud = response.body();
                                } else {
                                    ud.setStatus(444);
                                    ud.setStatusMessage((response.body().getTransactionStatus().getError()).getDescription());
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
                        saveResponseData.postValue(ud);
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                        UiUtil.cancelProgressDialogue();
                        Log.d("TEG :: ", t.getLocalizedMessage());

                        BaseResponseModel ud = new BaseResponseModel();
                        ud.setStatus(444);
                        ud.setStatusMessage("Something went wrong");
                        saveResponseData.postValue(ud);
                    }
                });

        return saveResponseData;
    }

    public MutableLiveData<BaseResponseModel> uploadLogoAddBusinessData(Context context, HashMap<String, String> hashMap) {
        UiUtil.showProgressDialogue(context, "", "Please wait..");
        if (uploadLogoResponseData == null) {
            uploadLogoResponseData = new MutableLiveData<>();
        }
        Api api = ApiUtils.getAPIService();
        api.uploadLogoCompany(Constant.X_SIGNATURE,
                "Bearer " + UiUtil.getAcccessToken(context),
                UiUtil.getComp_Id(context), hashMap)
                .enqueue(new Callback<BaseResponseModel>() {
                    @Override
                    public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                        UiUtil.cancelProgressDialogue();

                        BaseResponseModel ud = new BaseResponseModel();
                        ud.setStatus(response.code());

                        try {
                            if (response.code() == 200) {
                                if (response.body().getTransactionStatus().isIsSuccess()) {
                                    ud.setStatusMessage("Success");
                                    ud = response.body();
                                } else {
                                    ud.setStatus(444);
                                    ud.setStatusMessage((response.body().getTransactionStatus().getError()).getDescription());
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
                        uploadLogoResponseData.postValue(ud);
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                        UiUtil.cancelProgressDialogue();
                        Log.d("TEG :: ", t.getLocalizedMessage());

                        BaseResponseModel ud = new BaseResponseModel();
                        ud.setStatus(444);
                        ud.setStatusMessage("Something went wrong");
                        uploadLogoResponseData.postValue(ud);
                    }
                });

        return uploadLogoResponseData;
    }
}
