package org.itniorwings.aivita.SimpleClasses;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import timber.log.Timber;

public class ApiRequest {

    public static void Call_Api(final Context context, String url, JSONObject jsonObject, final Callback callback) {

        Timber.d(url);
        if (jsonObject != null)
            Timber.tag(Variables.tag).d(jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Timber.tag(Variables.tag).d(response.toString());
                    if (callback != null)
                        callback.Responce(response.toString());
                }, error -> {
                    Timber.tag(Variables.tag).d(error.toString());
                    if (callback != null)
                        callback.Responce(error.toString());
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }
}
