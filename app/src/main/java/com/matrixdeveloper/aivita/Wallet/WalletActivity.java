package com.matrixdeveloper.aivita.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.matrixdeveloper.aivita.MySingleton;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WalletActivity extends AppCompatActivity {

    TextView avt,diamondTxt,usdTxt;
    String avt_coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        avt = findViewById(R.id.txt_avt);
        diamondTxt = findViewById(R.id.txt_diamond);
        usdTxt = findViewById(R.id.txt_usd);
    }

    private void setData(String avt_coin, String diamond, String usd) {
        avt.setText(avt_coin);
        diamondTxt.setText(diamond);
        float a= Float.parseFloat(avt_coin);
        a= a/210;
        a = (float)((int)( a *100f ))/100f;
        usdTxt.setText(String.valueOf(a));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWalletCoin();
    }

    public void callOnbackpressed(View view) {
        onBackPressed();
    }

    public void withdrawConverty(View view) {
        startActivity(new Intent(getApplicationContext(),WithdrawConvert.class)
        .putExtra("avt_coin",avt_coin));
    }

    private void getWalletCoin() {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,""));
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.AVAILABLE_BALANCE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("success")){
                            JSONObject jsonObject = object.getJSONObject("data");
                            avt_coin= jsonObject.optString("avt_coin");
                            String diamond= jsonObject.optString("diamond_coin");
                            String usd = jsonObject.optString("usd_coin");
                            setData(avt_coin,diamond,usd);
                        }
                        else {
                            Toast.makeText(WalletActivity.this, "Something went wrong! Try after some time", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(getApplicationContext(), "2 "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setShouldCache(false);
            MySingleton.getInstance(WalletActivity.this).addToRequestQueue(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
