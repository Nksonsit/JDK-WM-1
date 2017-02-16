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
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<NotificationItem> productList;
    private OnClickListener onClickListener;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> productList, OnClickListener onClickListener) {
        this.context = context;
        this.productList = productList;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (productList.get(position).getNotificationTypeId() == AppConstants.UserNotification) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view1 = LayoutInflater.from(context).inflate(R.layout.user_notification_row, parent, false);
                return new UserNotificationViewHolder(view1);

            case 2:
                View view2 = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
                return new ProductViewHolder(view2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationItem notification = productList.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                UserNotificationViewHolder holder1 = (UserNotificationViewHolder) holder;
                holder1.setDetails(notification);
                break;

            case 2:
                ProductViewHolder holder2 = (ProductViewHolder) holder;
                holder2.setDetails(notification);
                holder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClickListener(position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtTitle, txtContent, txtOrder, txtTime;

        private ProductViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TfTextView) itemView.findViewById(R.id.txtTitle);
            txtContent = (TfTextView) itemView.findViewById(R.id.txtContent);
            txtTime = (TfTextView) itemView.findViewById(R.id.txtTime);
            txtOrder = (TfTextView) itemView.findViewById(R.id.txtOrder);
        }

        private void setDetails(NotificationItem notification) {
            Log.e("Notification type", Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtTitle.setText(notification.getTitle());
            //txtTitle.setText(AppConstants.NotificationTypeId.getRequestTypeName(notification.NotificationTypeId));
            txtContent.setText(notification.getDescription());
            txtTime.setText(Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtOrder.setText(notification.getReferCode());
        }
    }

    class UserNotificationViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtTitle, txtContent, txtOrder, txtTime;

        private UserNotificationViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TfTextView) itemView.findViewById(R.id.txtTitle);
            txtContent = (TfTextView) itemView.findViewById(R.id.txtContent);
            txtTime = (TfTextView) itemView.findViewById(R.id.txtTime);
            txtOrder = (TfTextView) itemView.findViewById(R.id.txtOrder);
        }

        private void setDetails(NotificationItem notification) {
            Log.e("Notification type", Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtTitle.setText(notification.getTitle());
            //txtTitle.setText(AppConstants.NotificationTypeId.getRequestTypeName(notification.NotificationTypeId));
            txtContent.setText(notification.getDescription());
            txtTime.setText(Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.ddMMMYYYY) + " at " +
                    Functions.formatDate(notification.getCreatedDate(), Functions.ServerDateTimeFormat, Functions.hhmmAMPM));
            txtOrder.setText(notification.getReferCode());
        }
    }

    public interface OnClickListener {
        public void onClickListener(int pos);
    }
}
