package com.speckpro.salonwiz.retrofit;

import com.speckpro.salonwiz.newmodels.ApiResponse;
import com.speckpro.salonwiz.newmodels.DealsModel;
import com.speckpro.salonwiz.newmodels.FilingModel;
import com.speckpro.salonwiz.newmodels.KnowledgeModel;
import com.speckpro.salonwiz.newmodels.LoginModel;
import com.speckpro.salonwiz.newmodels.NotificationsModel;
import com.speckpro.salonwiz.newmodels.RegisterModel;
import com.speckpro.salonwiz.newmodels.ResetPasswordModel;
import com.speckpro.salonwiz.newmodels.UserUtilitiesModel;
import com.speckpro.salonwiz.newmodels.UtilitiesModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    //user login
    @FormUrlEncoded
    @POST("login")
    Call<ArrayList<LoginModel>> login(@Header("Accept") String acceptHeader, @Field("email") String email, @Field("password") String password);

    //user signup
    @FormUrlEncoded
    @POST("register")
    Call<ArrayList<RegisterModel>> register(@Header("Accept") String acceptHeader, @Field("email") String email, @Field("password") String password,
                                 @Field("firstname") String firstname, @Field("lastname") String lastname,
                                 @Field("contactnumber") String contactnumber, @Field("bussinessname") String bussinessname,
                                 @Field("bussinessaddress") String bussinessaddress, @Field("postcode") String postcode);

    //user update
    @FormUrlEncoded
    @POST("user/update")
    Call<ApiResponse> updateUser(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader,
                                       @Field("email") String email, @Field("firstname") String firstname,
                                       @Field("lastname") String lastname, @Field("contactnumber") String contactnumber,
                                       @Field("bussinessname") String bussinessname, @Field("bussinessaddress") String bussinessaddress,
                                       @Field("postcode") String postcode);

    //user logout
    @POST("logout")
    Call<ResponseBody> logout(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //user password reset
    @FormUrlEncoded
    @POST("user/update")
    Call<ResetPasswordModel> resetPassword(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader, @Field("email") String email);

    //add digital assitance
    @POST("digitalassistance/add")
    Call<ResponseBody> getAssistance(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //get all utilities
    @GET("utilties/get")
    Call<ArrayList<UtilitiesModel>> getAllUtilities(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //get user all utilities
    @GET("userutilties/all/get")
    Call<ArrayList<UserUtilitiesModel>> getUserAllUtilities(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //get user active utilities
    @GET("userutilties/active")
    Call<ArrayList<UserUtilitiesModel>> getUserActiveUtilities(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //get user inactive utilities
    @GET("userutilties/inactive")
    Call<ArrayList<UserUtilitiesModel>> getUserInActiveUtilities(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //add user utility
    @FormUrlEncoded
    @POST("userutilties/add")
    Call<ApiResponse> addUserUtility(@Header("Accept") String acceptHeader,
                                                @Header("Authorization") String authHeader,
                                                @Field("utiltity") String utiltity);

    //update a utility / fill form
    @Multipart
    @POST("userutilties/update")
    Call<ApiResponse> updateUtility(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader,
                                    @Part("id") RequestBody id, @Part("billpaid") RequestBody billpaid,
                                    @Part("supplier") RequestBody supplier, @Part("utiltity") RequestBody utiltity,
                                    @Part("expirationdate") RequestBody expirationdate,
                                    @Part MultipartBody.Part lastbill, @Part MultipartBody.Part loaform);

    //update a utility / fill form with bill
    @Multipart
    @POST("userutilties/update/bill")
    Call<ApiResponse> updateUtilityWithBill(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader,
                                            @Part("id") RequestBody id, @Part("billpaid") RequestBody billpaid,
                                            @Part("supplier") RequestBody supplier, @Part("utiltity") RequestBody utiltity,
                                            @Part("expirationdate") RequestBody expirationdate, @Part MultipartBody.Part lastbill);

    //update a utility / fill form without bill
    @Multipart
    @POST("userutilties/update/nobill")
    Call<ApiResponse> updateUtilityWithoutBill(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader,
                                               @Part("id") RequestBody id, @Part("billpaid") RequestBody billpaid,
                                               @Part("supplier") RequestBody supplier, @Part("utiltity") RequestBody utiltity,
                                               @Part("expirationdate") RequestBody expirationdate);

    //request user utility
    @FormUrlEncoded
    @POST("deals/request")
    Call<ApiResponse> requestUserUtility(@Header("Accept") String acceptHeader,
                                     @Header("Authorization") String authHeader,
                                     @Field("id") Integer id);

    //request user utility
    @FormUrlEncoded
    @POST("deals/get")
    Call<ArrayList<DealsModel>> getdealsOfUtility(@Header("Accept") String acceptHeader,
                                                  @Header("Authorization") String authHeader,
                                                  @Field("id") String id);

    //get all filing
    @FormUrlEncoded
    @POST("filing/get")
    Call<ArrayList<FilingModel>> getAllFilings(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader, @Field("category") String category);

    //add a filing
    @Multipart
    @POST("filing/add")
    Call<ApiResponse> addUserFiling(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader,
                                    @Part("title") RequestBody title,  @Part("category") RequestBody category,
                                    @Part MultipartBody.Part image);

    //delete a filing
    @FormUrlEncoded
    @POST("filing/delete")
    Call<ApiResponse> deleteFiling(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader, @Field("id") String id);

    //get knowledge base tutorials or articles
    @FormUrlEncoded
    @POST("knowledgebase/get")
    Call<ArrayList<KnowledgeModel>> getKnowledgebase(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader, @Field("category") String category);

    //get notifications
    @GET("notification/get")
    Call<ArrayList<NotificationsModel>> getNotifications(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader);

    //read a notification
    @FormUrlEncoded
    @POST("notification/read")
    Call<ApiResponse> markReadNotification(@Header("Accept") String acceptHeader, @Header("Authorization") String authHeader, @Field("id") Integer id);


}
