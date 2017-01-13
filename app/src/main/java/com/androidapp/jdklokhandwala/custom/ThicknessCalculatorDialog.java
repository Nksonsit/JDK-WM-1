package com.androidapp.jdklokhandwala.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.helper.Functions;

/**
 * Created by ishan on 29-12-2016.
 */

public class ThicknessCalculatorDialog extends Dialog {
    private View view;
    private ImageView close;
    private TfEditText width;
    private TfEditText height;
    private Button calculate;
    private TfTextView answer;

    public ThicknessCalculatorDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view= LayoutInflater.from(context).inflate(R.layout.calculator_popup,null);
        setContentView(view);
        width = (TfEditText)view.findViewById(R.id.width);
        height = (TfEditText) view.findViewById(R.id.height);
        calculate = (Button) view.findViewById(R.id.calculate);
        answer = (TfTextView) view.findViewById(R.id.answer);
        close = (ImageView) view.findViewById(R.id.close);

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(context,v);
                Log.e(width.getText().toString().trim(),height.getText().toString().trim());
                if (!width.getText().toString().trim().equals("") && !height.getText().toString().trim().equals("")) {
                    answer.setText((Integer.valueOf(width.getText().toString()) + Integer.valueOf(height.getText().toString()))+"");
                }
            }
        });
    }

}
