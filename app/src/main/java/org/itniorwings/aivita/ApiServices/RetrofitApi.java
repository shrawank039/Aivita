package org.itniorwings.aivita.ApiServices;

import org.itniorwings.aivita.model.LoginModel;
import org.itniorwings.aivita.model.RegisterModel;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApi {

    @POST("index.php?p=user_login")
    @FormUrlEncoded
    Call<LoginModel> login(
            @Field("username") String email,
            @Field("password") String password);

    @Multipart
    @POST("index.php?p=user_registration")
    Call<RegisterModel> Register(@Part("username") RequestBody username,
                                 @Part("email") RequestBody email,
                                 @Part("phone") RequestBody phone,
                                 @Part("password") RequestBody password ,
                                 @Part("fileToUpload") RequestBody image

    );


    @POST("index.php?p=user_registration")
    Call<String> AddTransport(@Body RequestBody body);


}