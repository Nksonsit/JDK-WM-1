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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AddToCart;
import com.androidapp.jdklokhandwala.api.model.Bookmark;
import com.androidapp.jdklokhandwala.api.model.CityRes;
import com.androidapp.jdklokhandwala.api.model.UnitMeasure;
import com.androidapp.jdklokhandwala.api.model.UserPojo;
import com.droidbyme.toastlib.ToastEnum;
import com.droidbyme.toastlib.ToastLib;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

    //2017-01-12 17:31:03

    public static final String ServerDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String hhmmAMPM = "hh:mm a";
    public static final String ddMMMYYYY = "dd MMM, yyyy";
    //17 Jan, 2017 03:11pm
    public static final String CommonDateTimeFormat = ddMMMYYYY + "" + hhmmAMPM;

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
                .margin(128)
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

    public static void showAlertDialogWithOkCancel(Context mContext, String positiveText,String message, DialogOptionsSelectedListener dialogOptionsSelectedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(positiveText, (dialog, id) -> {
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

    public static void showAlertDialogWithYesNo(Context mContext, String message, DialogOptionsSelectedListener dialogOptionsSelectedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("YES", (dialog, id) -> {
                    if (dialogOptionsSelectedListener != null)
                        dialogOptionsSelectedListener.onSelect(true);
                    dialog.dismiss();
                })
                .setNegativeButton("NO", (dialog, id) -> {
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

    public static void setCity(int pincode, EditText edtCity, EditText edtArea) {
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
        AddToCart.DeleteAllData();
        Bookmark.DeleteAllData();
    }

    /*1 passrord
      2 new password
      3 confirm password
    */
    public static boolean checkPassrordLength(Context context, int whichPassrord, String password) {
        switch (whichPassrord) {
            case 1:
                if (password.trim().length() < 6 || password.trim().length() > 20) {
                    Functions.showToast(context, "Password should be minimum 6 and maximum 29 character long.");
                    return false;
                }
                break;
            case 2:
                if (password.trim().length() < 6 || password.trim().length() > 20) {
                    Functions.showToast(context, "New Password should be minimum 6 and maximum 20 character long.");
                    return false;
                }
                break;
            case 3:
                if (password.trim().length() < 6 || password.trim().length() > 20) {
                    Functions.showToast(context, "Confirm Password should be minimum 6 and maximum 20 character long.");
                    return false;
                }
                break;
        }

        return true;
    }

    public static String getFormatedInt(Double input) {
        String temp = String.valueOf(input);
        Log.e("temp", temp);
        temp = temp.replace(".", "-");
        if (temp.toString().contains("-")) {
            String[] split = temp.split("-");
            if (split.length == 2) {
                Log.e("split", split.length + "");
                if (Long.valueOf(split[1]) > 0) {
                    return String.valueOf(String.format("%.2f",input));
                } else {
                    return String.valueOf(split[0]);
                }
            } else {
                return String.valueOf(input);
            }
        }
        return String.valueOf(input);
    }
    public static String getFormatedInt(float input) {
        String temp = String.valueOf(input);
        Log.e("temp", temp);
        temp = temp.replace(".", "-");
        if (temp.toString().contains("-")) {
            String[] split = temp.split("-");
            if (split.length == 2) {
                Log.e("split", split.length + "");
                if (Long.valueOf(split[1]) > 0) {
                    return String.valueOf(String.format("%.2f",input));
                } else {
                    return String.valueOf(split[0]);
                }
            } else {
                return String.valueOf(input);
            }
        }
        return String.valueOf(input);
    }

    public static String formatDate(String inputDate, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

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

    public static String getStatus(int number) {
        String status = "Order Dispatched";
        switch (number) {
            case 8:
                status = "Quotation Request";
                break;
            case 9:
                status = "Quatation Cancelled By Admin";
                break;
            case 10:
                status = "Quotation Generated";
                break;
            case 11:
                status = "Cancelled By User";
                break;
            case 12:
                status = "Order Placed";
                break;
            case 13:
                status = "Order Cancelled By Admin";
                break;
            case 14:
                status = "Order Dispatched";
                break;
        }
        return status;
    }

    public static Double getTotalWeight() {
        List<AddToCart> list = AddToCart.getCartList();
        Log.e("size", list.size() + "");
        Double totalWeight = Double.valueOf(0);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                totalWeight = totalWeight + list.get(i).KgWeight();
            }
            return totalWeight;
        } else {
            return Double.valueOf(0);
        }
    }

    public static void setPermission(final Context context, @NonNull String[] permissions, PermissionListener permissionListene) {

        if (permissions != null && permissions.length == 0 && permissionListene != null) {
            return;
        }
        new TedPermission(context)
                .setPermissionListener(permissionListene)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(permissions)
                .check();
    }
}
