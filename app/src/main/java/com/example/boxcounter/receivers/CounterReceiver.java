package com.example.boxcounter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.utils.AppConstants;
import com.example.boxcounter.utils.NotificationHelper;
import com.example.boxcounter.utils.ShiftLogic;

public class CounterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null){
            return;
        }

        ShiftRepo repo = new ShiftRepo(context);
        ShiftLogic logic = new ShiftLogic(repo);
        switch (action){
            case AppConstants.ACTION_INCREMENT -> handleIncrement(logic, context);
            case AppConstants.ACTION_DECREMENT -> handleDecrement(logic, context);
        }
    }

    private void handleIncrement(ShiftLogic logic, Context context){
        logic.increment();
        NotificationHelper.startService(context);
    }
    private void handleDecrement(ShiftLogic logic, Context context){
        logic.decrement();
        NotificationHelper.startService(context);
    }
}
