package com.akounto.accountingsoftware.Services;

import com.akounto.accountingsoftware.Constants.Constant;

public class ApiUtils {

    private ApiUtils() {
    }

    //Create an implementation of the API endpoints defined by the service interface.
    public static Api getAPIService() {
        return RetrofitClient.getClient(Constant.BASE_URL).create(Api.class);
    }

}
