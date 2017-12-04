package com.heady.network;

import com.heady.bean.EcommResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("/json")
    Call<EcommResponse> getData();
}