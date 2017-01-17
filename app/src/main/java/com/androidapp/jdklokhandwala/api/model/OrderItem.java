package com.androidapp.jdklokhandwala.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ishan on 11-01-2017.
 */

public class OrderItem{
    int OrderID;
    String ReferCode;
    Double TotalCartWeight;
    Double NetAmount;
    String CreatedDate;
    int StatusID;

    public Double getTotalCartWeight() {
        return TotalCartWeight;
    }

    public void setTotalCartWeight(Double totalCartWeight) {
        TotalCartWeight = totalCartWeight;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getReferCode() {
        return ReferCode;
    }

    public void setReferCode(String referCode) {
        ReferCode = referCode;
    }

    public Double getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(Double netAmount) {
        NetAmount = netAmount;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
