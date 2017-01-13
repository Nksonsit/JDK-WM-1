package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 11-01-2017.
 */

public class OrderItemRes extends BaseResponse {
    List<OrderItem> DataList;

    public List<OrderItem> getDataList() {
        return DataList;
    }

    public void setDataList(List<OrderItem> dataList) {
        DataList = dataList;
    }
}
