package com.allein.freund.authapp.remote;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by freund on 1/10/18.
 */

public interface APIService {
    @GET("/api/invoices/")
    Call<List<Invoice>> getInvoices(@Header("Cookie") String userCookie);
}
