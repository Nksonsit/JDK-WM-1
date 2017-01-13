package com.androidapp.jdklokhandwala.api.call;

import android.content.Context;

import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.OrderItemRes;
import com.androidapp.jdklokhandwala.helper.MyApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 11-01-2017.
 */

public class GetOrderList {

    private final Context context;
    private OnGetOrders OnGetOrders;

    public GetOrderList(Context context, OnGetOrders OnGetOrders) {
        this.context = context;
        this.OnGetOrders = OnGetOrders;
    }

    public void CallApi(int userID,int lastId,int mode) {
        if (OnGetOrders != null) {
            OnGetOrders.showProgress();
        }
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getHistoryList(userID,lastId,mode).enqueue(new Callback<OrderItemRes>() {
            @Override
            public void onResponse(Call<OrderItemRes> call, Response<OrderItemRes> response) {
                if (OnGetOrders != null) {
                    OnGetOrders.dismissProgress();
                }
                if (response.body() != null) {
                    if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                        OnGetOrders.onGetOrders(response.body());
                    }
                } else {
                    OnGetOrders.onGetOrders(new OrderItemRes());
                }

            }

            @Override
            public void onFailure(Call<OrderItemRes> call, Throwable t) {
                OnGetOrders.onGetOrders(new OrderItemRes());
                if (OnGetOrders != null) {
                    OnGetOrders.dismissProgress();
                }
            }
        });
    }

    public interface OnGetOrders {
        void onGetOrders(OrderItemRes orderItemRes);

        void showProgress();

        void dismissProgress();
    }
}
