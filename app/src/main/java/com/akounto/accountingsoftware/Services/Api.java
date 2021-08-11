package com.akounto.accountingsoftware.Services;

import com.akounto.accountingsoftware.Data.BankLinkData;
import com.akounto.accountingsoftware.Data.CheckEmailData;
import com.akounto.accountingsoftware.Data.Dashboard.DashboardData;
import com.akounto.accountingsoftware.Data.DashboardSearchData.SearchDashboardData;
import com.akounto.accountingsoftware.Data.LoginData;
import com.akounto.accountingsoftware.Data.Profile.UserRegister;
import com.akounto.accountingsoftware.Data.RegisterBank.AutoSynData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData;
import com.akounto.accountingsoftware.Data.RegisterBank.BankAccountData2;
import com.akounto.accountingsoftware.Data.SoclData;
import com.akounto.accountingsoftware.model.ForgotPasswordData;
import com.akounto.accountingsoftware.request.GetDashboardRequest;
import com.akounto.accountingsoftware.request.RegisterBankRequest;
import com.akounto.accountingsoftware.response.BusinessDetails;
import com.akounto.accountingsoftware.response.GetCompanyResponse;
import com.akounto.accountingsoftware.response.GetStatesResponse;
import com.akounto.accountingsoftware.response.SignUp.SignUpResponse;
import com.akounto.accountingsoftware.ui.base.model.BaseResponseModel;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {


    @POST("api/profile/adnroid-log-error")
    @FormUrlEncoded
    Call<ResponseBody> addErrorLog(@Header("X-Signature") String signature, @Field("message") String message, @Field("level") int level, @Field("filename") String filename);

    @POST("token")
    @FormUrlEncoded
    Call<LoginData> loginRequest(@Header("X-Signature") String header, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @POST("api/bank/link-token")
    @FormUrlEncoded
    Call<BankLinkData> bankLinkRequest(@Header("X-Signature") String signature, @Header("X-Company") String header, @Header("Authorization") String authHeader, @Field("") String dummy);

    @POST("api/bank/register-bank-with-options")
    @FormUrlEncoded
    Call<BankAccountData2> registerBankRequest(@Header("X-Signature") String signature, @Header("X-Company") String company, @Header("Authorization") String authHeader, @Field("PublicToken") String public_token, @Field("InstitutionId") String institution_id, @Field("InstitutionName") String institution_name);

    @POST("api/bank/register-selected-bank-account")
    Call<BankAccountData> registerBank(@Header("X-Signature") String signature, @Header("X-Company") String company, @Header("Authorization") String authHeader, @Body RegisterBankRequest request);


    @GET("api/bank/get")
    Call<BankAccountData> getBankRequest(@Header("X-Signature") String signature, @Header("X-Company") String header, @Header("Authorization") String authHeader, @Query("") String dummy);

    @POST("api/bank/auto-import-set")
    @FormUrlEncoded
    Call<AutoSynData> autoImportSetRequest(@Header("X-Signature") String signature, @Header("X-Company") String header, @Header("Authorization") String authHeader, @Field("BankId") String BankId, @Field("AccountId") String AccountId, @Field("IsAutoImport") boolean IsAutoImport);

    @POST("api/bank/import-transaction")
    @FormUrlEncoded
    Call<BankAccountData> importBankTransactionRequest(@Header("X-Signature") String signature, @Header("X-Company") String company, @Header("Authorization") String authHeader, @Field("BankId") String BankId);

    @POST("api/profile/check-email-exist")
    @FormUrlEncoded
    Call<CheckEmailData> checkEmailExistRequest(@Header("X-Signature") String signature, @Field("Email") String email);

    @POST("api/profile/native-app-register-social-account")
    Call<SoclData> externalRegister(@Header("X-Signature") String signature, @Body JsonObject request);

    @POST("api/profile/native-app-register-social-account")
    Call<UserRegister> userRegister(@Header("X-Signature") String signature, @Body JsonObject request);

    @GET("api/profile/native-app-social-login")
    Call<SoclData> extLogin(@Header("X-Signature") String signature, @Query("provider") String provider, @Query("idToken") String externalAccessToken);

    @POST("api/accounting/dashboad")
    Call<DashboardData> getDashboard(@Header("X-Signature") String signature, @Header("X-Company") String company, @Header("Authorization") String authHeader, @Body JsonObject request);

    @GET("api/report/search-dashboard")
    Call<SearchDashboardData> searchDashboard(@Header("X-Signature") String signature, @Header("Authorization") String authorization, @Header("X-Company") String x_company);

    @POST("api/profile/forgot-user-password")
    Call<ForgotPasswordData> fogotPasswordRequest(@Header("X-Signature") String signature, @Body JsonObject email);

    @POST("api/profile/register-business-new")
    Call<SignUpResponse> register(@Header("X-Signature") String signature, @Body JsonObject request);

    @POST("api/accounting/dashboad")
    Call<DashboardData> getDashboard(@Header("X-Signature") String signature,
                                     @Header("X-Company") String company,
                                     @Header("Authorization") String authHeader,
                                     @Body GetDashboardRequest getDashboardRequest);

    @GET("api/profile/get-company/{id}")
    Call<GetCompanyResponse> getBusinessById(@Header("X-Signature") String signature,
                                             @Header("Authorization") String authHeader,
                                             @Header("X-Company") String company,
                                             @Path("id") int id);

    @GET("api/country/states-get/{state_id}")
    Call<GetStatesResponse> getStatesByCountryId(@Header("X-Signature") String signature,
                                                 @Header("Authorization") String authorization,
                                                 @Header("X-Company") String company,
                                                 @Path("state_id") int countryId);

    @POST("api/profile/edit-company")
    Call<BaseResponseModel> editCompany(@Header("X-Signature") String signature,
                                        @Header("Authorization") String authorization,
                                        @Header("X-Company") String company,
                                        @Body HashMap<String, String> jsonBody);
    /*@POST("api/profile/edit-company")
    Call<BaseResponseModel> editCompany(@Header("X-Signature") String signature,
                                        @Header("Authorization") String authorization,
                                        @Header("X-Company") String company,
                                        @Body RequestBody params);*/

    /*@POST("api/profile/edit-company")
    Call<BaseResponseModel> editCompany(@Header("X-Signature") String signature,
                                        @Header("Authorization") String authorization,
                                        @Header("X-Company") String company,
                                        @Body BusinessDetails businessDetails);*/

    @POST("api/invoice/set-business-logo")
    Call<BaseResponseModel> uploadLogoCompany(@Header("X-Signature") String signature,
                                              @Header("Authorization") String authorization,
                                              @Header("X-Company") String company,
                                              @Body HashMap<String, String> jsonBody);
}
