package com.androidapp.jdklokhandwala.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.NotificationAdapter;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private FamiliarRecyclerView notificationRV;
    private EmptyLayout emptyLayout;
    private ArrayList<NotificationItem> notificationItems;
    private NotificationAdapter adapter;

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

        for (int i = 0; i < 20; i++) {
            NotificationItem notificationItem = new NotificationItem();
            notificationItem.setContent("Content: " + getString(R.string.dummy_text));
            notificationItem.setTitle("Title " + i);
            notificationItems.add(notificationItem);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationRV.setLayoutManager(layoutManager);

        notificationRV.setEmptyView(emptyLayout);

        emptyLayout.setContent("No Notifications", R.drawable.ic_profile);

        adapter = new NotificationAdapter(NotificationActivity.this, notificationItems);
        notificationRV.setAdapter(adapter);

    }
}
