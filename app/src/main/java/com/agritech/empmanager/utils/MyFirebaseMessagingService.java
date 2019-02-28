package com.agritech.empmanager.utils;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.Nullable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    }

    @Override
    public void onNewToken(String token) {

        String uid = PrefUtilities.with(this).getUserId();

        PrefUtilities.with(this).saveFCMId(token);

        if (!uid.isEmpty()){

            FirebaseFirestore.getInstance().collection("Employees").document(uid).update("fcmId",token);

        }

        //Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

}
