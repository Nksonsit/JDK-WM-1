package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 13-01-2017.
 */

public class OrderDetail {
    int OrderID;
    String ReferCode;
    double TotalAmount;
    double TotalCartWeight;
    double NetAmount;
    double DiscountAmount;
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

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getTotalCartWeight() {
        return TotalCartWeight;
    }

    public void setTotalCartWeight(double totalCartWeight) {
        TotalCartWeight = totalCartWeight;
    }

    public double getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(double netAmount) {
        NetAmount = netAmount;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
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
