package com.androidapp.jdklokhandwala.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.OrderDetailActivity;
import com.androidapp.jdklokhandwala.adapter.OrderAdapter;
import com.androidapp.jdklokhandwala.api.call.GetOrderList;
import com.androidapp.jdklokhandwala.api.model.OrderItem;
import com.androidapp.jdklokhandwala.api.model.OrderItemRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerViewOnScrollListener;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by ishan on 29-12-2016.
 */

public class OrderHFragment extends Fragment {

    private View parentView;
    private EmptyLayout emptyLayout;
    private FamiliarRecyclerView recyclerView;
    private List<OrderItem> orderList;
    private OrderAdapter mAdapter;
    private UserPojo userPojo;
    private SpotsDialog dialog;
    private SwipeRefreshLayout swipeRefresh;
    private View footer;
    private boolean isLoadMore = false;

    public static OrderHFragment newInstance() {
        OrderHFragment orderHFragment = new OrderHFragment();
        return orderHFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            userPojo = PrefUtils.getUserFullProfileDetails(getActivity());
            getOrderList(userPojo.getUserID(), 0, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_h_order, container, false);
        init();
        return parentView;
    }

    private void init() {

        dialog = new SpotsDialog(getActivity(), R.style.Custom);
        emptyLayout = (EmptyLayout) parentView.findViewById(R.id.emptyLayout);
        recyclerView = (FamiliarRecyclerView) parentView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(emptyLayout);
        emptyLayout.setContent("No Order Found.", R.drawable.ic_order);

        swipeRefresh = (SwipeRefreshLayout) parentView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderList(userPojo.getUserID(), 0, true);
            }
        });
        orderList = new ArrayList<>();
        mAdapter = new OrderAdapter(getActivity(), orderList, new OrderAdapter.OnOptionSelectedListener() {
            @Override
            public void doPerformAction(int position) {

                Intent i = new Intent(getActivity(), OrderDetailActivity.class);
                i.putExtra("OrderID", orderList.get(position).getOrderID());
                i.putExtra(AppConstants.isInquiry, false);
                i.putExtra(AppConstants.statusTxt, Functions.getStatus(orderList.get(position).getStatusID()));
                i.putExtra(AppConstants.statusID, orderList.get(position).getStatusID());
                Functions.fireIntent(getActivity(), i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        recyclerView.setAdapter(mAdapter);

        userPojo = PrefUtils.getUserFullProfileDetails(getActivity());
        //getOrderList(userPojo.getUserID(), 0, true);


        footer = LayoutInflater.from(getActivity()).inflate(R.layout.load_more, null);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (orderList != null && orderList.size() > 9) {
            recyclerView.addFooterView(footer);
        }

        recyclerView.setOnScrollListener(new FamiliarRecyclerViewOnScrollListener(layoutManager) {
            @Override
            public void onScrolledToTop() {
            }

            @Override
            public void onScrolledToBottom() {
                if (isLoadMore) {
                    recyclerView.addFooterView(footer);
                    getOrderList(userPojo.getUserID(), orderList.get(mAdapter.getItemCount() - 1).getOrderID(), false);
                } else {
                    recyclerView.removeFooterView(footer);
                }
            }
        });
    }

    private void getOrderList(int userID, int lastRecordID, boolean isPullRefresh) {
        if (isPullRefresh) {
            orderList = new ArrayList<>();
        }
        new GetOrderList(getActivity(), new GetOrderList.OnGetOrders() {
            @Override
            public void onGetOrders(OrderItemRes orderItemRes) {
                Log.e("size", orderItemRes.getDataList().size() + "");

                if (orderItemRes != null && orderItemRes.getDataList() != null && orderItemRes.getDataList().size() > 0) {
                    orderList.addAll(orderItemRes.getDataList());
                    mAdapter.setDataList(orderList);
                    if (orderItemRes.getDataList().size() < 10) {
                        isLoadMore = false;
                        recyclerView.removeFooterView(footer);
                    } else {
                        isLoadMore = true;
                        recyclerView.addFooterView(footer);
                    }
                } else {
                    isLoadMore = false;
                    recyclerView.removeFooterView(footer);
                }
            }

            @Override
            public void showProgress() {
                if (isPullRefresh) {
                    if (swipeRefresh != null) {
                        recyclerView.setClickable(false);
                        swipeRefresh.setRefreshing(true);
                    }
                } else {
                    if (dialog != null) {
                        dialog.show();
                    }
                }
            }

            @Override
            public void dismissProgress() {
                if (isPullRefresh) {
                    if (swipeRefresh != null) {
                        recyclerView.setClickable(true);
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        }).CallApi(userID, lastRecordID, 0);
    }

}
