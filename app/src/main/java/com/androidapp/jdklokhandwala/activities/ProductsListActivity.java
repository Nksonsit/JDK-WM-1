package com.androidapp.jdklokhandwala.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.adapter.ProductAdapter;
import com.androidapp.jdklokhandwala.api.call.GetProducts;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.AddToCartPojo;
import com.androidapp.jdklokhandwala.api.model.Category;
import com.androidapp.jdklokhandwala.api.model.Product;
import com.androidapp.jdklokhandwala.custom.AddToCartDialog;
import com.androidapp.jdklokhandwala.custom.BadgeHelper;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ProductsListActivity extends AppCompatActivity {

    private Category category;
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private EmptyLayout emptyLayout;
    private FamiliarRecyclerView productRV;
    private List<Product> productList;
    private ProductAdapter adapter;
    private ImageView imgCart;
    private SwipeRefreshLayout swipeRefresh;
    private AlertDialog dialog;
    private MenuItem cartItem;
    private BadgeHelper badgeCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        category = (Category) getIntent().getSerializableExtra("category");

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu);
        cartItem = menu.findItem(R.id.menu_cart);
        badgeCart = new BadgeHelper(this, menu.findItem(R.id.menu_cart), ActionItemBadge.BadgeStyles.GREY);
        int cartSize = AddToCart.getCartList().size();
        if (badgeCart != null) {
            badgeCart.displayBadge(cartSize);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cartSize = AddToCart.getCartList().size();
        if (badgeCart != null) {
            badgeCart.displayBadge(cartSize);
        }
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(category.getName());

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new SpotsDialog(this, R.style.Custom);
        imgCart = (ImageView) findViewById(R.id.imgCart);

        initRecyclerView();

        clickListener();
    }

    private void clickListener() {
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsListActivity.this, CartActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
            }
        });

        productRV.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Product product = productList.get(position);
                if (!AddToCart.CheckDuplication(category.getCategoryID(), product.getProductID())) {

                    new AddToCartDialog(ProductsListActivity.this, "ADD TO ORDER BOOK", "", (double) 0, product.getUnitsOfMeasure(), new AddToCartDialog.OnAddClick() {
                        @Override
                        public void onAddClick(String quantity, String type) {
                            Log.e(quantity, type);
                            AddToCartPojo addToCart = new AddToCartPojo();
                            addToCart.setCategoryID((long) category.getCategoryID());
                            addToCart.setProductID((long) product.getProductID());
                            addToCart.setName(product.getName() + "  " + product.getCodeValue() + "  " + Functions.getFormatedInt(product.getWeight()));
                            addToCart.setUnitType(type);
                            addToCart.setDefaultWeight(product.getWeight());
                            addToCart.setUnitValue(Double.valueOf(quantity));

                            Log.e("new valuew",String.format("%.2f",new BigDecimal((Double.valueOf("222.2")*Double.valueOf("99999.0")))));

                            if (type.toString().toLowerCase().trim().contains("kg")) {
                                addToCart.setKgWeight(Double.valueOf(quantity));
                            } else {
                                addToCart.setKgWeight((product.getWeight() * Double.valueOf(String.format("%.2f",Double.valueOf(quantity)))));
                            }

                            String unitTypes = "";
                            for (int i = 0; i < product.getUnitsOfMeasure().size(); i++) {
                                unitTypes = unitTypes + "," + product.getUnitsOfMeasure().get(i).getUnitOfMeasure();
                            }
                            addToCart.setUnitTypes(unitTypes);

                            if (!AddToCart.CheckDuplication(addToCart)) {
                                Functions.showToast(ProductsListActivity.this, "Added Successfully.");
                                AddToCart.InsertProduct(addToCart);
                            } else {
                                Functions.showToast(ProductsListActivity.this, "You have already added this product to cart.");
                            }
                            int cartSize = AddToCart.getCartList().size();
                            if (badgeCart != null) {
                                badgeCart.displayBadge(cartSize);
                            }
                        }
                    }).show();

                } else {
                    Functions.showToast(ProductsListActivity.this, "You have already added to cart.");
                }
            }
        });
    }

    private void initRecyclerView() {
        productList = new ArrayList<>();

        productRV = (FamiliarRecyclerView) findViewById(R.id.productRV);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        productRV.setLayoutManager(layoutManager);
        // productRV.addItemDecoration(new LineDividerItemDecoration(this, R.drawable.line_divider_half_black));

        productRV.setEmptyView(emptyLayout);

        emptyLayout.setContent("No Product for this category.", R.drawable.ic_profile);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getList(true);
                    }
                });
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getList(false);
            }
        });

    }

    private void getList(boolean isPullToRefresh) {
        new GetProducts(this, category.getCategoryID(), new GetProducts.OnGetProducts() {
            @Override
            public void onGetProduct(List<Product> dataList) {
                productList = dataList;
                adapter = new ProductAdapter(ProductsListActivity.this, productList, new ProductAdapter.OnOptionSelectedListener() {
                    @Override
                    public void doPerformAction(int actionType, int adapterPosition) {
                        Product product = productList.get(adapterPosition);
                        switch (actionType) {
                            case AppConstants.ADD_CART_PRODUCT:

                                if (!AddToCart.CheckDuplication(category.getCategoryID(), product.getProductID())) {

                                    new AddToCartDialog(ProductsListActivity.this, "ADD TO ORDER BOOK", "", (double) 0, product.getUnitsOfMeasure(), new AddToCartDialog.OnAddClick() {
                                        @Override
                                        public void onAddClick(String quantity, String type) {
                                            Log.e("quantity-->",quantity+" "+Double.valueOf(quantity)+" "+Double.valueOf(String.format("%.2f",Double.valueOf(quantity))));
                                            Log.e(quantity, type);
                                            AddToCartPojo addToCart = new AddToCartPojo();
                                            addToCart.setCategoryID((long) category.getCategoryID());
                                            addToCart.setProductID((long) product.getProductID());
                                            addToCart.setName(product.getName() + "  " + product.getCodeValue() + "  " + product.getWeight());
                                            addToCart.setUnitType(type);
                                            addToCart.setUnitValue(Double.valueOf(String.format("%.2f",Double.valueOf(quantity))));


                                            Log.e(""+(product.getWeight() * Double.valueOf(String.format("%.2f",Double.valueOf(quantity)))),(product.getWeight() +" "+ Double.valueOf(String.format("%.2f",Double.valueOf(quantity)))));
                                            Log.e(""+(Float.valueOf(String.format("%.2f",product.getWeight())) * Float.valueOf(String.format("%.2f",Double.valueOf(quantity)))),(Float.valueOf(String.format("%.2f",product.getWeight())) +" "+ Float.valueOf(String.format("%.2f",Double.valueOf(quantity)))));

                                            if (type.toString().toLowerCase().trim().contains("kg")) {
                                                addToCart.setKgWeight(Double.valueOf(quantity));
                                            } else {
                                                addToCart.setKgWeight((product.getWeight() * Double.valueOf(String.format("%.2f",Double.valueOf(quantity)))));
                                            }
                                            addToCart.setDefaultWeight(product.getWeight());
                                            String unitTypes = "";
                                            for (int i = 0; i < product.getUnitsOfMeasure().size(); i++) {
                                                unitTypes = unitTypes + "," + product.getUnitsOfMeasure().get(i).getUnitOfMeasure();
                                            }
                                            addToCart.setUnitTypes(unitTypes);

                                            if (!AddToCart.CheckDuplication(addToCart)) {
                                                Functions.showToast(ProductsListActivity.this, "Added Successfully.");
                                                AddToCart.InsertProduct(addToCart);
                                            } else {
                                                Functions.showToast(ProductsListActivity.this, "You have already added this product to order book.");
                                            }

                                            int cartSize = AddToCart.getCartList().size();
                                            if (badgeCart != null) {
                                                badgeCart.displayBadge(cartSize);
                                            }
                                        }
                                    }).show();

                                } else {
                                    Functions.showToast(ProductsListActivity.this, "You have already added to order book.");
                                }

                                break;


                        }
                    }
                });
                productRV.setAdapter(adapter);
            }

            @Override
            public void showProgress() {
                if (dialog != null && !isPullToRefresh) {
                    dialog.show();
                }
            }

            @Override
            public void dismissProgress() {
                if (dialog != null && !isPullToRefresh) {
                    dialog.dismiss();
                }
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }).CallApi();
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
