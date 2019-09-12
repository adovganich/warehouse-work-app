package com.allein.freund.authapp.remote;

/**
 * Created by freund on 1/9/18.
 */

public class APIUtils {

    private APIUtils() {
    }

    //private static final String BASE_URL = "https://warehouseserver20190809081742.azurewebsites.net/";
    private static final String BASE_URL = "http://52.209.116.253/";
    private static String customBaseUrl = null;

    public static AuthService getAuthService() {
        if(customBaseUrl == null)
            return RetrofitClient.getClient(BASE_URL).create(AuthService.class);
        else
            return RetrofitClient.getClient(customBaseUrl).create(AuthService.class);
    }

    public static APIService getApiService() {
        if(customBaseUrl == null)
            return RetrofitClient.getClient(BASE_URL).create(APIService.class);
        else
            return RetrofitClient.getClient(customBaseUrl).create(APIService.class);
    }

    public static void setCustomBaseUrl(String baseUrl) {
        customBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        if(customBaseUrl == null)
            return BASE_URL;
        else
            return customBaseUrl;
    }

}