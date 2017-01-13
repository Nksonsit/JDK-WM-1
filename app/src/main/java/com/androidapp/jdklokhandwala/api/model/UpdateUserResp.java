package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class UpdateUserResp extends BaseResponse {
    UpdateUserRequest Data;

    public UpdateUserRequest getData() {
        return Data;
    }

    public void setData(UpdateUserRequest data) {
        Data = data;
    }
}
