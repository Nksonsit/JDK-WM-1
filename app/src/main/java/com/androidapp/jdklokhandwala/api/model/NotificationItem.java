package com.androidapp.jdklokhandwala.api.model;

import com.facebook.stetho.inspector.protocol.module.Network;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 11-01-2017.
 */

public class NotificationItem extends BaseResponse implements Serializable {

    public int NotificationId,NotificationTypeId;
    public String Description;
    public String Title;

    public String getContent() {
        return Description;
    }

    public void setContent(String content) {
        this.Description = content;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }


}
