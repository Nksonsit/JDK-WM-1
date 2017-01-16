package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 13-01-2017.
 */

public class AcceptOrder {
    int OrderID;
    int IsAccept;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getIsAccept() {
        return IsAccept;
    }

    public void setIsAccept(int isAccept) {
        IsAccept = isAccept;
    }
}
