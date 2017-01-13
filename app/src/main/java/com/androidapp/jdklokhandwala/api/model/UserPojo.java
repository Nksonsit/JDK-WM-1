package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class UserPojo {
    int UserID;
    String Name;
    String Mobile;
    String EmailID;
    String Password;
    String BillingAddress1;
    String BillingAddress2;
    String BillingPinCode;
    String BillingCity;
    String BillingArea;
    String ShippingAddress1;
    String ShippingAddress2;
    String ShippingPinCode;
    String ShippingCity;
    String ShippingArea;
    int IdentityTypeID;
    String IdentityNo;


    public String getShippingCity() {
        return ShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        ShippingCity = shippingCity;
    }

    public String getBillingCity() {
        return BillingCity;
    }

    public void setBillingCity(String billingCity) {
        BillingCity = billingCity;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getBillingArea() {
        return BillingArea;
    }

    public void setBillingArea(String billingArea) {
        BillingArea = billingArea;
    }

    public String getShippingArea() {
        return ShippingArea;
    }

    public void setShippingArea(String shippingArea) {
        ShippingArea = shippingArea;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public int getIdentityTypeID() {
        return IdentityTypeID;
    }

    public void setIdentityTypeID(int identityTypeID) {
        IdentityTypeID = identityTypeID;
    }

    public String getIdentityNo() {
        return IdentityNo;
    }

    public void setIdentityNo(String identityNo) {
        IdentityNo = identityNo;
    }
}
