package com.androidapp.jdklokhandwala.api.model;

import com.facebook.stetho.inspector.protocol.module.Network;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 11-01-2017.
 */

public class NotificationItem extends BaseResponse implements Serializable {

    private int NotificationId, NotificationTypeId, OrderID;
    private String Description;
    private String CreatedDate;
    private String ReferCode;
    private String Title;

    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }

    public int getNotificationTypeId() {
        return NotificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        NotificationTypeId = notificationTypeId;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getReferCode() {
        return ReferCode;
    }

    public void setReferCode(String referCode) {
        ReferCode = referCode;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
