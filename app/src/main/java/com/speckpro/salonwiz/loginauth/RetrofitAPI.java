package com.speckpro.salonwiz.loginauth;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitAPI {
    @Headers({"Accept: application/json"})
    @POST("/api/user/signup")
   Call<registermodelclass> createPost(@Body registermodelclass mregistermodelclass);

    @Multipart
    @POST("/api/user/update")
   Call<ResponseBody> updateprofile(@Part("Email") RequestBody Email,@Part("FirstName") RequestBody FirstName,@Part("LastName") RequestBody LastName,@Part("BusinessName") RequestBody BusinessName,@Part("BusinessAddress") RequestBody BusinessAddress,@Part("City") RequestBody City);

    @Multipart
    @POST("/api/user/updatepassword")
    Call<ResponseBody> updatePassword(@Part("OldPassword") RequestBody OldPassword,@Part("NewPassword") RequestBody NewPassword,@Part("Email") RequestBody Email);


    @Headers({"Accept: application/json"})
    @POST("/api/user/login")
   Call<LoginResponse> createPost(@Body LoginResponse mLoginResponse);

    @Multipart
    @POST("/api/UserServices/add")
    Call<ResponseBody> useradd(@Part("User") RequestBody User, @Part("Status") RequestBody Status,@Part("Package") RequestBody Package,@Part("Date") RequestBody Date);

    @Multipart
    @POST("/api/user/addfile")
    Call<ResponseBody> generalsubmit(@Part("Date") RequestBody Date, @Part("Type") RequestBody Type, @Part("Title") RequestBody Title, @Part("Email") RequestBody Email, @Part MultipartBody.Part image);

    @Headers({"Accept: application/json"})
    @POST("/api/user/signup")
   Call<fbgregistermodelclass> dbgregister(@Body fbgregistermodelclass mregistermodelclass);

    @Multipart
    @POST("/api/Userutilities/getUtilitiesOfOneUser")
    Call<ResponseBody> checkpopup(@Part("User") RequestBody User);

}