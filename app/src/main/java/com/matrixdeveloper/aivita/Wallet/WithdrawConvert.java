package com.matrixdeveloper.aivita.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.matrixdeveloper.aivita.Accounts.Login_A;
import com.matrixdeveloper.aivita.MySingleton;
import com.matrixdeveloper.aivita.PhoneAuth.NewPassword;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithdrawConvert extends AppCompatActivity {

    RadioButton convert,withdraw;
    Spinner spinner_type;
    String type= "diamond",avt_coin;
    EditText edtAmt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_convert);

        convert = findViewById(R.id.convert);
        withdraw = findViewById(R.id.withdraw);
        spinner_type = findViewById(R.id.spinner_type);
        edtAmt = findViewById(R.id.edt_amt);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        if (getIntent().hasExtra("avt_coin")) {
            avt_coin = getIntent().getStringExtra("avt_coin");
           // Toast.makeText(this, avt_coin, Toast.LENGTH_SHORT).show();
        }

        // ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("diamond");
        arrayList.add("usd");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(arrayAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.YELLOW);
                type = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Selected: " + mode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void withdrawClick(View view) {
        convert.setChecked(true);
        withdraw.setChecked(false);
        Toast.makeText(this, "Withdrawal is coming soon!!!", Toast.LENGTH_SHORT).show();
    }

    public void convertClick(View view) {
        int ammount= Integer.parseInt(edtAmt.getText().toString().trim());
        ammount =ammount*10;
        if (Integer.parseInt(avt_coin)>=ammount) {
            sendConversion(ammount);
        }else {
            Toast.makeText(this, "You don't have "+ammount+" avita coins!!!", Toast.LENGTH_LONG).show();
        }

    }

    private void sendConversion(int ammount) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("avt_coin", ammount);
            jsonBody.put("type", type);
            jsonBody.put("fb_id",Variables.sharedPreferences.getString(Variables.u_id,""));
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.CONVERT_COIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("success")){
                            Toast.makeText(getApplicationContext(), "Coin converted successfully!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(WithdrawConvert.this, "Something went wrong! Try after some time", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
            MySingleton.getInstance(WithdrawConvert.this).addToRequestQueue(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
