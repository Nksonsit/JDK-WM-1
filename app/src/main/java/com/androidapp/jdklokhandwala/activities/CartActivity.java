package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.CartAdapter;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartPojo;
import com.androidapp.jdklokhandwala.api.model.AddToCartTemp;
import com.androidapp.jdklokhandwala.api.model.BaseResponse;
import com.androidapp.jdklokhandwala.api.model.PlaceOrderReq;
import com.androidapp.jdklokhandwala.api.model.Product;
import com.androidapp.jdklokhandwala.api.model.UnitMeasure;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.AddToCartDialog;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.PaymentMethodDialog;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private EmptyLayout emptyLayout;
    private FamiliarRecyclerView cartProductRV;
    private List<AddToCart> addToCartList;
    private CartAdapter adapter;
    private TfButton placeOrderBtn;
    private LinearLayout buttonLayout;
    private TfButton quotationBtn;
    private UserPojo userPojo;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.cart_title));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        dialog = new SpotsDialog(CartActivity.this, R.style.Custom);
        initRecyclerView();

        placeOrderBtn = (TfButton) findViewById(R.id.placeOrderBtn);
        quotationBtn = (TfButton) findViewById(R.id.quotationBtn);

        userPojo = PrefUtils.getUserFullProfileDetails(CartActivity.this);

        initRecyclerView();

        clickListener();
    }


    private void initRecyclerView() {
        cartProductRV = (FamiliarRecyclerView) findViewById(R.id.cartProductRV);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        addToCartList = new ArrayList<>();

        addToCartList = AddToCart.getCartList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        cartProductRV.setLayoutManager(layoutManager);

        cartProductRV.setEmptyView(emptyLayout);

        emptyLayout.setContent("Your Cart is empty.", R.drawable.ic_empty_cart);

        adapter = new CartAdapter(this, addToCartList, new CartAdapter.OnOptionSelectedListener() {
            @Override
            public void doPerformAction(int actionType, int adapterPosition) {
                AddToCart product = addToCartList.get(adapterPosition);
                switch (actionType) {
                    case AppConstants.DELETE_CART_PRODUCT:
                        addToCartList.remove(adapterPosition);
                        adapter.notifyItemRemoved(adapterPosition);
                        AddToCart.DeleteItem(product.CategoryID(), product.ProductID());
                        Functions.showToast(CartActivity.this, "Delete");
                        if (addToCartList.size() == 0) {
                            buttonLayout.setVisibility(View.GONE);
                        } else {
                            buttonLayout.setVisibility(View.VISIBLE);
                        }
                        break;

                    case AppConstants.EDIT_CART_PRODUCT:
                        new AddToCartDialog(CartActivity.this, "UPDATE",product.UnitValue(),Functions.getListFromString(product.UnitTypes()), new AddToCartDialog.OnAddClick() {
                            @Override
                            public void onAddClick(String quantity, String type) {
                                Log.e(quantity, type);
                                AddToCartPojo addToCartPojo = new AddToCartPojo();
                                addToCartPojo.setCategoryID(product.CategoryID());
                                addToCartPojo.setProductID(product.ProductID());
                                addToCartPojo.setName(product.Name());
                                addToCartPojo.setUnitValue(Double.valueOf(quantity));
                                addToCartPojo.setUnitType(type);
                                addToCartPojo.setKgWeight(product.KgWeight());
                                AddToCart.UpdateItem(addToCartPojo);
                                adapter.updateList();
                            }
                        }).show();
                        break;
                }
            }
        });

        if (addToCartList.size() == 0) {
            buttonLayout.setVisibility(View.GONE);
        } else {
            buttonLayout.setVisibility(View.VISIBLE);
        }
        cartProductRV.setAdapter(adapter);
    }

    private void clickListener() {
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addToCartList.size() > 0) {
                    if (PrefUtils.isUserLoggedIn(CartActivity.this)) {
                        Intent i = new Intent(CartActivity.this, BillingActivity.class);
                        i.putExtra(AppConstants.isPlaceOrder, 1);
                        Functions.fireIntent(CartActivity.this, i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Functions.showAlertDialogWithOkCancel(CartActivity.this, "", new Functions.DialogOptionsSelectedListener() {
                            @Override
                            public void onSelect(boolean isYes) {
                                if (isYes) {
                                    Intent i = new Intent(CartActivity.this, LoginActivity.class);
                                    i.putExtra(AppConstants.isPlaceOrder, 1);
                                    Functions.fireIntent(CartActivity.this, i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        });
                    }
                } else {
                    Functions.showToast(CartActivity.this, "Please first add item to cart.");
                }
            }
        });
        quotationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addToCartList.size() > 0) {
                    if (PrefUtils.isUserLoggedIn(CartActivity.this)) {
                        doGetQuotation();
                    } else {
                        new PaymentMethodDialog(CartActivity.this, "", new PaymentMethodDialog.OnSelectClick() {
                            @Override
                            public void onSelectClick(int id) {
                                Functions.showAlertDialogWithOkCancel(CartActivity.this, "", new Functions.DialogOptionsSelectedListener() {
                                    @Override
                                    public void onSelect(boolean isYes) {
                                        if (isYes) {
                                            Intent i = new Intent(CartActivity.this, LoginActivity.class);
                                            i.putExtra(AppConstants.isPlaceOrder, 0);
                                            i.putExtra(AppConstants.paymentMethodID, id);
                                            Functions.fireIntent(CartActivity.this, i);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        }
                                    }
                                });

                            }
                        });
                    }
                } else {
                    Functions.showToast(CartActivity.this, "Please first add item to cart.");
                }
            }
        });
    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    private void doGetQuotation() {
        dialog.show();
        List<AddToCart> list = AddToCart.getCartList();
        List<AddToCartTemp> listInput = new ArrayList<AddToCartTemp>();
        Double totalWeight = Double.valueOf(0);
        for (int i = 0; i < list.size(); i++) {
            totalWeight = totalWeight + list.get(i).KgWeight();
            listInput.add(new AddToCartTemp(list.get(i).CategoryID(), list.get(i).ProductID(), list.get(i).Name(), list.get(i).UnitType(), list.get(i).UnitValue(), list.get(i).KgWeight()));
        }

        PlaceOrderReq placeOrderReq = new PlaceOrderReq();
        placeOrderReq.setOrderID(0);
        placeOrderReq.setIsOrder(0);
        placeOrderReq.setUserID(userPojo.getUserID());
        placeOrderReq.setTotalCartWeight(totalWeight);
        placeOrderReq.setPaymentMethodID(20);
        placeOrderReq.setTotalCartItem(listInput.size());
        placeOrderReq.setCartItemList(listInput);

        doPlaceOrderApiCall(placeOrderReq);
    }

    private void doPlaceOrderApiCall(PlaceOrderReq placeOrderReq) {
        Log.e("place order req", MyApplication.getGson().toJson(placeOrderReq).toString());

        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.placeQuotationOrOrder(placeOrderReq).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                dialog.dismiss();
                if (response.body() != null && response.body().getResponseMessage() != null) {
                    Log.e("place order res", MyApplication.getGson().toJson(response.body()).toString());
                    if (response.body().getResponseMessage().toString().trim().toLowerCase().contains("success")) {
                        AddToCart.DeleteAllData();
                        Functions.showToast(CartActivity.this, response.body().getResponseMessage());
                        Intent i = new Intent(CartActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Functions.fireIntent(CartActivity.this, i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Functions.showToast(CartActivity.this, "Fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }
}
