package com.allein.freund.authapp.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by freund on 1/10/18.
 */

public interface APIService {
    @GET("/api/items/")
    Call<List<Item>> getItems(@Header("Cookie") String userCookie);

    @GET("/api/items/{id}")
    Call<List<ItemDetails>> getItemDetails(@Header("Cookie") String userCookie,
                                           @Path("id") int itemId);

    @PUT("/api/items/{id}")
    Call<String> getItemRequest(@Header("Cookie") String userCookie,
                                @Path("id") String itemId);

    @DELETE("/api/items/{id}")
    Call<String> deleteItemRequest(@Header("Cookie") String userCookie,
                                   @Path("id") String itemId);
}
