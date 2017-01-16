package com.androidapp.jdklokhandwala.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.ProductsListActivity;
import com.androidapp.jdklokhandwala.adapter.BookmarkAdapter;
import com.androidapp.jdklokhandwala.api.model.Bookmark;
import com.androidapp.jdklokhandwala.api.model.Category;
import com.androidapp.jdklokhandwala.custom.EmptyLayout;
import com.androidapp.jdklokhandwala.custom.familiarrecyclerview.FamiliarRecyclerView;

import java.util.List;

/**
 * Created by ishan on 29-12-2016.
 */

public class BookmarksFragment extends Fragment {

    private View parentView;
    private List<Bookmark> bookmarkList;
    private FamiliarRecyclerView categoryRV;
    //private List<Category> categoryList;
    private EmptyLayout emptyLayout;
    private BookmarkAdapter adapter;

    public static BookmarksFragment newInstance() {
        BookmarksFragment bookmarksFragment = new BookmarksFragment();
        return bookmarksFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_category_list, container, false);
        init();
        return parentView;
    }

    private void init() {

        bookmarkList = Bookmark.GetBookmarkList();
        Log.e("Bookmark List", bookmarkList.toString());

        categoryRV = (FamiliarRecyclerView) parentView.findViewById(R.id.categoryRV);
        categoryRV.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Bookmark bookmark = bookmarkList.get(position);

                Category category = new Category();
                category.setImage(bookmark.Image());
                category.setName(bookmark.Name());
                category.setCategoryID(bookmark.CategoryId().intValue());

                Intent intent = new Intent(getActivity(), ProductsListActivity.class);
                intent.putExtra("category", category);
                getContext().startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        emptyLayout = (EmptyLayout) parentView.findViewById(R.id.emptyLayout);
        categoryRV.setEmptyView(emptyLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        categoryRV.setLayoutManager(layoutManager);
        adapter = new BookmarkAdapter(getActivity(), bookmarkList);
        categoryRV.setAdapter(adapter);

        emptyLayout.setContent("No Bookmark Category Found.", R.drawable.ic_category);

    }
}
