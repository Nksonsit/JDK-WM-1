package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.NotificationAdapter;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.api.model.NotificationItemRes;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerViewOnScrollListener;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
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
    private View footer;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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
        readNotificationApi();
        notificationItems = (ArrayList<NotificationItem>) getIntent().getSerializableExtra("notificationItems");
        initRecyclerView();

    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    private void initRecyclerView() {
        notificationRV = (FamiliarRecyclerView) findViewById(R.id.notificationRV);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationRV.setLayoutManager(layoutManager);

        notificationRV.setEmptyView(emptyLayout);

        emptyLayout.setContent("No Notifications", R.drawable.ic_action_notification);

        adapter = new NotificationAdapter(NotificationActivity.this, notificationItems, new NotificationAdapter.OnClickListener() {
            @Override
            public void onClickListener(int pos) {
              //  Log.e("item clicked",notificationItems.get(pos).OrderID+" || "+ notificationItems.get(pos).NotificationTypeId);
                Intent i = new Intent(NotificationActivity.this, OrderDetailActivity.class);
                i.putExtra("OrderID", notificationItems.get(pos).OrderID);
                switch (notificationItems.get(pos).NotificationTypeId)
                {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        i.putExtra(AppConstants.isInquiry, true);
                        break;
                    case 12:
                    case 13:
                    case 14:
                        i.putExtra(AppConstants.isInquiry, false);
                        break;

                    default:
                        i.putExtra(AppConstants.isInquiry, false);
                        break;
                }
                //i.putExtra(AppConstants.isInquiry, false);
                i.putExtra(AppConstants.statusID, notificationItems.get(pos).NotificationId);
                Functions.fireIntent(NotificationActivity.this, i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        notificationRV.setAdapter(adapter);
        // callNotificationApi();


        footer = LayoutInflater.from(this).inflate(R.layout.load_more, null);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (notificationItems != null && notificationItems.size() > 9) {
            notificationRV.addFooterView(footer);
            isLoadMore = true;
        }

        notificationRV.setOnScrollListener(new FamiliarRecyclerViewOnScrollListener(layoutManager) {
            @Override
            public void onScrolledToTop() {
            }

            @Override
            public void onScrolledToBottom() {
                if (isLoadMore) {
                    notificationRV.addFooterView(footer);
                    callNotificationApi(notificationItems.get(adapter.getItemCount() - 1).NotificationId);
                } else {
                    notificationRV.removeFooterView(footer);
                }
            }
        });

    }

    private void callNotificationApi(int lastNotificationId) {
        // call API
        //Log.e("req",PrefUtils.getUserFullProfileDetails(this).getUserID()+"");
        if (PrefUtils.getUserFullProfileDetails(this) != null) {
            AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
            appApi.getNotificationList(PrefUtils.getUserFullProfileDetails(this).getUserID(), lastNotificationId).enqueue(new Callback<NotificationItemRes>() {
                @Override
                public void onResponse(Call<NotificationItemRes> call, Response<NotificationItemRes> response) {

                    if (response.body() != null) {
                        // Log.e("resp",response.body().getResponseMessage() +" || " + response.body().Data.lstnotification.size());
                        if (response.body().Data != null && response.body().Data.lstnotification != null && response.body().Data.lstnotification.size() > 0) {
                            notificationItems.addAll(response.body().Data.lstnotification);
                            if(notificationItems.size()<10){
                                isLoadMore = false;
                                notificationRV.removeFooterView(footer);
                            }else{
                                isLoadMore = true;
                                notificationRV.addFooterView(footer);
                            }
                        }else {
                            isLoadMore = false;
                            notificationRV.removeFooterView(footer);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NotificationItemRes> call, Throwable t) {

                }
            });
        }

    }

    private void readNotificationApi() {

        // call API
        Log.e("req", PrefUtils.getUserFullProfileDetails(this).getUserID() + "");
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.setNotificationRead(PrefUtils.getUserFullProfileDetails(this).getUserID()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.body() != null) {
                    Log.e("resp", response.body().getResponseMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }
}
