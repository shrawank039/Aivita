package com.matrixdeveloper.aivita.Accounts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.gson.Gson;

import com.matrixdeveloper.aivita.ApiServices.ApiClient;
import com.matrixdeveloper.aivita.ApiServices.RetrofitApi;
 import com.matrixdeveloper.aivita.R;
 import com.matrixdeveloper.aivita.SimpleClasses.Variables;


import java.io.ByteArrayOutputStream;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 import static com.matrixdeveloper.aivita.SimpleClasses.Variables.sharedPreferences;

public class SignUpActivity extends AppCompatActivity {
    EditText tv_username, tv_useremail, txtphone, password;
    String id;
    Button btnSignUp;
    ImageView imageView;
    Button btn_take_photo, btn_save;
    private static final int SELECT_PHOTO = 200;
    private static final int CAMERA_REQUEST = 1888;
    final int MY_PERMISSIONS_REQUEST_WRITE = 103;
    IOSDialog iosDialog;
    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView relogin = findViewById(R.id.tv_loginagain);
        tv_username = findViewById(R.id.simpleEditText);
        tv_useremail = findViewById(R.id.edtemail);
        password = findViewById(R.id.password);
        txtphone = findViewById(R.id.edtphone);
        btnSignUp = findViewById(R.id.btnSignUp);
        btn_take_photo = findViewById(R.id.btn_take_photo);
        imageView = findViewById(R.id.imageView);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
        }
        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, Login_A.class));
            }
        });





        btnSignUp.setOnClickListener(view -> {
            builder
                    .addFormDataPart("username", Objects.requireNonNull(tv_username.getText().toString()))
                    .addFormDataPart("email", Objects.requireNonNull(tv_useremail.getText().toString()))
                    .addFormDataPart("phone", Objects.requireNonNull(txtphone.getText().toString()))
                    .addFormDataPart("password ", Objects.requireNonNull(password.getText().toString()));
            RetrofitApi apiService = ApiClient.getRawClient().create(RetrofitApi.class);
            apiService.AddTransport(builder.build()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.errorBody()==null&&response.body()!=null ){
                        Gson newq =new Gson();

                        Log.d("SignUpA","SignUp : "+ response.body().toString());
                        Toast.makeText(SignUpActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        System.out.println("**************** resp "+newq.toJson(response.body())+"    "+ response.body() + "   "+response.body().toString());

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString(Variables.username, String.valueOf(tv_username));
                        editor.putString(Variables.email,String.valueOf(tv_useremail));
                        editor.putString(Variables.phone, String.valueOf(txtphone));
                        editor.putString(Variables.password,String.valueOf(password));
                        editor.putString(Variables.u_pic,String.valueOf(imageView));
                        editor.putString(Variables.u_id,String.valueOf(id));

                        editor.putBoolean(Variables.islogin,true);
                        editor.apply();

                        Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

                        //  Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
                        System.out.println("******** shar pref "+ Variables.sharedPreferences.getString(Variables.username, "")+ " pass value "+
                                Variables.sharedPreferences.getString(Variables.password, ""));


                        startActivity(new Intent(SignUpActivity.this,Login_A.class));
                     //   Toast.makeText(getApplicationContext(),"Register Successfull",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"Check Your Credentials",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        });
        btn_take_photo.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true), "Select Picture"), 1);
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == 1) {
            try {
                Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(Objects.requireNonNull(Objects.requireNonNull(data).getData())));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray());
                MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", System.currentTimeMillis() + ".jpg", requestFile);
                builder.addPart(body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }









}
