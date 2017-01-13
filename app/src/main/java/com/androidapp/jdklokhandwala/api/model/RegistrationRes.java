package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class RegistrationRes extends BaseResponse {
    UserPojo Data;

    public UserPojo getData() {
        return Data;
    }

    public void setData(UserPojo data) {
        Data = data;
    }
}
