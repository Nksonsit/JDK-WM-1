package com.androidapp.jdklokhandwala.activities;

import android.app.AlertDialog;
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
import com.androidapp.jdklokhandwala.api.model.RegistrationRes;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.androidapp.jdklokhandwala.custom.AddToCartDialog;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.PaymentMethodDialog;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.UserInActiveDialog;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.custom.wheelpicker.OrderSuccessDialog;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;

import java.math.BigDecimal;
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

        emptyLayout.setContent("Your Order Book is empty.", R.drawable.ic_empty_cart);
        emptyLayout.setAddProductLink(CartActivity.this);

        adapter = new CartAdapter(this, addToCartList, new CartAdapter.OnOptionSelectedListener() {
            @Override
            public void doPerformAction(int actionType, int adapterPosition) {
                AddToCart product = addToCartList.get(adapterPosition);
                switch (actionType) {
                    case AppConstants.DELETE_CART_PRODUCT:
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setMessage("Are you sure want to remove this product?")
                                .setCancelable(true)
                                .setPositiveButton("YES", (dialog, id) -> {
                                    addToCartList.remove(adapterPosition);
                                    adapter.notifyItemRemoved(adapterPosition);
                                    AddToCart.DeleteItem(product.CategoryID(), product.ProductID());

                                    Functions.showToast(CartActivity.this, "Delete product from order book.");
                                    if (addToCartList.size() == 0) {
                                        buttonLayout.setVisibility(View.GONE);
                                    } else {
                                        adapter.setdataList(addToCartList);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                    }
                                    dialog.dismiss();
                                })
                                .setNegativeButton("NO", (dialog, id) -> {
                                    dialog.dismiss();
                                });
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();
                        break;

                    case AppConstants.EDIT_CART_PRODUCT:
                        new AddToCartDialog(CartActivity.this, "UPDATE", product.UnitType(), product.UnitValue(), Functions.getListFromString(product.UnitTypes()), new AddToCartDialog.OnAddClick() {
                            @Override
                            public void onAddClick(String quantity, String type) {

                                AddToCartPojo addToCartPojo = new AddToCartPojo();
                                addToCartPojo.setCategoryID(product.CategoryID());
                                addToCartPojo.setProductID(product.ProductID());
                                addToCartPojo.setName(product.Name());
                                addToCartPojo.setUnitValue(Double.valueOf(String.format("%.2f", Double.valueOf(quantity))));
                                addToCartPojo.setUnitType(type);
                                addToCartPojo.setUnitTypes(product.UnitTypes());

                                if (type.toString().toLowerCase().trim().contains("kg")) {
                                    addToCartPojo.setKgWeight(Double.valueOf(quantity));
                                } else {
                                    addToCartPojo.setKgWeight((product.DefaultWeight() * Double.valueOf(String.format("%.2f", Double.valueOf(quantity)))));
                                }

                                addToCartPojo.setDefaultWeight(product.DefaultWeight());

                                AddToCart.UpdateItem(addToCartPojo);
                                addToCartList = AddToCart.getCartList();
//                                adapter.notifyDataSetChanged();

                                Log.e("addToCartPojo---> ",Functions.jsonString(addToCartPojo));
                                adapter.setdataList(AddToCart.getCartList());
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
                if (Functions.isConnected(CartActivity.this)) {
                    if (addToCartList.size() > 0) {

                        if (Functions.getTotalWeight() >= 5000) {
                            Functions.showAlertDialogWithYesNo(CartActivity.this, getString(R.string.weight_limit), new Functions.DialogOptionsSelectedListener() {
                                @Override
                                public void onSelect(boolean isYes) {
                                    if (isYes) {
                                        proceedlaceOrder();
                                    }
                                }
                            });
                        } else {
                            proceedlaceOrder();
                        }
                    } else {
                        Functions.showToast(CartActivity.this, "Please first add item to order book.");
                    }
                } else {
                    Functions.showToast(CartActivity.this, getResources().getString(R.string.no_internet_connection));
                }
            }
        });
        quotationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isConnected(CartActivity.this)) {
                    if (addToCartList.size() > 0) {
                        if (PrefUtils.isUserLoggedIn(CartActivity.this)) {
                            new PaymentMethodDialog(CartActivity.this, getResources().getString(R.string.payment_mode_dialog), new PaymentMethodDialog.OnSelectClick() {
                                @Override
                                public void onSelectClick(int id) {
                                    doGetQuotation(id);
                                }
                            }).show();
                        } else {
                            new PaymentMethodDialog(CartActivity.this, getResources().getString(R.string.payment_mode_dialog), new PaymentMethodDialog.OnSelectClick() {
                                @Override
                                public void onSelectClick(int id) {
                                    Intent i = new Intent(CartActivity.this, LoginActivity.class);
                                    i.putExtra(AppConstants.isPlaceOrder, 0);
                                    i.putExtra(AppConstants.paymentMethodID, id);
                                    Functions.fireIntent(CartActivity.this, i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }).show();
                        }
                    } else {
                        Functions.showToast(CartActivity.this, "Please first add item to order book.");
                    }
                } else {
                    Functions.showToast(CartActivity.this, getResources().getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void proceedlaceOrder() {
        if (PrefUtils.isUserLoggedIn(CartActivity.this)) {
            Intent i = new Intent(CartActivity.this, BillingActivity.class);
            i.putExtra(AppConstants.isPlaceOrder, 1);
            i.putExtra(AppConstants.paymentMethodID, 21);
            Functions.fireIntent(CartActivity.this, i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Intent i = new Intent(CartActivity.this, LoginActivity.class);
            i.putExtra(AppConstants.isPlaceOrder, 1);
            i.putExtra(AppConstants.paymentMethodID, 21);
            Functions.fireIntent(CartActivity.this, i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    private void doGetQuotation(int id) {
        dialog.show();
        List<AddToCart> list = AddToCart.getCartList();
        List<AddToCartTemp> listInput = new ArrayList<AddToCartTemp>();
        Double totalWeight = Double.valueOf(0);
        for (int i = 0; i < list.size(); i++) {
            totalWeight = totalWeight + list.get(i).KgWeight();
            listInput.add(new AddToCartTemp(list.get(i).CategoryID(), list.get(i).ProductID(), list.get(i).Name(), list.get(i).UnitType(), list.get(i).UnitValue(), new BigDecimal(list.get(i).KgWeight()).setScale(2, BigDecimal.ROUND_HALF_UP)+""));
        }

        PlaceOrderReq placeOrderReq = new PlaceOrderReq();
        placeOrderReq.setOrderID(0);
        placeOrderReq.setIsOrder(0);
        placeOrderReq.setUserID(userPojo.getUserID());
        placeOrderReq.setTotalCartWeight(new BigDecimal(totalWeight).setScale(2, BigDecimal.ROUND_HALF_UP)+"");
        placeOrderReq.setPaymentMethodID(id);
        placeOrderReq.setTotalCartItem(listInput.size());
        placeOrderReq.setCartItemList(listInput);


        Log.e("Place Order Pojo --> ",Functions.jsonString(placeOrderReq));

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
                    if (response.body().getResponseCode() == 1) {
                        AddToCart.DeleteAllData();
                        new OrderSuccessDialog(CartActivity.this, "Q").show();
                    } else {
                        new UserInActiveDialog(CartActivity.this,response.body().getResponseMessage().trim()).show();
                    }
                } else {
                    Functions.showToast(CartActivity.this, getResources().getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                dialog.dismiss();
                RetrofitErrorHelper.showErrorMsg(t, CartActivity.this);

            }
        });
    }
}
