package com.androidapp.jdklokhandwala.helper;

import android.content.Context;

import com.androidapp.jdklokhandwala.R;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by sagartahelyani on 13-09-2016.
 */
public class RetrofitErrorHelper {

    public static void showErrorMsg(Throwable throwable, Context context) {

        if (throwable instanceof TimeoutException) {
            Functions.showToast(context, context.getString(R.string.time_out));

        } else if (throwable instanceof UnknownHostException) {
            Functions.showToast(context, context.getString(R.string.no_internet_connection));

        } else {
            Functions.showToast(context, context.getString(R.string.try_again));
        }
    }
}
