package com.androidapp.jdklokhandwala.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.activities.DashboardActivity;
import com.androidapp.jdklokhandwala.activities.NotificationActivity;
import com.androidapp.jdklokhandwala.helper.Functions;
import com.androidapp.jdklokhandwala.helper.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by ishan on 11-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("From", "" + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData());
            try {
                JSONObject json = new JSONObject(Functions.jsonString(remoteMessage.getData()));
                Log.e("j1",Functions.jsonString(json));
                Log.e("j4",json.get("message")+"");
                sendNotification(new JSONObject(json.get("message")+""));
                // handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception1: " + e.getMessage());
            }
        }

    }

    private void sendNotification(JSONObject msgObject) {
        try {

//            Log.e("j2",obj.toString());

//            JSONObject msgObject=obj.getJSONObject("message");
            Log.e("j3",Functions.jsonString(msgObject));
            String title = msgObject.getString("NotificationFor");
            String desc = msgObject.getString("Message");

            Log.e(title,desc);

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification;
            notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentText(desc)
                    .build();

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(m, notification);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error",e.toString());
        }
    }

}