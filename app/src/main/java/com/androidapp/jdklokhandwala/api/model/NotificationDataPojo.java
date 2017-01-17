package com.androidapp.jdklokhandwala.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vaibhavirana on 16-01-2017.
 */
public class NotificationDataPojo implements Serializable {
    public int UnReadCount;
    public List<NotificationItem> lstnotification;
}
