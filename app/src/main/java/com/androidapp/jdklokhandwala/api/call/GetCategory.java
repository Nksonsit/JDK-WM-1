package com.androidapp.jdklokhandwala.api.call;

import android.content.Context;
import android.util.Log;

import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.Category;
import com.androidapp.jdklokhandwala.api.model.CategoryResponse;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 05-01-2017.
 */

public class GetCategory {

    private final Context context;
    private final OnGetCategoryListener OnGetCategory;

    public GetCategory(Context context, OnGetCategoryListener OnGetCategory) {
        this.context = context;
        this.OnGetCategory = OnGetCategory;
    }

    public void call() {
        if (OnGetCategory != null) {
            OnGetCategory.showProgress();
        }
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getCategoryApi().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (OnGetCategory != null) {
                    OnGetCategory.dismissProgress();
                }
                if (response.body() != null) {
                    Log.e("res1", Functions.jsonString(response.body()));
                    if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                        Log.e("res2", Functions.jsonString(response.body()));
                        OnGetCategory.onGet(response.body().getDataList());
                    }
                } else {
                    Log.e("res","null");
                    OnGetCategory.onGet(new ArrayList<Category>());
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, context);
                if (OnGetCategory != null) {
                    Log.e("res3", "null "+t.getMessage());
                    OnGetCategory.onGet(new ArrayList<Category>());
                    OnGetCategory.dismissProgress();
                }
            }
        });
    }

    public interface OnGetCategoryListener {

        void onGet(List<Category> dataList);

        void showProgress();

        void dismissProgress();
    }
}
