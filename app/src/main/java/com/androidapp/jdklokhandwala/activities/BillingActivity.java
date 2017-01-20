package com.androidapp.jdklokhandwala.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AcceptOrder;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartTemp;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.CityRes;
import com.androidapp.jdklokhandwala.api.model.PlaceOrderReq;
import com.androidapp.jdklokhandwala.api.model.UpdateUserRequest;
import com.androidapp.jdklokhandwala.api.model.UpdateUserResp;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private CheckBox useShippingAddressCB;
    private CheckBox useBillingAddressCB;
    private TfEditText enterShippingAddress1;
    private TfEditText enterShippingAddress2;
    private TfEditText enterSPincode;
    private TfEditText enterScity;
    private TfEditText enterBillingAddress1;
    private TfEditText enterBillingAddress2;
    private TfEditText enterBPincode;
    private TfEditText enterBcity;
    private UserPojo userPojo;
    private TfButton submitBtn;
    private TfEditText enterBArea;
    private TfEditText enterSArea;
    private SpotsDialog dialog;
    private boolean isAccept = false;
    private int OrderID;

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
        setContentView(R.layout.activity_billing);
        init();
    }

    private void init() {

        Functions.hideKeyPad(this, findViewById(android.R.id.content));
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.billing_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isAccept = getIntent().getBooleanExtra(AppConstants.isAccept, false);
        OrderID = getIntent().getIntExtra("OrderID", 0);

        dialog = new SpotsDialog(BillingActivity.this, R.style.Custom);

        useShippingAddressCB = (CheckBox) findViewById(R.id.useShippingAddress);
        useBillingAddressCB = (CheckBox) findViewById(R.id.useBillingAddress);

        useShippingAddressCB.setTypeface(Functions.getBoldFont(BillingActivity.this));
        useBillingAddressCB.setTypeface(Functions.getBoldFont(BillingActivity.this));

        enterShippingAddress1 = (TfEditText) findViewById(R.id.enterSAddress1);
        enterShippingAddress2 = (TfEditText) findViewById(R.id.enterSAddress2);
        enterSPincode = (TfEditText) findViewById(R.id.enterSPincode);
        enterScity = (TfEditText) findViewById(R.id.enterSCity);
        enterSArea = (TfEditText) findViewById(R.id.enterSArea);

        enterBillingAddress1 = (TfEditText) findViewById(R.id.enterBAddress1);
        enterBillingAddress2 = (TfEditText) findViewById(R.id.enterBAddress2);
        enterBPincode = (TfEditText) findViewById(R.id.enterBPincode);
        enterBcity = (TfEditText) findViewById(R.id.enterBCity);
        enterBArea = (TfEditText) findViewById(R.id.enterBArea);

        submitBtn = (TfButton) findViewById(R.id.submitBtn);

        userPojo = PrefUtils.getUserFullProfileDetails(this);

        setShippingAddress();
        setBillingAddress();

        onCheckChangeListener();

        textChangedListener();

        clickListener();
    }

    private void onCheckChangeListener() {
        useBillingAddressCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setBillingToShippingAddress();
                }
            }
        });
        useShippingAddressCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setShippingToBillingAddress();
                }
            }
        });
    }

    private void clickListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(BillingActivity.this, view);
                if (validateField()) {
                    dialog.show();

                    List<AddToCart> list = AddToCart.getCartList();
                    List<AddToCartTemp> listInput = new ArrayList<AddToCartTemp>();
                    Double totalWeight = Double.valueOf(0);
                    for (int i = 0; i < list.size(); i++) {
                        totalWeight = totalWeight + list.get(i).KgWeight();
                        listInput.add(new AddToCartTemp(list.get(i).CategoryID(), list.get(i).ProductID(), list.get(i).Name(), list.get(i).UnitType(), list.get(i).UnitValue(), list.get(i).KgWeight()));
                    }

                    PlaceOrderReq placeOrderReq = new PlaceOrderReq();
                    placeOrderReq.setOrderID(0);
                    placeOrderReq.setIsOrder(1);
                    placeOrderReq.setUserID(userPojo.getUserID());
                    placeOrderReq.setTotalCartWeight(totalWeight);
                    placeOrderReq.setBillingAddress1(enterBillingAddress1.getText().toString().trim());
                    placeOrderReq.setBillingAddress2(enterBillingAddress2.getText().toString().trim());
                    placeOrderReq.setBillingPinCode(enterBPincode.getText().toString().trim());
                    placeOrderReq.setShippingAddress1(enterShippingAddress1.getText().toString().trim());
                    placeOrderReq.setShippingAddress2(enterShippingAddress2.getText().toString().trim());
                    placeOrderReq.setShippingPinCode(enterSPincode.getText().toString().trim());
                    placeOrderReq.setPaymentMethodID(21);
                    placeOrderReq.setTotalCartItem(listInput.size());
                    placeOrderReq.setCartItemList(listInput);


                    Log.e("IsAccept",isAccept+"");
                    if (isAccept) {
                        doupdateProfile();
                    } else {
                        doPlaceOrderApiCall(placeOrderReq);
                    }


                }
            }
        });
    }

    private void doupdateProfile() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.UserID = userPojo.getUserID();
        updateUserRequest.Name = userPojo.getName().trim();
        updateUserRequest.Mobile = userPojo.getMobile();
        updateUserRequest.EmailID = userPojo.getEmailID().trim();
        updateUserRequest.BillingAddress1 = enterBillingAddress1.getText().toString().trim();
        updateUserRequest.BillingAddress2 = enterBillingAddress2.getText().toString().trim();
        updateUserRequest.BillingPinCode = enterBPincode.getText().toString().trim();
        updateUserRequest.ShippingAddress1 = enterShippingAddress1.getText().toString().trim();
        updateUserRequest.ShippingAddress2 = enterShippingAddress2.getText().toString().trim();
        updateUserRequest.ShippingPinCode = enterSPincode.getText().toString().trim();

        updateUserApiCall(updateUserRequest);
    }

    private void textChangedListener() {

        enterBPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 6) {
                    //setBillingCity(Integer.valueOf(charSequence.toString().trim()));
                    Functions.setCity(Integer.valueOf(charSequence.toString().trim()), enterBcity, enterBArea);
                } else {
                    enterBcity.setText("");
                    enterBArea.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        enterSPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().length() == 6) {
                    Functions.setCity(Integer.valueOf(charSequence.toString().trim()), enterScity, enterSArea);
                    //setShippingCity(Integer.valueOf(charSequence.toString().trim()));
                } else {
                    enterScity.setText("");
                    enterSArea.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private boolean validateField() {
        if (enterBillingAddress1.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter billing address line 1");
            return false;
        } else if (enterBillingAddress2.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter billing address line 2");
            return false;
        } else if (enterBPincode.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter billing pin code");
            return false;
        } else if (enterBPincode.getText().toString().trim().length() != 6) {
            Functions.showToast(BillingActivity.this, "Please enter valid billing pin code");
            return false;
        } else if (enterShippingAddress1.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter delivery address line 1");
            return false;
        } else if (enterShippingAddress2.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter delivery address line 2");
            return false;
        } else if (enterSPincode.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter delivery pin code");
            return false;
        } else if (enterSPincode.getText().toString().trim().length() != 6) {
            Functions.showToast(BillingActivity.this, "Please enter valid delivery pin code");
            return false;
        } else if (enterBcity.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter valid billing pin code");
            return false;
        } else if (enterScity.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter valid delivery pin code");
            return false;
        } else if (enterBArea.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter valid billing pin code");
            return false;
        } else if (enterSArea.getText().toString().trim().length() == 0) {
            Functions.showToast(BillingActivity.this, "Please enter valid delivery pin code");
            return false;
        }
        return true;
    }

    private void setBillingToShippingAddress() {
        if (enterBillingAddress1.getText().toString().trim().length() > 0) {
            enterShippingAddress1.setText(enterBillingAddress1.getText().toString().trim());
        }
        if (enterBillingAddress2.getText().toString().trim().length() > 0) {
            enterShippingAddress2.setText(enterBillingAddress2.getText().toString().trim());
        }
        if (enterBPincode.getText().toString().trim().length() > 0) {
            enterSPincode.setText(enterBPincode.getText().toString().trim());
        }
        if (enterBcity.getText().toString().trim().length() > 0) {
            enterScity.setText(enterBcity.getText().toString().trim());
        }
        if (enterBArea.getText().toString().trim().length() > 0) {
            enterSArea.setText(enterBArea.getText().toString().trim());
        }
    }

    private void setShippingToBillingAddress() {
        if (enterShippingAddress1.getText().toString().trim().length() > 0) {
            enterBillingAddress1.setText(enterShippingAddress1.getText().toString().trim());
        }
        if (enterShippingAddress2.getText().toString().trim().length() > 0) {
            enterBillingAddress2.setText(enterShippingAddress2.getText().toString().trim());
        }
        if (enterSPincode.getText().toString().trim().length() > 0) {
            enterBPincode.setText(enterSPincode.getText().toString().trim());
        }
        if (enterScity.getText().toString().trim().length() > 0) {
            enterBcity.setText(enterScity.getText().toString().trim());
        }
        if (enterSArea.getText().toString().trim().length() > 0) {
            enterBArea.setText(enterSArea.getText().toString().trim());
        }
    }

    private void setShippingAddress() {
        enterShippingAddress1.setText(Functions.checkString(userPojo.getShippingAddress1()));
        enterShippingAddress2.setText(Functions.checkString(userPojo.getShippingAddress2()));
        enterScity.setText(Functions.checkString(userPojo.getShippingCity()));
        enterSPincode.setText(Functions.checkString(userPojo.getShippingPinCode()));
        if (userPojo.getShippingPinCode() != null && userPojo.getShippingPinCode().length() == 6) {
            setShippingCity(Integer.valueOf(userPojo.getShippingPinCode().trim()));
        }
        enterSArea.setText(Functions.checkString(userPojo.getShippingArea()));
    }

    private void setBillingAddress() {
        enterBillingAddress1.setText(Functions.checkString(Functions.checkString(userPojo.getBillingAddress1())));
        enterBillingAddress2.setText(Functions.checkString(userPojo.getBillingAddress2()));
        enterBcity.setText(Functions.checkString(userPojo.getBillingCity()));
        enterBPincode.setText(Functions.checkString(userPojo.getBillingPinCode()));
        if (userPojo.getBillingPinCode() != null && userPojo.getBillingPinCode().length() == 6) {
            setBillingCity(Integer.valueOf(userPojo.getBillingPinCode().trim()));
        }
        enterBArea.setText(Functions.checkString(userPojo.getBillingArea()));
    }

    private void setBillingCity(int pincode) {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getCity(pincode).enqueue(new Callback<CityRes>() {
            @Override
            public void onResponse(Call<CityRes> call, Response<CityRes> response) {
                if (response.body() != null && response.body().getData() != null) {
                    enterBcity.setText(response.body().getData().getCity());
                    enterBArea.setText(response.body().getData().getArea());
                }
            }

            @Override
            public void onFailure(Call<CityRes> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void setShippingCity(int pincode) {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getCity(pincode).enqueue(new Callback<CityRes>() {
            @Override
            public void onResponse(Call<CityRes> call, Response<CityRes> response) {
                if (response.body() != null && response.body().getData() != null) {
                    enterScity.setText(response.body().getData().getCity());
                    enterSArea.setText(response.body().getData().getArea());
                }
            }

            @Override
            public void onFailure(Call<CityRes> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void doPlaceOrderApiCall(PlaceOrderReq placeOrderReq) {
        Log.e("place order req", MyApplication.getGson().toJson(placeOrderReq).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.placeQuotationOrOrder(placeOrderReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                dialog.dismiss();
                if (response.body() != null && response.body().getResponseMessage() != null) {
                    Log.e("place order res", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getResponseCode()==1) {
                        AddToCart.DeleteAllData();
                        Functions.showToast(BillingActivity.this, response.body().getResponseMessage());
                        Intent i = new Intent(BillingActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(BillingActivity.this, i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Functions.showToast(BillingActivity.this, "Fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    private void updateUserApiCall(UpdateUserRequest updateUserRequest) {

//        Log.e("update user",Functions.jsonString(updateUserRequest));

        Log.e("place order req", MyApplication.getGson().toJson(updateUserRequest).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.updateUser(updateUserRequest).enqueue(new Callback<UpdateUserResp>() {
            @Override
            public void onResponse(Call<UpdateUserResp> call, Response<UpdateUserResp> response) {
                dialog.dismiss();
                if (response.body() != null && response.body().getResponseMessage() != null) {
                    Log.e("update user resp", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getResponseCode() == 1) {
                        AcceptOrder acceptOrder = new AcceptOrder();
                        acceptOrder.setOrderID(OrderID);
                        acceptOrder.setIsAccept(1);
                        callApi(acceptOrder);
                    } else {
                        Functions.showToast(BillingActivity.this, "Fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResp> call, Throwable t) {
                dialog.dismiss();
            }

        });
    }


    private void callApi(AcceptOrder acceptOrder) {
        Log.e("order accept",Functions.jsonString(acceptOrder));
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.acceptRejectQuotation(acceptOrder).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    Log.e("order accept res",Functions.jsonString(response.body()));
                    if (response.body().getResponseCode() == 1) {
                        Functions.showToast(BillingActivity.this, response.body().getResponseMessage());
                        Intent i = new Intent(BillingActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(BillingActivity.this, i);
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
