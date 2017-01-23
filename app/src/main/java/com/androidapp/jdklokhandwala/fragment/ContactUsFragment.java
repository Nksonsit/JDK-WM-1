package com.androidapp.jdklokhandwala.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.AppApi;
import com.androidapp.jdklokhandwala.api.model.AboutUsResponse;
import com.androidapp.jdklokhandwala.api.model.ContactUsResponse;
import com.androidapp.jdklokhandwala.custom.TfTextView;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.androidapp.jdklokhandwala.helper.RetrofitErrorHelper;
import com.androidapp.jdklokhandwala.support.ImageLoader;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 30-12-2016.
 */

public class ContactUsFragment extends Fragment {
    private View parentView;
    private TfTextView contactUsTV;
    private ImageLoader imageLoader;
    private SpotsDialog dialog;

    public static ContactUsFragment newInstance() {
        ContactUsFragment contactUsFragment = new ContactUsFragment();
        return contactUsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_about_us, container, false);
        init();
        return parentView;
    }

    private void init() {
        contactUsTV=(TfTextView)parentView.findViewById(R.id.contactUsTV);
        imageLoader = new ImageLoader(getActivity());
        dialog = new SpotsDialog(getActivity(), R.style.Custom);

        dialog.show();
        AppApi appApi= MyApplication.getRetrofit().create(AppApi.class);
        appApi.getContactUsApi().enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                dialog.dismiss();
                if(response.body()!=null){
                    if(response.body().getData()!=null&&response.body().getData().getDescription()!=null&&response.body().getData().getDescription().length()>0){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            contactUsTV.setText(Html.fromHtml(response.body().getData().getDescription(), 0, new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String s) {
                                    LevelListDrawable d = new LevelListDrawable();


                                    Drawable empty = getResources().getDrawable(android.R.drawable.arrow_up_float);

                                    d.addLevel(0, 0, empty);
                                    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                    new ImageGetterAsyncTask(getActivity(), s, d).execute(contactUsTV);

                                    return d;
                                }
                            }, null));
                        }else {
                            contactUsTV.setText(Html.fromHtml(response.body().getData().getDescription(),new Html.ImageGetter() {
                                @Override
                                public Drawable getDrawable(String s) {
                                    LevelListDrawable d = new LevelListDrawable();


                                    Drawable empty = getResources().getDrawable(android.R.drawable.arrow_up_float);

                                    d.addLevel(0, 0, empty);
                                    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                                    new ImageGetterAsyncTask(getActivity(), s, d).execute(contactUsTV);

                                    return d;
                                }
                            },null));
                        }
                    }
                }else {
                    contactUsTV.setText("There is no data available.");
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                dialog.dismiss();
                contactUsTV.setText("There is no data available.");
                RetrofitErrorHelper.showErrorMsg(t, getActivity());
            }
        });
    }

    class ImageGetterAsyncTask extends AsyncTask<TextView, Void, Bitmap> {


        private LevelListDrawable levelListDrawable;
        private Context context;
        private String source;
        private TextView t;

        public ImageGetterAsyncTask(Context context, String source, LevelListDrawable levelListDrawable) {
            this.context = context;
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextView... params) {
            t = params[0];
            try {
                //Log.e("Loader", "Downloading the image from: " + source);
                return imageLoader.getBitmap(source);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                Point size = new Point();
                ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, (int) (bitmap.getWidth()), (int) (bitmap.getHeight()));
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) {
            }
        }
    }

}

