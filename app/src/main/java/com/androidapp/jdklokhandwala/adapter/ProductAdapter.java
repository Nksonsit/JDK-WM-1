package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartPojo;
import com.androidapp.jdklokhandwala.api.model.Product;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnOptionSelectedListener OnOptionSelectedListener;

    public ProductAdapter(Context context, List<Product> productList, OnOptionSelectedListener OnOptionSelectedListener) {
        this.context = context;
        this.productList = productList;
        this.OnOptionSelectedListener = OnOptionSelectedListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.setProduct(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private TfTextView textView, tvAddCart;

        private ProductViewHolder(View itemView) {
            super(itemView);
            textView = (TfTextView) itemView.findViewById(R.id.textView);
            tvAddCart = (TfTextView) itemView.findViewById(R.id.tvAddCart);
        }

        private void setProduct(Product product) {
            textView.setText(product.getName() + "  " + product.getCodeValue() + "  " + product.getWeight());

            tvAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (OnOptionSelectedListener != null) {
                        OnOptionSelectedListener.doPerformAction(AppConstants.ADD_CART_PRODUCT, getAdapterPosition());
                    }
                }
            });

        }
    }

    public interface OnOptionSelectedListener {
        public void doPerformAction(int actionType, int position);
    }
}
