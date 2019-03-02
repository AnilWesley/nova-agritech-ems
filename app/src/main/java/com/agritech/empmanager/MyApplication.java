package com.agritech.empmanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //for channel Events
        NotificationChannel mBirthdayChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
        mBirthdayChannel.enableLights(true);
        mBirthdayChannel.setLightColor(Color.RED);
        mBirthdayChannel.enableVibration(true);
        mBirthdayChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mBirthdayChannel);


    }


}
