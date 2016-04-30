package com.mozilla.hackathon.twigamsituni.datausage.datausage.persist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by eugene on 29/04/2016.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    TrafficRecordUpdateReceiver alarm = new TrafficRecordUpdateReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarm.setAlarm(context);
        }
    }
}