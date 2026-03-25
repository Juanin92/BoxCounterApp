package com.example.boxcounter.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.boxcounter.services.NotificationService;

public class NotificationHelper {

    public static void startService(Context context) {
        Intent intent = new Intent(context, NotificationService.class);

        context.startForegroundService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        context.stopService(intent);
    }
}
