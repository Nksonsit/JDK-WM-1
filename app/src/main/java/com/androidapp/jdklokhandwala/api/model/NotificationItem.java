package com.androidapp.jdklokhandwala.api.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 11-01-2017.
 */

public class NotificationItem implements Serializable {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
