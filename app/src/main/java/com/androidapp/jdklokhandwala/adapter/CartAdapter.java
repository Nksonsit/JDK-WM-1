package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.Product;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {

    private Context context;
    private List<AddToCart> addToCartList;
    private OnOptionSelectedListener OnOptionSelectedListener;
    private List<AddToCart> dataList;

    public CartAdapter(Context context, List<AddToCart> addToCartList, OnOptionSelectedListener OnOptionSelectedListener) {
        this.context = context;
        this.addToCartList = addToCartList;
        this.OnOptionSelectedListener = OnOptionSelectedListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        AddToCart addToCart = addToCartList.get(position);
        holder.setProduct(addToCart, position);
    }

    @Override
    public int getItemCount() {
        return addToCartList.size();
    }

    public void setdataList(List<AddToCart> dataList) {
        this.addToCartList = new ArrayList<>();
        this.addToCartList = dataList;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private TfTextView textView, textViewQty;
        private TfTextView tvEdit, tvDelete;

        private ProductViewHolder(View itemView) {
            super(itemView);
            textView = (TfTextView) itemView.findViewById(R.id.textView);
            textViewQty = (TfTextView) itemView.findViewById(R.id.textViewQty);
            tvEdit = (TfTextView) itemView.findViewById(R.id.tvEdit);
            tvDelete = (TfTextView) itemView.findViewById(R.id.tvDelete);
        }

        private void setProduct(AddToCart addToCart, final int position) {
            Log.e("value",Functions.getFormatedInt(addToCart.UnitValue()));
            textView.setText(addToCart.Name() + " Kg");
            if (addToCart.UnitType().trim().toLowerCase().equals("kg")) {
                textViewQty.setText("Quantity : " + Functions.getFormatedInt(addToCart.UnitValue()) + " " + addToCart.UnitType());
            } else {
                String totalWeight=String.format("%2f",new BigDecimal(Functions.getFormatedInt((addToCart.DefaultWeight() * addToCart.UnitValue()))));
                textViewQty.setText("Quantity : " + Functions.getFormatedInt(addToCart.UnitValue()) + " " + addToCart.UnitType() + " (" + Functions.getFormatedInt((addToCart.DefaultWeight() * addToCart.UnitValue())) + " Kg)");
            }
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (OnOptionSelectedListener != null) {
                        OnOptionSelectedListener.doPerformAction(AppConstants.EDIT_CART_PRODUCT, getAdapterPosition());
                    }
                }
            });

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (OnOptionSelectedListener != null) {
                        OnOptionSelectedListener.doPerformAction(AppConstants.DELETE_CART_PRODUCT, getAdapterPosition());
                    }
                }
            });

        }
    }

    public void updateList() {
        addToCartList = AddToCart.getCartList();
        notifyDataSetChanged();
    }

    public interface OnOptionSelectedListener {
        void doPerformAction(int actionType, int position);
    }
}
