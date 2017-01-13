package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 05-01-2017.
 */

public class CategoryResponse extends BaseResponse {

    List<Category> DataList;

    public List<Category> getDataList() {
        return DataList;
    }

    public void setDataList(List<Category> dataList) {
        DataList = dataList;
    }
}
