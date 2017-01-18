package com.androidapp.jdklokhandwala.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.DashboardActivity;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UpdateUserRequest;
import com.androidapp.jdklokhandwala.api.model.UpdateUserResp;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.ChangePasswordDialog;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 29-12-2016.
 */

public class MyProfileFragment extends Fragment {

    private View parentView;
    private TfTextView changePassword;
    private ChangePasswordDialog changePasswordDialog;

    private TfEditText enterName;
    private TfEditText enterEmailId;
    private TfEditText enterMobileNo;

    private TfEditText enterSAddress1;
    private TfEditText enterSAddress2;
    private TfEditText enterSPincode;
    private TfEditText enterSCity;
    private TfEditText enterSArea;

    private TfEditText enterBAddress1;
    private TfEditText enterBAddress2;
    private TfEditText enterBPincode;
    private TfEditText enterBCity;
    private TfEditText enterBArea;

    private TfButton profileUpdate;
    private UserPojo userPojo;
    private SpotsDialog dialog;

    public static MyProfileFragment newInstance() {
        MyProfileFragment myProfileFragment = new MyProfileFragment();
        return myProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        init();
        return parentView;
    }

    private void init() {

        enterName = (TfEditText) parentView.findViewById(R.id.enterName);
        enterEmailId = (TfEditText) parentView.findViewById(R.id.enterEmailId);
        enterMobileNo = (TfEditText) parentView.findViewById(R.id.enterMobileNo);

        enterBAddress1 = (TfEditText) parentView.findViewById(R.id.enterBAddress1);
        enterBAddress2 = (TfEditText) parentView.findViewById(R.id.enterBAddress2);
        enterBPincode = (TfEditText) parentView.findViewById(R.id.enterBPincode);
        enterBCity = (TfEditText) parentView.findViewById(R.id.enterBCity);
        enterBArea = (TfEditText) parentView.findViewById(R.id.enterBArea);

        enterSAddress1 = (TfEditText) parentView.findViewById(R.id.enterSAddress1);
        enterSAddress2 = (TfEditText) parentView.findViewById(R.id.enterSAddress2);
        enterSPincode = (TfEditText) parentView.findViewById(R.id.enterSPincode);
        enterSCity = (TfEditText) parentView.findViewById(R.id.enterSCity);
        enterSArea = (TfEditText) parentView.findViewById(R.id.enterSArea);

        profileUpdate = (TfButton) parentView.findViewById(R.id.profileUpdate);

        dialog = new SpotsDialog(getActivity(), R.style.Custom);
        userPojo = PrefUtils.getUserFullProfileDetails(getActivity());
        updateUI(userPojo);
        getUserProfile(PrefUtils.getUserID(getActivity()));

        textChangedListener();

        clickListener();

        changePasswordDialog = new ChangePasswordDialog(getActivity(), new ChangePasswordDialog.OnUpdateClick() {
            @Override
            public void onUpdateClick(String newPassword) {
                Log.e("new Password", newPassword);
            }

            @Override
            public void onError(String error) {
                Log.e("error", error);
            }
        });
        changePassword = (TfTextView) parentView.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog.show();
            }
        });
    }

    private void getUserProfile(int userID) {
        Log.e("user id", userID + "");
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getProfileApi(userID).enqueue(new Callback<RegistrationRes>() {
            @Override
            public void onResponse(Call<RegistrationRes> call, Response<RegistrationRes> response) {
                Log.e("res", response.code() + "");

                if (response.body() != null && response.body().getData() != null) {
                    Log.e("profile", MyApplication.getGson().toJson(response.body()).toString());
                    UserPojo userPojo = response.body().getData();
                    Log.e("user", MyApplication.getGson().toJson(userPojo).toString());
                    PrefUtils.setUserFullProfileDetails(getActivity(), userPojo);
                    updateUI(userPojo);
                }
            }

            @Override
            public void onFailure(Call<RegistrationRes> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void updateUI(UserPojo userPojo) {

        enterName.setText(Functions.checkString(userPojo.getName()));
        enterEmailId.setText(Functions.checkString(userPojo.getEmailID()));
        enterMobileNo.setText(Functions.checkString(userPojo.getMobile()));

        enterBAddress1.setText(Functions.checkString(userPojo.getBillingAddress1()));
        enterBAddress2.setText(Functions.checkString(userPojo.getBillingAddress2()));
        enterBPincode.setText(Functions.checkString(userPojo.getBillingPinCode()));
        enterBCity.setText(Functions.checkString(userPojo.getBillingCity()));
        enterBArea.setText(Functions.checkString(userPojo.getBillingArea()));


        enterSAddress1.setText(Functions.checkString(userPojo.getShippingAddress1()));
        enterSAddress2.setText(Functions.checkString(userPojo.getShippingAddress2()));
        enterSPincode.setText(Functions.checkString(userPojo.getShippingPinCode()));
        enterSCity.setText(Functions.checkString(userPojo.getShippingCity()));
        enterSArea.setText(Functions.checkString(userPojo.getShippingArea()));
    }

    private void textChangedListener() {

        enterBPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 6) {
                    // setBillingCity(Integer.valueOf(charSequence.toString().trim()));
                    Functions.setCity(Integer.valueOf(charSequence.toString().trim()), enterBCity, enterBArea);
                } else {
                    enterBCity.setText("");
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
                    //setShippingCity(Integer.valueOf(charSequence.toString().trim()));
                    Functions.setCity(Integer.valueOf(charSequence.toString().trim()), enterSCity, enterSArea);
                } else {
                    enterSCity.setText("");
                    enterSArea.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void clickListener() {

        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(getActivity(), view);
                if (validateField()) {
                    dialog.show();

                    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                    updateUserRequest.UserID = userPojo.getUserID();
                    updateUserRequest.Name = enterName.getText().toString().trim();
                    updateUserRequest.Mobile = userPojo.getMobile();
                    updateUserRequest.EmailID = enterEmailId.getText().toString().trim();
                    updateUserRequest.BillingAddress1 = enterBAddress1.getText().toString().trim();
                    updateUserRequest.BillingAddress2 = enterBAddress2.getText().toString().trim();
                    updateUserRequest.BillingPinCode = enterBPincode.getText().toString().trim();
                    updateUserRequest.ShippingAddress1 = enterSAddress1.getText().toString().trim();
                    updateUserRequest.ShippingAddress2 = enterSAddress2.getText().toString().trim();
                    updateUserRequest.ShippingPinCode = enterSPincode.getText().toString().trim();

                    updateUserApiCall(updateUserRequest);
                }
            }
        });
    }

    private void updateUserApiCall(UpdateUserRequest updateUserRequest) {

        Log.e("place order req", MyApplication.getGson().toJson(updateUserRequest).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.updateUser(updateUserRequest).enqueue(new Callback<UpdateUserResp>() {
            @Override
            public void onResponse(Call<UpdateUserResp> call, Response<UpdateUserResp> response) {
                dialog.dismiss();
                if (response.body() != null && response.body().getResponseMessage() != null) {
                    Log.e("update user resp", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getResponseCode() == 1) {
                        Functions.showToast(getActivity(), response.body().getResponseMessage());
                        updateUserPojo(response.body().getData());
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(getActivity(), i);
                        ((Activity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        ((Activity) getActivity()).finish();
                    } else {
                        Functions.showToast(getActivity(), "Fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResp> call, Throwable t) {
                dialog.dismiss();
            }

        });
    }

    private void updateUserPojo(UpdateUserRequest data) {
        userPojo.setUserID(data.UserID);
        userPojo.setName(data.Name);
        userPojo.setMobile(data.Mobile);
        userPojo.setEmailID(data.EmailID);
        userPojo.setBillingAddress1(data.BillingAddress1);
        userPojo.setBillingAddress2(data.BillingAddress2);
        userPojo.setBillingPinCode(data.BillingPinCode);
        userPojo.setShippingAddress1(data.ShippingAddress1);
        userPojo.setShippingAddress2(data.ShippingAddress2);
        userPojo.setShippingPinCode(data.ShippingPinCode);

        PrefUtils.setUserFullProfileDetails(getActivity(), userPojo);
        updateUI(userPojo);
    }

    private boolean validateField() {
        if (enterName.getText().toString().trim().length() == 0) {
            Functions.showToast(getActivity(), "Please enter UserName");
            return false;
        } else if (enterEmailId.getText().toString().trim().length() == 0) {
            Functions.showToast(getActivity(), "Please enter Email");
            return false;
        } else if (enterMobileNo.getText().toString().trim().length() == 0) {
            Functions.showToast(getActivity(), "Please enter MobileNo");
            return false;
        } else if (enterBAddress1.getText().toString().trim().length() != 0 ||
                enterBAddress2.getText().toString().trim().length() != 0 ||
                enterBPincode.getText().toString().trim().length() != 0) {
            if (enterBAddress1.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter billing address line 1");
                return false;
            } else if (enterBAddress2.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter billing address line 2");
                return false;
            } else if (enterBPincode.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter billing pin code");
                return false;
            } else if (enterBPincode.getText().toString().trim().length() != 6) {
                Functions.showToast(getActivity(), "Please enter valid billing pin code");
                return false;
            }
        } else if (enterSAddress1.getText().toString().trim().length() != 0 ||
                enterSAddress2.getText().toString().trim().length() != 0 ||
                enterSPincode.getText().toString().trim().length() != 0) {
            if (enterSAddress1.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter shipping address line 1");
                return false;
            } else if (enterSAddress2.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter shipping address line 2");
                return false;
            } else if (enterSPincode.getText().toString().trim().length() == 0) {
                Functions.showToast(getActivity(), "Please enter shipping pin code");
                return false;
            } else if (enterSPincode.getText().toString().trim().length() != 6) {
                Functions.showToast(getActivity(), "Please enter valid shipping pin code");
                return false;
            }
        }
        return true;
    }

}
