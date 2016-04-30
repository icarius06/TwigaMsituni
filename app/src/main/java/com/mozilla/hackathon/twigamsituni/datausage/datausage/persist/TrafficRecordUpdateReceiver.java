package com.mozilla.hackathon.twigamsituni.datausage.datausage.persist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

public class TrafficRecordUpdateReceiver extends WakefulBroadcastReceiver {

    private static String TRAFFIC_RECORD_ACTION = "TRAFFIC_RECORD_ACTION";
    // Restart service every 30 seconds
    private static final long REPEAT_TIME = 1000 * 30;
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    public TrafficRecordUpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (ApplicationInfo app :
                context.getPackageManager().getInstalledApplications(0)) {
            long dataConsumed=TrafficStats.getUidTxBytes(app.uid) + TrafficStats.getUidRxBytes(app.uid);
            if(dataConsumed>0) {
                long time=System.currentTimeMillis();
                TrafficRecord trafficRecord = new TrafficRecord(app.uid,TrafficStats.getUidTxBytes(app.uid),TrafficStats.getUidRxBytes(app.uid),time);
                trafficRecord.save();
            }
        }
        DeviceData deviceData=new DeviceData(TrafficStats.getMobileTxBytes(),TrafficStats.getMobileTxBytes(),System.currentTimeMillis());
        deviceData.save();
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TrafficRecordUpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                10000,
                10000, alarmIntent);

        ComponentName receiver = new ComponentName(context, TrafficRecordUpdateReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    public void cancelAlarm(Context context) {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, TrafficRecordUpdateReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
