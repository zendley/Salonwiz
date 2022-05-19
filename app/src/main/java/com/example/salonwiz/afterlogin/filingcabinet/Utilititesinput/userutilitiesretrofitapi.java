package com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface userutilitiesretrofitapi {
    @Multipart
    @POST("/api/Userutilities/add")
    Call<ResponseBody> useradd(@Part("User") RequestBody User, @Part("UtilitiesTitle") RequestBody UtilitiesTitle, @Part("UtilitiesSupplier") RequestBody UtilitiesSupplier, @Part("ContractExpiryDate") RequestBody ContractExpiryDate, @Part("IsPaid") RequestBody IsPaid, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2);

    @Multipart
    @POST("/api/Userutilities/update")
    Call<ResponseBody> userutilupdate(@Part("Id") RequestBody Id,@Part("User") RequestBody User, @Part("UtilitiesTitle") RequestBody UtilitiesTitle, @Part("UtilitiesSupplier") RequestBody UtilitiesSupplier, @Part("ContractExpiryDate") RequestBody ContractExpiryDate, @Part("IsPaid") RequestBody IsPaid, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2);

    @Multipart
    @POST("/api/Userutilities/update")
    Call<ResponseBody> userutilupdateupdate(@Part("Id") RequestBody Id,@Part("User") RequestBody User, @Part("UtilitiesTitle") RequestBody UtilitiesTitle, @Part("UtilitiesSupplier") RequestBody UtilitiesSupplier, @Part("ContractExpiryDate") RequestBody ContractExpiryDate, @Part("IsPaid") RequestBody IsPaid, @Part MultipartBody.Part image1);

    @Multipart
    @POST("/api/Userutilities/update")
    Call<ResponseBody> userutilupdatewithoutimages(@Part("Id") RequestBody Id,@Part("User") RequestBody User, @Part("UtilitiesTitle") RequestBody UtilitiesTitle, @Part("UtilitiesSupplier") RequestBody UtilitiesSupplier, @Part("ContractExpiryDate") RequestBody ContractExpiryDate, @Part("IsPaid") RequestBody IsPaid);


    @Multipart
    @POST("/api/Userutilities/add")
    Call<ResponseBody> useraddinput(@Part("User") RequestBody User, @Part("UtilitiesTitle") RequestBody UtilitiesTitle);

    @Multipart
    @POST("/api/user/update")
    Call<ResponseBody> signupimage(@Part("Email") RequestBody Email,@Part MultipartBody.Part image1);
}
