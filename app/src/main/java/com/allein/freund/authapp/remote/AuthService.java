package com.allein.freund.authapp.remote;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by freund on 1/9/18.
 */

public interface AuthService {
    @POST("/auth/login/")
    Call<User> sendCredentials(@Query("email") String email,
                               @Query("password") String password);

    @GET("/auth/logout/")
    Call<User> logout();
}