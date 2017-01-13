package com.androidapp.jdklokhandwala.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.LoginActivity;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.ChangePasswordDialog;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

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

        UserPojo userPojo = PrefUtils.getUserFullProfileDetails(getActivity());
        updateUI(userPojo);
        getUserProfile(PrefUtils.getUserID(getActivity()));

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


}
