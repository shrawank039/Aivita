package com.matrixdeveloper.aivita.network;

import android.database.Observable;

import com.matrixdeveloper.aivita.model.request.SearchRequest;
import com.matrixdeveloper.aivita.model.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public interface APIinterface {


    @POST("index.php?p=get_hastagpost")
    Call<SearchResponse> getSearch(@Body SearchRequest request);
}