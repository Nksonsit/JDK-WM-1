package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.OrderItem;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ishan on 12-01-2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final Context context;
    private final boolean isOrder;
    private List<OrderItem> orderList;
    private OnOptionSelectedListener OnOptionSelectedListener;
    DecimalFormat formatter;

    public OrderAdapter(Context context, boolean isOrder, List<OrderItem> orderList, OnOptionSelectedListener OnOptionSelectedListener) {
        this.context = context;
        this.orderList = orderList;
        this.OnOptionSelectedListener = OnOptionSelectedListener;
        formatter = new DecimalFormat("#,##,##,###");
        this.isOrder = isOrder;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderItem orderItem = orderList.get(position);
        Log.e("order item", MyApplication.getGson().toJson(orderItem).toString());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnOptionSelectedListener.doPerformAction(position);
            }
        });

        // TODO: 15-02-2017
        if (isOrder) {
//        if (orderItem.isViaInquiry() == 0) {
            holder.netAmount.setVisibility(View.GONE);
        }

        holder.setOrder(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setDataList(List<OrderItem> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TfTextView referCode, netAmount, totalWeight, status, createdDate;
        private LinearLayout parentView;

        private OrderViewHolder(View itemView) {
            super(itemView);
            referCode = (TfTextView) itemView.findViewById(R.id.referCodeTV);
            netAmount = (TfTextView) itemView.findViewById(R.id.netAmountTV);
            totalWeight = (TfTextView) itemView.findViewById(R.id.totalWeightTV);
            status = (TfTextView) itemView.findViewById(R.id.statusTV);
            createdDate = (TfTextView) itemView.findViewById(R.id.createdDateTV);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView);
        }

        private void setOrder(OrderItem orderItem) {
            referCode.setText(orderItem.getReferCode());
            String myString = formatter.format(orderItem.getNetAmount());
            netAmount.setText(context.getResources().getString(R.string.Rs) + " " + myString);
            totalWeight.setText("Total Weight : " + Functions.getFormatedInt(orderItem.getTotalCartWeight()) + "Kg");
            status.setText("Status : " + Functions.getStatus(orderItem.getStatusID()));
            createdDate.setText(Functions.formatDate(orderItem.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(orderItem.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
        }
    }

    public interface OnOptionSelectedListener {
        void doPerformAction(int position);
    }
}
