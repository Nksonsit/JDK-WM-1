package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.NotificationItem;
import com.androidapp.jdklokhandwala.api.model.NotificationItemRes;
import com.androidapp.jdklokhandwala.custom.BadgeHelper;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.fragment.AboutUsFragment;
import com.androidapp.jdklokhandwala.fragment.BookmarksFragment;
import com.androidapp.jdklokhandwala.fragment.CategoryListFragment;
import com.androidapp.jdklokhandwala.fragment.ContactUsFragment;
import com.androidapp.jdklokhandwala.fragment.MyOrderFragment;
import com.androidapp.jdklokhandwala.fragment.MyProfileFragment;
import com.androidapp.jdklokhandwala.helper.AppConstants;
import com.androidapp.jdklokhandwala.helper.CustomTypefaceSpan;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.PrefUtils;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private TfTextView txtCustomTitle;
    private ArrayList<NotificationItem> notificationItems;
    private int no_of_notification;
    private BadgeHelper badgeHelper, badgeCart;
    MenuItem cartItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callNotificationApi();
        hideUserAction();
        int cartSize = AddToCart.getCartList().size();
        if (badgeCart != null) {
            badgeCart.displayBadge(cartSize);
        }
    }

    public void selectMenuItem(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    private void init() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);
        txtCustomTitle.setText(getString(R.string.categories_title));
        setSupportActionBar(toolbar);

        initDrawer();


    }

    private void hideUserAction() {
        Menu m = navigationView.getMenu();
        MenuItem orderItem = m.getItem(2);
        MenuItem logoutItem = m.getItem(6);
        if (PrefUtils.isUserLoggedIn(DashboardActivity.this)) {
            orderItem.setVisible(true);
            logoutItem.setVisible(true);
        } else {
            orderItem.setVisible(false);
            logoutItem.setVisible(false);
        }
    }

    private void initDrawer() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            applyFontToMenuItem(menuItem);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                setDrawerClick(menuItem.getItemId(), menuItem);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        initFragment(CategoryListFragment.newInstance(), "Categories");
    }

    private void setDrawerClick(int itemId, MenuItem menuItem) {
        switch (itemId) {
            case R.id.drawer_category:
                setUnCheckedDrawerMenu();
                menuItem.setChecked(true);
                initFragment(CategoryListFragment.newInstance(), "Categories");
                break;

            case R.id.drawer_profile:
                if (PrefUtils.isUserLoggedIn(DashboardActivity.this)) {
                    setUnCheckedDrawerMenu();
                    menuItem.setChecked(true);
                    initFragment(MyProfileFragment.newInstance(), "My Profile");
                } else {
                    Functions.showAlertDialogWithOkCancel(DashboardActivity.this, "You need to login first.", new Functions.DialogOptionsSelectedListener() {
                        @Override
                        public void onSelect(boolean isYes) {
                            if (isYes) {
                                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                                i.putExtra(AppConstants.isPlaceOrder, 2);
                                Functions.fireIntent(DashboardActivity.this, i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                            }
                        }
                    });
                }
                break;

            case R.id.drawer_order:
                setUnCheckedDrawerMenu();
                menuItem.setChecked(true);
                initFragment(MyOrderFragment.newInstance(), "My Orders");
                break;

            case R.id.drawer_bookmark:
                setUnCheckedDrawerMenu();
                menuItem.setChecked(true);
                initFragment(BookmarksFragment.newInstance(), "Bookmarks");
                break;

            case R.id.drawer_about:
                setUnCheckedDrawerMenu();
                menuItem.setChecked(true);
                initFragment(AboutUsFragment.newInstance(), "About Us");
                break;

            case R.id.drawer_contact_us:
                setUnCheckedDrawerMenu();
                menuItem.setChecked(true);
                initFragment(ContactUsFragment.newInstance(), "Contact Us");
                break;

            case R.id.drawer_logout:
                setUnCheckedDrawerMenu();
                // menuItem.setChecked(true);
                Functions.showAlertDialogWithOkCancel(DashboardActivity.this, "Are you sure want to logout?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes) {
                            Functions.clearSession(DashboardActivity.this);
                            Functions.showToast(DashboardActivity.this, "Logout Successfully.");
                            hideUserAction();

                            Intent intent = new Intent(DashboardActivity.this, SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                });
                break;

        }
    }

    private void initFragment(Fragment fragment, String title) {
        setHeaderTitle(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, title);
        fragmentTransaction.commit();
    }

    private void setHeaderTitle(String title) {
        txtCustomTitle.setText(title);
    }

    private void applyFontToMenuItem(MenuItem menuItem) {
        SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", Functions.getBoldFont(this)), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(mNewTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        cartItem = menu.findItem(R.id.menu_cart);
        badgeHelper = new BadgeHelper(this, menu.findItem(R.id.menu_noti), ActionItemBadge.BadgeStyles.GREY);
        badgeCart = new BadgeHelper(this, menu.findItem(R.id.menu_cart), ActionItemBadge.BadgeStyles.GREY);
        if (no_of_notification > 0) {
            badgeHelper.displayBadge(no_of_notification);
        }
        int cartSize = AddToCart.getCartList().size();
        if (cartSize > 0 && badgeCart != null) {
            badgeCart.displayBadge(cartSize);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calc:
                startActivity(new Intent(this, WeightCalculatorActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);

                break;

            case R.id.menu_noti:
                Intent i1 = new Intent(this, NotificationActivity.class);
                i1.putExtra("notificationItems", (Serializable) notificationItems);
                startActivity(i1);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
                break;

            case R.id.menu_cart:
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUnCheckedDrawerMenu() {
        for (int i = 0; i < 6; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private ArrayList<NotificationItem> callNotificationApi() {
        notificationItems = new ArrayList<>();
        if (PrefUtils.isUserLoggedIn(DashboardActivity.this)) {
            AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
            appApi.getNotificationList(PrefUtils.getUserFullProfileDetails(this).getUserID(), 0).enqueue(new Callback<NotificationItemRes>() {
                @Override
                public void onResponse(Call<NotificationItemRes> call, Response<NotificationItemRes> response) {

                    if (response.body() != null) {
                        // Log.e("resp", response.body().getResponseMessage() + " || " + response.body().Data.lstnotification.size());
                        if (response.body().Data != null && response.body().Data.lstnotification != null && response.body().Data.lstnotification.size() > 0) {
                            no_of_notification = response.body().Data.UnReadCount;
                            if(badgeHelper!=null){
                            badgeHelper.displayBadge(no_of_notification);}

                            notificationItems.addAll(response.body().Data.lstnotification);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationItemRes> call, Throwable t) {

                }
            });

        }
        return notificationItems;
    }
}
