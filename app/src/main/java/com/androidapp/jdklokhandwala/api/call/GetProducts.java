package com.androidapp.jdklokhandwala.api.call;

import android.content.Context;

import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.Product;
import com.androidapp.jdklokhandwala.api.model.ProductResponse;
import com.androidapp.jdklokhandwala.helper.MyApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 05-01-2017.
 */

public class GetProducts {
    private final Context context;
    private final int categoryId;
    private final OnGetProducts OnGetProducts;

    public GetProducts(Context context, int categoryId, OnGetProducts OnGetProducts) {
        this.context = context;
        this.categoryId = categoryId;
        this.OnGetProducts = OnGetProducts;
    }

    public void CallApi() {

        if (OnGetProducts != null) {
            OnGetProducts.showProgress();
        }
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getProductsApi(categoryId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (OnGetProducts != null) {
                    OnGetProducts.dismissProgress();
                }
                if (response.body() != null && response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                    OnGetProducts.onGetProduct(response.body().getDataList());
                } else {
                    OnGetProducts.onGetProduct(new ArrayList<Product>());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                if (OnGetProducts != null) {
                    OnGetProducts.dismissProgress();
                }
                OnGetProducts.onGetProduct(new ArrayList<Product>());
            }
        });
    }

    public interface OnGetProducts {
        void onGetProduct(List<Product> dataList);

        void showProgress();

        void dismissProgress();
    }
}
