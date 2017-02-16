package com.androidapp.jdklokhandwala.api.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ishan on 11-01-2017.
 */

public class PlaceOrderReq {
    int OrderID;
    int UserID;
    int PaymentMethodID;
    int TotalCartItem;
    int IsOrder;
    String TotalCartWeight;
    String BillingAddress1;
    String BillingAddress2;
    String BillingPinCode;
    String ShippingAddress1;
    String ShippingAddress2;
    String ShippingPinCode;
    List<AddToCartTemp> CartItemList;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getPaymentMethodID() {
        return PaymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        PaymentMethodID = paymentMethodID;
    }

    public int getTotalCartItem() {
        return TotalCartItem;
    }

    public void setTotalCartItem(int totalCartItem) {
        TotalCartItem = totalCartItem;
    }

    public int getIsOrder() {
        return IsOrder;
    }

    public void setIsOrder(int isOrder) {
        IsOrder = isOrder;
    }

    public String getTotalCartWeight() {
        return TotalCartWeight;
    }

    public void setTotalCartWeight(String totalCartWeight) {
        TotalCartWeight = totalCartWeight;
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

    public List<AddToCartTemp> getCartItemList() {
        return CartItemList;
    }

    public void setCartItemList(List<AddToCartTemp> cartItemList) {
        CartItemList = cartItemList;
    }
}
