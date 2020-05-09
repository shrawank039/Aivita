package com.matrixdeveloper.aivita.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matrixdeveloper.aivita.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            final String BASE_URL ="http://167.71.215.98/aivita/API/";

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                   // .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                   // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //    .client(okHttpClient)
                    .build();
        }

        retrofit.create(APIinterface.class);

        return retrofit;
    }
}