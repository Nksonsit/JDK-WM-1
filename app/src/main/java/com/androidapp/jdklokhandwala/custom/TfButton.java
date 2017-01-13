package com.androidapp.jdklokhandwala.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.helper.Functions;

/**
 * Created by dhruvil on 27-07-2016.
 */

public class TfButton extends Button {

    private Context _ctx;

    public TfButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    public TfButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    private void init() {
        try {
            setTypeface(Functions.getBoldFont(_ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
