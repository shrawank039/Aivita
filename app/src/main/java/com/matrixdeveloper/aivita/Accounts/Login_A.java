package com.matrixdeveloper.aivita.Accounts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.matrixdeveloper.aivita.ApiServices.PrefManager;
import com.matrixdeveloper.aivita.Main_Menu.MainMenuActivity;
import com.matrixdeveloper.aivita.Net.parser.LoginParser;
import com.matrixdeveloper.aivita.PhoneAuth.NewPassword;
import com.matrixdeveloper.aivita.PhoneAuth.PhoneAuth;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matrixdeveloper.aivita.model.AuthBean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Login_A extends Activity {
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    IOSDialog iosDialog;
    SharedPreferences sharedPreferences;
    TextView createAccount;
    private String verificationId;
   // View top_view;
    Button login_btn;
    String TAG = "Login_A";
    RelativeLayout rlPhone,rlOtp,rlReferral;
    EditText tv_username, otp; //password;
    private PrefManager prefManager;
    String email;
    Spinner spinnerCountryCodes;
    CoordinatorLayout rl;
    String mode ="+91",otpCode="";
    Boolean userExist =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        this.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        rlPhone= (RelativeLayout) findViewById(R.id.rl_login_details);
        otp = findViewById(R.id.edt_otp);
        rlReferral = findViewById(R.id.rl_referral);
        rlOtp = (RelativeLayout) findViewById(R.id.rl_otp);
        rlOtp.setVisibility(View.GONE);
        rlPhone.setVisibility(View.VISIBLE);
        rl = findViewById(R.id.coordinatorLayout);
        //createAccount = findViewById(R.id.tc_createaccount);
        login_btn = findViewById(R.id.login_btn);
       // password = findViewById(R.id.password);
        tv_username = findViewById(R.id.simpleEditText);

        spinnerCountryCodes = (Spinner) findViewById(R.id.spinner_country_code);
        // ArrayList<String> arrayList = new ArrayList<>();
        List<String> Lines = Arrays.asList(getResources().getStringArray(R.array.countrycodes));
//        arrayList.add("+91");
//        arrayList.add("private");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Lines);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountryCodes.setAdapter(arrayAdapter);
        spinnerCountryCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                mode = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Selected: " + mode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });


        login_btn.setOnClickListener(v -> {

            if (tv_username.getText().toString().trim().isEmpty()) {
                tv_username.setError("Phone number is required!");
                tv_username.requestFocus();
//            } else if (password.getText().toString().trim().isEmpty()) {
//                password.setError("password is required!");
//                password.requestFocus();
            } else {
                email = tv_username.getText().toString();

                    if (email.length() <= 9) {

                            Toast.makeText(this, "Please enter valid phone number or email!", Toast.LENGTH_LONG).show();

                    }  else {
                       // String password1 = password.getText().toString().trim();
                        // Login(username,password1);
                        login(mode+email, "");
                    }
                }

                // TODO Auto-generated method stub
//                loading.setVisibility(View.VISIBLE);
//                btnLogin.setVisibility(View.GONE);
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
//
//        // if the user is already login trought facebook then we will logout the user automatically
     //   LoginManager.getInstance().logOut();

        iosDialog = new IOSDialog.Builder(this)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();

        sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

//        findViewById(R.id.facebook_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Loginwith_FB();
//            }
//        });


        findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign_in_with_gmail();
            }
        });


//        findViewById(R.id.tc_createaccount).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call_Api_For_Signup(i);
//            }
//        });


        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

      //  top_view = findViewById(R.id.top_view);


     //   printKeyHash();


//        createAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Login_A.this, SignUpActivity.class));
//            }
//        });

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
//        top_view.startAnimation(anim);
//        top_view.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
      //  top_view.setVisibility(View.GONE);
        finish();
        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);

    }


    // Bottom two function are related to Fb implimentation
 //   private CallbackManager mCallbackManager;

    //facebook implimentation
