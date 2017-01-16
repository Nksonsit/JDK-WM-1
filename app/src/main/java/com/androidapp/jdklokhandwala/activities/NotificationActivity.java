package com.androidapp.jdklokhandwala.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.NotificationAdapter;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.api.model.NotificationItemRes;
import com.androidapp.jdklokhandwala.api.model.OrderItemRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private FamiliarRecyclerView notificationRV;
    private EmptyLayout emptyLayout;
    private ArrayList<NotificationItem> notificationItems;
    private NotificationAdapter adapter;
    private UserPojo userPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        userPojo = PrefUtils.getUserFullProfileDetails(this);
        init();
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.notification_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();

    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    private void initRecyclerView() {
        notificationRV = (FamiliarRecyclerView) findViewById(R.id.notificationRV);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        notificationItems = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationRV.setLayoutManager(layoutManager);

        notificationRV.setEmptyView(emptyLayout);

        emptyLayout.setContent("No Notifications", R.drawable.ic_action_notification);

        adapter = new NotificationAdapter(NotificationActivity.this, notificationItems);
        notificationRV.setAdapter(adapter);
        callNotificationApi();
    }

    private void callNotificationApi() {

        // call API
        //Log.e("req",userPojo.getUserID()+"");
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getNotificationList(userPojo.getUserID(),0).enqueue(new Callback<NotificationItemRes>() {
            @Override
            public void onResponse(Call<NotificationItemRes> call, Response<NotificationItemRes> response) {

                if (response.body() != null) {
                  //  Log.e("resp",response.body().getResponseMessage() +" || " + response.body().getDataList().size());
                    if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                        notificationItems.addAll(response.body().getDataList());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NotificationItemRes> call, Throwable t) {

            }
        });

       /* for (int i = 0; i < 20; i++) {
            NotificationItem notificationItem = new NotificationItem();
            notificationItem.setContent("Content: " + getString(R.string.dummy_text));
            notificationItem.setTitle("Title " + i);
            notificationItems.add(notificationItem);
            adapter.notifyDataSetChanged();
        }*/
    }
}
