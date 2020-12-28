package com.xu.artifactorydemo;

import android.app.Application;
import android.content.res.Resources;

import com.xu.plugin_core.HookUtil;
import com.xu.plugin_core.LoadUtil;
import com.xu.plugin_core.ProxyActivity;

/**
 * Created by xutengfei
 * on 2020/12/24
 */
public class MyApplication extends Application {
    private Resources resources;
    public static LoadUtil loadUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        HookUtil hookUtil = new HookUtil(this, ProxyActivity.class);
        hookUtil.hookStartActivity();
        try {
            hookUtil.hookLaunchActivity();
            loadUtil = LoadUtil.getInstance(this);
            resources = loadUtil.loadPluginResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }
}
