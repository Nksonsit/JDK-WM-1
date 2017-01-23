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
import com.androidapp.jdklokhandwala.adapter.CalculatorAdapter;
import com.androidapp.jdklokhandwala.adapter.WeightAdapter;
import com.androidapp.jdklokhandwala.api.model.Calculator;
import com.androidapp.jdklokhandwala.api.model.WeightObj;
import com.androidapp.jdklokhandwala.custom.TfButton;
import com.androidapp.jdklokhandwala.custom.TfEditText;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.Functions;

import java.util.ArrayList;
import java.util.List;

public class WeightCalculatorActivity extends AppCompatActivity {

    private Spinner calculateType;
    private ArrayList<Calculator> typeList;
    private LinearLayout ll;
    private ImageView calculate;
    private TfTextView ans;
    private Toolbar toolbar;
    private TfTextView txtCustomTitle;
    private View calRowitem;
    private View spinnerRow;
    private WeightObj weightObj = null;

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
        typeList.add(new Calculator(1, "Chequered plates"));
        typeList.add(new Calculator(1, "M.s. TMT Bars"));
        typeList.add(new Calculator(1, "Torsteel or Twisted bar"));
        typeList.add(new Calculator(1, "Hexagon bar"));

        CalculatorAdapter adapter = new CalculatorAdapter(this, R.layout.spinner_item, typeList);
        calculateType.setAdapter(adapter);

        ll = (LinearLayout) findViewById(R.id.container);

        calculateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                ans.setText("0.00 Kg/ft");
                ll.removeAllViews();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                Log.e("type", typeList.get(pos).getType() + "");
                if (typeList.get(pos).getType() == 0) {

                    ll.removeAllViews();
                    for (int i = 0; i < getNumberOfET(typeList.get(pos).getName()); i++) {
                        calRowitem = LayoutInflater.from(WeightCalculatorActivity.this).inflate(R.layout.calculator_input, null);
                        calRowitem.setLayoutParams(param);
                        ((TfTextView) calRowitem.findViewById(R.id.txtHint)).setText(getHint(pos, i) + " : ");
                        ll.addView(calRowitem);
                    }
                    calculate.setVisibility(View.VISIBLE);

                } else if (typeList.get(pos).getType() == 1) {
                    ll.removeAllViews();
                    spinnerRow = LayoutInflater.from(WeightCalculatorActivity.this).inflate(R.layout.spinner_row, null);
                    spinnerRow.setLayoutParams(param);
                    List<WeightObj> weightList = getSpinnerList(pos);
                    WeightAdapter weightAdapter = new WeightAdapter(WeightCalculatorActivity.this, R.layout.spinner_item, weightList);
                    ((Spinner) spinnerRow.findViewById(R.id.spinnerWeight)).setAdapter(weightAdapter);

                    ll.addView(spinnerRow);

                    ((Spinner) spinnerRow.findViewById(R.id.spinnerWeight)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (weightList.size() > 0) {
                                weightObj = weightList.get(i);
                                Log.e("i 1",i+"");
                                int pos=calculateType.getSelectedItemPosition();
                                if (pos == 6 || pos == 7 || pos == 11 || pos == 12) {
                                    ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()) / 3.2808)) + " Kg/ft");
                                }else {
                                    ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()))) + " Kg/ft");
                                }
                            }
                    }

                    @Override
                    public void onNothingSelected (AdapterView < ? > adapterView){

                    }
                });
                weightObj = (WeightObj) ((Spinner) spinnerRow.findViewById(R.id.spinnerWeight)).getSelectedItem();
                    int i=calculateType.getSelectedItemPosition();
                    Log.e("i 2",i+"");
                    if (i == 6 || i == 7 || i == 11 || i == 12) {
                        ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()) / 3.2808)) + " Kg/ft");
                    }else {
                        ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()))) + " Kg/ft");
                    }
                calculate.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){

        }
    }

    );

    calculate=(ImageView)

    findViewById(R.id.calculate);

    calculate.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){

        Calculator obj = (Calculator) calculateType.getSelectedItem();
        Log.e("obj", Functions.jsonString(obj));
        if (obj.getType() == 0) {
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
            ans.setText(String.format("%.2f", getCalculation(list, calculateType.getSelectedItemPosition())) + " Kg/ft");
        } else if (obj.getType() == 1) {
            if (weightObj != null) {
                int i=calculateType.getSelectedItemPosition();
                Log.e("i 4",i+"");
                if (i == 6 || i == 7 || i == 11 || i == 12) {
                    ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()) / 3.2808)) + " Kg/ft");
                }else {
                    ans.setText(String.format("%.2f", (Double.valueOf(weightObj.getWeight()))) + " Kg/ft");
                }
            } else {
                Functions.showToast(WeightCalculatorActivity.this, "Please select item.");
            }
        }
    }
    }

    );
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
            return 1;
        }
        if (type.equals("Ms Round Bar")) {
            return 1;
        }
        if (type.equals("Ms Round Pipe")) {
            return 2;
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
            ans = ((((Double.valueOf(inputList.get(0)) * Double.valueOf(inputList.get(0))) / 127)) / 3.2808);
        } else if (type == 3) {
            ans = ((((Double.valueOf(inputList.get(0)) * Double.valueOf(inputList.get(0))) / 160)) / 3.2808);
        } else if (type == 4) {
            ans = ((Double.valueOf(inputList.get(0)) - Double.valueOf(inputList.get(1)) * Double.valueOf(inputList.get(1)) * 0.024) / 3.2808);
        }
        return ans;
    }

    @Override
    public void onBackPressed() {
        doFinish();
    }

    public List<WeightObj> getSpinnerList(int pos) {

        List<WeightObj> list = new ArrayList<>();
        switch (pos) {
            case 5:
                list.add(new WeightObj("75 x 40 mm", "2.172"));
                list.add(new WeightObj("100 x 50 mm", "2.925"));
                list.add(new WeightObj("125 x 65 mm", "3.992"));
                list.add(new WeightObj("150 x 75 mm", "5.120"));
                list.add(new WeightObj("175 x 75 mm", "5.973"));
                list.add(new WeightObj("200 x 75 mm", "6.796"));
                list.add(new WeightObj("250 x 80 mm", "9.326"));
                list.add(new WeightObj("300 x 90 mm", "11.063"));
                list.add(new WeightObj("400 x 100 mm", "15.270"));
                break;

            case 6:
                list.add(new WeightObj("ISLAB 100 x 50 mm", "8.00"));
                list.add(new WeightObj("ISMB 116 x 100 mm", "23.00"));
                list.add(new WeightObj("ISMB 125 x 75 mm", "13.20"));
                list.add(new WeightObj("ISMB 150 x 80 mm", "15.00"));
                list.add(new WeightObj("ISMB 175 x 85 mm", "19.50"));
                list.add(new WeightObj("ISMB 200 x 100 mm", "25.40"));
                list.add(new WeightObj("ISMB 225 x 100 mm", "31.20"));
                list.add(new WeightObj("ISMB 250 x 125 mm", "37.30"));
                list.add(new WeightObj("ISLB 300 x 150 mm", "37.70"));
                list.add(new WeightObj("ISMB 300 x 140 mm", "44.20"));
                list.add(new WeightObj("ISLB 350 x 165 mm", "49.50"));
                list.add(new WeightObj("ISMB 350 x 140 mm", "52.40"));
                list.add(new WeightObj("ISMB 400 x 140 mm", "61.60"));
                list.add(new WeightObj("ISLB 400 x 165 mm", "56.90"));
                list.add(new WeightObj("ISMB 450 x 150 mm", "72.40"));
                list.add(new WeightObj("ISMB 500 x 180 mm", "86.92"));
                list.add(new WeightObj("ISMB 600 x 210 mm", "122.60"));
                break;

            case 7:
                list.add(new WeightObj("BS 30 lb./Yard", "14.88"));
                list.add(new WeightObj("BS 90 lb./Yard", "44.61"));
                list.add(new WeightObj("BS 105 lb./Yard", "52.08"));
                list.add(new WeightObj("CR 80", "63.52"));
                list.add(new WeightObj("CR 100", "88.73"));
                break;

            case 8:
                list.add(new WeightObj("20 x 20 x 3 mm", "0.274"));
                list.add(new WeightObj("30 x 30 x 3 mm", "0.426"));
                list.add(new WeightObj("40 x 40 x 6 mm", "1.0266"));
                list.add(new WeightObj("50 x 50 x 6 mm", "1.344"));
                list.add(new WeightObj("60 x 60 x 6 mm", "1.646"));
                list.add(new WeightObj("75 x 75 x 10 mm", "3.337"));
                list.add(new WeightObj("100 x 100 x 10 mm", "4.572"));
                list.add(new WeightObj("150 x 150 x 150 mm", "6.95"));
                break;

            case 9:
                list.add(new WeightObj("5 mm", " 3.969"));
                list.add(new WeightObj("6 mm ", "5.216"));
                list.add(new WeightObj("8 mm ", "6.123"));
                list.add(new WeightObj("10 mm", " 7.371"));
                list.add(new WeightObj("12 mm ", "9.639"));
                break;

            case 10:
                list.add(new WeightObj("6(R) mm", "0.067"));
                list.add(new WeightObj("8 mm", "0.120"));
                list.add(new WeightObj("10 mm", "0.188"));
                list.add(new WeightObj("12 mm", "0.270"));
                list.add(new WeightObj("16 mm", "0.480"));
                list.add(new WeightObj("20 mm", "0.751"));
                list.add(new WeightObj("16 mm", "0.480"));
                list.add(new WeightObj("16 mm", "0.480"));
                list.add(new WeightObj("20 mm", "0.751"));
                list.add(new WeightObj("25 mm", "1.174"));
                list.add(new WeightObj("32 mm", "1.925"));
                break;

            case 11:
                list.add(new WeightObj("6 mm", "0.222"));
                list.add(new WeightObj("8 mm", "0.395"));
                list.add(new WeightObj("10 mm", "0.617"));
                list.add(new WeightObj("12 mm", "0.888"));
                list.add(new WeightObj("16 mm", "1.578"));
                list.add(new WeightObj("20 mm", "2.466"));
                list.add(new WeightObj("22 mm", "2.980"));
                list.add(new WeightObj("25 mm", "3.854"));
                list.add(new WeightObj("28 mm", "4.830"));
                list.add(new WeightObj("32 mm", "6.313"));
                list.add(new WeightObj("36 mm", "7.990"));
                list.add(new WeightObj("40 mm", "9.864"));
                list.add(new WeightObj("50 mm", "15.410"));
                break;

            case 12:
//                list.add(new WeightObj("0.193\"", "0.0531"));
                list.add(new WeightObj("5 mm", "0.0519"));
                list.add(new WeightObj("6 mm", "0.072"));
//                list.add(new WeightObj("0.248\"", "0.083"));
//                list.add(new WeightObj("0.250\"", "0.086"));
                list.add(new WeightObj("7 mm", "0.102"));
//                list.add(new WeightObj("0.312\"", "0.131"));
                list.add(new WeightObj("8 mm", "0.133"));
//                list.add(new WeightObj("0.324\"", "0.141"));
//                list.add(new WeightObj("0.375\"", "0.195"));
                list.add(new WeightObj("10 mm", "0.207"));
//                list.add(new WeightObj("0.413\"", "0.220"));
                list.add(new WeightObj("11 mm", "0.250"));
//                list.add(new WeightObj("0.437\"", "0.258"));
//                list.add(new WeightObj("0.445\"", "0.265"));
                list.add(new WeightObj("12 mm", "0.295"));
//                list.add(new WeightObj("0.500\"", "0.338"));
                list.add(new WeightObj("13 mm", "0.350"));
//                list.add(new WeightObj("0.525\"", "0.368"));
                list.add(new WeightObj("14 mm", "0.402"));
//                list.add(new WeightObj("0.562\"", "0.423"));
                list.add(new WeightObj("15 mm", "0.465"));
//                list.add(new WeightObj("0.600\"", "0.475"));
//                list.add(new WeightObj("0.625\"", "0.522"));
                list.add(new WeightObj("17 mm", "0.596"));
//                list.add(new WeightObj("0.687\"", "0.632"));
//                list.add(new WeightObj("0.710\"", "0.671"));
                list.add(new WeightObj("19 mm", "0.746"));
//                list.add(new WeightObj("0.750\"", "0.762"));
                list.add(new WeightObj("20 mm", "0.828"));
//                list.add(new WeightObj("0.812\"", "0.882"));
//                list.add(new WeightObj("0.820\"", "0.885"));
                list.add(new WeightObj("22 mm", "0.998"));
//                list.add(new WeightObj("0.875\"", "1.020"));
//                list.add(new WeightObj("0.920\"", "1.124"));
//                list.add(new WeightObj("0.937\"", "1.170"));
                list.add(new WeightObj("24 mm", "1.20"));
//                list.add(new WeightObj("1.00\"", "1.336"));
//                list.add(new WeightObj("1.01\"", "1.342"));
//                list.add(new WeightObj("1.062\"", "1.517"));
                list.add(new WeightObj("27 mm", "1.518"));
//                list.add(new WeightObj("1.100\"", "1.592"));
//                list.add(new WeightObj("1.125\"", "1.690"));
                break;
        }
        return list;
    }


    /*
    MS CHANNELS
    ---SIZE---
    ArrayList<WeightObj> list=new ArrayList<>();
    ---Weight---
    MS BEAMS
    ---SIZE---
    ---WEIGHT---
    MS RAILS
    ---SIZE---
    ---WEIGHT---
 MS TEES
    ---SIZE---
                list.add(new WeightObj("20 x 20 x 3 mm","0.90"));
                list.add(new WeightObj("25 x 25 x 3 mm","1.15"));
                list.add(new WeightObj("30 x 30 x 3 mm","1.40"));
                list.add(new WeightObj("40 x 40 x 6 mm","3.50"));
                list.add(new WeightObj("50 x 50 x 6 mm","4.50"));
                list.add(new WeightObj("60 x 60 x 6 mm","5.40"));
                list.add(new WeightObj("75 x 75 x 10 mm","10.95"));
                list.add(new WeightObj("100 x 100 x 10 mm","14.90"));
                list.add(new WeightObj("150 x 150 x 10 mm","22.70"));
    ---WEIGHT---
    Chequered Plates
    ---THICKNESS--
    --WEIGHT--
 MS Tee
    ---SIZE--
    ---WEIGHT---
    MS TMT BARS
    ---SIZE---
    ---WEIGHT---
Torsteel or Twisted Bar ISS 1786
    ---THICKNESS--
    ---WEIGHT---
 Hexagon Bar Weights
    ---SIZE---
    ---WEIGHT---


















































    */
}
