package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.Bookmark;
import com.androidapp.jdklokhandwala.api.model.Category;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.bumptech.glide.Glide;
import com.intrusoft.squint.DiagonalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.setCategory(category);

        if (Bookmark.IsBookmark(categoryList.get(position).getCategoryID())) {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark_selected);
            holder.bookmark.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

        } else {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
            holder.bookmark.setColorFilter(ContextCompat.getColor(context, R.color.half_black));
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Bookmark.IsBookmark(categoryList.get(position).getCategoryID())) {
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark);
                    holder.bookmark.setColorFilter(ContextCompat.getColor(context, R.color.half_black));
                    Bookmark.DeleteBookmark((long) categoryList.get(position).getCategoryID());

                } else {
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_selected);
                    holder.bookmark.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                    Bookmark.InsertBookmark(categoryList.get(position).getCategoryID(), categoryList.get(position).getName(), categoryList.get(position).getImage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void addAll(List<Category> categoryList) {
        this.categoryList = new ArrayList<>();
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        private DiagonalView imageView;
        private TfTextView textView;
        private ImageView bookmark;

        private CategoryHolder(View itemView) {
            super(itemView);
            textView = (TfTextView) itemView.findViewById(R.id.textView);
            imageView = (DiagonalView) itemView.findViewById(R.id.imageView);
            bookmark = (ImageView) itemView.findViewById(R.id.bookmark);
        }

        private void setCategory(Category category) {
            textView.setText(category.getName());
            Glide.with(context).load(category.getImage()).centerCrop().into(imageView);
        }
    }
}
