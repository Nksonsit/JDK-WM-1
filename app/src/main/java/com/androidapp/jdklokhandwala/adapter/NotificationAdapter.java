package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<NotificationItem> productList;
    private OnClickListener onClickListener;
    public NotificationAdapter(Context context, ArrayList<NotificationItem> productList, OnClickListener onClickListener) {
        this.context = context;
        this.productList = productList;
        this.onClickListener=onClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        NotificationItem notification = productList.get(position);
        holder.setDetails(notification);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtTitle, txtContent, txtOrder, txtTime;

        private ProductViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TfTextView) itemView.findViewById(R.id.txtTitle);
            txtContent = (TfTextView) itemView.findViewById(R.id.txtContent);
            txtTime = (TfTextView) itemView.findViewById(R.id.txtTime);
            txtOrder = (TfTextView) itemView.findViewById(R.id.txtOrder);
        }

        public void setDetails(NotificationItem notification) {
            Log.e("Notification type", Functions.formatDate(notification.CreatedDate, Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.CreatedDate, Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtTitle.setText(notification.Title);
            //txtTitle.setText(AppConstants.NotificationTypeId.getRequestTypeName(notification.NotificationTypeId));
            txtContent.setText(notification.getContent());
            txtTime.setText(Functions.formatDate(notification.CreatedDate, Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.CreatedDate, Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtOrder.setText(notification.ReferCode);
        }
    }

    public interface OnClickListener {
        public void onClickListener(int pos);
    }
}
