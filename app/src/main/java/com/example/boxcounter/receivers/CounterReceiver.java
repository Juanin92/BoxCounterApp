package com.example.boxcounter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.boxcounter.utils.AppConstants;

public class CounterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null){
            return;
        }

        switch (action){
            case AppConstants.ACTION_INCREMENT -> handleIncrement(context);
            case AppConstants.ACTION_DECREMENT -> handleDecrement(context);
        }
    }

    private void handleIncrement(Context context){}
    private void handleDecrement(Context context){}

}
