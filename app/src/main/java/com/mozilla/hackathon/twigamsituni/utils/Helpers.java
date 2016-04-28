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
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<Map<String,String>> permissions = new ArrayList();

        PackageManager mPm = context.getPackageManager();

        //Get list of all packages
        List<PackageInfo> packages = mPm.getInstalledPackages(PackageManager.GET_META_DATA);
        
        // Loop through all installed packages to get a list of used permissions and PackageInfos
        for (PackageInfo pi : packages) {
            // Do not add System Packages
            if ((pi.requestedPermissions == null || pi.packageName.equals("android")) ||(pi.applicationInfo != null && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0))
                continue;

            if(pi.packageName.equals(app_name)){
                for (String permission : pi.requestedPermissions) {
                    Map<String, String> curChildMap = new HashMap<String, String>();
                    try {
                        PermissionInfo pinfo = mPm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
                        CharSequence label = pinfo.loadLabel(mPm);
                        CharSequence desc = pinfo.loadDescription(mPm);
                        curChildMap.put(label.toString(),desc.toString());
                        permissions.add(curChildMap);
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
