package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.ProofAdapter;
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
        paymentMethodID = getIntent().getIntExtra(AppConstants.paymentMethodID, 20);

        getUserIdentityType();

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
                    showProgress();
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

                    Log.e("registration req", Functions.jsonString(registrationReq));

                    doRegistar(registrationReq);
                }

            }

            private void doRegistar(RegistrationReq input) {

                AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
                appApi.registrationApi(input).enqueue(new Callback<RegistrationRes>() {
                    @Override
                    public void onResponse(Call<RegistrationRes> call, Response<RegistrationRes> response) {

                        hideProgress();

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
                                Functions.showToast(RegistrationActivity.this, response.body().getResponseMessage());
                            }
                        } else {
                            Functions.showToast(RegistrationActivity.this, "Some thing went wrong please try again later.");
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationRes> call, Throwable t) {
                        dialog.dismiss();
                        RetrofitErrorHelper.showErrorMsg(t, RegistrationActivity.this);
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

        } else {
            UserIdentityType type = (UserIdentityType) proofTypeSpinner.getSelectedItem();
            switch (type.getCodeID()) {
                case 1:
                    if (!Functions.toStr(proofTV).matches(AppConstants.AADHAR_CARD)) {
                        Functions.showToast(RegistrationActivity.this, "Invalid Adhar Card No.");
                        return false;
                    }
                    break;

                case 2:
                    if (!Functions.toStr(proofTV).matches(AppConstants.VOTER_ID)) {
                        Functions.showToast(RegistrationActivity.this, "Invalid Voter ID");
                        return false;
                    }
                    break;

                case 3:
                    if (!Functions.toStr(proofTV).matches(AppConstants.PAN_CARD)) {
                        Functions.showToast(RegistrationActivity.this, "Invalid PAN Card No.");
                        return false;
                    }
                    break;

                case 4:
                    if (!Functions.toStr(proofTV).matches(AppConstants.DRIVING_LICENSE)) {
                        Functions.showToast(RegistrationActivity.this, "Invalid Driving License No.");
                        return false;
                    }
                    break;

            }
            return true;
        }
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

                    ProofAdapter adapter = new ProofAdapter(RegistrationActivity.this, R.layout.spinner_item, proofList);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_item, R.id.spinnerItem, proofTypeList);
                    proofTypeSpinner.setAdapter(adapter);
                    proofTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            UserIdentityType type = adapter.getItem(i);
                            proofTV.setText("");
                            switch (type.getCodeID()) {
                                case 1:
                                    proofTV.setHint("Proof No. (e.g. 4552 6369 3654)");
                                    proofTV.setFloatingLabelText("Proof No. (e.g. 4552 6369 3654)");
                                    proofTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                                    break;

                                case 2:
                                    proofTV.setHint("Proof No. (e.g. GJG1234567)");
                                    proofTV.setFloatingLabelText("Proof No. (e.g. GJG1234567)");
                                    proofTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                                    break;

                                case 3:
                                    proofTV.setHint("Proof No. (e.g. CPFPP4441E)");
                                    proofTV.setFloatingLabelText("Proof No. (e.g. CPFPP4441E)");
                                    proofTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                                    break;

                                case 4:
                                    proofTV.setHint("Proof No. (e.g. GJ06 20110004369)");
                                    proofTV.setFloatingLabelText("Proof No. (e.g. GJ06 20110004369)");
                                    proofTV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Functions.showToast(RegistrationActivity.this, response.body().getResponseMessage().trim());
                }

            }

            @Override
            public void onFailure(Call<UserIdentityTypeRes> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, RegistrationActivity.this);
            }
        });
    }


    private void doGetQuotation() {
        showProgress();

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
        Log.e("place order req", Functions.jsonString(placeOrderReq));

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.placeQuotationOrOrder(placeOrderReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();

                if (response.body() != null) {
                    BaseResponse baseResponse = response.body();

                    Log.e("place order res", Functions.jsonString(baseResponse));

                    if (baseResponse.getResponseCode() == 1) {
                        AddToCart.DeleteAllData();
                        new OrderSuccessDialog(RegistrationActivity.this, "Q").show();
                    } else {
                        Functions.showToast(RegistrationActivity.this, response.body().getResponseMessage().trim());
                    }
                } else {
                    Functions.showToast(RegistrationActivity.this, response.body().getResponseMessage().trim());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, RegistrationActivity.this);
                hideProgress();
            }
        });
    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(RegistrationActivity.this, "", R.style.Custom);
        }
        dialog.show();
    }

    private void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
