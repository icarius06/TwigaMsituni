package com.mozilla.hackathon.twigamsituni.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 4/28/16.
 */
public class Helpers {

    private static String TAG = "TAG";

    /**
     * This returns the permissions used by an app given the app's name
     * @param app_name
     * @return ArrayList<String> permissions
     */
    public static ArrayList<Map<String,String>> getAppPermissions(String app_name, Context context){
        PackageManager mPm = context.getPackageManager();
        List<PackageInfo> packages = mPm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        ArrayList<Map<String,String>> permissions = new ArrayList();

        for(PackageInfo pi:packages) {
            if (pi.requestedPermissions == null || pi.packageName.contains("com.android"))
                continue;
                
                if (pi.packageName.contains(app_name)) {
                    Map<String, String> curChildMap = new HashMap();
                    for (String permission : pi.requestedPermissions) {
                        try{
                            PermissionInfo pinfo = mPm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS);
                            if(pinfo.loadDescription(mPm)!=null) {
                                curChildMap.put(pinfo.name, pinfo.loadDescription(mPm).toString());
                                permissions.add(curChildMap);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.i("TAG", "Ignoring unknown permission " + permission);
                            continue;
                        }
                    }
                }
        }

        return permissions;

    }
}
