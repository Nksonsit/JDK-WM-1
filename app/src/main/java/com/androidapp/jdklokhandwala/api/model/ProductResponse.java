package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 05-01-2017.
 */

public class ProductResponse extends BaseResponse {
    List<Product> DataList;

    public List<Product> getDataList() {
        return DataList;
    }

    public void setDataList(List<Product> dataList) {
        DataList = dataList;
    }
}
