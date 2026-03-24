package com.example.boxcounter.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.boxcounter.R;
import com.example.boxcounter.receivers.CounterReceiver;
import com.example.boxcounter.utils.AppConstants;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int currentCount = 0;
        if (intent != null) {
            currentCount = intent.getIntExtra("CURRENT_COUNT", 0);
        }
        createNotificationChannel();

        startForeground(AppConstants.NOTIFICATION_ID, buildNotification(currentCount));
        return START_STICKY;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                AppConstants.NOTIFICATION_CHANNEL_ID,
                "Box Counter Service",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification(int count) {
        Intent incrementIntent = new Intent(this, CounterReceiver.class);
        incrementIntent.setAction(AppConstants.ACTION_INCREMENT);
        PendingIntent incrementPending = PendingIntent.getBroadcast(this, 1,
                incrementIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent stopIntent = new Intent(this, CounterReceiver.class);
        stopIntent.setAction(AppConstants.ACTION_DECREMENT);
        PendingIntent decrementPending = PendingIntent.getBroadcast(this, 2,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Box Counter Active")
                .setContentText("Cajas Actual: " + count)
                .setSmallIcon(R.drawable.ic_box_counter)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(android.R.drawable.ic_input_add, "+", incrementPending)
                .addAction(android.R.drawable.ic_delete, "-", decrementPending)
                .build();
    }
}
