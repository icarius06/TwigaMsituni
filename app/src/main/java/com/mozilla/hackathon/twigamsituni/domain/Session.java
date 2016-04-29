package com.mozilla.hackathon.twigamsituni.domain;

import android.app.Application;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 10/10/15.
 */
public class Session extends Application {

    List<PackageInfo> packages=null;

    public List<PackageInfo> getInstalledPackages(){
        return packages;
    }

    public void setInstalledPackages(List<PackageInfo> packages){
        this.packages = packages;
    }
}
