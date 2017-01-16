package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 11-01-2017.
 */

public class NotificationItemRes extends BaseResponse {
    List<NotificationItem> DataList;

    public List<NotificationItem> getDataList() {
        return DataList;
    }

    public void setDataList(List<NotificationItem> dataList) {
        DataList = dataList;
    }
}
