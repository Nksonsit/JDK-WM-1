package com.androidapp.jdklokhandwala.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.custom.TfTextView;


/**
 * Created by sagartahelyani on 20-06-2016.
 */
public class EmptyLayout extends LinearLayout {

    private Context context;
    private View parentView;
    private TfTextView emptyTextView;
    private ImageView emptyImageView;

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
    }

    public void setContent(String text) {
        emptyTextView.setText(text);
    }

    public void setContent(String text, int icon) {
        emptyTextView.setText(text);
        emptyImageView.setImageResource(icon);
    }
}
