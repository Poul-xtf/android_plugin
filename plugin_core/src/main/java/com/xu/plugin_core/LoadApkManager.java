package com.xu.plugin_core;

import android.content.Context;
import android.content.res.Resources;


/**
 * Created by xutengfei
 * on 2020/12/30
 */
public class LoadApkManager extends BaseLoadApkManager {
    private static LoadApkManager loadApkManager;
    private ILoadBack iLoadBack;

    private LoadApkManager(LoadUtil loadInstance) {
        super(loadInstance);
        iLoadBack = loadInstance.getILoadBack();
    }

    public static void instance(Context context) {
        synchronized (LoadApkManager.class) {
            if (loadApkManager == null) {
                LoadUtil loadInstance = LoadUtil.getInstance(context);
                loadApkManager = new LoadApkManager(loadInstance);
            }
        }
    }

    public static LoadApkManager getLoadApkManager() {
        return loadApkManager;
    }

    public Resources loadApk(String apkPath) {
        this.apkPath = apkPath;
        resources = getResources();
        iLoadBack.loadClass();
        return resources;
    }

    @Override
    protected Resources getResources() {
        if (apkPath == null) {
            try {
                throw new Exception("apkPath is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (Resources) iLoadBack.loadResources(apkPath);
    }

}
