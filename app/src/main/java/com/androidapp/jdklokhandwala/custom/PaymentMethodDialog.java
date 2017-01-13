package com.androidapp.jdklokhandwala.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.androidapp.jdklokhandwala.R;

/**
 * Created by ishan on 12-01-2017.
 */

public class PaymentMethodDialog extends Dialog {
    private RadioGroup radioGroup;
    private ImageView close;
    private View view;
    private TfTextView titleTV;
    private Button selectbtn;
    private int seletedTypeID = 20;
    private OnSelectClick OnSelectClick;

    public PaymentMethodDialog(Context context, String s, final OnSelectClick OnSelectClick) {
        super(context);
        this.OnSelectClick = OnSelectClick;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_method, null);
        setContentView(view);

        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);

        titleTV = (TfTextView) view.findViewById(R.id.title);
        selectbtn = (Button) view.findViewById(R.id.selectBtn);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        close = (ImageView) view.findViewById(R.id.close);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioBtn1:
                        seletedTypeID = 20;
                        break;
                    case R.id.radioBtn2:
                        seletedTypeID = 19;
                        break;
                    case R.id.radioBtn3:
                        seletedTypeID = 21;
                        break;
                }
            }
        });

//        radioGroup.check(R.id.unitRadioBtn);
        titleTV.setText(s);
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    OnSelectClick.onSelectClick(seletedTypeID);
                    dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnSelectClick {
        void onSelectClick(int id);
    }
}
