package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class ContactUsResponse extends BaseResponse {
    ContactUs Data;

    public ContactUs getData() {
        return Data;
    }

    public void setData(ContactUs data) {
        Data = data;
    }
}
