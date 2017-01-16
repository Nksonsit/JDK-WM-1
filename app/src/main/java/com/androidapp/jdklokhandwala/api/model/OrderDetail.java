package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 13-01-2017.
 */

public class OrderDetail {
    int OrderID;
    String ReferCode;
    int TotalAmount;
    int TotalCartWeight;
    int NetAmount;
    int DiscountAmount;
    String CreatedDate;
    String BillingAddress1;
    String BillingAddress2;
    String BillingPinCode;
    String ShippingAddress1;
    String ShippingAddress2;
    String ShippingPinCode;

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

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getTotalCartWeight() {
        return TotalCartWeight;
    }

    public void setTotalCartWeight(int totalCartWeight) {
        TotalCartWeight = totalCartWeight;
    }

    public int getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(int netAmount) {
        NetAmount = netAmount;
    }

    public int getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getBillingAddress1() {
        return BillingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        BillingAddress1 = billingAddress1;
    }

    public String getBillingAddress2() {
        return BillingAddress2;
    }

    public void setBillingAddress2(String billingAddress2) {
        BillingAddress2 = billingAddress2;
    }

    public String getBillingPinCode() {
        return BillingPinCode;
    }

    public void setBillingPinCode(String billingPinCode) {
        BillingPinCode = billingPinCode;
    }

    public String getShippingAddress1() {
        return ShippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        ShippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return ShippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        ShippingAddress2 = shippingAddress2;
    }

    public String getShippingPinCode() {
        return ShippingPinCode;
    }

    public void setShippingPinCode(String shippingPinCode) {
        ShippingPinCode = shippingPinCode;
    }
}
