package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartTemp;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.LoginReq;
import com.androidapp.jdklokhandwala.api.model.PlaceOrderReq;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.wheelpicker.OrderSuccessDialog;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TfButton loginBtn;
    private TfTextView signUpBtn;
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private TfEditText phoneTV;
    private TfEditText passwordTV;
    private String deviceId;
    private int isPlaceOrder;
    private int paymentMethodID;
    private UserPojo userPojo;
    private SpotsDialog dialog;

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.login_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new SpotsDialog(LoginActivity.this, R.style.Custom);

        isPlaceOrder = getIntent().getIntExtra(AppConstants.isPlaceOrder, 2);
        paymentMethodID = getIntent().getIntExtra(AppConstants.paymentMethodID, 20);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        phoneTV = (TfEditText) findViewById(R.id.enterPhone);
        passwordTV = (TfEditText) findViewById(R.id.enterPassword);

        loginBtn = (TfButton) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateField()) {
                    dialog.show();
                    LoginReq loginReq = new LoginReq();
                    loginReq.setMobile(phoneTV.getText().toString().trim());
                    loginReq.setPassword(passwordTV.getText().toString().trim());
                    loginReq.setDeviceID(deviceId);
                    //loginReq.setGCMToken("12345");
                    String fcmToken = PrefUtils.getFCMToken(LoginActivity.this);
                    if (fcmToken != null && fcmToken.trim().length() > 0) {
                        loginReq.setGCMToken(fcmToken);
                    } else {
                        loginReq.setGCMToken(FirebaseInstanceId.getInstance().getToken());
                    }
                    loginReq.setDeviceType("Android");
                    doLogin(loginReq);

                }
            }
        });


        signUpBtn = (TfTextView) findViewById(R.id.signUpBtn);
        signUpBtn.setText(Html.fromHtml("<u>New User? Sign Up</u>"));
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.putExtra(AppConstants.isPlaceOrder, isPlaceOrder);
                i.putExtra(AppConstants.paymentMethodID, paymentMethodID);
                Functions.fireIntent(LoginActivity.this, i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void doLogin(LoginReq input) {
        Log.e("login req", MyApplication.getGson().toJson(input).toString());
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.loginApi(input).enqueue(new Callback<RegistrationRes>() {
            @Override
            public void onResponse(Call<RegistrationRes> call, Response<RegistrationRes> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    Log.e("login req", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getData() != null) {
                        userPojo = response.body().getData();
                        Log.e("user", MyApplication.getGson().toJson(userPojo).toString());
                        PrefUtils.setLoggedIn(LoginActivity.this, true);
                        PrefUtils.setUserFullProfileDetails(LoginActivity.this, userPojo);
                        Functions.showToast(LoginActivity.this, "Successfully Logged in.");


                        if (isPlaceOrder == 1) {
                            Functions.fireIntent(LoginActivity.this, BillingActivity.class);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else if (isPlaceOrder == 0) {
                            doGetQuotation();
                        } else {
                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Functions.fireIntent(LoginActivity.this, i);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    } else {
                        Functions.showToast(LoginActivity.this, response.body().getResponseMessage());
                    }
                } else {
                    Functions.showToast(LoginActivity.this, "Some thing went wrong please try again later.");
                }
            }

            @Override
            public void onFailure(Call<RegistrationRes> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, LoginActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void doGetQuotation() {
        List<AddToCart> list = AddToCart.getCartList();
        List<AddToCartTemp> listInput = new ArrayList<AddToCartTemp>();
        Double totalWeight = Double.valueOf(0);
        for (int i = 0; i < list.size(); i++) {
            totalWeight = totalWeight + list.get(i).KgWeight();
            listInput.add(new AddToCartTemp(list.get(i).CategoryID(), list.get(i).ProductID(), list.get(i).Name(), list.get(i).UnitType(), list.get(i).UnitValue(), list.get(i).KgWeight()));
        }

        PlaceOrderReq placeOrderReq = new PlaceOrderReq();
        placeOrderReq.setOrderID(0);
        placeOrderReq.setIsOrder(0);
        placeOrderReq.setUserID(userPojo.getUserID());
        placeOrderReq.setTotalCartWeight(totalWeight);
        placeOrderReq.setPaymentMethodID(paymentMethodID);
        placeOrderReq.setTotalCartItem(listInput.size());
        placeOrderReq.setCartItemList(listInput);

        doPlaceOrderApiCall(placeOrderReq);
    }

    private void doPlaceOrderApiCall(PlaceOrderReq placeOrderReq) {
        Log.e("place order req", MyApplication.getGson().toJson(placeOrderReq).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.placeQuotationOrOrder(placeOrderReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null && response.body().getResponseMessage() != null) {
                    Log.e("place order res", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getResponseMessage().toString().trim().toLowerCase().contains("success")) {
                        AddToCart.DeleteAllData();
                        new OrderSuccessDialog(LoginActivity.this, "Q").show();
                    }else {
                    Functions.showToast(LoginActivity.this, "Some thing went wrong please try again later.");}
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, LoginActivity.this);
            }
        });
    }

    public boolean validateField() {
        if (phoneTV.getText().toString().trim().length() == 0) {
            Functions.showToast(LoginActivity.this, "Please enter your phone number.");
            return false;
        } else if (passwordTV.getText().toString().trim().length() == 0) {
            Functions.showToast(LoginActivity.this, "Please enter your password.");
            return false;
        }
        return true;
    }
}
