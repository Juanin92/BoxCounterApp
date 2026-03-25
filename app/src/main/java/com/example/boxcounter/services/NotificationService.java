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
import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.receivers.CounterReceiver;
import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.ui.activities.MainActivity;
import com.example.boxcounter.utils.AppConstants;

public class NotificationService extends Service {

    private ShiftRepo repo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        repo = new ShiftRepo(getApplicationContext());
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Shift activeShift = repo.getActiveShiftSync();
        int currentCount = 0;
        if (activeShift != null) {
            currentCount = activeShift.getQuantity();
        }
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

        Intent decrementIntent = new Intent(this, CounterReceiver.class);
        decrementIntent.setAction(AppConstants.ACTION_DECREMENT);
        PendingIntent decrementPending = PendingIntent.getBroadcast(this, 2,
                decrementIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Turno Activo")
                .setContentText("Cajas registradas: " + count)
                .setSmallIcon(R.drawable.ic_box_counter)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(android.R.drawable.ic_input_add, "+", incrementPending)
                .addAction(R.drawable.ic_minus, "-", decrementPending)
                .build();
    }
}
