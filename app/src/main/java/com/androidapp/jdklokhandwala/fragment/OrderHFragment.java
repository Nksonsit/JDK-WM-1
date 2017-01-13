package com.androidapp.jdklokhandwala.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.OrderAdapter;
import com.androidapp.jdklokhandwala.api.call.GetOrderList;
import com.androidapp.jdklokhandwala.api.model.OrderItem;
import com.androidapp.jdklokhandwala.api.model.OrderItemRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
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

    public static OrderHFragment newInstance() {
        OrderHFragment orderHFragment = new OrderHFragment();
        return orderHFragment;
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
        orderList = new ArrayList<>();
        mAdapter = new OrderAdapter(getActivity(), orderList, new OrderAdapter.OnOptionSelectedListener() {
            @Override
            public void doPerformAction(int actionType, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);


        userPojo = PrefUtils.getUserFullProfileDetails(getActivity());
        getOrderList(userPojo.getUserID(), 0);
    }

    private void getOrderList(int userID, int lastRecordID) {
        new GetOrderList(getActivity(), new GetOrderList.OnGetOrders() {
            @Override
            public void onGetOrders(OrderItemRes orderItemRes) {
                if (orderItemRes != null && orderItemRes.getDataList() != null && orderItemRes.getDataList().size() > 0) {
                    orderList=orderItemRes.getDataList();
                    mAdapter.setDataList(orderList);
                }
            }

            @Override
            public void showProgress() {

                if(dialog!=null){
                    dialog.show();}
            }

            @Override
            public void dismissProgress() {

                if(dialog!=null){
                    dialog.dismiss();}
            }
        }).CallApi(userID, lastRecordID, 0);
    }

}
