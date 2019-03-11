package com.example.summit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface RetroInterface {
        String ENDPOINT = "http://arma-123.herokuapp.com";
        @GET("/")
        Call<String> getUser();
}
