package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartTemp;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.PlaceOrderReq;
import com.androidapp.jdklokhandwala.api.model.RegistrationReq;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UserIdentityType;
import com.androidapp.jdklokhandwala.api.model.UserIdentityTypeRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private TfButton registerBtn;
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private TfEditText nameTV;
    private TfEditText emailTV;
    private TfEditText proofTV;
    private TfEditText phoneTV;
    private TfEditText passwordTV;
    private Spinner proofTypeSpinner;
    private String deviceId;
    private List<UserIdentityType> proofList;
    private UserPojo userPojo;
    private int isPlaceOrder;
    private int paymentMethodID;
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
        setContentView(R.layout.activity_registration);
        Functions.hideKeyPad(this, findViewById(android.R.id.content));
        init();
    }

    private void init() {

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.registration_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isPlaceOrder = getIntent().getIntExtra(AppConstants.isPlaceOrder, 2);
        paymentMethodID= getIntent().getIntExtra(AppConstants.paymentMethodID, 20);

        getUserIdentityType();

        dialog = new SpotsDialog(RegistrationActivity.this, R.style.Custom);
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        nameTV = (TfEditText) findViewById(R.id.enterName);
        emailTV = (TfEditText) findViewById(R.id.enterEmailId);
        phoneTV = (TfEditText) findViewById(R.id.enterPhone);
        passwordTV = (TfEditText) findViewById(R.id.enterPassword);
        proofTV = (TfEditText) findViewById(R.id.enterProof);
        proofTypeSpinner = (Spinner) findViewById(R.id.proofType);
        registerBtn = (TfButton) findViewById(R.id.registerBtn);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateField()) {
                    dialog.show();
                    RegistrationReq registrationReq = new RegistrationReq();
                    registrationReq.setName(nameTV.getText().toString().trim());
                    registrationReq.setEmailID(emailTV.getText().toString().trim());
                    registrationReq.setMobile(phoneTV.getText().toString().trim());
                    registrationReq.setPassword(passwordTV.getText().toString().trim());
                    registrationReq.setDeviceID(deviceId);
                    String fcmToken = PrefUtils.getFCMToken(RegistrationActivity.this);
                    if (fcmToken != null && fcmToken.trim().length() > 0) {
                        registrationReq.setGCMToken(fcmToken);
                    } else {
                        registrationReq.setGCMToken(FirebaseInstanceId.getInstance().getToken());
                    }
                    if (proofList != null && proofList.size() > 0) {
                        registrationReq.setIdentityTypeID(proofList.get(proofTypeSpinner.getSelectedItemPosition()).getCodeID());
                    } else {
                        registrationReq.setIdentityTypeID(0);
                    }
                    registrationReq.setIdentityNo(proofTV.getText().toString().trim());

                    doRegistar(registrationReq);
                }


            }

            private void doRegistar(RegistrationReq input) {
                Log.e("registration req", MyApplication.getGson().toJson(input).toString());
                AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
                appApi.registrationApi(input).enqueue(new Callback<RegistrationRes>() {
                    @Override
                    public void onResponse(Call<RegistrationRes> call, Response<RegistrationRes> response) {
                        dialog.dismiss();
                        if (response.body() != null) {
                            Log.e("registration res", MyApplication.getGson().toJson(response.body()).toString());
                            if (response.body().getData() != null) {
                                Functions.showToast(RegistrationActivity.this, "Successfully Register.");
                                userPojo = response.body().getData();
                                Log.e("user", MyApplication.getGson().toJson(userPojo).toString());
                                PrefUtils.setLoggedIn(RegistrationActivity.this, true);
                                PrefUtils.setUserFullProfileDetails(RegistrationActivity.this, userPojo);
                                if (isPlaceOrder == 1) {
                                    Functions.fireIntent(RegistrationActivity.this, BillingActivity.class);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                } else if (isPlaceOrder == 0) {
                                    doGetQuotation();
                                } else {
                                    Intent i = new Intent(RegistrationActivity.this, DashboardActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    Functions.fireIntent(RegistrationActivity.this, i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                }
                            } else {
                                Functions.showToast(RegistrationActivity.this, "Unsuccessful.");
                            }
                        } else {
                            Functions.showToast(RegistrationActivity.this, "Some thing went wrong please try again later.");
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationRes> call, Throwable t) {
                        dialog.dismiss();
                        Functions.showToast(RegistrationActivity.this, "Some thing went wrong please try again later.");
                    }
                });
            }
        });
    }

    public boolean validateField() {
        if (nameTV.getText().toString().trim().length() == 0) {
            Functions.showToast(RegistrationActivity.this, "Please enter your name.");
            return false;
        } else if (emailTV.getText().toString().trim().length() == 0) {
            Functions.showToast(RegistrationActivity.this, "Please enter your email id.");
            return false;
        } else if (phoneTV.getText().toString().trim().length() == 0) {
            Functions.showToast(RegistrationActivity.this, "Please enter your phone number.");
            return false;
        } else if (passwordTV.getText().toString().trim().length() == 0) {
            Functions.showToast(RegistrationActivity.this, "Please enter your password.");
            return false;
        } else if (proofTV.getText().toString().trim().length() == 0) {
            Functions.showToast(RegistrationActivity.this, "Please enter proof id.");
            return false;
        }
        return true;
    }


    public void getUserIdentityType() {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getIdentityType().enqueue(new Callback<UserIdentityTypeRes>() {
            @Override
            public void onResponse(Call<UserIdentityTypeRes> call, Response<UserIdentityTypeRes> response) {
                ArrayList<String> proofTypeList = new ArrayList<String>();
                if (response.body() != null && response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                    proofList = response.body().getDataList();
                    for (int i = 0; i < response.body().getDataList().size(); i++) {
                        proofTypeList.add(response.body().getDataList().get(i).getCodeValue());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_item, R.id.spinnerItem, proofTypeList);
                    proofTypeSpinner.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<UserIdentityTypeRes> call, Throwable t) {

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
                        Functions.showToast(RegistrationActivity.this, response.body().getResponseMessage());
                        Intent i = new Intent(RegistrationActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(RegistrationActivity.this, i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }
}
