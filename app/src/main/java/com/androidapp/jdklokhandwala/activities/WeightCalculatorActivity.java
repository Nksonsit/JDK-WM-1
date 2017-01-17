package com.androidapp.jdklokhandwala.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.Calculator;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;

import java.util.ArrayList;

public class WeightCalculatorActivity extends AppCompatActivity {

    private Spinner calculateType;
    private ArrayList<Calculator> typeList;
    private LinearLayout ll;
    private ImageView calculate;
    private TfTextView ans;
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private View calRowitem;

    private void doFinish() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calculator);
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txtCustomTitle = (TfTextView) toolbar.findViewById(R.id.txtCustomTitle);

        txtCustomTitle.setText(getString(R.string.menu_calc));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        ans = (TfTextView) findViewById(R.id.answer);

        calculateType = (Spinner) findViewById(R.id.calculateType);
        typeList = new ArrayList<>();
        typeList.add(new Calculator(0, "Ms Flat Bar"));
        typeList.add(new Calculator(0, "Ms plates/sheets"));
        typeList.add(new Calculator(0, "Ms square bar"));
        typeList.add(new Calculator(0, "Ms Round Bar"));
        typeList.add(new Calculator(0, "Ms Round Pipe"));
        typeList.add(new Calculator(1, "M.s. channels"));
        typeList.add(new Calculator(1, "M.s. Beams"));
        typeList.add(new Calculator(1, "M.s. Rails"));
        typeList.add(new Calculator(1, "M.s. Tees"));
        typeList.add(new Calculator(1, "Checquered plates"));
        typeList.add(new Calculator(1, "M.s. TMT Bars"));
        typeList.add(new Calculator(1, "Torsteel or Twisted bar"));
        typeList.add(new Calculator(1, "Hexagon bar"));


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnerItem, typeList);
//        calculateType.setAdapter(adapter);

/*
        ll = (LinearLayout) findViewById(R.id.container);

        calculateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                ll.removeAllViews();
                for (int i = 0; i < getNumberOfET(typeList.get(pos)); i++) {
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    calRowitem = LayoutInflater.from(WeightCalculatorActivity.this).inflate(R.layout.calculator_input, null);
                    calRowitem.setLayoutParams(param);
                    ((TfTextView) calRowitem.findViewById(R.id.txtHint)).setText(getHint(pos, i) + " : ");
                    ll.addView(calRowitem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calculate = (ImageView) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> list = new ArrayList<>();
                for (int i = 0; i < ll.getChildCount(); i++) {
                    LinearLayout ll2 = ((LinearLayout) ll.getChildAt(i));
                    String value = ((TfEditText) ll2.getChildAt(1)).getText().toString().trim();
                    if (value != null && value.length() > 0) {
                        list.add(Double.valueOf(value));
                    } else {
                        return;
                    }
                }
                ans.setText(getCalculation(list, calculateType.getSelectedItemPosition()) + "");
            }
        });*/
    }

    public int getNumberOfET(String type) {
        Log.e("type", type);
        if (type.equals("Ms Flat Bar")) {
            return 3;
        }
        if (type.equals("Ms plates/sheets")) {
            return 3;
        }
        if (type.equals("Ms square bar")) {
            return 2;
        }
        if (type.equals("Ms Round Bar")) {
            return 2;
        }
        if (type.equals("Ms Round Pipe")) {
            return 3;
        }
        return 0;
    }

    public String getHint(int type, int pos) {
        String hint = "";
        if (type == 0 || type == 1) {
            switch (pos) {
                case 0:
                    hint = "Length";
                    break;
                case 1:
                    hint = "Width";
                    break;
                case 2:
                    hint = "Thickness";
                    break;

            }
        } else if (type == 2 || type == 3) {
            hint = "OD";
        } else if (type == 4) {

            switch (pos) {
                case 0:
                    hint = "OD";
                    break;
                case 1:
                    hint = "Wall Thickness";
                    break;
                case 2:
                    hint = "Wall Thickness";
                    break;

            }
        }

        return hint;
    }

    public double getCalculation(ArrayList<Double> inputList, int type) {
        Log.e(inputList.toString(), type + "");
        double ans = 0;
        if (type == 0 || type == 1) {
            ans = (Double.valueOf(inputList.get(0)) * Double.valueOf(inputList.get(1)) * Double.valueOf(inputList.get(2))) / 127415;
        } else if (type == 2) {
            ans = ((((Double.valueOf(inputList.get(0)) * Double.valueOf(inputList.get(1))) / 127)) / 3.2808);
        } else if (type == 3) {
            ans = ((((Double.valueOf(inputList.get(0)) * Double.valueOf(inputList.get(1))) / 160)) / 3.2808);
        } else if (type == 4) {
            ans = ((Double.valueOf(inputList.get(0)) - Double.valueOf(inputList.get(1)) * Double.valueOf(inputList.get(2)) * 0.024) / 3.2808);
        }
        return ans;
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }
}
