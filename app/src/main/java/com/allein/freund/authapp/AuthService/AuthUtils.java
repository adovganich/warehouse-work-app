package com.allein.freund.authapp.AuthService;

import com.allein.freund.authapp.R;

/**
 * Created by freund on 1/9/18.
 */

public class AuthUtils {

    private AuthUtils() {
    }

    public static final String BASE_URL = "http://192.168.20.156:8888/";

    public static AuthService getAuthService() {
        return RetrofitClient.getClient(BASE_URL).create(AuthService.class);
    }
}