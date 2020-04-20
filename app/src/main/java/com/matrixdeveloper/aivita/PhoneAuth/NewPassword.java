package com.matrixdeveloper.aivita.PhoneAuth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.matrixdeveloper.aivita.Accounts.Login_A;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import com.matrixdeveloper.aivita.MySingleton;
import com.matrixdeveloper.aivita.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NewPassword extends AppCompatActivity {

    String phone, pass;
    EditText newPass, confirmPass;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPass = findViewById(R.id.new_pass);
        confirmPass = findViewById(R.id.confirm_pass);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                phone= null;
            } else {
                phone= extras.getString("phone");
            }
        } else {
            phone= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

    }

    public void update(View view) {
        if (newPass.length() == 0 || confirmPass.length() == 0) {
            newPass.setError("Password cannot be empty");
            newPass.requestFocus();
        } else {
            String b;
            pass = newPass.getText().toString().trim();
            b = confirmPass.getText().toString().trim();
            if (!pass.equals(b)) {
                confirmPass.setError("Password is different");
                confirmPass.requestFocus();
            } else {
                updatePassword();
            }
        }

    }

    private void updatePassword() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("type", "customer");
            jsonBody.put("phone", phone);
            jsonBody.put("password", pass);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.CHANGE_PASS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONObject object = new JSONObject(response);
                         String status = object.getString("status");
                         if (status.equals("success")){
                             Toast.makeText(getApplicationContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), Login_A.class));finish();
                         }
                         else {
                             Toast.makeText(NewPassword.this, "Something went wrong! Try after some time", Toast.LENGTH_LONG).show();
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
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            stringRequest.setShouldCache(false);
            MySingleton.getInstance(NewPassword.this).addToRequestQueue(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callOnbackpressed(View view) {
        super.onBackPressed();
    }
}
