package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 11-01-2017.
 */

public class OrderItem {
    int OrderID;
    String ReferCode;
    Double TotalKGWeight;
    Double NetAmount;
    String CreatedDate;
    String ProcessStap;

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

    public Double getTotalKGWeight() {
        return TotalKGWeight;
    }

    public void setTotalKGWeight(Double totalKGWeight) {
        TotalKGWeight = totalKGWeight;
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

    public String getProcessStap() {
        return ProcessStap;
    }

    public void setProcessStap(String processStap) {
        ProcessStap = processStap;
    }
}
