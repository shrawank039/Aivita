package com.matrixdeveloper.aivita.PhoneAuth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.matrixdeveloper.aivita.Accounts.SignUpActivity;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import com.matrixdeveloper.aivita.MySingleton;
import com.matrixdeveloper.aivita.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText, editTextPhone;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    private static final long START_TIME_IN_MILLIS = 30000;
    private TextView mTextViewCountDown, phoneText;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    public long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    String phonenumber, idd;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.edt).setFocusable(false);

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        phoneText = findViewById(R.id.phoneText);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter valid code.");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonReset.setVisibility(View.INVISIBLE);
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                progressBar.setVisibility(View.VISIBLE);
                startTimer();
                sendVerificationCode(phonenumber);

                findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String code = editText.getText().toString().trim();

                        if (code.isEmpty() || code.length() < 6) {

                            editText.setError("Enter code...");
                            editText.requestFocus();
                            return;
                        }
                        verifyCode(code);
                    }
                });
            }
        });
        progressBar.setVisibility(View.INVISIBLE);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
                mTimerRunning = false;
                mButtonReset.setVisibility(View.VISIBLE);
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", 00, 00);
                mTextViewCountDown.setText(timeLeftFormatted);
            }
        }.start();
        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //  SharedPreferenceUtil.storeBooleanValue(getApplicationContext(), Constants.ISUSERLOGGEDIN, true);

                            Intent intent = new Intent(getApplicationContext(), NewPassword.class);
                          //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("phone", editTextPhone.getText().toString().trim());
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "0"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        findViewById(R.id.phonenumber_verification_layout).setVisibility(View.GONE);
        findViewById(R.id.otpLayout).setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onBackPressed() {
        // Finish SignUpActivity to goto the LoginActivity
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Finish SignUpActivity to goto the LoginActivity
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendOtp(View view) {
        if (editTextPhone.length() == 10) {
            phoneText.setText(editTextPhone.getText().toString());
            phonenumber = "+91" + editTextPhone.getText().toString().trim();
//          //  Toast.makeText(this, phonenumber, Toast.LENGTH_SHORT).show();
            checkPhoneAvailability();

        } else {
            editTextPhone.setError("Enter valid number");
            editText.requestFocus();
        }
    }

    private void checkPhoneAvailability() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phone", editTextPhone.getText().toString().trim());
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.CHECK_AVAIL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                       // Toast.makeText(PhoneAuth.this, status, Toast.LENGTH_SHORT).show();
                        if (status.equalsIgnoreCase("success")) {
                            sendVerificationCode(phonenumber);
                            updateCountDownText();
                            startTimer();
                        }
                        else {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Don't have any registered account with this number!!!", Snackbar.LENGTH_LONG)
                                    .setAction("DISMISS", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Try new registration!", Snackbar.LENGTH_SHORT);
                                            snackbar1.show();
                                        }
                                    });

                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhoneAuth.this, "2"+error.getMessage(), Toast.LENGTH_SHORT).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setShouldCache(false);
            MySingleton.getInstance(PhoneAuth.this).addToRequestQueue(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void veifyOtp(View view) {
    }

    private void processRegistration() {
    }

    public void callOnbackpressed(View view) {
        super.onBackPressed();
    }
    public void signUpClick(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }
}
