package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.BookmarkModel;
import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.Bookmark;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.bumptech.glide.Glide;
import com.intrusoft.squint.DiagonalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CategoryHolder> {

    private Context context;
    private List<Bookmark> categoryList;

    public BookmarkAdapter(Context context, List<Bookmark> categoryList) {
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
        BookmarkModel bookmark = categoryList.get(position);
        holder.setCategory(bookmark, position);

        if (Bookmark.IsBookmark(bookmark.CategoryId().intValue())) {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark_selected);
        } else {
            holder.bookmark.setImageResource(R.drawable.ic_bookmark);
        }
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void addAll(List<Bookmark> categoryList) {
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

        private void setCategory(BookmarkModel bookmarkModel, int position) {
            textView.setText(bookmarkModel.Name());
            Glide.with(context).load(bookmarkModel.Image()).centerCrop().into(imageView);

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Bookmark.IsBookmark(bookmarkModel.CategoryId().intValue())) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Do you really want to delete bookmark category? ");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do your work here
                                dialog.dismiss();
                                bookmark.setImageResource(R.drawable.ic_bookmark);
                                Bookmark.DeleteBookmark(bookmarkModel.CategoryId());
                                categoryList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, categoryList.size());
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();

                    } else {
                        bookmark.setImageResource(R.drawable.ic_bookmark_selected);
                        Bookmark.InsertBookmark(bookmarkModel.CategoryId().intValue(), bookmarkModel.Name(), bookmarkModel.Image());
                    }
                }
            });
        }
    }
}
