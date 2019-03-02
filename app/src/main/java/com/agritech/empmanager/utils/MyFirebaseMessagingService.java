package com.agritech.empmanager.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.agritech.empmanager.LeaveActivity;
import com.agritech.empmanager.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            Map<String, String> map = remoteMessage.getData();

            if (map.get("type").equals("New Leave")) {

                sendNotification(map.get("uid"), map.get("title"), map.get("subject"));

                //sendNotification(map.get("doc_id"),map.get("invoice"),map.get("amount"));

            }

        }

    }

    @Override
    public void onNewToken(String token) {

        String uid = PrefUtilities.with(this).getUserId();

        PrefUtilities.with(this).saveFCMId(token);

        if (!uid.isEmpty()) {

            FirebaseFirestore.getInstance().collection("Employees").document(uid).update("fcmId", token);

        }

    }


    public void sendNotification(String uid, String title, String subject) {

        Intent intent = new Intent(this, LeaveActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("forHR", true);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));


        builder.setSmallIcon(R.drawable.ic_outline_notifications_24px);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);


        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));


        builder.setContentTitle("Leave " + title);


        builder.setContentText(subject);


        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }

    }


}
