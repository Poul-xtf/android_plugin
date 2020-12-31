package com.xu.plugin_core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.lang.reflect.Field;


/**
 * Created by xutengfei
 * on 2020/12/30
 */
public class LoadApkManager extends BaseLoadApkManager {
    private static LoadApkManager loadApkManager;
    private static Context mContext;
    private ILoadBack iLoadBack;

    private LoadApkManager(LoadUtil loadInstance) {
        super(loadInstance);
        iLoadBack = loadInstance.getILoadBack();
    }

    public static LoadApkManager instance(Context context) {
        synchronized (LoadApkManager.class) {
            if (loadApkManager == null) {
                LoadUtil loadInstance = LoadUtil.getInstance(context);
                loadApkManager = new LoadApkManager(loadInstance);
            }
        }
        return loadApkManager;
    }

    public static LoadApkManager getLoadApkManager(Context context) {
        mContext = context;
        return loadApkManager;
    }

    public LoadApkManager IntentTo(String intent) {
        try {
            //1、AMS要检查目的地activity是否注册了清单
            //2、AMS要通知activityThread来创建目的地的类然后去启动生命周期
            Class<?> aClass = mContext.getClassLoader().loadClass(intent);
            mContext.startActivity(new Intent(mContext, aClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadApkManager;
    }

    public LoadApkManager loadApk(Class<?> clazz, String apkPath) throws Exception {
        this.apkPath = apkPath;
        this.resources = getResources();
        iLoadBack.loadClass();
        Field resources = clazz.getDeclaredField("myResources");
        resources.setAccessible(true);
        Object o = resources.get(null);
        resources.set(o, this.resources);
        return loadApkManager;
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
