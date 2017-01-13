package com.androidapp.jdklokhandwala.api;

import com.androidapp.jdklokhandwala.api.model.AboutUsResponse;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.CategoryResponse;
import com.androidapp.jdklokhandwala.api.model.ChangePasswordReq;
import com.androidapp.jdklokhandwala.api.model.CityRes;
import com.androidapp.jdklokhandwala.api.model.ContactUsResponse;
import com.androidapp.jdklokhandwala.api.model.LoginReq;
import com.androidapp.jdklokhandwala.api.model.OrderItemRes;
import com.androidapp.jdklokhandwala.api.model.PlaceOrderReq;
import com.androidapp.jdklokhandwala.api.model.ProductResponse;
import com.androidapp.jdklokhandwala.api.model.RegistrationReq;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UpdateUserRequest;
import com.androidapp.jdklokhandwala.api.model.UpdateUserResp;
import com.androidapp.jdklokhandwala.api.model.UserIdentityTypeRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.helper.AppConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ishan on 05-01-2017.
 */

public interface AppApi {

    //this api for get all categories
    @GET(AppConstants.GetCategoryUrl)
    Call<CategoryResponse> getCategoryApi();

    //this api for get about us text
    @GET(AppConstants.GetAboutUsUrl)
    Call<AboutUsResponse> getAboutUsApi();

    //this api for get about us text
    @GET(AppConstants.GetContactUsUrl)
    Call<ContactUsResponse> getContactUsApi();

    //this api for get about us text
    @GET(AppConstants.GetProductsUrl)
    Call<ProductResponse> getProductsApi(@Path("CategoryID") int id);

    //this api for get user registration
    @POST(AppConstants.RegistrationUrl)
    Call<RegistrationRes> registrationApi(@Body RegistrationReq registrationReq);

    //this api for get user login
    @POST(AppConstants.LoginUrl)
    Call<RegistrationRes> loginApi(@Body LoginReq loginReq);

    //this api for get user profile
    @GET(AppConstants.GetProfileUrl)
    Call<RegistrationRes> getProfileApi(@Path("UserID") int id);

    //this api for change password
    @POST(AppConstants.ChangePasswordUrl)
    Call<BaseResponse> changePassword(@Body ChangePasswordReq changePasswordReq);

    //this api for get user identity type
    @GET(AppConstants.GetUSerIdentityTypeUrl)
    Call<UserIdentityTypeRes> getIdentityType();

    //this api for place order
    @POST(AppConstants.PlaceQuotationOrOrderUrl)
    Call<BaseResponse> placeQuotationOrOrder(@Body PlaceOrderReq placeOrderReq);

    //this api for place order
    @GET(AppConstants.GetCityByPinUrl)
    Call<CityRes> getCity(@Query("Pincode") int pincode);

    //this api for get order list
    @GET(AppConstants.GetHistoryUrl)
    Call<OrderItemRes> getHistoryList(@Query("userID") int userID,@Query("lastRecordID") int lastRecordID,@Query("mode") int mode);

    @POST(AppConstants.UpdateUserUrl)
    Call<UpdateUserResp> updateUser(@Body UpdateUserRequest updateUserRequest);
}
