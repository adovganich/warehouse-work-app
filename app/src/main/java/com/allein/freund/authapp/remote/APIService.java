package com.allein.freund.authapp.remote;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by freund on 1/10/18.
 */

public interface APIService {
    @GET("/api/invoices/")
    Call<List<Invoice>> getInvoices(@Header("Cookie") String userCookie);

    @GET("/api/invoices/{id}")
    Call<List<InvoiceDetails>> getInvoiceDetails(@Header("Cookie") String userCookie,
                                                 @Path("id") int invoiceId);
}