//    public void Loginwith_FB() {
//
//        LoginManager.getInstance()
//                .logInWithReadPermissions(Login_A.this,
//                        Arrays.asList("public_profile", "email"));
//
//        // initialze the facebook sdk and request to facebook for login
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//                Log.d("resp_token", loginResult.getAccessToken() + "");
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Toast.makeText(Login_A.this, "Login Cancel", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("resp", "" + error.toString());
//                Toast.makeText(Login_A.this, "Login Error" + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//
//    }

//    private void handleFacebookAccessToken(final AccessToken token) {
//        // if user is login then this method will call and
//        // facebook will return us a token which will user for get the info of user
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        Log.d("resp_token", token.getToken() + "");
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            iosDialog.show();
//                            final String id = Profile.getCurrentProfile().getId();
//                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
//                                @Override
//                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {
//
//                                    Log.d("resp", user.toString());
//                                    //after get the info of user we will pass to function which will store the info in our server
//
//                                    String fname = "" + user.optString("first_name");
//                                    String lname = "" + user.optString("last_name");
//
//
//                                    if (fname.equals("") || fname.equals("null"))
//                                        fname = getResources().getString(R.string.app_name);
//
//                                    if (lname.equals("") || lname.equals("null"))
//                                        lname = "";
//
//                                    Toast.makeText(Login_A.this, "Facebood "+fname, Toast.LENGTH_SHORT).show();
////                                    Call_Api_For_Signup("" + id, fname
////                                            , lname,
////                                            "https://graph.facebook.com/" + id + "/picture?width=500&width=500",
////                                            "facebook");
//
//                                }
//                            });
//
//                            // here is the request to facebook sdk for which type of info we have required
//                            Bundle parameters = new Bundle();
//                            parameters.putString("fields", "last_name,first_name,email");
//                            request.setParameters(parameters);
//                            request.executeAsync();
//                        } else {
//
//                            Toast.makeText(Login_A.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
//        else if (mCallbackManager != null)
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }


    //google Implimentation
    GoogleSignInClient mGoogleSignInClient;

    public void Sign_in_with_gmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Login_A.this);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 123);

        if (account != null) {
            String id = account.getId();
            String fname = account.getGivenName();
            String lname = account.getFamilyName();
            String personEmail = account.getEmail();

            String pic_url;
            if (account.getPhotoUrl() != null) {
                pic_url = account.getPhotoUrl().toString();
            } else {
                pic_url = "null";
            }


            socialLogin(personEmail);

        }

    }

    private void socialLogin(String personEmail) {

        JSONObject parameter = new JSONObject();
        try {
            parameter.put("email",personEmail);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        iosDialog.show();
        ApiRequest.Call_Api(getApplicationContext(), Variables.social_login, parameter, new Callback() {
            @Override
            public void Responce(String resp) {
                iosDialog.cancel();
                Log.d(TAG, "login :- " + resp);

                Parse_login_data(resp);

            }
        });
    }


    // Relate to google login
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String id = account.getId();
                String fname = account.getGivenName();
                String lname = account.getFamilyName();

                // if we do not get the picture of user then we will use default profile picture

                String pic_url;
                if (account.getPhotoUrl() != null) {
                    pic_url = account.getPhotoUrl().toString();
                } else {
                    pic_url = "null";
                }


                if (fname.equals("") || fname.equals("null"))
                    fname = getResources().getString(R.string.app_name);

                if (lname.equals("") || lname.equals("null"))
                    lname = "";

               // Call_Api_For_Signup(id, fname, lname, pic_url, "gmail");


            }
        } catch (ApiException e) {
            Log.w("Error message", "signInResult:failed code=" + e.getStatusCode());
        }

    }


    // this function call an Api for Signin
//    private void Call_Api_For_Signup(String id,
//                                     String f_name,
//                                     String l_name,
//                                     String picture,
//                                     String singnup_type) {


//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String appversion = packageInfo.versionName;
//
//        JSONObject parameters = new JSONObject();
//        try {
//
//            parameters.put("fb_id", id);
//            parameters.put("first_name", "" + f_name);
//            parameters.put("last_name", "" + l_name);
//            parameters.put("profile_pic", picture);
//            parameters.put("gender", "m");
//            parameters.put("version", appversion);
//            parameters.put("signup_type", singnup_type);
//            parameters.put("device", Variables.device);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        iosDialog.show();
//        ApiRequest.Call_Api(this, Variables.SignUp, parameters, new Callback() {
//            @Override
//            public void Responce(String resp) {
//                iosDialog.cancel();
//                Parse_signup_data(resp);
//
//            }
//        });
//
//    }


    // if the signup successfull then this method will call and it store the user info in local
//    public void Parse_signup_data(String loginData) {
//        try {
//            JSONObject jsonObject = new JSONObject(loginData);
//            String code = jsonObject.optString("code");
//            if (code.equals("200")) {
//                JSONArray jsonArray = jsonObject.getJSONArray("msg");
//                JSONObject userdata = jsonArray.getJSONObject(0);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(Variables.u_id, userdata.optString("fb_id"));
//                editor.putString(Variables.f_name, userdata.optString("first_name"));
//                editor.putString(Variables.l_name, userdata.optString("last_name"));
//                editor.putString(Variables.u_name, userdata.optString("first_name") + " " + userdata.optString("last_name"));
//                editor.putString(Variables.gender, userdata.optString("gender"));
//                editor.putString(Variables.u_pic, userdata.optString("profile_pic"));
//                editor.putBoolean(Variables.islogin, true);
//                editor.commit();
//
//                Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
//                Variables.user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
//
//                top_view.setVisibility(View.GONE);
//                finish();
//                startActivity(new Intent(this, MainMenuActivity.class));
//
//
//            } else {
//                Toast.makeText(this, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (JSONException e) {
//            Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//    }


    // this function will print the keyhash of your project
    // which is very helpfull during Fb login implimentation
