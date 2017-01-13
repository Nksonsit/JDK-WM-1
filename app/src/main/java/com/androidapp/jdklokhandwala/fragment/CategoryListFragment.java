package com.androidapp.jdklokhandwala.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.ProductsListActivity;
import com.androidapp.jdklokhandwala.adapter.CategoryAdapter;
import com.androidapp.jdklokhandwala.api.call.GetCategory;
import com.androidapp.jdklokhandwala.api.model.Category;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.LineDividerItemDecoration;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class CategoryListFragment extends Fragment {

    private View parentView;
    private FamiliarRecyclerView categoryRV;
    private List<Category> categoryList;
    private EmptyLayout emptyLayout;
    private CategoryAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private AlertDialog dialog;

    public static CategoryListFragment newInstance() {
        CategoryListFragment fragment = new CategoryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_category_list, container, false);
        init();
        return parentView;
    }

    private void init() {

        categoryList = new ArrayList<>();
        dialog = new SpotsDialog(getActivity(), R.style.Custom);

        categoryRV = (FamiliarRecyclerView) parentView.findViewById(R.id.categoryRV);
        emptyLayout = (EmptyLayout) parentView.findViewById(R.id.emptyLayout);
        swipeRefresh = (SwipeRefreshLayout) parentView.findViewById(R.id.swipeRefresh);
        categoryRV.setEmptyView(emptyLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        categoryRV.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(getActivity(), categoryList);
        categoryRV.setAdapter(adapter);


        categoryRV.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Category category = categoryList.get(position);
                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                intent.putExtra("category", category);
                getContext().startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        emptyLayout.setContent("No Category Found.", R.drawable.ic_profile);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getList(true);
                    }
                });
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getList(false);
            }
        });

    }

    public void getList(final boolean isPullToRefresh) {

        if(isPullToRefresh)
            categoryList.clear();

        new GetCategory(getActivity(), new GetCategory.OnGetCategoryListener() {
            @Override
            public void onGet(List<Category> dataList) {
                categoryList.addAll(dataList);
                adapter.addAll(categoryList);
            }

            @Override
            public void showProgress() {
                if (!isPullToRefresh) {
                    if (dialog != null) {
                        dialog.show();
                    }
                }
            }

            @Override
            public void dismissProgress() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }

        }).call();

    }
}
