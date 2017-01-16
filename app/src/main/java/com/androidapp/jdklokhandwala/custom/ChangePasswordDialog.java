package com.androidapp.jdklokhandwala.custom;

import android.app.Dialog;
import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.ChangePasswordReq;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 29-12-2016.
 */

public class ChangePasswordDialog extends Dialog {
    private final Context context;
    private final UserPojo userPojo;
    private View view;
    private TfEditText oldPassword;
    private TfEditText newPassword;
    private TfEditText conformPassword;
    private View.OnTouchListener touch;
    private Button update, btnCancel;
    private OnUpdateClick OnUpdateClick;


    public ChangePasswordDialog(final Context context, final OnUpdateClick OnUpdateClick) {
        super(context);
        this.context = context;
        this.OnUpdateClick = OnUpdateClick;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.change_password_popup, null);
        setContentView(view);

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);

        userPojo = PrefUtils.getUserFullProfileDetails(context);

        oldPassword = (TfEditText) view.findViewById(R.id.oldPassword);
        newPassword = (TfEditText) view.findViewById(R.id.newPassowrd);
        conformPassword = (TfEditText) view.findViewById(R.id.conformPassword);
        update = (Button) view.findViewById(R.id.updatePassword);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        touch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TfEditText editText = (TfEditText) view;
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // hide password
                        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        return true;
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // show password
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                    }
                }

                return false;
            }
        };

        oldPassword.setOnTouchListener(touch);
        newPassword.setOnTouchListener(touch);
        conformPassword.setOnTouchListener(touch);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context, view);
                if (validate()) {
                    OnUpdateClick.onUpdateClick(newPassword.getText().toString().trim());
                    changePassword(new ChangePasswordReq((long) userPojo.getUserID(), newPassword.getText().toString().trim(), oldPassword.getText().toString().trim()));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }

    private void changePassword(ChangePasswordReq changePasswordReq) {
        Log.e("changepassword req", MyApplication.getGson().toJson(changePasswordReq).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.changePassword(changePasswordReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    Log.e("changepassword res", MyApplication.getGson().toJson(response.body()).toString());
                    Functions.showToast(context, response.body().getResponseMessage().toString().trim());
                    if (response.body().getResponseMessage().toString().trim().toLowerCase().contains("success")) {
                        dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        if (oldPassword.getText().toString().trim().equals("")) {
            OnUpdateClick.onError("Please enter old password.");
            return false;
        } else if (newPassword.getText().toString().trim().equals("")) {
            OnUpdateClick.onError("Please enter new password.");
            return false;
        } else if (conformPassword.getText().toString().trim().equals("")) {
            OnUpdateClick.onError("Please enter conform password.");
            return false;
        } else if (!newPassword.getText().toString().trim().equals(conformPassword.getText().toString().trim())) {
            OnUpdateClick.onError("New password and conform password should be same.");
            return false;
        }
        return true;
    }

    public interface OnUpdateClick {
        void onUpdateClick(String newPassword);

        void onError(String error);
    }
}
