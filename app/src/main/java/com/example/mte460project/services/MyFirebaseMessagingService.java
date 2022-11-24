package com.example.mte460project.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mte460project.MainActivity;
import com.example.mte460project.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";


    private SharedPreferences sharedPref;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (true) {
            //wakeUpScreen();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
//                    .setSmallIcon(com.google.firebase.messaging.directboot.R.drawable.common_google_signin_btn_icon_dark_focused)
//                    .setContentTitle("NOTIFICATION")
//                    .setContentText(remoteMessage.getData().toString())
//                    .setPriority(NotificationCompat.PRIORITY_HIGH);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(12, builder.build());

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());

            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isInteractive(); // check if screen is on
            if (!isScreenOn) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
                wl.acquire(3000); //set your time in milliseconds
            }
        }


        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            wakeUpScreen();
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
//                    .setSmallIcon(com.google.firebase.messaging.directboot.R.drawable.common_google_signin_btn_icon_dark_focused)
//                    .setContentTitle("NOTIFICATION")
//                    .setContentText(remoteMessage.getNotification().getBody())
//                    .setPriority(NotificationCompat.PRIORITY_HIGH);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(12, builder.build());
//
//        }

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

//    @Override
//    public void onNewToken(@NonNull String token) {
//        Log.d(TAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        String companyId = sharedPref.getString("companyId", "");
//        companyId = "-NFpRzghQ2GAVj8IleOF";
//        FirebaseDatabase.getInstance().getReference().child("companies").child(companyId).child("tokens").child(token).setValue(true);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = this.getSharedPreferences("com.example.mte460project", Context.MODE_PRIVATE);
    }
}
