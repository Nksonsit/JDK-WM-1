package com.androidapp.jdklokhandwala.helper;

import android.app.Application;

import com.androidapp.jdklokhandwala.dbhelper.DatabaseManager;
import com.androidapp.jdklokhandwala.dbhelper.JDKOpenHelper;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by sagartahelyani on 27-12-2016.
 */

public class MyApplication extends Application {

    private static Retrofit retrofit;
    private static Gson gson;

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initDataBase();
        initStetho();
        initRetrofit();
        initGson();
    }

    private void initGson() {
        gson = new Gson();
    }

    public static Gson getGson() {
        return gson;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.WEB_SERVICE_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    private void initDataBase() {
        DatabaseManager.initialize(JDKOpenHelper.getInstance((this)));
        JDKOpenHelper.getInstance((this)).createDataBase(this);
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
