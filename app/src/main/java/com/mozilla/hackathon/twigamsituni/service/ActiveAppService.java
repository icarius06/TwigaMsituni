package com.mozilla.hackathon.twigamsituni.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.mozilla.hackathon.twigamsituni.activities.OverLayActivity;

import java.util.List;

/**
 * Created by patrickmunene on 28/04/2016.
 */
public class ActiveAppService extends Service {

    private static final String Tag = "ActiveAppService";
    ActivityManager am ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(Tag, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);;
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // get the info from the currently running task
                    List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
                    for(ActivityManager.RunningAppProcessInfo info : processInfos){
                        Log.d(Tag, "ProcessName: " + info.processName);
                    }
                    List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(processInfos.size());
                    for(ActivityManager.RunningTaskInfo info : taskInfo){
                        Log.d(Tag, "baseActivity: " + info.baseActivity.getClassName());
                        try {
                            Log.d(Tag, "topActivity: " + info.topActivity.getClassName());
                        }catch (Exception e){}
                    }

                    Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    componentInfo.getPackageName();
                    if(taskInfo.get(0).baseActivity.getClassName().equals("com.mozilla.hackathon.twigamsituni.activities.OverLayActivity")){
                        sleep(1000 * 60);
                        continue;
                    }
                    try {
                        Intent i = OverLayActivity.newInstance(ActiveAppService.this, taskInfo.get(0).baseActivity.getClassName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                        startActivity(i);
                    }catch (Exception e){e.printStackTrace();}
                    sleep(1000 * 60);
                }
            }
        });
        if(!thread2.isAlive()){
            Log.d(Tag, "start thread");
            thread2.start();
        }

    }

    private void sleep(long time){
        try {
            //Log.d(Tag, "runThread sleep");
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
