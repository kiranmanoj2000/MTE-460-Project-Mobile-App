package com.example.mte460project.services;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            wakeUpScreen();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
                    .setSmallIcon(com.google.firebase.messaging.directboot.R.drawable.common_google_signin_btn_icon_dark_focused)
                    .setContentTitle("NOTIFICATION")
                    .setContentText(remoteMessage.getData().toString())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(12, builder.build());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            wakeUpScreen();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
                    .setSmallIcon(com.google.firebase.messaging.directboot.R.drawable.common_google_signin_btn_icon_dark_focused)
                    .setContentTitle("NOTIFICATION")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(12, builder.build());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void wakeUpScreen() {
        // Wake up screen
        PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT >= 20) {
            isScreenOn = powerManager.isInteractive();
        } else {
            isScreenOn = powerManager.isScreenOn();
        }
        if (!isScreenOn){
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP,"MH24:SCREENLOCK");
            wl.acquire(2000);
            PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24:SCREENLOCK");
            wl_cpu.acquire(2000);
        }
    }
}
