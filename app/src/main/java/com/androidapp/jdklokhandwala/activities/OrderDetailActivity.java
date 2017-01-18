package com.androidapp.jdklokhandwala.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AcceptOrder;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.OrderDetail;
import com.androidapp.jdklokhandwala.api.model.OrderListPojo;
import com.androidapp.jdklokhandwala.api.model.OrderListRes;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private TfTextView txtReferCode;
    private TfTextView txtTotalCartWeight;
    private TfTextView txtDiscount;
    private TfTextView txtNetAmount;
    private LinearLayout orderContainer;
    private View orderItemView;
    private int orderID;
    private OrderDetail orderDetail;
    private List<OrderListPojo> orderList;
    private TfTextView txtTotalAmount;
    private boolean isInquiry;
    private TfTextView txtBAddress1;
    //    private TfTextView txtBAddress2;
//    private TfTextView txtBPincode;
    private TfTextView txtSAddress1;
    //    private TfTextView txtSAddress2;
//    private TfTextView txtSPincode;
    private LinearLayout billingAddressView;
    private LinearLayout shippingAddressView;
    private LinearLayout bottomView;
    private TfButton btnAccept;
    private TfButton btnReject;
    private TfTextView txtNetAmount2;
    DecimalFormat formatter;
    private int statusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.order_detail_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formatter = new DecimalFormat("#,##,##,###");

        orderID = getIntent().getIntExtra("OrderID", 0);
        statusID= getIntent().getIntExtra(AppConstants.statusID, 10);
        isInquiry = getIntent().getBooleanExtra(AppConstants.isInquiry, true);

        Log.e("details come",orderID + " || " + statusID +" || "+ isInquiry);
        txtReferCode = (TfTextView) findViewById(R.id.txtReferCode);

        orderContainer = (LinearLayout) findViewById(R.id.orderListContainer);

        txtTotalAmount = (TfTextView) findViewById(R.id.txtTotalAmount);
        txtTotalCartWeight = (TfTextView) findViewById(R.id.txtTotalCartWeight);
        txtDiscount = (TfTextView) findViewById(R.id.txtDiscount);
        txtNetAmount = (TfTextView) findViewById(R.id.txtNetAmount);
        txtNetAmount2 = (TfTextView) findViewById(R.id.txtNetAmount2);

        txtBAddress1 = (TfTextView) findViewById(R.id.txtBAddress1);
//        txtBAddress2 = (TfTextView) findViewById(R.id.txtBAddress2);
//        txtBPincode = (TfTextView) findViewById(R.id.txtBPincode);

        txtSAddress1 = (TfTextView) findViewById(R.id.txtSAddress1);
//        txtSAddress2 = (TfTextView) findViewById(R.id.txtSAddress2);
//        txtSPincode = (TfTextView) findViewById(R.id.txtSPincode);

        billingAddressView = (LinearLayout) findViewById(R.id.billingAddressView);
        shippingAddressView = (LinearLayout) findViewById(R.id.shippingAddressView);

        bottomView = (LinearLayout) findViewById(R.id.bottomView);

        if (orderID != 0) {
            getOrderDetail(orderID);
        } else {
            Functions.showToast(OrderDetailActivity.this, "Did not find your order please try again.");
            finish();
        }

        if (isInquiry) {
            bottomView.setVisibility(View.VISIBLE);
            billingAddressView.setVisibility(View.GONE);
            shippingAddressView.setVisibility(View.GONE);
        } else {
            bottomView.setVisibility(View.GONE);
            billingAddressView.setVisibility(View.VISIBLE);
            shippingAddressView.setVisibility(View.VISIBLE);
        }

        btnAccept = (TfButton) findViewById(R.id.btnAccept);
        btnReject = (TfButton) findViewById(R.id.btnReject);

        if(statusID==10){
            bottomView.setVisibility(View.VISIBLE);
        }else{
            bottomView.setVisibility(View.GONE);}

        clickListener();
    }

    private void clickListener() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptOrder acceptOrder = new AcceptOrder();
                acceptOrder.setOrderID(orderDetail.getOrderID());
                acceptOrder.setIsAccept(1);
                callApi(acceptOrder);
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptOrder acceptOrder = new AcceptOrder();
                acceptOrder.setOrderID(orderDetail.getOrderID());
                acceptOrder.setIsAccept(0);
                callApi(acceptOrder);
            }
        });
    }

    private void callApi(AcceptOrder acceptOrder) {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.acceptRejectQuotation(acceptOrder).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    if (response.body().getResponseMessage().toString().toLowerCase().contains("success")) {
                        Functions.showToast(OrderDetailActivity.this, response.body().getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    private void getOrderDetail(int orderID) {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getOrderDetailApi(orderID).enqueue(new Callback<OrderListRes>() {
            @Override
            public void onResponse(Call<OrderListRes> call, Response<OrderListRes> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null && response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                        orderDetail = response.body().getData();
                        orderList = response.body().getDataList();

                        setUi();
                    }
                } else {
                    Functions.showToast(OrderDetailActivity.this, "Did not find your order please try again.");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {
                Functions.showToast(OrderDetailActivity.this, "Did not find your order please try again.");
                finish();
            }
        });
    }

    private void setUi() {
        txtReferCode.setText(orderDetail.getReferCode());
        txtTotalAmount.setText(formatter.format(orderDetail.getTotalAmount()) + "");
        txtTotalCartWeight.setText(orderDetail.getTotalCartWeight() + " Kg");
        txtDiscount.setText(formatter.format(orderDetail.getDiscountAmount()) + "");
        txtNetAmount.setText(formatter.format(orderDetail.getNetAmount()) + "");
        txtNetAmount2.setText(formatter.format(orderDetail.getNetAmount()) + "");

        orderContainer.removeAllViews();

        for (int i = 0; i < orderList.size(); i++) {
            orderItemView = LayoutInflater.from(this).inflate(R.layout.item_order_detail, null);
            orderContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((TfTextView) orderItemView.findViewById(R.id.txtName)).setText("Order Name : " + orderList.get(i).getName());
            ((TfTextView) orderItemView.findViewById(R.id.txtUnitValue)).setText("Unit Value : " + orderList.get(i).getUnitValue() + " " + orderList.get(i).getUnitType());
            ((TfTextView) orderItemView.findViewById(R.id.txtKgWeight)).setText("Kg Weight : " + orderList.get(i).getKGWeight() + " Kg");
            ((TfTextView) orderItemView.findViewById(R.id.txtCPrice)).setText("Current Market Price : " + formatter.format(orderList.get(i).getCurrentMarketPrice()) + " Rs.");
            ((TfTextView) orderItemView.findViewById(R.id.txtPrice)).setText("Price : " + formatter.format(orderList.get(i).getPrice()) + " Rs.");
            orderContainer.addView(orderItemView);
        }

        if (!isInquiry) {
            txtBAddress1.setText(orderDetail.getBillingAddress1() + ", " + orderDetail.getBillingAddress2() + ", " + orderDetail.getBillingPinCode());

            txtSAddress1.setText(orderDetail.getShippingAddress1() + ", " + orderDetail.getShippingAddress2() + ", " + orderDetail.getShippingPinCode());
        }
    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }
}