//    public void printKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.i("keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }




   /* public void Login(String username, String password){
        RetrofitApi apiService = ApiClient.getClient().create(RetrofitApi.class);
        Call<LoginModel> call=apiService.login(username,password);
        call.enqueue(new retrofit2.Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.errorBody()==null&&response.body()!=null ){
                    Gson newq =new Gson();



                    System.out.println("**************** resp "+newq.toJson(response.body())+"    "+ response.body() + "   "+response.body().toString());

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(Variables.username,username);
                    editor.putString(Variables.password,password);
                    editor.putBoolean(Variables.islogin,true);
                    editor.commit();

                    Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

                    //  Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
                    System.out.println("******** shar pref "+ Variables.sharedPreferences.getString(Variables.username, "")+ " pass value "+
                            Variables.sharedPreferences.getString(Variables.password, ""));


                    //  prefManager.setUserName(username);
                    //  prefManager.setPassword(password);


                    startActivity(new Intent(Login_A.this,MainMenuActivity.class));
                    Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Check Your Credentials",Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Check Your Credentials",Toast.LENGTH_LONG).show();
            }
        });

    }*/


    // this function call an Api for Signin
    private void login(String username, String password) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion = packageInfo.versionName;

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("password", password);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendVerificationCode(username);

        iosDialog.show();
        ApiRequest.Call_Api(this, Variables.send_otp, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                iosDialog.cancel();
                Log.e(TAG, "login :- " + resp);

                JSONObject userdata = null;
                try {
                    userdata = new JSONObject(resp);
                    if (userdata.optString("status").equalsIgnoreCase("success")) {
                        if (userdata.optString("userExits").equalsIgnoreCase("true"))
                            userExist = true;
                            rlReferral.setVisibility(View.GONE);
                        rlPhone.setVisibility(View.GONE);
                        rlOtp.setVisibility(View.VISIBLE);

                        Toast.makeText(Login_A.this, "OTP has been sent successfully.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void verifyCode(String code) {
        otp.setText(code);
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e){
            Toast.makeText(this, "OTP didn't match!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            JSONObject parameter = new JSONObject();
                            try {
                                EditText ref = findViewById(R.id.edt_referral);
                                parameter.put("phone", mode + email);
                                parameter.put("otp", "123456");
                                parameter.put("referal", ref.getText().toString().trim());
                                parameter.put("email","");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                                iosDialog.show();
                            ApiRequest.Call_Api(getApplicationContext(), Variables.verify_otp, parameter, new Callback() {
                                @Override
                                public void Responce(String resp) {
                                iosDialog.cancel();
                                    Log.d(TAG, "login :- " + resp);

                                    Parse_login_data(resp);

                                }
                            });
                        } else {
                            iosDialog.show();
                            Toast.makeText(getApplicationContext(), "0 "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
      //  progressBar.setVisibility(View.VISIBLE);
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
            otpCode = phoneAuthCredential.getSmsCode();
            otp.setText(otpCode);
            if (otpCode != null && userExist) {
                verifyCode(otpCode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), "10 "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // if the signup successfull then this method will call and it store the user info in local
    public void Parse_login_data(String loginData) {
        try {
            JSONObject userdata = new JSONObject(loginData);
            if (userdata.optString("status").equalsIgnoreCase("success")) {
                JSONObject dataObj = userdata.optJSONObject("data");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                assert dataObj != null;
                editor.putString(Variables.username, dataObj.optString("username"));

                String a = Variables.sharedPreferences.getString(Variables.username, null);
                Toast.makeText(Login_A.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                editor.putString(Variables.password, dataObj.optString("password"));
                editor.putString(Variables.u_id, dataObj.optString("fb_id"));
                editor.putString(Variables.f_name, dataObj.optString("first_name"));
                editor.putString(Variables.l_name, dataObj.optString("last_name"));
                editor.putString(Variables.u_name, dataObj.optString("first_name") + " " + dataObj.optString("last_name"));
                editor.putString(Variables.gender, dataObj.optString("gender"));
                editor.putString(Variables.u_pic, dataObj.optString("profile_pic"));
                editor.putBoolean(Variables.islogin, true);
                editor.apply();
                Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
                Variables.user_id = Variables.sharedPreferences.getString(Variables.u_id, "0");
              //  top_view.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(this, MainMenuActivity.class));
            } else {
                Toast.makeText(this, userdata.optString("msg"), Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void goBack(View view) {
        rlPhone.setVisibility(View.VISIBLE);
        rlOtp.setVisibility(View.GONE);
        userExist = false;
        onBackPressed();
    }

    public void submitOTP(View view) {

        String code = otp.getText().toString().trim();

        if (code.isEmpty() || code.length() < 6) {
            otp.setError("Enter valid code.");
            otp.requestFocus();
            return;
        }
        else {
            verifyCode(code);
        }


        }

//    public void forgotPassword(View view) {
//        startActivity(new Intent(getApplicationContext(), PhoneAuth.class));
//    }
}
