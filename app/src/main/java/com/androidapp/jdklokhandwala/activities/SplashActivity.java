package com.androidapp.jdklokhandwala.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.helper.Functions;

public class SplashActivity extends AppCompatActivity {

    private TextView appTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appTextView = (TextView) findViewById(R.id.appTextView);
        appTextView.setTypeface(Functions.getBoldFont(this));

        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                Functions.fireIntent(SplashActivity.this, DashboardActivity.class);
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }.start();
    }
}
