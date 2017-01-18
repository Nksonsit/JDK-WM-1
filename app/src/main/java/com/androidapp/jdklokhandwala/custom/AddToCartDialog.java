package com.androidapp.jdklokhandwala.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.UnitMeasure;
import com.androidapp.jdklokhandwala.helper.Functions;

import java.util.List;

/**
 * Created by ishan on 29-12-2016.
 */

public class AddToCartDialog extends Dialog {
    private RadioGroup radioGroup;
    private ImageView close;
    private View view;
    private TfEditText quantity;
    private Button addToCart, btnCancel;
    private String seletedType = "";
    private OnAddClick OnAddClick;

    public AddToCartDialog(Context context, String s, String selected, Double unitValue, List<UnitMeasure> unitMeasureList, final OnAddClick OnAddClick) {
        super(context);
        this.OnAddClick = OnAddClick;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_cart, null);
        setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        this.setCanceledOnTouchOutside(true);

        quantity = (TfEditText) view.findViewById(R.id.quantity);
        addToCart = (Button) view.findViewById(R.id.addToCart);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        if (unitValue > 0) {
            quantity.setText(Functions.getFormatedInt(unitValue) + "");
        }

        for (int i = 0; i < unitMeasureList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(unitMeasureList.get(i).getUnitOfMeasure());
            radioGroup.addView(radioButton);
        }

        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

        if (selected != null && selected.toString().trim().length() > 0) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (((RadioButton) radioGroup.getChildAt(i)).getText().equals(selected)) {
                    ((RadioButton) radioGroup.getChildAt(i)).setChecked(true);
                }
            }
        }

        close = (ImageView) view.findViewById(R.id.close);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                seletedType = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim();
                Log.e("int", i + " " + seletedType);
            }
        });

//        radioGroup.check(R.id.unitRadioBtn);
        addToCart.setText(s);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quantity.getText().toString().trim().equals("") && (Integer.valueOf(quantity.getText().toString().trim()) > 0)) {
                    seletedType = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim();
                    if (seletedType != null && !seletedType.toString().trim().equals("")) {
                        OnAddClick.onAddClick(quantity.getText().toString().trim(), seletedType);
                        dismiss();
                    } else {
                        Functions.showToast(context, "Please select unit type.");
                    }
                } else {
                    Functions.showToast(context, "Please enter quantity.");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnAddClick {
        void onAddClick(String quantity, String type);
    }
}
