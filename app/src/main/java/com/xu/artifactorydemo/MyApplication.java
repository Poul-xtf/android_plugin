package com.xu.artifactorydemo;

import android.app.Application;
import android.content.res.Resources;
import com.xu.plugin_core.LoadApkManager;

/**
 * Created by xutengfei
 * on 2020/12/24
 */
public class MyApplication extends Application {
    public static Resources myResources;
    @Override
    public void onCreate() {
        super.onCreate();
        LoadApkManager.instance(this);
    }

    @Override
    public Resources getResources() {
        return myResources == null ? super.getResources() : myResources;
    }
}
