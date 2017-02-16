package com.androidapp.jdklokhandwala.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AcceptOrder;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.OrderDetail;
import com.androidapp.jdklokhandwala.api.model.OrderListPojo;
import com.androidapp.jdklokhandwala.api.model.OrderListRes;
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.UserInActiveDialog;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dmax.dialog.SpotsDialog;
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
    private View categoryNameRow;
    private View dividerRow;
    private View listDivider;
    private String status = "";
    private TfTextView txtStatus;
    private RelativeLayout parentView;
    private AlertDialog dialog;
    private RelativeLayout netAmountView;
    private RelativeLayout totalAmountView;
    private RelativeLayout discountView;

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

        statusID = getIntent().getIntExtra(AppConstants.statusID, 10);
        if (statusID == 8 || statusID == 9 || statusID == 10 || statusID == 11) {
            txtCustomTitle.setText(getString(R.string.quotation_detail_title));
        } else {
            txtCustomTitle.setText(getString(R.string.order_detail_title));
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formatter = new DecimalFormat("#,##,##,###");

        parentView = (RelativeLayout) findViewById(R.id.parentView);
        parentView.setVisibility(View.GONE);
        dialog = new SpotsDialog(OrderDetailActivity.this, R.style.Custom);

        orderID = getIntent().getIntExtra("OrderID", 0);
        isInquiry = getIntent().getBooleanExtra(AppConstants.isInquiry, true);
        status = getIntent().getStringExtra(AppConstants.statusTxt);

        Log.e("details come", orderID + " || " + statusID + " || " + isInquiry);

        txtStatus = (TfTextView) findViewById(R.id.txtStatus);
        txtStatus.setText("Status : " + status);
        txtReferCode = (TfTextView) findViewById(R.id.txtReferCode);

        orderContainer = (LinearLayout) findViewById(R.id.orderListContainer);

        txtTotalAmount = (TfTextView) findViewById(R.id.txtTotalAmount);
        txtTotalCartWeight = (TfTextView) findViewById(R.id.txtTotalCartWeight);
        txtDiscount = (TfTextView) findViewById(R.id.txtDiscount);
        txtNetAmount = (TfTextView) findViewById(R.id.txtNetAmount);
        txtNetAmount2 = (TfTextView) findViewById(R.id.txtNetAmount2);


        netAmountView=(RelativeLayout)findViewById(R.id.netAmountView);
        totalAmountView=(RelativeLayout)findViewById(R.id.totalAmountView);
        discountView=(RelativeLayout)findViewById(R.id.discountView);

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
            if (Functions.isConnected(OrderDetailActivity.this)) {
                getOrderDetail(orderID);
            } else {
                Functions.showToast(OrderDetailActivity.this, getResources().getString(R.string.no_internet_connection));
            }
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

        if (statusID == 10) {
            bottomView.setVisibility(View.VISIBLE);
        } else {
            bottomView.setVisibility(View.GONE);
        }

        clickListener();
    }

    private void clickListener() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isConnected(OrderDetailActivity.this)) {
                    Intent i = new Intent(OrderDetailActivity.this, BillingActivity.class);
                    i.putExtra(AppConstants.isPlaceOrder, 1);
                    i.putExtra(AppConstants.paymentMethodID, 21);
                    i.putExtra("OrderID", orderDetail.getOrderID());
                    i.putExtra(AppConstants.isAccept, true);
                    Functions.fireIntent(OrderDetailActivity.this, i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Functions.showToast(OrderDetailActivity.this, getString(R.string.no_internet_connection));
                }
/*                AcceptOrder acceptOrder = new AcceptOrder();
                acceptOrder.setOrderID(orderDetail.getOrderID());
                acceptOrder.setIsAccept(1);
                callApi(acceptOrder);*/
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isConnected(OrderDetailActivity.this)) {
                    AcceptOrder acceptOrder = new AcceptOrder();
                    acceptOrder.setOrderID(orderDetail.getOrderID());
                    acceptOrder.setIsAccept(0);
                    if (Functions.isConnected(OrderDetailActivity.this)) {
                        callApi(acceptOrder);
                    } else {
                        Functions.showToast(OrderDetailActivity.this, getResources().getString(R.string.no_internet_connection));
                    }
                } else {
                    Functions.showToast(OrderDetailActivity.this, getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void callApi(AcceptOrder acceptOrder) {
        if (dialog != null) {
            dialog.show();
        }
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.acceptRejectQuotation(acceptOrder).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    if (response.body().getResponseCode() == 1) {
                        Functions.showToast(OrderDetailActivity.this, "Order cancel successfully.");
                        Intent i = new Intent(OrderDetailActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(OrderDetailActivity.this, i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        new UserInActiveDialog(OrderDetailActivity.this, response.body().getResponseMessage().trim()).show();
                    }
                } else {
                    Functions.showToast(OrderDetailActivity.this, getResources().getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                RetrofitErrorHelper.showErrorMsg(t, OrderDetailActivity.this);
            }
        });
    }

    private void getOrderDetail(int orderID) {
        if (dialog != null) {
            dialog.show();
        }
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getOrderDetailApi(orderID).enqueue(new Callback<OrderListRes>() {
            @Override
            public void onResponse(Call<OrderListRes> call, Response<OrderListRes> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.body() != null) {
                    if (response.body().getData() != null && response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                        orderDetail = response.body().getData();
                        orderList = response.body().getDataList();

                        setUi();
                    } else {
                        Functions.showToast(OrderDetailActivity.this, response.body().getResponseMessage().trim());
                    }
                } else {
                    Functions.showToast(OrderDetailActivity.this, "Did not find your order please try again.");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                RetrofitErrorHelper.showErrorMsg(t, OrderDetailActivity.this);
                finish();
            }
        });
    }

    private void setUi() {

        parentView.setVisibility(View.VISIBLE);

        Collections.sort(orderList, new Comparator<OrderListPojo>() {
            @Override
            public int compare(OrderListPojo orderListPojo, OrderListPojo t1) {
                return orderListPojo.getCategoryName().compareTo(t1.getCategoryName());
            }
        });


        Log.e("total cart weight", orderDetail.getTotalCartWeight() + "");
        Log.e("total cart weight", Functions.jsonString(orderDetail));
        txtReferCode.setText(orderDetail.getReferCode());
        txtTotalAmount.setText(getString(R.string.Rs) + " " + formatter.format(orderDetail.getTotalAmount()) + "");
        txtTotalCartWeight.setText(Functions.getFormatedInt(orderDetail.getTotalCartWeight()) + " Kg");
        txtDiscount.setText(getString(R.string.Rs) + " " + formatter.format(orderDetail.getDiscountAmount()) + "");
        txtNetAmount.setText(getString(R.string.Rs) + " " + formatter.format(orderDetail.getNetAmount()) + "");
        txtNetAmount2.setText(getString(R.string.Rs) + " " + formatter.format(orderDetail.getNetAmount()) + "");

        // TODO: 15-02-2017
        if (getPriceVisiblity()) {
//        if (!orderDetail.isViaInquiry()) {
            txtTotalAmount.setVisibility(View.VISIBLE);
            txtDiscount.setVisibility(View.VISIBLE);
            txtNetAmount.setVisibility(View.VISIBLE);
            txtNetAmount2.setVisibility(View.VISIBLE);

            discountView.setVisibility(View.VISIBLE);
            netAmountView.setVisibility(View.VISIBLE);
            totalAmountView.setVisibility(View.VISIBLE);
        } else {
            txtTotalAmount.setVisibility(View.GONE);
            txtDiscount.setVisibility(View.GONE);
            txtNetAmount.setVisibility(View.GONE);
            txtNetAmount2.setVisibility(View.GONE);

            discountView.setVisibility(View.GONE);
            netAmountView.setVisibility(View.GONE);
            totalAmountView.setVisibility(View.GONE);
        }

        orderContainer.removeAllViews();

        String categoryName = "";
        int listLine = 0;
        for (int i = 0; i < orderList.size(); i++) {
            orderItemView = LayoutInflater.from(this).inflate(R.layout.item_order_detail, null);
            orderContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            categoryNameRow = LayoutInflater.from(this).inflate(R.layout.item_category_name, null);
            dividerRow = LayoutInflater.from(this).inflate(R.layout.item_divider, null);
            listDivider = LayoutInflater.from(this).inflate(R.layout.item_list_divider, null);
            categoryNameRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dividerRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listDivider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Log.e(categoryName.toString().trim(), orderList.get(i).getName().trim());
            if (!categoryName.toString().trim().equals(orderList.get(i).getCategoryName().trim())) {
                categoryName = orderList.get(i).getCategoryName().trim();
                listLine = 0;
                if (i != 0) {
                    orderContainer.addView(dividerRow);
                }
                ((TfTextView) categoryNameRow.findViewById(R.id.txtCategoryName)).setText(orderList.get(i).getCategoryName());
                orderContainer.addView(categoryNameRow);
            }
            if (listLine != 0) {
                orderContainer.addView(listDivider);
            }
            listLine++;
            ((TfTextView) orderItemView.findViewById(R.id.txtName)).setText("Product Name : " + orderList.get(i).getName());
            ((TfTextView) orderItemView.findViewById(R.id.txtUnitValue)).setText("Unit Value : " + Functions.getFormatedInt(orderList.get(i).getUnitValue()) + " " + orderList.get(i).getUnitType());
            ((TfTextView) orderItemView.findViewById(R.id.txtKgWeight)).setText("Kg Weight : " + Functions.getFormatedInt(orderList.get(i).getKGWeight()) + " Kg");
            ((TfTextView) orderItemView.findViewById(R.id.txtCPrice)).setText("Current Market Price : " + getString(R.string.Rs) + " " + formatter.format(orderList.get(i).getCurrentMarketPrice()) + " Rs.");
            ((TfTextView) orderItemView.findViewById(R.id.txtPrice)).setText(getString(R.string.Rs) + " " + formatter.format(orderList.get(i).getPrice()) + "");

            if (getPriceVisiblity()) {
                ((RelativeLayout) orderItemView.findViewById(R.id.priceView)).setVisibility(View.VISIBLE);
            } else {
                ((RelativeLayout) orderItemView.findViewById(R.id.priceView)).setVisibility(View.GONE);
            }

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

    public boolean getPriceVisiblity() {
        if (statusID == 12 || statusID == 13 || statusID == 14) {
            return false;
        } else {
            return true;
        }
    }
}
