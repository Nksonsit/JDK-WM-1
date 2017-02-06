package com.androidapp.jdklokhandwala.custom;

import android.app.Dialog;
import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.ChangePasswordReq;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 29-12-2016.
 */

public class ChangePasswordDialog extends Dialog {
    private final Context context;
    private UserPojo userPojo;
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

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);

        init();

    }

    private void init() {

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        userPojo = PrefUtils.getUserFullProfileDetails(context);

        oldPassword = (TfEditText) view.findViewById(R.id.oldPassword);
        newPassword = (TfEditText) view.findViewById(R.id.newPassowrd);
        conformPassword = (TfEditText) view.findViewById(R.id.conformPassword);

        oldPassword.setText("");
        newPassword.setText("");
        conformPassword.setText("");

        update = (Button) view.findViewById(R.id.updatePassword);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        actionListener();
    }

    private void actionListener() {


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
                if (Functions.isConnected(context)) {
                    if (validate()) {
                        OnUpdateClick.onUpdateClick(newPassword.getText().toString().trim());
                        changePassword(new ChangePasswordReq((long) userPojo.getUserID(), newPassword.getText().toString().trim(), oldPassword.getText().toString().trim()));
                    }
                } else {
                    Functions.showToast(context, context.getResources().getString(R.string.no_internet_connection));
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
        Log.e("changepassword req", Functions.jsonString(changePasswordReq));

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.changePassword(changePasswordReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    Log.e("changepassword res", Functions.jsonString(baseResponse));

                    if (baseResponse.getResponseCode() == AppConstants.RESPONSE_SUCCESS) {
                        Functions.showToast(context, "Password changed successfully.");
                        oldPassword.setText("");
                        newPassword.setText("");
                        conformPassword.setText("");
                        dismiss();
                    } else {
                        Functions.showToast(context, baseResponse.getResponseMessage().trim());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, context);
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
            OnUpdateClick.onError("Please enter confirm password.");
            return false;
        } else if (!Functions.checkPassrordLength(context, 2, newPassword.getText().toString().trim())) {
            return false;
        } else if (!Functions.checkPassrordLength(context, 3, conformPassword.getText().toString().trim())) {
            return false;
        } else if (!newPassword.getText().toString().trim().equals(conformPassword.getText().toString().trim())) {
            OnUpdateClick.onError("New password and confirm password should be same.");
            return false;
        }
        return true;
    }

    public interface OnUpdateClick {
        void onUpdateClick(String newPassword);

        void onError(String error);
    }
}
