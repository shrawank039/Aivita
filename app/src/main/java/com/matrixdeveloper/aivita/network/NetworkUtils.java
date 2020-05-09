package com.matrixdeveloper.aivita.network;

public class NetworkUtils {

    private static APIinterface service;

    public static APIinterface getClient() {
        if (service == null)
            service = RetrofitClientInstance.getRetrofitInstance().create(APIinterface.class);

        return service;
    }
}
