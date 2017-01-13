package com.androidapp.jdklokhandwala.api.model;

import java.util.List;

/**
 * Created by ishan on 11-01-2017.
 */

public class UserIdentityTypeRes extends BaseResponse {
    List<UserIdentityType> DataList;

    public List<UserIdentityType> getDataList() {
        return DataList;
    }

    public void setDataList(List<UserIdentityType> dataList) {
        DataList = dataList;
    }
}
