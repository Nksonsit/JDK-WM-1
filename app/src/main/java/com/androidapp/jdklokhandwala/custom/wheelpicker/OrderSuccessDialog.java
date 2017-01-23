package com.androidapp.jdklokhandwala.custom.wheelpicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.BillingActivity;
import com.androidapp.jdklokhandwala.activities.DashboardActivity;
import com.androidapp.jdklokhandwala.helper.Functions;

/**
 * Created by ishan on 29-12-2016.
 */

public class OrderSuccessDialog extends Dialog {

    private final Context context;
    private View view;
    private Button btnOk;
    private String str;

    public OrderSuccessDialog(final Context context, String str) {
        super(context);
        this.context = context;
        this.str = str;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (str.equals("O")) {
            view = LayoutInflater.from(context).inflate(R.layout.order_success_dialog, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.quotation_success_dialog, null);
        }
        setContentView(view);

        this.setCancelable(true);

        init();

    }

    private void init() {
        btnOk = (Button) view.findViewById(R.id.btnOk);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        actionListener();
    }

    private void actionListener() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent i = new Intent(context, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Functions.fireIntent(context, i);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) context).finish();
            }
        });
    }
}
