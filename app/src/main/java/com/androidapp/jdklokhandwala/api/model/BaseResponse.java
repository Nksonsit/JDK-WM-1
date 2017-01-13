package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class BaseResponse {
    int ResponseCode;
    String ResponseMessage;

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
