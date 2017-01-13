package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.custom.TfTextView;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<NotificationItem> productList;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> productList) {
        this.context = context;
        this.productList = productList;
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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private TfTextView txtTitle, txtContent;

        private ProductViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TfTextView) itemView.findViewById(R.id.txtTitle);
            txtContent = (TfTextView) itemView.findViewById(R.id.txtContent);
        }

        public void setDetails(NotificationItem notification) {
            txtTitle.setText(notification.getTitle());
            txtContent.setText(notification.getContent());
        }
    }

}
