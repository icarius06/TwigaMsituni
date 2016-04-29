package com.mozilla.hackathon.twigamsituni.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;

import com.mozilla.hackathon.twigamsituni.domain.Session;

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
     * This returns the permissions used by an app and their descriptions given the app's package name
     * @param package_name
     * @return ArrayList<Map<String,String>> permissions
     */
    public static ArrayList<Map<String,String>> getAppPermissions(String package_name, Context context){
        Session session = (Session)((Activity)context).getApplication();
        List<PackageInfo> packages = session.getInstalledPackages();

        PackageManager mPm = context.getPackageManager();

        ArrayList<Map<String,String>> permissions = new ArrayList();

        for(PackageInfo pi:packages) {
            if (pi.requestedPermissions == null || pi.packageName.contains("com.android"))
                continue;
                if (pi.packageName.contains(package_name)) {
                    Map<String, String> curChildMap = new HashMap();
                    for (String permission : pi.requestedPermissions) {
                        try{
                            PermissionInfo pinfo = mPm.getPermissionInfo(permission, PackageManager.GET_PERMISSIONS);
                            if(pinfo.loadDescription(mPm)!=null) {
                                curChildMap.put(pinfo.name, pinfo.loadDescription(mPm).toString());
                                permissions.add(curChildMap);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.i(TAG, "Ignoring unknown permission " + permission);
                            continue;
                        }
                    }
                }
        }
        return permissions;
    }
}
