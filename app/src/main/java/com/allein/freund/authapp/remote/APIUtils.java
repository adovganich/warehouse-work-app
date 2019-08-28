package com.allein.freund.authapp.remote;

/**
 * Created by freund on 1/9/18.
 */

public class APIUtils {

    private APIUtils() {
    }

    public static final String BASE_URL = "https://warehouseserver20190809081742.azurewebsites.net/";

    public static AuthService getAuthService() {
        return RetrofitClient.getClient(BASE_URL).create(AuthService.class);
    }

    public static APIService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}