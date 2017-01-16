package com.androidapp.jdklokhandwala.helper;

/**
 * @author jatin
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.CityRes;
import com.androidapp.jdklokhandwala.api.model.UnitMeasure;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.droidbyme.toastlib.ToastEnum;
import com.droidbyme.toastlib.ToastLib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Functions {

    public static final String ServerDateTimeFormat = "yyyy/MM/dd HH:mm:ss";

    public static final String ServerDateFormat = "yyyy-MM-dd";

    public static DialogOptionsSelectedListener dialogOptionsSelectedListener = null;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void fireIntent(Context context, Class cls) {
        Intent i = new Intent(context, cls);
        context.startActivity(i);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void fireIntent(Context context, Intent intent) {
        context.startActivity(intent);

    }

    public static void hideKeyPad(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Typeface getRegularFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/regular.ttf");
        return tf;
    }

    public static Typeface getBoldFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/bold.ttf");
        return tf;
    }

    public static boolean emailValidation(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String toStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static int toLength(EditText editText) {
        return editText.getText().toString().trim().length();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void openInMap(Context context, double latitude, double longitude, String labelName) {
        String newUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + labelName + ")";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newUri));
        context.startActivity(intent);
    }

    public static void makePhoneCall(Context context, String callNo) {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + callNo));
        context.startActivity(dialIntent);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static String parseDate2(String inputDate, SimpleDateFormat outputFormat, SimpleDateFormat inputFormat) {

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String jsonString(Object obj) {
        return "" + MyApplication.getGson().toJson(obj);
    }

    public static void showToast(Context context, String message) {
        new ToastLib.Builder(context, message)
                .duration(ToastEnum.SHORT)
                .backgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .textColor(ContextCompat.getColor(context, R.color.white))
                .textSize(18)
                .typeface(getRegularFont(context))
                .corner(12)
                .margin(56)
                .padding(22)
                .spacing(1)
                .gravity(Gravity.BOTTOM)
                .show();
    }

    public static String checkString(String input) {
        if (input != null && input.trim().toString().length() > 0 && !input.toString().toLowerCase().equals("null")) {
            return input;
        } else {
            return "";
        }
    }

    public static void showAlertDialogWithOkCancel(Context mContext, String message, DialogOptionsSelectedListener dialogOptionsSelectedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (dialogOptionsSelectedListener != null)
                        dialogOptionsSelectedListener.onSelect(true);
                    dialog.dismiss();
                })
                .setNegativeButton("CANCEL", (dialog, id) -> {
                    if (dialogOptionsSelectedListener != null)
                        dialogOptionsSelectedListener.onSelect(false);
                    dialog.dismiss();
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }

    public interface DialogOptionsSelectedListener {
        void onSelect(boolean isYes);
    }

    public static void setDialogOptionsSelectedListener(DialogOptionsSelectedListener _dialogOptionsSelectedListener) {
        dialogOptionsSelectedListener = _dialogOptionsSelectedListener;
    }


    public static List<UnitMeasure> getListFromString(String input) {
        List<UnitMeasure> output = new ArrayList<>();
        String[] list = input.split(",");
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].length() > 0) {
                UnitMeasure unitMeasure = new UnitMeasure();
                unitMeasure.setUnitOfMeasure(list[i]);
                output.add(unitMeasure);
            }
        }
        return output;
    }

    public static void setCity(int pincode, EditText edtCity,EditText edtArea) {
        AppApi appApi = MyApplication.getRetrofit().create(AppApi.class);
        appApi.getCity(pincode).enqueue(new Callback<CityRes>() {
            @Override
            public void onResponse(Call<CityRes> call, Response<CityRes> response) {
                if (response.body() != null && response.body().getData() != null) {
                    edtCity.setText(response.body().getData().getCity());
                    edtArea.setText(response.body().getData().getArea());
                }
            }

            @Override
            public void onFailure(Call<CityRes> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public static void clearSession(Context context) {
        UserPojo userPojo = new UserPojo();
        PrefUtils.setLoggedIn(context, false);
        PrefUtils.setUserFullProfileDetails(context, userPojo);
    }
}
