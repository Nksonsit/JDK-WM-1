package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.OrderItem;
import com.androidapp.jdklokhandwala.custom.TfTextView;

import java.util.List;

/**
 * Created by ishan on 12-01-2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final Context context;
    private List<OrderItem> orderList;
    private OnOptionSelectedListener OnOptionSelectedListener;

    public OrderAdapter(Context context, List<OrderItem> orderList, OnOptionSelectedListener OnOptionSelectedListener) {
        this.context = context;
        this.orderList = orderList;
        this.OnOptionSelectedListener = OnOptionSelectedListener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderItem orderItem = orderList.get(position);
        holder.setOrder(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setDataList(List<OrderItem> orderList) {
        this.orderList=orderList;
        notifyDataSetChanged();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TfTextView referCode, netAmount, totalWeight, status, createdDate;

        private OrderViewHolder(View itemView) {
            super(itemView);
            referCode = (TfTextView) itemView.findViewById(R.id.referCodeTV);
            netAmount = (TfTextView) itemView.findViewById(R.id.netAmountTV);
            totalWeight = (TfTextView) itemView.findViewById(R.id.totalWeightTV);
            status = (TfTextView) itemView.findViewById(R.id.statusTV);
            createdDate = (TfTextView) itemView.findViewById(R.id.createdDateTV);
        }

        private void setOrder(OrderItem orderItem) {
            referCode.setText(orderItem.getReferCode());
            netAmount.setText("Net Amount : " + orderItem.getNetAmount());
            totalWeight.setText("Total Weight : " + orderItem.getTotalKGWeight() + "Kg");
            status.setText("Status : " + orderItem.getProcessStap());
            createdDate.setText(orderItem.getCreatedDate());
        }
    }

    public interface OnOptionSelectedListener {
        public void doPerformAction(int actionType, int position);
    }
}
