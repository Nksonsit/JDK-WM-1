package com.androidapp.jdklokhandwala.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.DashboardActivity;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;


/**
 * Created by sagartahelyani on 20-06-2016.
 */
public class EmptyLayout extends LinearLayout {

    private Context context;
    private View parentView;
    private TfTextView emptyTextView;
    private ImageView emptyImageView;
    private TfTextView addProductLink;

    public EmptyLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            this.context = context;
            init();
        }
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this.context = context;
            init();
        }
    }

    private void init() {
        parentView = inflate(context, R.layout.layout_empty_view, this);
        emptyTextView = (TfTextView) parentView.findViewById(R.id.emptyTextView);
        emptyImageView = (ImageView) parentView.findViewById(R.id.emptyImageView);

        addProductLink=(TfTextView)parentView.findViewById(R.id.add_product_link);
        addProductLink.setVisibility(GONE);
    }

    public void setAddProductLink(Context context){
        addProductLink.setVisibility(VISIBLE);
        addProductLink.setText(Html.fromHtml("<u>Add Some Products from here.</u>"));
        addProductLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Functions.fireIntent(context, i);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) context).finish();
            }
        });
    }

    public void setContent(String text) {
        emptyTextView.setText(text);
    }

    public void setContent(String text, int icon) {
        emptyTextView.setText(text);
        emptyImageView.setImageResource(icon);
    }
}
