package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 13-01-2017.
 */

public class OrderListRes extends BaseResponse {
    OrderDetail Data;
    List<OrderListPojo> DataList;

    public OrderDetail getData() {
        return Data;
    }

    public void setData(OrderDetail data) {
        Data = data;
    }

    public List<OrderListPojo> getDataList() {
        return DataList;
    }

    public void setDataList(List<OrderListPojo> dataList) {
        DataList = dataList;
    }
}
