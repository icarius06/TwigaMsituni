package com.mozilla.hackathon.twigamsituni.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mozilla.hackathon.twigamsituni.service.ActiveAppService;

/**
 * Created by patrickmunene on 28/04/2016.
 */
public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhhhhh");
        context.startService(new Intent(context, ActiveAppService.class));
    }

}
