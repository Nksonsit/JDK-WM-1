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
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
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
    private SpotsDialog spotsDialog;
    private String call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        call = getIntent().getStringExtra(AppConstants.NOTIFICATION_CALL);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doFinish();
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

        if (PrefUtils.isUserLoggedIn(this)) {
            if (Functions.isConnected(NotificationActivity.this)) {
                readNotificationApi();
            } else {
                Functions.showToast(NotificationActivity.this, getResources().getString(R.string.no_internet_connection));
            }
        }

        // notificationItems = (ArrayList<NotificationItem>) getIntent().getSerializableExtra("notificationItems");
        notificationItems = new ArrayList<>();
        initRecyclerView();

        callNotificationApi(0);

    }

    private void doFinish() {
        if (call.equalsIgnoreCase(AppConstants.HOME_CLICK)) {

        } else {
            Functions.fireIntent(this, DashboardActivity.class);
        }
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
                i.putExtra("OrderID", notificationItems.get(pos).getOrderID());
                i.putExtra(AppConstants.statusTxt, notificationItems.get(pos).getTitle());
                switch (notificationItems.get(pos).getNotificationTypeId()) {
                    case 8:
                        i.putExtra(AppConstants.isInquiry, true);
                        i.putExtra(AppConstants.isAccept, true);
                        break;
                    case 9:
                        i.putExtra(AppConstants.isInquiry, true);
                        i.putExtra(AppConstants.isAccept, true);
                        break;
                    case 10:
                        i.putExtra(AppConstants.isInquiry, true);
                        i.putExtra(AppConstants.isAccept, true);
                        break;
                    case 11:
                        i.putExtra(AppConstants.isInquiry, true);
                        i.putExtra(AppConstants.isAccept, true);
                        break;
                    default:
                        i.putExtra(AppConstants.isAccept, false);
                        i.putExtra(AppConstants.isInquiry, false);
                        break;
                }
                //i.putExtra(AppConstants.isInquiry, false);
                i.putExtra(AppConstants.statusID, notificationItems.get(pos).getNotificationTypeId());
                if (Functions.isConnected(NotificationActivity.this)) {
                    Functions.fireIntent(NotificationActivity.this, i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Functions.showToast(NotificationActivity.this, getString(R.string.no_internet_connection));
                }
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
                    if (Functions.isConnected(NotificationActivity.this)) {
                        notificationRV.addFooterView(footer);
                        callNotificationApi(notificationItems.get(adapter.getItemCount() - 1).getNotificationId());
                    } else {
                        Functions.showToast(NotificationActivity.this, getResources().getString(R.string.no_internet_connection));
                    }
                } else {
                    notificationRV.removeFooterView(footer);
                }
            }
        });

    }

    private void callNotificationApi(int lastNotificationId) {

        if (PrefUtils.getUserFullProfileDetails(this) != null) {

            showProgress();

            AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
            appApi.getNotificationList(PrefUtils.getUserFullProfileDetails(this).getUserID(), lastNotificationId).enqueue(new Callback<NotificationItemRes>() {
                @Override
                public void onResponse(Call<NotificationItemRes> call, Response<NotificationItemRes> response) {

                    dismissProgress();

                    if (response.body() != null) {
                        // Log.e("resp",response.body().getResponseMessage() +" || " + response.body().Data.lstnotification.size());
                        if (response.body().Data != null && response.body().Data.lstnotification != null && response.body().Data.lstnotification.size() > 0) {
                            notificationItems.addAll(response.body().Data.lstnotification);
                            if (notificationItems.size() < 10) {
                                isLoadMore = false;
                                notificationRV.removeFooterView(footer);
                            } else {
                                isLoadMore = true;
                                notificationRV.addFooterView(footer);
                            }
                        } else {
                            isLoadMore = false;
                            notificationRV.removeFooterView(footer);
                        }
                    } else {
                        Functions.showToast(NotificationActivity.this, getResources().getString(R.string.error));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NotificationItemRes> call, Throwable t) {
                    RetrofitErrorHelper.showErrorMsg(t, NotificationActivity.this);
                    dismissProgress();
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
                } else {
                    Functions.showToast(NotificationActivity.this, getResources().getString(R.string.error));
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                RetrofitErrorHelper.showErrorMsg(t, NotificationActivity.this);
            }
        });
    }

    public void showProgress() {
        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog(NotificationActivity.this, R.style.Custom);
        }
        spotsDialog.show();
    }

    public void dismissProgress() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
    }
}
